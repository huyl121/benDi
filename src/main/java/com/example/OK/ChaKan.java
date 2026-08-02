package com.example.OK;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.OK.conn.client.APIClient;
import com.example.OK.conn.trade.TradeAPIService;
import com.example.OK.conn.trade.impl.TradeAPI;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;

import java.math.BigDecimal;
import java.util.List;

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
        postOrder.method(PrivateConfig.daiDanOk_personInfoList);
    }

    public void method( List<JSONObject> listPersonInfo ) {

        for (JSONObject personInfo : listPersonInfo) {
            try {

                TradeAPIService tradeAPIService = (TradeAPIService) personInfo.get(PrivateConfig.tradeAPIService);
                APIClient apiClient = (APIClient) personInfo.get(PrivateConfig.apiClient);
                TradeAPI tradeAPI = (TradeAPI) personInfo.get(PrivateConfig.tradeAPI);
                JSONObject result = tradeAPIService.getAccountAndPosition(apiClient, tradeAPI, "SWAP");
                if(result.getString("code").equals("0")){
                    JSONArray data = result.getJSONArray("data");
                    BigDecimal qian = data.getJSONObject(0).getJSONArray("balData").getJSONObject(0).getBigDecimal("eq");
                    System.out.println(personInfo.getString(PrivateConfig.alias) + "：" + qian.toString());
                }
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