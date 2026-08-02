package com.example.bian.xin;

import com.alibaba.fastjson.JSONObject;
import com.example.bian.QingCang;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import com.example.bian.client.model.enums.*;
import com.example.bian.client.model.trade.Order;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class KaiDuo3 {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("https.proxySet", "true");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyPort", "10819");
        args = new String[2];
        System.out.println("开始啦");
        args[0] = "E://code//biance";
        args[1] = "KaiDuo3";

        PrivateConfig.before(args[0], args[1]);
        PrivateConfig.getJGXsw();
        PrivateConfig.xsw(true);



        KaiDuo3 postOrder = new KaiDuo3();
        postOrder.method1(PrivateConfig.genDan_personInfoList);
    }


    public void method1(List<JSONObject> listPersonInfo) throws InterruptedException {


        System.out.println("开多测试");
        for (JSONObject personInfo : listPersonInfo) {
            try {
                SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient));
                Order myOrder;
                if(PrivateConfig.both.equals("1")){
                    myOrder = syncRequestClient.postOrder(
                            "BTCUSDT",
                            OrderSide.valueOf("BUY"),//买还是卖
                            PositionSide.valueOf("BOTH"),//做多还是做空 long SHORT both
                            OrderType.valueOf("LIMIT"),// 订单类型，LIMIT：限价单；MARKET：市价单（想要成功买卖，使用这个）
                            TimeInForce.valueOf("GTC"),//,//成交为止，一直有效，不用管
                            "0.2",//跟单数量，需要大于5
                            "1000",//跟单单价，总价需要大于5（市价时，可以不填）
                            "FALSE",//order.getReduceOnly().toString(),
                            null,//order.getClientOrderId(),
                            null,//order.getStopPrice().toString(),
                            null,//WorkingType.valueOf(order.getWorkingType()),
                            NewOrderRespType.RESULT);
                }else {
                    myOrder = syncRequestClient.postOrder(
                            "BTCUSDT",
                            OrderSide.valueOf("BUY"),//买还是卖
                            PositionSide.valueOf("LONG"),//做多还是做空 long SHORT both
                            OrderType.valueOf("LIMIT"),// 订单类型，LIMIT：限价单；MARKET：市价单（想要成功买卖，使用这个）
                            TimeInForce.valueOf("GTC"),//,//成交为止，一直有效，不用管
                            "0.2",//跟单数量，需要大于5
                            "1000",//跟单单价，总价需要大于5（市价时，可以不填）
                            null,//order.getReduceOnly().toString(),
                            null,//order.getClientOrderId(),
                            null,//order.getStopPrice().toString(),
                            null,//WorkingType.valueOf(order.getWorkingType()),
                            NewOrderRespType.RESULT);
                }

                Thread.sleep(3000);
                syncRequestClient.cancelOrder(myOrder.getSymbol(), myOrder.getOrderId(), myOrder.getClientOrderId());

            } catch (Exception e) {
                System.out.println(personInfo.getString(PrivateConfig.alias) + "开多失败");
                T5.searchAll(personInfo.getString(PrivateConfig.alias) + "开多失败");
                e.printStackTrace();
            }
        }
        Thread.sleep(3000);
        System.out.println("开空测试");
        for (JSONObject personInfo : listPersonInfo) {
            try {
                SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient));

                Order myOrder;
                if(PrivateConfig.both.equals("1")){
                    myOrder = syncRequestClient.postOrder(
                            "ETHUSDT",
                            OrderSide.valueOf("SELL"),//买还是卖
                            PositionSide.valueOf("BOTH"),//做多还是做空 long SHORT both
                            OrderType.valueOf("LIMIT"),// 订单类型，LIMIT：限价单；MARKET：市价单（想要成功买卖，使用这个）
                            TimeInForce.valueOf("GTC"),//,//成交为止，一直有效，不用管
                            "0.01",//跟单数量，需要大于5
                            "10000",//跟单单价，总价需要大于5（市价时，可以不填）
                            "FALSE",//order.getReduceOnly().toString(),
                            null,//order.getClientOrderId(),
                            null,//order.getStopPrice().toString(),
                            null,//WorkingType.valueOf(order.getWorkingType()),
                            NewOrderRespType.RESULT);
                }else {
                    myOrder = syncRequestClient.postOrder(
                            "ETHUSDT",
                            OrderSide.valueOf("SELL"),//买还是卖
                            PositionSide.valueOf("SHORT"),//做多还是做空 long SHORT both
                            OrderType.valueOf("LIMIT"),// 订单类型，LIMIT：限价单；MARKET：市价单（想要成功买卖，使用这个）
                            TimeInForce.valueOf("GTC"),//,//成交为止，一直有效，不用管
                            "0.01",//跟单数量，需要大于5
                            "10000",//跟单单价，总价需要大于5（市价时，可以不填）
                            null,//order.getReduceOnly().toString(),
                            null,//order.getClientOrderId(),
                            null,//order.getStopPrice().toString(),
                            null,//WorkingType.valueOf(order.getWorkingType()),
                            NewOrderRespType.RESULT);
                }
                Thread.sleep(3000);
                syncRequestClient.cancelOrder(myOrder.getSymbol(), myOrder.getOrderId(), myOrder.getClientOrderId());

            } catch (Exception e) {
                System.out.println(personInfo.getString(PrivateConfig.alias) + "开空失败");
                T5.searchAll(personInfo.getString(PrivateConfig.alias) + "开空失败");
                e.printStackTrace();
            }
        }


        System.out.println("测试完成！");
    }

    /*public void method(String[] args) {

        List<JSONObject> listPersonInfo =  PrivateConfig.personInfoList;

        String symbol = "ETHUSDT";
        String num = "0.005";
        System.out.println("开多测试");
        for (JSONObject personInfo : listPersonInfo) {
            try {
                SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient));
                Order myOrder = syncRequestClient.postOrder(
                        symbol,
                        OrderSide.valueOf("BUY"),//买还是卖
                        PositionSide.valueOf("BOTH"),//做多还是做空 long SHORT both
                        OrderType.valueOf("MARKET"),// 订单类型，limit：限价单；MARKET：市价单（想要成功买卖，使用这个）
                        null,//TimeInForce.valueOf("GTC"),//成交为止，一直有效，不用管
                        num,//跟单数量，需要大于5
                        null,//跟单单价，总价需要大于5（市价时，可以不填）
                        "FALSE",//order.getReduceOnly().toString(),
                        null,//order.getClientOrderId(),
                        null,//order.getStopPrice().toString(),
                        null,//WorkingType.valueOf(order.getWorkingType()),
                        NewOrderRespType.RESULT);
                System.out.println(myOrder);
            } catch (Exception e) {
                System.out.println(personInfo.getString(PrivateConfig.alias) + "开多失败");
                e.printStackTrace();
            }
        }

        QingCang3 qingCang = new QingCang3();
        qingCang.qingCang(listPersonInfo, null);

        System.out.println("开空测试");
        for (JSONObject personInfo : listPersonInfo) {
            try {
                SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient));
                Order myOrder = syncRequestClient.postOrder(
                        symbol,
                        OrderSide.valueOf("SELL"),//买还是卖
                        PositionSide.valueOf("BOTH"),//做多还是做空 long SHORT both
                        OrderType.valueOf("MARKET"),// 订单类型，limit：限价单；MARKET：市价单（想要成功买卖，使用这个）
                        null,//TimeInForce.valueOf("GTC"),//成交为止，一直有效，不用管
                        num,//跟单数量，需要大于5
                        null,//跟单单价，总价需要大于5（市价时，可以不填）
                        "FALSE",//order.getReduceOnly().toString(),
                        null,//order.getClientOrderId(),
                        null,//order.getStopPrice().toString(),
                        null,//WorkingType.valueOf(order.getWorkingType()),
                        NewOrderRespType.RESULT);
                System.out.println(myOrder);
            } catch (Exception e) {
                System.out.println(personInfo.getString(PrivateConfig.alias) + "开空失败");
                e.printStackTrace();
            }
        }

        qingCang.qingCang(listPersonInfo, null);

        System.out.println("测试完成！");
    }*/
}