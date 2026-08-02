package com.example.bian.ok;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import com.example.bian.client.model.enums.OrderSide;
import com.example.bian.client.model.enums.PositionSide;
import com.example.bian.client.model.trade.Order;
import com.example.bian.client.model.trade.Position;
import com.example.bian.genDan.MulPostOrders;
import com.example.bian.xin.JianKong4;
import com.example.bian.xin.QingCang3;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.example.bian.client.bushu.PrivateConfig.*;

public class genOkByPosition {

    /*
     * 单向持仓时的下单记录
     * 做多买入 "side":"BUY","realizedProfit":0.00000000（买入时没有盈亏）,"qty":2601.00000000,"positionSide":"BOTH",
     * {"time":1704557420000,"symbol":"GMTUSDT","side":"BUY","quantity":867.43350,"quantityAsset":"USDT","realizedProfit":0.00000000,"qty":2601.00000000,"positionSide":"BOTH","activeBuy":true}
     * 做多卖出 "side":"SELL","realizedProfit":282.93366852（卖出时有盈亏，挣了是正数，赔了是负数）,
     * {"time":1704608070000,"symbol":"GMTUSDT","side":"SELL","quantity":1606.24640,"quantityAsset":"USDT","realizedProfit":282.93366852,"qty":3968.00000000,"positionSide":"BOTH","activeBuy":true}
     * 做空买入 "side":"SELL","realizedProfit":0.00000000,
     * {"time":1704469693000,"symbol":"SEIUSDT","side":"SELL","quantity":219.2520000,"quantityAsset":"USDT","realizedProfit":0.00000000,"qty":302.00000000,"positionSide":"BOTH","activeBuy":false}
     * 做空卖出 "side":"BUY","realizedProfit":10.69158117,
     * {"time":1704475064000,"symbol":"SEIUSDT","side":"BUY","quantity":3526.5360000,"quantityAsset":"USDT","realizedProfit":10.69158117,"qty":4953.00000000,"positionSide":"BOTH","activeBuy":false}
     *
     *position
     * 做多正在持仓
     * {"symbol":"OPUSDT","positionAmount":"506.9","leverage":20,"positionSide":"BOTH"}
     * 做空正在持仓
     * {"symbol":"UMAUSDT","positionAmount":"-57","leverage":20,"positionSide":"BOTH"}
     *
     *
     * 做多的历史持仓，已经全部清仓的 closed有时间，"status":"All Closed"，千万不要用"updateTime"，它是会改变的，不知道啥原因
     * {"symbol":"CELOUSDT","opened":1705095733741,"closed":1705102034801,"side":"Long","updateTime":1705102034949, "status":"All Closed"},
     * * 做多的历史持仓，已经部分清仓的 closed=null，"status":"Partially Closed"
     * {"symbol":"CELOUSDT","opened":1705095733741,"closed":,"side":"Long","updateTime":1705102034949, "status":"Partially Closed"},
     * 做空的历史持仓
     * {"symbol":"ENSUSDT","opened":1705059695456,"closed":1705073895126,"side":"Short","updateTime":1705073895763}
     *







     *
     * 双向持仓
     *      做多
            买：side=BUY,positionSide=LONG    qty":2778
            卖：side=SELL,positionSide=LONG   qty":2778
            做空
            买：side=SELL,positionSide=SHORT  qty":2778
            卖：side=BUY,positionSide=SHORT   qty":2778
     * {"time":1705751560000,"symbol":"UMAUSDT","side":"SELL","quantity":11239.788000,"realizedProfit":0.00000000,"qty":2778.00000000,"positionSide":"SHORT","activeBuy":false}
     *
     * position
     * 做多的正在持仓
     * {"symbol":"ORDIUSDT","positionAmount":"50.8","leverage":20,"positionSide":"LONG",}
     * 做空的持仓
     * {"symbol":"UMAUSDT","positionAmount":"-3471","leverage":20,"positionSide":"SHORT"}
     *
     * 做多的历史持仓（注意long和short是小写）
     * {"symbol":"UMAUSDT","opened":1705665671378,"closed":1705679892671,"side":"Long","updateTime":1705679892860}
     * 做空的历史持仓
     * {"symbol":"UMAUSDT","opened":1705749980512,"closed":1705750379058,"side":"Short","updateTime":1705750379223}
     *
     *
     *
     * */


    public static String con_realizedProfit = "realizedProfit";




