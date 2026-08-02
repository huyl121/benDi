package com.example.bian.genDan;

import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.model.enums.OrderSide;
import com.example.bian.client.model.enums.PositionSide;
import com.example.bian.xin.QingCang3;

import java.util.concurrent.*;

public class ZaoShuJu {


    public static void main(String[] args) throws InterruptedException {

        // 对https也开启代理
        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10819");

        args = new String[2];
        PrivateConfig.printLog("开始啦");
        args[0] = "E://code//biance";
        args[1] = "0-genDan";
        PrivateConfig.before(args[0], args[1]);
//        PrivateConfig.getJGXsw();
//        PrivateConfig.xsw(true);

        int mai = 0;
        /*
        *
        * 1、单向持仓，价差保护关
        * 2、调整倍数，这个很重要，根据安卓手机调整到最大u
        * 3、下个小单试试
        * 4、两个账号都操作成功后，再下大单
        * 比特币，10倍杠杆，1600的资金能用15000
        * */
//        String symbol = "BTC" + "USDT";
        String symbol = "STX" + "USDT";
        String getOrigQty = "5500";
        if(mai == 1){
            ZaoShuJu genDan = new ZaoShuJu();
            genDan.method(symbol, getOrigQty);
        }else {

            ThreadPoolExecutor threadPoolExecutor =
                    new ThreadPoolExecutor(2, 2, 2,
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(),
                            Executors.defaultThreadFactory(),
                            new ThreadPoolExecutor.DiscardPolicy());

            Callable callable1 = new Callable() {
                @Override
                public String call() throws Exception {
                    QingCang3 qingCang3 = new QingCang3();
                    qingCang3.qingCang(PrivateConfig.genDan_personInfoList, symbol, PositionSide.BOTH.toString());
                    return "";
                }
            };
            threadPoolExecutor.submit(callable1);

            Callable callable2 = new Callable() {
                @Override
                public String call() throws Exception {
                    QingCang3 qingCang3 = new QingCang3();
                    qingCang3.qingCang(PrivateConfig.personInfoList, symbol, PositionSide.BOTH.toString());
                    return "";
                }
            };
            threadPoolExecutor.submit(callable2);
        }

    }


    public static void method(String symbol, String getOrigQty) throws InterruptedException {


        String orderSide1 = OrderSide.BUY.toString();
//        String orderSide1 = OrderSide.SELL.toString();
        String orderSide2 = OrderSide.SELL.toString();

        if(OrderSide.SELL.toString().equals(orderSide1)){
            orderSide2 = OrderSide.BUY.toString();
        }
        for(JSONObject personInfo : PrivateConfig.genDan_personInfoList){
            SyncRequestClient syncRequestClient = (SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient);
            PrivateConfig.postOrder(syncRequestClient, symbol, orderSide1, PositionSide.BOTH.toString(), "FALSE", getOrigQty);
            Thread.sleep(50);
        }

        for(JSONObject personInfo : PrivateConfig.personInfoList){
            SyncRequestClient syncRequestClient = (SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient);
            PrivateConfig.postOrder(syncRequestClient, symbol, orderSide2, PositionSide.BOTH.toString(), "FALSE", getOrigQty);
            Thread.sleep(50);
        }

    }






}
