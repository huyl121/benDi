package com.example.bian;

import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.model.trade.PositionRisk;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ChaKan {
    public static void main(String[] args) {
        System.setProperty("https.proxySet", "true");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyPort", "10819");
        args = new String[1];
        System.out.println("开始啦");
        args[0] = "E://code//biance";
        PrivateConfig.before(args[0], "0");
        ChaKan postOrder = new ChaKan();
        postOrder.method(PrivateConfig.genDans_personInfoList);
    }

    public void method( List<JSONObject> listPersonInfo ) {

        for (JSONObject personInfo : listPersonInfo) {
            try {
                SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient));
                /*List<PositionRisk> positionRiskList = syncRequestClient.getPositionRisk();
                Set<String> symbolSet = new TreeSet<>();
                for(PositionRisk positionRisk : positionRiskList){
                    if(!symbolSet.contains(positionRisk.getSymbol())){
                        System.out.println(positionRisk.getSymbol());
                        symbolSet.add(positionRisk.getSymbol());
                    }
                }*/
                System.out.println(personInfo.getString(PrivateConfig.alias) + "：" + syncRequestClient.getAccountInformation().getTotalMarginBalance());
//                System.out.println(personInfo.getString(PrivateConfig.alias) + "：" + syncRequestClient.getAccountInformation());
            } catch (Exception e) {
                System.out.println(personInfo.getString(PrivateConfig.alias));
                e.printStackTrace();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}