    public static void main(String[] args) throws InterruptedException, IOException {


        args = new String[2];
        PrivateConfig.printLog("开始啦");
        args[0] = "E://code//biance";
        args[1] = "0-ok";
        PrivateConfig.init((args[0]));
        PrivateConfig.before(args[0], args[1]);

        // 对https也开启代理

        System.out.println("开代理");
        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10819");


        PrivateConfig.getJGXsw();
        PrivateConfig.xsw(true);

        JSONArray jsonArrayNew = new JSONArray();
        JSONObject test = JSONObject.parseObject("");
        jsonArrayNew.add(test);
        genOkByPosition ok = new genOkByPosition();
        ok.method(args);


    }


    private static final ObjectMapper objectMapper = new ObjectMapper();
    JSONObject startBeiShu = new JSONObject();
    JSONObject dynamicInfoOld = new JSONObject();
    public void method(String[] args) throws InterruptedException, IOException {
        PrivateConfig.printLog("OK跟单开始时间为：" + PrivateConfig.getCurrentTime());

        JSONObject dynamicInfo = PrivateConfig.readJsonFile(args[0] + "/dynamicInfo.json");
        if (dynamicInfo != null) {
            if (StringUtils.isNotBlank(dynamicInfo.getString("hasOrderSet"))) {
                hasOrderSet = Arrays.stream(dynamicInfo.getString("hasOrderSet").split(",")).collect(Collectors.toSet());
            }
            if (StringUtils.isNotBlank(dynamicInfo.getString("beiShu"))) {
                startBeiShu = dynamicInfo.getJSONObject("beiShu");
            }
        }else {
            PrivateConfig.printLog("dynamicInfo文件有问题，重启启动。" + PrivateConfig.getCurrentTime());
            return;
        }

        //设置倍数
        PrivateConfig.setStartBeiShu(PrivateConfig.ok_personInfoList, startBeiShu);

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
                JianKong4 jianKong4 = new JianKong4();
                jianKong4.method(null, threadPoolExecutor, PrivateConfig.ok_personInfoList);
                return "";
            }
        };
        threadPoolExecutor.submit(callable1);

        if("1".equals(ok_zhuan)){
            Callable callable2 = new Callable() {
                @Override
                public String call() throws Exception {
                    huoQuBeiFen huoQuBeiFen = new huoQuBeiFen();
                    huoQuBeiFen.method();
                    return "";
                }
            };
            threadPoolExecutor.submit(callable2);
        }


        //设置倍数
