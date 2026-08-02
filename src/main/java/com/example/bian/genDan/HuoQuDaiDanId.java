package com.example.bian.genDan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.bushu.PrivateConfig;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;

public class HuoQuDaiDanId {

    public static void main(String[] args) throws InterruptedException, IOException {

        // 对https也开启代理
        System.out.println("开代理");
        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10819");

        args = new String[2];
        PrivateConfig.printLog("开始啦");
        args[0] = "E://code//biance";
        args[1] = "0-genDan";
        PrivateConfig.before(args[0], args[1]);


        PrivateConfig.getJGXsw();
        PrivateConfig.xsw(true);

        HuoQuDaiDanId jianKongSol = new HuoQuDaiDanId();
        jianKongSol.method();

    }

    public void method() throws InterruptedException, IOException {
        JSONObject param = new JSONObject();
        param.put("pageSize", 50);
        param.put("timeRange", "30D");
        param.put("dataType", "ROI");
        param.put("favoriteOnly", false);
        param.put("hideFull", false);
        param.put("nickname", "");
        param.put("order", "DESC");
        param.put("userAsset", 0);
        param.put("portfolioType", "PUBLIC");
        param.put("useAiRecommended", false);
        param.put("apiKeyOnly", false);
        param.put("lockPeriod", null);
        for (int i=1; i<2; i++){
            param.put("pageNumber", i);
            String s = Postman.sendPost("https://" + PrivateConfig.genDan_url + "/bapi/futures/v1/friendly/future/copy-trade/home-page/query-list",
                    param.toJSONString(), null, null);

            if (StringUtils.isNotBlank(s)) {
                JSONObject jsonObject = JSON.parseObject(s);
                if ("000000".equals(jsonObject.getString("code"))) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray jsonArray = data.getJSONArray("list");
                    for(Object o : jsonArray){
                        JSONObject jsonObject1 = (JSONObject) o;
                        String leadPortfolioId = jsonObject1.getString("leadPortfolioId");
                        String ss = "\"" + leadPortfolioId + "\",";
                        System.out.println(ss);
                    }
                }
            }

            Thread.sleep(1000 * 3);
        }

    }

}


