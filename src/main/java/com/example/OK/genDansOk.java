package com.example.OK;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import com.example.bian.client.model.enums.OrderSide;
import com.example.bian.client.model.enums.PositionSide;
import com.example.bian.client.model.trade.Order;
import com.example.bian.genDan.GetPositions;
import com.example.bian.genDan.MulPostOrders;
import com.example.bian.genDan.genDans.BuySell;
import com.example.bian.xin.JianKong4;
import com.example.bian.xin.QingCang3;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.example.bian.client.bushu.PrivateConfig.*;



public class genDansOk {


    public static void main(String[] args) throws InterruptedException, IOException {


        args = new String[2];
        PrivateConfig.printLog("开始啦");
        args[0] = "E://code//biance";
        args[1] = "0-genDan";
        PrivateConfig.init((args[0]));
        PrivateConfig.before(args[0], args[1]);

        // 对https也开启代理

        System.out.println("开代理");
        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10819");


        PrivateConfig.getJGXsw();
        PrivateConfig.xsw(true);

        genDansOk genDan = new genDansOk();
        genDan.method(args);


    }
    private static final ObjectMapper objectMapper = new ObjectMapper();
    Set<String> hasOrderSet = new TreeSet<>();
    public void method(String[] args) throws InterruptedException, IOException {
        PrivateConfig.printLog("genDansOk跟单开始时间为：" + PrivateConfig.getCurrentTime());
        int youOrder = 0;
        BigDecimal count = ling;
        JSONObject dynamicInfo = PrivateConfig.readJsonFile(args[0] + "/dynamicInfo.json");
        if (dynamicInfo != null) {
            if (StringUtils.isNotBlank(dynamicInfo.getString("genPortfolioId"))) {
                genDans_genPortfolioId = dynamicInfo.getString("genPortfolioId");
            }
            if (StringUtils.isNotBlank(dynamicInfo.getString("youOrder"))) {
                youOrder = dynamicInfo.getInteger("youOrder");
            }
            if (StringUtils.isNotBlank(dynamicInfo.getString("count"))) {
                count = dynamicInfo.getBigDecimal("count");
            }
            if (StringUtils.isNotBlank(dynamicInfo.getString("hasOrderSet"))) {
                hasOrderSet = Arrays.stream(dynamicInfo.getString("hasOrderSet").split(",")).collect(Collectors.toSet());
            }
        }

        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(5, 5, 10,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.DiscardPolicy());
        PrivateConfig.threadPoolExecutor = threadPoolExecutor;

        //启动监控线程
        Callable callable1 = new Callable() {
            @Override
            public String call() throws Exception {
                JianKongOk jianKong4 = new JianKongOk();
                jianKong4.method(null, threadPoolExecutor, PrivateConfig.daiDanOk_personInfoList);
                return "";
            }
        };
        threadPoolExecutor.submit(callable1);

        Set<String> tradeSet = new LinkedHashSet<>();
        int countErr = 0;
        int countErrLarge = 0;
        Map<String, Order> oldOrders = getOrders1(threadPoolExecutor, true, genDans_genPortfolioId, count);

        int diaoYongCount = 0;
        BigDecimal winTotal = ling;
        Map<String, BuySell> buySellMap = new HashMap<>();
        boolean xiaoCeShi = false;
        PrivateConfig.printLogJianKong();
        while (true) {

            try {


                Map<String, Order> newOrders = new HashMap<>();
                if (youOrder == 1) {
                    Thread.sleep(Long.parseLong(PrivateConfig.shiJian));
                    newOrders = getOrders1(threadPoolExecutor, false, genDans_genPortfolioId, count);
                } else {
                    for (Object o : genDans_genPortfolioIds) {
                        JSONObject genPortfolioId = (JSONObject) o;
                        genDans_genPortfolioId = genPortfolioId.getString("genPortfolioId");
                        count = genPortfolioId.getBigDecimal("count");
                        newOrders = getOrders1(threadPoolExecutor, false, genDans_genPortfolioId, count);
                        if (newOrders != null && !newOrders.isEmpty()) {
                            youOrder = 1;
                            //第一次购买时，设置倍数
                            BigDecimal laoShiMoney = com.example.bian.genDan.Constants.getLaoShiMoney(genDans_genPortfolioId);
                            if (laoShiMoney != null) {
                                PrivateConfig.printLog("第一次购买时，设置倍数");
                                PrivateConfig.setBeiShuOk(laoShiMoney, daiDanOk_personInfoList);
                            }
                            break;
                        }
                        Thread.sleep(Long.parseLong(PrivateConfig.shiJian));
                    }
                }

                if (newOrders == null) {
                    T5.searchAll("停止跟单了，抓紧报告");
                    return;
                }

                diaoYongCount++;
                if (diaoYongCount > 100) {
                    diaoYongCount = 0;
                    PrivateConfig.printLogJianKong();
                    PrivateConfig.printLog("newOrders");
                    for (Map.Entry<String, Order> entryNew : newOrders.entrySet()) {
                        PrivateConfig.printLog(entryNew.getKey());
                        PrivateConfig.printLog(entryNew.getValue().toString());
                    }
                    PrivateConfig.printLog("oldOrders");
                    for (Map.Entry<String, Order> entryNew : oldOrders.entrySet()) {
                        PrivateConfig.printLog(entryNew.getKey());
                        PrivateConfig.printLog(entryNew.getValue().toString());
                    }
                }

                List<Order> orderListNew = new ArrayList<>();

                for (Map.Entry<String, Order> entryNew : newOrders.entrySet()) {
                    String key = entryNew.getKey();
                    if (!oldOrders.containsKey(key)) {
                        //有新单子了
                        String symbolPoSide = key.split("__")[0];
                        for (Map.Entry<String, Order> entryOld : oldOrders.entrySet()) {
                            if (entryOld.getKey().contains(symbolPoSide)) {

                                Order orderNew = new Order();
                                orderNew.setSymbol(entryNew.getValue().getSymbol());
                                orderNew.setPositionSide(entryNew.getValue().getPositionSide());

                                BigDecimal oldCount = entryOld.getValue().getOrigQty();
                                BigDecimal newCount = entryNew.getValue().getOrigQty();
                                BigDecimal result = newCount.subtract(oldCount);
                                orderNew.setOrigQty(result.abs());

                                orderNew.setSide(OrderSide.BUY.toString());
                                if (result.compareTo(ling) < 0) {
                                    orderNew.setSide(OrderSide.SELL.toString());
                                }
                                orderListNew.add(orderNew);

                                //更新旧单子（当数量从1变成2时，数量1的订单移除，所以再从2变成1时，又是一个新单子了）
                                oldOrders.remove(entryOld.getKey());
                                oldOrders.put(key, entryNew.getValue());
                                break;
                            }
                        }

                        if (!oldOrders.containsKey(key)) {
                            //有新币种的单子了
                            Order orderNew = new Order();
                            orderNew.setSymbol(entryNew.getValue().getSymbol());
                            orderNew.setOrigQty(entryNew.getValue().getOrigQty().abs());
                            orderNew.setPositionSide(entryNew.getValue().getPositionSide());

                            orderNew.setSide(OrderSide.BUY.toString());
                            if (entryNew.getValue().getOrigQty().compareTo(ling) < 0) {
                                orderNew.setSide(OrderSide.SELL.toString());
                            }
                            orderListNew.add(orderNew);
                            //更新旧单子
                            oldOrders.put(key, entryNew.getValue());
                        }
                    }
                }

                //处理清仓的币种
                Iterator<Map.Entry<String, Order>> iterator = oldOrders.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Order> entryOld = iterator.next();
                    String symbolPoSide = entryOld.getKey().split("__")[0];
                    boolean qingCang = true;
                    for (Map.Entry<String, Order> entryNew : newOrders.entrySet()) {
                        if (entryNew.getKey().contains(symbolPoSide)) {
                            //现在还有
                            qingCang = false;
                            break;
                        }
                    }
                    //现在没有了
                    if (qingCang) {
                        PrivateConfig.printLog(PrivateConfig.getCurrentTime());
                        PrivateConfig.printLog(genDans_genPortfolioId + "：清仓了哈哈："+ JSON.toJSONString(entryOld));
                        Order qingOrder = new Order();
                        qingOrder.setSymbol(entryOld.getValue().getSymbol());
                        qingOrder.setPositionSide(getPositionSide(entryOld.getValue().getPositionSide(), entryOld.getValue().getOrigQty()));
                        for (JSONObject jsonObject : PrivateConfig.daiDanOk_personInfoList) {
                            xiaDan xiaDan = new xiaDan(jsonObject, qingOrder, true);
                            threadPoolExecutor.submit(xiaDan);//启动一般的线程
                            try {
                                Thread.sleep(50L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        //删除旧订单
                        iterator.remove();

                        hasOrderSet.remove(symbolPoSide);
                        if(xiaoCeShi){
                            SyncRequestClient syncRequestClient = ((SyncRequestClient) PrivateConfig.daiDanOk_personInfoList.get(0).get(PrivateConfig.syncRequestClient));
                            BigDecimal markPrice = syncRequestClient.getMarkPrice(entryOld.getValue().getSymbol()).get(0).getMarkPrice();
                            String buySellKey = entryOld.getValue().getSymbol() + "_" + entryOld.getValue().getPositionSide();
                            BuySell buySell = buySellMap.get(buySellKey);
                            buySell.setSell(entryOld.getValue().getOrigQty().multiply(markPrice));

                            BigDecimal sell = buySell.getSell();
                            BigDecimal buy = buySell.getBuy();
                            if(PositionSide.LONG.toString().equals(entryOld.getValue().getPositionSide())){
                                winTotal = winTotal.add(sell.subtract(buy).divide(buy, 4, RoundingMode.DOWN));
                            }else {
                                winTotal = winTotal.add(sell.subtract(buy).divide(buy, 4, RoundingMode.DOWN).negate());
                            }
                            PrivateConfig.printLog("总盈利："+ winTotal);
                            buySellMap.remove(buySellKey);
                        }
                    }
                }

                if(oldOrders.isEmpty()){
                    youOrder = 0;
                    hasOrderSet.clear();
                }


                /*for(Map.Entry<String, Order> entryOld : oldOrders.entrySet()){
                    System.out.println(entryOld.getKey());
                }*/


                countErr = 0;

                if (!orderListNew.isEmpty()) {
                    PrivateConfig.printLog(genDans_genPortfolioId + "：下单了哈哈：" + JSON.toJSONString(orderListNew));
                    //下单
                    for (JSONObject person : PrivateConfig.daiDanOk_personInfoList) {
                        try {
                            for (Order order : orderListNew) {
                                xiaDan xiaDan = new xiaDan(person, order, false);
                                threadPoolExecutor.submit(xiaDan);//启动一般的线程
                                //两个账号之间执行的间隔
                                Thread.sleep(50);

                                if(xiaoCeShi){
                                    SyncRequestClient syncRequestClient = ((SyncRequestClient) person.get(PrivateConfig.syncRequestClient));
                                    BigDecimal markPrice = syncRequestClient.getMarkPrice(order.getSymbol()).get(0).getMarkPrice();
                                    String buySellKey = order.getSymbol() + "_" + order.getPositionSide();
                                    BuySell buySell = buySellMap.get(buySellKey);
                                    if(PrivateConfig.isMai(order.getSide(), order.getPositionSide(), null)){
                                        if(buySell == null){
                                            buySell = new BuySell();
                                            buySellMap.put(buySellKey, buySell);
                                        }
                                        buySell.setBuy(order.getOrigQty().multiply(markPrice).abs());
                                        buySell.setPositionSide(order.getPositionSide());
                                    }else {
                                        buySell.setSell(order.getOrigQty().multiply(markPrice).abs());
                                    }
                                }
                            }
                        } catch (Exception e) {
                            PrivateConfig.printLog(e.getMessage());
                            T5.searchAll("连续三次，有问题，关闭软件，重新启动33。" + e.getMessage());
                        }
                    }
                    JianKong4.needCheck = true;
                    JianKong4.checkCount = 0;
//                    PrivateConfig.printLog("下单了1：" + PrivateConfig.getCurrentTime());
//                    Thread.sleep(2000);//前面有跟单，歇2秒再跟
                }

                dynamicInfo.put("youOrder", youOrder);
                if(youOrder == 1){
                    dynamicInfo.put("genPortfolioId", genDans_genPortfolioId);
                }else {
                    dynamicInfo.put("genPortfolioId", "");
                }
                dynamicInfo.put("count", count);
                dynamicInfo.put("hasOrderSet", String.join(",", hasOrderSet));
                objectMapper.writeValue(new File(args[0] + "/dynamicInfo.json"), dynamicInfo);

            } catch (Exception e) {
                countErr++;
                if (countErr > 3) {
                    countErr = 0;
                    PrivateConfig.printLog(e.getMessage());
                    T5.searchAll("连续三次，有问题，关闭软件，重新启动34。" + e.getMessage());
                    countErrLarge++;
                }
                if (countErrLarge > 5) {
                    countErrLarge = 0;
                    //更换Url地址
                    PrivateConfig.genDan_urlIndex++;
                    if (PrivateConfig.genDan_urlIndex >= PrivateConfig.genDan_urls.split(";").length) {
                        PrivateConfig.genDan_urlIndex = 0;
                    }
                    PrivateConfig.genDan_url = PrivateConfig.genDan_urls.split(";")[PrivateConfig.genDan_urlIndex];
                    System.out.println("正在访问的域名：" + PrivateConfig.genDan_url);
                }
            }

            if (tradeSet.size() > 1000) {
                //删除最后100个元素
                for (int i = 0; i < 90; i++) {
                    tradeSet.remove(900);
                }
            }
        }
    }


    /**
     *
     * @param threadPoolExecutor
     * @param old
     * @param genPortfolioId
     * @param count
     *
     * @return  返回null时，说明停止带单了
     * @throws InterruptedException
     */
    public Map<String, Order> getOrders1(ThreadPoolExecutor threadPoolExecutor, boolean old, String genPortfolioId, BigDecimal count) throws InterruptedException {

        Map<String, Order> map = new HashMap<>();

        if(StringUtils.isBlank(genPortfolioId)){
            return map;
        }

        JSONArray jsonArray;
        if(old && daiDanOk_genXianYou.equals("1")){
            //从json文件中读取现有持仓， 这样就可以随时添加客户
            /*
             * 现有订单：MASKUSDT_1104.00000000_LONG_1717553618000
             * 现有订单：1000PEPEUSDT_-13371307.00000000_SHORT_1710610697000
             * */
            return map;
        }else {
            jsonArray = GetPositions.getOrders(threadPoolExecutor, genPortfolioId);
        }
        if(daYin.equals("1")){
            if(jsonArray != null){
                printLog(jsonArray.toJSONString());
            }else {
                printLog("获取订单失败了");
            }
        }

//        System.out.println("订单：" + jsonArray.toJSONString());
        if(jsonArray != null){
            if(CollectionUtils.isEmpty(hasOrderSet)){
                BigDecimal tatal = ling;
                for (Object o : jsonArray) {
                    JSONObject trade = (JSONObject) o;
                    BigDecimal qty = trade.getBigDecimal(PrivateConfig.positionAmount);
                    BigDecimal entryPrice = trade.getBigDecimal("entryPrice");

                    tatal = tatal.add(qty.multiply(entryPrice).abs());
                }
                //拥有数量大于设定数量，证明是真的有单子，因为不限币种，所以只能根据持仓金额判断了
                if (tatal.abs().compareTo(count) < 0) {
                    return map;
                }
            }
            for (Object o : jsonArray) {
                JSONObject trade = (JSONObject) o;
                String symbol = trade.getString(PrivateConfig.symbol);
                BigDecimal qty = trade.getBigDecimal(PrivateConfig.positionAmount);
                BigDecimal entryPrice = trade.getBigDecimal("entryPrice");

                //拥有数量大于设定数量，证明是真的有单子，因为不限币种，所以只能根据持仓金额判断了
                /*if(qty.multiply(entryPrice).abs().compareTo(count) < 0){
                    continue;
                }*/

                //带单员在挣钱，我们就不跟了：造成的问题是，带单员赔钱买，稍微挣钱就卖了，我想要的效果是一但买了，就等着一起卖
                String positionSide = getPositionSide(trade.getString(PrivateConfig.positionSide), qty);
                String hasOrderFlag = symbol + "_" + positionSide;
                if(!hasOrderSet.contains(hasOrderFlag)){
                    BigDecimal unrealizedProfit = trade.getBigDecimal("unrealizedProfit");
                    if(unrealizedProfit.compareTo(new BigDecimal(PrivateConfig.genDans_unrealizedProfit)) > 0){
                        continue;
                    }
                    hasOrderSet.add(hasOrderFlag);
                }

                Order order = new Order();
                order.setSymbol(symbol);
                order.setPositionSide(positionSide);
                order.setOrigQty(qty);
                map.put(flg1(trade), order);
            }
        }else {
            return null;
        }


        return map;
    }

    public String flg1(JSONObject trade){
        String symbol = trade.getString(PrivateConfig.symbol);
        String positionSide = trade.getString(PrivateConfig.positionSide);
        BigDecimal qty = trade.getBigDecimal(PrivateConfig.positionAmount);
        return new StringBuilder().append(symbol).append("_").append(positionSide).append("__").append(qty).toString();
    }




}