//        PrivateConfig.setBeiShu(threadPoolExecutor, PrivateConfig.ok_personInfoList);

        Set<String> tradeSet = new LinkedHashSet<>();
        int countErr = 0;
        int countErrLarge = 0;
        Map<String, Order> oldOrders = getOrders1(threadPoolExecutor, true);
        int diaoYongCount = 0;
        PrivateConfig.printLogJianKong();
        while (true) {

            try {
//                PrivateConfig.printLog("执行1次：" + PrivateConfig.getCurrentTime());

                Thread.sleep(Long.parseLong(PrivateConfig.shiJian));
                /*if(PrivateConfig.isZhiSun){
                    Thread.sleep(1000*60*30);
                    T5.searchAll("止损了，重大问题，抓紧报告");
                    System.out.println("止损了，重大问题，抓紧报告");
                    continue;
                }*/

                Map<String, Order> newOrders = getOrders1(threadPoolExecutor, false);
                if(newOrders == null){
                    if("2".equals(ok_position)){
                        T5.searchAll("停止跟单了，抓紧报告");
                        return;
                    }
                    ok_position = "2";
                }

                diaoYongCount++;
//                if(diaoYongCount > 10 * 30){
                if(diaoYongCount > 100){

                    diaoYongCount = 0;
                    PrivateConfig.printLogJianKong();
                    PrivateConfig.printLog("newOrders");
                    for(Map.Entry<String, Order> entryNew : newOrders.entrySet()){
                        PrivateConfig.printLog(entryNew.getKey());
                        PrivateConfig.printLog(entryNew.getValue().toString());
                    }
                    PrivateConfig.printLog("oldOrders");
                    for(Map.Entry<String, Order> entryNew : oldOrders.entrySet()){
                        PrivateConfig.printLog(entryNew.getKey());
                        PrivateConfig.printLog(entryNew.getValue().toString());
                    }
                }

                List<Order> orderListNew = new ArrayList<>();

                for(Map.Entry<String, Order> entryNew : newOrders.entrySet()){
                    String key = entryNew.getKey();
                    if(!oldOrders.containsKey(key)){
                        //有新单子了
                        String symbolPoSide = key.split("__")[0];
                        for(Map.Entry<String, Order> entryOld : oldOrders.entrySet()){
                            if(entryOld.getKey().contains(symbolPoSide)){

                                Order orderNew = new Order();
                                orderNew.setSymbol(entryNew.getValue().getSymbol());
                                orderNew.setPositionSide(getPositionSide(entryNew.getValue().getPositionSide(), entryNew.getValue().getOrigQty()));
                                orderNew.setRemoveSide(getRemoveSide(entryNew.getValue().getPositionSide(), entryNew.getValue().getOrigQty()));
                                orderNew.setRemovePositionSide(entryNew.getValue().getPositionSide());

                                BigDecimal oldCount = entryOld.getValue().getOrigQty();
                                BigDecimal newCount = entryNew.getValue().getOrigQty();
                                BigDecimal result = newCount.subtract(oldCount);
                                orderNew.setOrigQty(result.abs());

                                orderNew.setSide(OrderSide.BUY.toString());
                                if(result.compareTo(ling)<0){
                                    orderNew.setSide(OrderSide.SELL.toString());
                                }
                                orderListNew.add(orderNew);

                                //更新旧单子（当数量从1变成2时，数量1的订单移除，所以再从2变成1时，又是一个新单子了）
                                oldOrders.remove(entryOld.getKey());
                                oldOrders.put(key, entryNew.getValue());
                                break;
                            }
                        }

                        if(!oldOrders.containsKey(key)){
                            //有新币种的单子了
                            Order orderNew = new Order();
                            orderNew.setSymbol(entryNew.getValue().getSymbol());
                            orderNew.setOrigQty(entryNew.getValue().getOrigQty().abs());

                            orderNew.setPositionSide(getPositionSide(entryNew.getValue().getPositionSide(), entryNew.getValue().getOrigQty()));
                            orderNew.setRemoveSide(getRemoveSide(entryNew.getValue().getPositionSide(), entryNew.getValue().getOrigQty()));
                            orderNew.setRemovePositionSide(entryNew.getValue().getPositionSide());

                            orderNew.setSide(OrderSide.BUY.toString());
                            if(entryNew.getValue().getOrigQty().compareTo(ling)<0){
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
                    for(Map.Entry<String, Order> entryNew : newOrders.entrySet()){
                        if(entryNew.getKey().contains(symbolPoSide)){
                            //现在还有
                            qingCang = false;
                            break;
                        }
                    }
                    //现在没有了
                    if(qingCang){
                        QingCang3 qingCang3 = new QingCang3();
                        qingCang3.qingCang(PrivateConfig.ok_personInfoList, entryOld.getValue().getSymbol(), getPositionSide(entryOld.getValue().getPositionSide(), entryOld.getValue().getOrigQty()));
                        //删除旧订单
                        iterator.remove();
                    }
                }


                /*for(Map.Entry<String, Order> entryOld : oldOrders.entrySet()){
                    System.out.println(entryOld.getKey());
                }*/


                countErr = 0;

                if (!orderListNew.isEmpty()) {
                    //下单
                    for (JSONObject person : PrivateConfig.ok_personInfoList) {

                        try {
                            for (Order order : orderListNew) {
                                MulPostOrders mulGetAllOrders = new MulPostOrders(person, order);
                                threadPoolExecutor.submit(mulGetAllOrders);//启动一般的线程
                                //两个账号之间执行的间隔
                                Thread.sleep(50);

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

                if(oldOrders.isEmpty()){
                    hasOrderSet.clear();
                }

                //把倍数记录到本地，供重启时使用
                for (JSONObject personInfo : ok_personInfoList) {
                    String apikey = personInfo.getString(apiKey);
                    String testBeiShu = personInfo.getString(beiShu);
                    startBeiShu.put(apikey, testBeiShu);
                }
                dynamicInfo.put("beiShu", startBeiShu);
                dynamicInfo.put("hasOrderSet", String.join(",", hasOrderSet));

                if(!dynamicInfo.equals(dynamicInfoOld)){
                    PrivateConfig.printLog("新旧不同，写入一次");
                    //新旧不同，写入一次
                    objectMapper.writeValue(new File(args[0] + "/dynamicInfo.json"), dynamicInfo);
                    //把新值赋给旧变量
                    dynamicInfoOld = JSONObject.parseObject(dynamicInfo.toJSONString());
                }


            } catch (Exception e) {
                countErr++;
                if (countErr > 3) {
                    countErr = 0;
                    PrivateConfig.printLog(e.getMessage());
                    T5.searchAll("连续三次，有问题，关闭软件，重新启动34。" + e.getMessage());
                    countErrLarge++;
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




    public Map<String, Order> getOrders1(ThreadPoolExecutor threadPoolExecutor, boolean old) throws InterruptedException {
        Map<String, Order> map = new HashMap<>();
        JSONArray jsonArray;
        if(old && !CollectionUtils.isEmpty(PrivateConfig.ok_xianYou)){
            //从json文件中读取现有持仓， 这样就可以随时添加客户
            /*
             * 现有订单：MASKUSDT_1104.00000000_LONG_1717553618000
             * 现有订单：1000PEPEUSDT_-13371307.00000000_SHORT_1710610697000
             * */
            jsonArray = PrivateConfig.ok_xianYou;
        }else {
            jsonArray = GetOKPositions.getOrders(threadPoolExecutor, ok_genPortfolioId, true, false);
        }

        if(jsonArray != null){
            Map<String, Position> positionMap = PrivateConfig.buildPositionMap(jsonArray);

            for (Map.Entry<String, Position> positionNew : positionMap.entrySet()) {
                Order order = new Order();
                order.setSymbol(positionNew.getValue().getSymbol());
                order.setPositionSide(positionNew.getValue().getPositionSide());
                order.setOrigQty(positionNew.getValue().getPositionAmt());
                order.setPos(positionNew.getValue().getPos());//这个表示张数，是ok的数量单位，以此作为仓位数量发生变化的标志

                PrivateConfig.gangGan(ok_personInfoList, positionNew.getValue().getSymbol(), positionNew.getValue().getLeverage());

                map.put(flg1(order), order);
            }
        }else {
            return null;
        }


        return map;
    }

    public String flg1(Order order){
        String symbol = order.getSymbol();
        String positionSide = order.getPositionSide();
        BigDecimal qty = order.getPos();
        return new StringBuilder().append(symbol).append("_").append(positionSide).append("__").append(qty).toString();
    }




}

class huoQuBeiFen {

    public void method() throws InterruptedException, IOException {
        int errorCount = 0;
        while (true) {
            try {


                Thread.sleep(Long.parseLong(PrivateConfig.shiJian) * 4);
                if ("2".equals(ok_position)) {
                    continue;
                }
                String s = GetOKPositions.get2(ok_genPortfolioId);
                JSONObject jsonObject = JSON.parseObject(s);
                if ("0".equals(jsonObject.getString("code"))) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray != null) {
                        for (Object o : jsonArray) {
                            JSONObject position = (JSONObject) o;
                            String instType = position.getString("instType");
                            if (!"SWAP".equals(instType)) {//只做永续合约
                                continue;
                            }

                            Position positionNew = getOkPositionSide(position);
                            //先判断单子是否是想要的
                            if (StringUtils.isNotBlank(ok_count)) {
                                //不为空时，校验是否挣钱
                                //带单员在挣钱，我们就不跟了：造成的问题是，带单员赔钱买，稍微挣钱就卖了，我想要的效果是一但买了，就等着一起卖
                                String hasOrderFlag = positionNew.getSymbol() + "_" + positionNew.getPositionSide() + "_" + positionNew.getOpenTme();
                                if (!hasOrderSet.contains(hasOrderFlag)) {
                                    BigDecimal unrealizedProfit = positionNew.getUnrealizedProfit();
                                    if (unrealizedProfit.compareTo(new BigDecimal(ok_count)) >= 0) {
                                        continue;
                                    }
                                    hasOrderSet.add(hasOrderFlag);
                                }
                            }
                        }
                    }
                    errorCount = 0;
                }else if ("50113".equals(jsonObject.getString("code"))) {
                    errorCount++;
                    if (errorCount > 3) {
                        T5.searchAll("token过期了，OK备份数据有误");
                        errorCount = 0;
                    }
                }
            } catch (Exception e) {
                errorCount++;
                if (errorCount > 3) {
                    T5.searchAll("OK备份数据有误");
                    errorCount = 0;
                }
                e.printStackTrace();

            }
        }
    }

    public static Position getOkPositionSide(JSONObject position) {
        Position p = new Position();
        BigDecimal value = position.getBigDecimal("notionalUsd");
        BigDecimal markPx = position.getBigDecimal("markPx");
        BigDecimal count = value.divide(markPx, 4, BigDecimal.ROUND_HALF_UP);
        String positionSid = position.getString("posSide").toUpperCase();
        BigDecimal pos = position.getBigDecimal("subPos");
        p.setUnrealizedProfit(position.getBigDecimal("pnlRatio").divide(position.getBigDecimal("lever"), 5, BigDecimal.ROUND_HALF_UP));
        p.setOpenTme(position.getLong("openTime"));
        if (PositionSide.SHORT.toString().equals(positionSid)) {
            count = count.negate();
        } else if (PositionSide.NET.toString().equals(positionSid)) {
            positionSid = PositionSide.BOTH.toString();
            if (pos.compareTo(PrivateConfig.ling) < 0) {
                count = count.negate();
            }
        }

        p.setPositionSide(getPositionSide(positionSid, count));
        p.setPositionAmt(count);
        p.setSymbol(position.getString("instId").split("-")[0] + "USDT");

        p.setPos(pos);

        return p;
    }

}


