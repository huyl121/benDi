package com.example.bian;

import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.model.enums.NewOrderRespType;
import com.example.bian.client.model.enums.OrderSide;
import com.example.bian.client.model.enums.OrderType;
import com.example.bian.client.model.enums.PositionSide;
import com.example.bian.client.model.trade.Order;

import java.util.List;

public class KaiDuo {
    public static void main(String[] args) {
        System.setProperty("https.proxySet", "true");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyPort", "10819");
        args = new String[1];
        System.out.println("开始啦");
        args[0] = "E://baidutongbu//baidutongbu//tongbu//bian//bian";
        PrivateConfig.before(args[0], args[1]);
        KaiDuo postOrder = new KaiDuo();
        postOrder.method(args);
    }

    public void method(String[] args) {

        List<JSONObject> listPersonInfo =  PrivateConfig.personInfoList;

        String symbol = "IMXUSDT";
        String num = "3";
        System.out.println("开多测试");
        for (JSONObject personInfo : listPersonInfo) {
            try {
                SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient));
                Order myOrder = syncRequestClient.postOrder(
                        symbol,
                        OrderSide.valueOf("BUY"),//买还是卖
                        PositionSide.valueOf("LONG"),//做多还是做空 long SHORT both
                        OrderType.valueOf("MARKET"),// 订单类型，limit：限价单；MARKET：市价单（想要成功买卖，使用这个）
                        null,//TimeInForce.valueOf("GTC"),//成交为止，一直有效，不用管
                        num,//跟单数量，需要大于5
                        null,//跟单单价，总价需要大于5（市价时，可以不填）
                        null,//order.getReduceOnly().toString(),
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

        QingCang qingCang = new QingCang();
        qingCang.qingCang(listPersonInfo, null);

        System.out.println("开空测试");
        for (JSONObject personInfo : listPersonInfo) {
            try {
                SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient));
                Order myOrder = syncRequestClient.postOrder(
                        symbol,
                        OrderSide.valueOf("SELL"),//买还是卖
                        PositionSide.valueOf("SHORT"),//做多还是做空 long SHORT both
                        OrderType.valueOf("MARKET"),// 订单类型，limit：限价单；MARKET：市价单（想要成功买卖，使用这个）
                        null,//TimeInForce.valueOf("GTC"),//成交为止，一直有效，不用管
                        num,//跟单数量，需要大于5
                        null,//跟单单价，总价需要大于5（市价时，可以不填）
                        null,//order.getReduceOnly().toString(),
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
    }
}