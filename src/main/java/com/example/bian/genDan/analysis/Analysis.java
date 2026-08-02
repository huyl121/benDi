package com.example.bian.genDan.analysis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.spot.Market;
import com.example.bian.client.RequestOptions;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import com.example.bian.client.model.enums.OrderSide;
import com.example.bian.client.model.enums.PositionSide;
import com.example.bian.client.model.trade.AccountInformation;
import com.example.bian.client.model.trade.Order;
import com.example.bian.client.model.trade.Position;
import com.example.bian.genDan.GetPositions;
import com.example.bian.genDan.MulPostOrders;
import com.example.bian.xin.QingCang3;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.example.bian.client.bushu.PrivateConfig.*;

public class Analysis {

    @SneakyThrows
    public static void main(String args[]){
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

        Analysis genDan = new Analysis();
        genDan.method(args[0]);


    }

    static float buyMoney = 0f;
    static float winMoney = 0f;
    static boolean ceShi = false;
    static Long nowTimeTest = 1772380800000L;

    static boolean youCaoZuo = false;

    public void method(String myOrderPath) throws InterruptedException, IOException {
        PrivateConfig.printLog("Analysis跟单开始时间为：" + PrivateConfig.getCurrentTime());

        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(5, 5, 10,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.DiscardPolicy());
        PrivateConfig.threadPoolExecutor = threadPoolExecutor;

        BigDecimal analysis_money2 = analysis_money.multiply(new BigDecimal("2"));
        BigDecimal analysis_money3 = analysis_money.multiply(new BigDecimal("3"));
        BigDecimal analysis_money4 = analysis_money.multiply(new BigDecimal("4"));
        int countErr = 0;
        int countErrLarge = 0;
        RequestOptions options = new RequestOptions();
        SyncRequestClient syncRequestClientTiansc = SyncRequestClient.create(analysis_API_KEY, analysis_secretKey, options);
        MyOrder myOrderYou = getMyOrder(myOrderPath);
        QingCang3 qingCang3 = new QingCang3();
        ObjectMapper objectMapper = new ObjectMapper();
        
        JSONObject otherOrderTest = new JSONObject();
        Position positionTest = new Position();
        
        if(ceShi){
            otherOrderTest = readJsonFile("E://code//biance//otherOrder.json");
        }

        while (true) {

            try {
                nowTimeTest += 60 * 1000L;
                //获取其他人的订单
                Map<String, JSONArray> portIdMap = new HashMap<>();
                Map<String, BigDecimal> portIdCountMap = new HashMap<>();
                Iterator<Object> iterator = analysis_genPortfolioIds.iterator();
                while (iterator.hasNext()) {
                    if(!ceShi){
                        Thread.sleep(Long.parseLong(PrivateConfig.shiJian));
                    }
                    JSONObject analysis_genPortfolioId = (JSONObject) iterator.next();
                    String genPortfolioId = analysis_genPortfolioId.getString("genPortfolioId");
                    String name = analysis_genPortfolioId.getString("name");
                    BigDecimal count = analysis_genPortfolioId.getBigDecimal("count");
                    JSONArray jsonArray = getOtherOrderTest(genPortfolioId, otherOrderTest);
                    if(jsonArray == null){
                        //移除无效的带单，防止一直报错
                        T5.searchAll(name + "：无效了");
                        iterator.remove(); // ✅ 迭代器删除，安全
                    }
                    if(CollectionUtils.isEmpty(jsonArray)){
                        continue;
                    }
                    portIdMap.put(genPortfolioId, jsonArray);
                    portIdCountMap.put(genPortfolioId, count);
                }

                Map<String, Order> otherOrderMap = method2(portIdCountMap, portIdMap);
                TherOrder therOrder = new TherOrder(otherOrderMap);

                //获取我的订单
                Position positionYou = getMyOrderListTest(syncRequestClientTiansc, positionTest);
                int fenShuYou = 0;
//                positionYou = null;
                if(positionYou != null){
                    fenShuYou = positionYou.getPositionAmt().divide(PrivateConfig.analysis_money).abs().intValue();
                }

                PrivateConfig.printLog(PrivateConfig.getCurrentTime() + "，我们购买个数：" + fenShuYou);
                PrivateConfig.printLog("他人订单数据：" + JSON.toJSON(therOrder));

                if(otherOrderMap.isEmpty()){
                    if(positionYou != null){
                        myOrderYou = sell(qingCang3, positionYou, positionTest, nowTimeTest);
                    }
                }

                Order orderNew = new Order();
                orderNew.setSymbol(analysis_symbol);
                Set<String> portIdSetNew = new TreeSet<>();

                if(positionYou == null){
                    if (therOrder.getCount() >= analysis_peopleCount) {
                        if (therOrder.getMinCount() == 0) {
                            if (therOrder.getCount() == analysis_peopleCount) {
                                portIdSetNew = buy(orderNew, analysis_money2, 2, myOrderYou, therOrder, fenShuYou);
                            } else if (therOrder.getCount() == analysis_peopleCount + 1) {
                                portIdSetNew = buy(orderNew, analysis_money3, 3, myOrderYou, therOrder, fenShuYou);
                            } else if (therOrder.getCount() > analysis_peopleCount + 1) {
                                portIdSetNew = buy(orderNew, analysis_money4, 4, myOrderYou, therOrder, fenShuYou);
                            }

                        } else {
                            if (therOrder.getCount() > analysis_peopleCount + 2) {
                                if (therOrder.getMinCount() == 1) {
                                    portIdSetNew = buy(orderNew, analysis_money2, 2, myOrderYou, therOrder, fenShuYou);
                                }
                            }
                        }
                    }
                }else {
                    if(otherOrderMap.isEmpty()) {
                        myOrderYou = sell(qingCang3, positionYou, positionTest,nowTimeTest);
                    }else {
                        if(therOrder.getMinCount() == 0){
                            if(therOrder.getPositionSide().equals(positionYou.getPositionSide())){
                                if (therOrder.getCount() < analysis_peopleCount) {
                                    if(isQingKong(myOrderYou, otherOrderMap)){
                                        myOrderYou = sell(qingCang3, positionYou, positionTest,nowTimeTest);
                                    }
                                }else if (therOrder.getCount() == analysis_peopleCount) {

                                }else if (therOrder.getCount() == analysis_peopleCount + 1) {
                                    if(fenShuYou == 2){
                                        portIdSetNew = buy(orderNew, analysis_money, 1, myOrderYou, therOrder, fenShuYou);
                                    }
                                }else if (therOrder.getCount() > analysis_peopleCount + 1) {
                                    if(fenShuYou == 2){
                                        portIdSetNew = buy(orderNew, analysis_money2, 2, myOrderYou, therOrder, fenShuYou);
                                    }if(fenShuYou == 3){
                                        portIdSetNew = buy(orderNew, analysis_money, 1, myOrderYou, therOrder, fenShuYou);
                                    }
                                }
                            }else {
                                myOrderYou = sell(qingCang3, positionYou, positionTest,nowTimeTest);
                            }
                        }else if(therOrder.getMinCount() == 1){
                            if (therOrder.getCount() <= analysis_peopleCount + 2) {
                                myOrderYou = sell(qingCang3, positionYou, positionTest,nowTimeTest);
                            }else {
                                if(!therOrder.getPositionSide().equals(positionYou.getPositionSide())){
                                    myOrderYou = sell(qingCang3, positionYou, positionTest,nowTimeTest);
                                }
                            }
                        }else if(therOrder.getMinCount() > 1){
                            myOrderYou = sell(qingCang3, positionYou, positionTest,nowTimeTest);
                        }
                    }
                }


                orderNew.setSide(OrderSide.BUY.toString());
                if(therOrder.getPositionSide().equals(PositionSide.SHORT.toString())){
                    orderNew.setSide(OrderSide.SELL.toString());
                }
                orderNew.setPositionSide(therOrder.getPositionSide());


                if (orderNew.getOrigQty() != null) {
                    //下单
                    for (JSONObject person : PrivateConfig.genDan_personInfoList) {
                        try {
                            if(!ceShi){
                                MulPostOrders mulGetAllOrders = new MulPostOrders(person, orderNew);
                                threadPoolExecutor.submit(mulGetAllOrders);//启动一般的线程
                                //两个账号之间执行的间隔
                                Thread.sleep(50);
                            }else {
                                if(positionTest != null){
                                    positionTest.setPositionAmt(positionTest.getPositionAmt().add(orderNew.getOrigQty()));
                                }else {
                                    positionTest = new Position();
                                    positionTest.setPositionAmt((orderNew.getOrigQty()));
                                }

                            }

                        } catch (Exception e) {
                            PrivateConfig.printLog(e.getMessage());
                            T5.searchAll("连续三次，有问题，关闭软件，重新启动33。" + e.getMessage());
                        }
                    }
                }

                if(youCaoZuo){
                    //再次获取我的订单
                    positionYou = getMyOrderListTest(syncRequestClientTiansc, positionTest);
                    if(positionYou != null){
                        fenShuYou = positionYou.getPositionAmt().divide(PrivateConfig.analysis_money).abs().intValue();
                    }
                }

                //把现有单子写入文件里，重启时用
                if(fenShuYou == 0){
                    if(CollectionUtils.isNotEmpty(myOrderYou.otherPortIdSet)){
                        myOrderYou.otherPortIdSet.clear();;
                    }
                    myOrderYou.positionSide = null;
                }else {
                    for(String s : portIdSetNew){
                        myOrderYou.setOtherPortIdSet(s);
                    }
                    if(StringUtils.isBlank(myOrderYou.getPositionSide())){
                        myOrderYou.setPositionSide(orderNew.getPositionSide());
                    }
                }
//                String json = objectMapper.writeValueAsString(myOrderYou);
                objectMapper.writeValue(new File(myOrderPath + "/myOrder.json"), myOrderYou);
                youCaoZuo = false;

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

        }



    }

    static Position getMyOrderListTest(SyncRequestClient syncRequestClientTiansc, Position myOrderListTest) throws InterruptedException {
        if(!ceShi){
            AccountInformation accountInformationTian = PrivateConfig.getAccountInformation(syncRequestClientTiansc, threadPoolExecutor);
            List<Position> positionListTian = accountInformationTian.getPositions();
            for (Position position : positionListTian) {
                if (position.getPositionAmt().abs().compareTo(ling) > 0) {
                    if(analysis_symbol.equals(position.getSymbol())){
                        return position;
                    }
                }
            }
            return null;
        }else {
            return myOrderListTest;
        }
    }
    static JSONArray getOtherOrderTest(String genPortfolioId, JSONObject otherOrder) throws InterruptedException {
        if(!ceShi){
            return GetPositions.getOrders(threadPoolExecutor, genPortfolioId);
        }else {
            JSONArray jsonArray = otherOrder.getJSONArray(genPortfolioId);
            if(CollectionUtils.isNotEmpty(jsonArray)){
                for (Object o : jsonArray) {
                    JSONObject trade = (JSONObject) o;
                    Long sTime = trade.getLong("opened");
                    Long endTime = trade.getLong("closed");
                    if(sTime<nowTimeTest && nowTimeTest<endTime){
                        JSONArray jsonArray1 = new JSONArray();
                        jsonArray1.add(trade);
                        return jsonArray1;
                    }
                }
            }
            return new JSONArray();
        }
    }


    static Set<String> buy(Order orderNew, BigDecimal Qty, int count, MyOrder myOrderYou, TherOrder therOrder, int fenShuYou){
        youCaoZuo = true;
        //购买时，大于4份肯定是不对的
        if(fenShuYou + count > 4){
            return therOrder.getPortIdSet();
        }
        if(ceShi){
            BigDecimal jiaGe = getSymbolRice(analysis_symbol, nowTimeTest);
            buyMoney += Qty.multiply(jiaGe).floatValue();
            System.out.println("买：" + Qty + "，价格：" + jiaGe);
        }
        orderNew.setOrigQty(Qty);
        myOrderYou.setFunShu(count);
        return therOrder.getPortIdSet();
    }


    static MyOrder sell(QingCang3 qingCang3, Position position, Position positionTest, Long nowTimeTest){
        youCaoZuo = true;
        if(!ceShi){
            qingCang3.qingCang(genDan_personInfoList, analysis_symbol, null);
        }else {
            BigDecimal jiaGe = getSymbolRice(analysis_symbol, nowTimeTest);
            float zhengQian;
            if(PositionSide.LONG.toString().equals(position.getPositionSide())){
                zhengQian = position.getPositionAmt().multiply(jiaGe).floatValue() - buyMoney;
            }else {
                zhengQian = buyMoney - position.getPositionAmt().multiply(jiaGe).floatValue();
            }
            winMoney += zhengQian;
            System.out.println("卖：" + position.getPositionAmt() + "，方向：" + position.getPositionSide() + "，价格：" + jiaGe + "，挣钱：" + zhengQian);
            position.setPositionAmt(null);
            System.out.println("累计挣钱：" + winMoney);
            buyMoney = 0f;
            System.out.println("--------------------------------");
            positionTest = null;
        }

        return new MyOrder();
    }

    public static BigDecimal getSymbolRice(String symbol, Long time){
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("symbol", symbol);
        parameters.put("startTime", time);
        parameters.put("endTime", time + 3000L);
        parameters.put("limit", 1);
        Market market = new SpotClientImpl().createMarket();
        String s = market.aggTrades(parameters);
        JSONArray jsonArray = JSONObject.parseArray(s);
        return ((JSONObject) jsonArray.get(0)).getBigDecimal("p");
    }

    /**
     * 我是根据张三、李四买的，当张三和李四都清仓了，王五又买了，此时我也跟着清仓
     * @param myOrderYou
     * @param otherOrderMap
     * @return
     */
    static boolean isQingKong(MyOrder myOrderYou, Map<String, Order> otherOrderMap){
        Set<String> portIdSet = myOrderYou.getOtherPortIdSet();
        for(String s : portIdSet){
            if(otherOrderMap.get(s) != null){
                return false;
            }
        }
        return true;
    }

    static MyOrder getMyOrder(String myOrderPath){
        JSONObject myOrderJ = readJsonFile(myOrderPath + "/myOrder.json");
        MyOrder myOrder = new MyOrder();

        if(myOrderJ == null || myOrderJ.isEmpty()){
            return myOrder;
        }
        if(myOrderJ.getInteger("funShu") == 0){
            return myOrder;
        }
        myOrder.setFunShu(myOrderJ.getInteger("funShu"));
        myOrder.setPositionSide(myOrderJ.getString("positionSide"));
        for(Object object : myOrderJ.getJSONArray("otherPortIdSet")){
            myOrder.setOtherPortIdSet((String) object);
        }

        return myOrder;
    }


    /**
     * 根据币种筛选我们需要的ETH
     * 只去一个方向的单子，如果双向同时存在，则舍去
     * 把所有单子都转换成双向的
     * @param portIdMap
     * @return 谁真正有单子
     */
    static Map<String, Order> method2(Map<String, BigDecimal> portIdCountMap, Map<String, JSONArray> portIdMap){
        Map<String, Order> orderMap = new HashMap<>();

        if(portIdMap.isEmpty()){
            return orderMap;
        }
        Map<String, JSONObject> portIdMapNew = new HashMap<>();
        for(Map.Entry<String, JSONArray> entry : portIdMap.entrySet()){
            JSONArray jsonArray = new JSONArray();
            for (Object o : entry.getValue()) {
                JSONObject trade = (JSONObject) o;
                String symbol = trade.getString(PrivateConfig.symbol);
                if(analysis_symbol.equals(symbol)){
                    jsonArray.add(trade);
                }
            }
            //有且有1个的时候，认为是有的，有2个时，证明是双向同时持仓的，就认为没有
            if(jsonArray.size()==1){
                portIdMapNew.put(entry.getKey(), (jsonArray.getJSONObject(0)));
            }
        }


        for(Map.Entry<String, JSONObject> trade : portIdMapNew.entrySet()) {
            String positionSide = trade.getValue().getString("positionSide").toUpperCase();
            BigDecimal qty = trade.getValue().getBigDecimal("positionAmount");
            String symbol = trade.getValue().getString(PrivateConfig.symbol);

            //拥有数量大于设定数量，证明是真的有单子
            if(qty.abs().compareTo(portIdCountMap.get(trade.getKey())) < 0){
                continue;
            }

            Order order = new Order();
            order.setSymbol(symbol);
            order.setPositionSide(getPositionSide(positionSide, qty));
            order.setOrigQty(qty);
            orderMap.put(trade.getKey(), order);
        }
        return orderMap;
    }

}
