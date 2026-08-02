package com;

import bsh.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.RequestOptions;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import com.example.bian.client.model.market.ExchangeInfoEntry;
import com.example.bian.client.model.market.ExchangeInformation;
import com.sun.jna.platform.win32.WinDef;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static com.example.bian.client.bushu.PrivateConfig.both;
import static com.example.bian.client.bushu.PrivateConfig.readJsonFile;

public class Musk {


    public static void main(String[] args) throws InterruptedException, IOException {
        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10819");


        args = new String[2];
        args[0] = "E://code//biance";
        args[1] = "0-genDan";
        PrivateConfig.before(args[0], "0-" + args[1]);
        PrivateConfig.xsw(true);

        JSONObject config = readJsonFile("E:/code/biance/data.json");

//        JSONArray jsonArray = config.getJSONArray("data");

   /*map.put("1000LUNCUSDT",	135d	);
        map.put("ARUSDT",	1.5	);
        map.put("ATOMUSDT",	2.55	);
        map.put("BCHUSDT",	0.034	);
        map.put("BNBUSDT",	0.01	);
        map.put("BTCUSDT", 0.002);
        map.put("COMPUSDT", 0.193);
        map.put("ETHUSDT", 0.002);
        map.put("EVAAUSDT",	6.3	);
        map.put("GIGGLEUSDT",	0.08	);
        map.put("LUNA2USDT",	48d	);
        map.put("PIPPINUSDT",	11d	);
        map.put("USTCUSDT",	762d	);
        map.put("ZECUSDT",	0.012	);
        map.put("ZENUSDT",	0.7	);*/


        int isOk = 0; //0：币安，1：ok的概况，2：okd带单；3：聪明钱
        JSONArray jsonArray = new JSONArray();
        getOp(jsonArray, "4788776444236355328", System.currentTimeMillis(), isOk);
        BigDecimal beiShu = new BigDecimal("0.0262");
        Map<String, BigDecimal> mapCount = new HashMap();


        RequestOptions options = new RequestOptions();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, options);
        ExchangeInformation exchangeInformation = syncRequestClient.getExchangeInformation();
        List<ExchangeInfoEntry> exchangeInfoEntryList = exchangeInformation.getSymbols();
        Map<String, BigDecimal> zuiDiMoney = new HashMap<>();//币种最低购买金额
        for (ExchangeInfoEntry entry : exchangeInfoEntryList) {

            List<List<Map<String, String>>> filters = entry.getFilters();
            for (List<Map<String, String>> filter : filters) {
                //查找每个币种最低购买金额
                for (Map<String, String> map : filter) {
                    if (map.values().contains("MIN_NOTIONAL")) {
                        for (Map<String, String> map1 : filter) {
                            if (map1.get("notional") != null) {
                                zuiDiMoney.put(entry.getSymbol(), new BigDecimal(map1.get("notional")));
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }

        Double zongShu = 0d;
        Double chengGong = 0d;
        for (Object o : jsonArray) {

            JSONObject jsonObject = (JSONObject) o;
            String symbol = jsonObject.getString("symbol");
            if(isOk == 1){
                symbol = jsonObject.getString("baseName") + "USDT";
            }else if(isOk == 2){
                symbol = jsonObject.getString("instId").split("-")[0] + "USDT";
            }

            if (!mapCount.containsKey(symbol)) {
                try{
                    BigDecimal markPrice = syncRequestClient.getMarkPrice(symbol).get(0).getMarkPrice();
                    if(markPrice != null && zuiDiMoney.get(symbol)!=null){
                        BigDecimal count = zuiDiMoney.get(symbol).divide(markPrice, PrivateConfig.getXSM(symbol), BigDecimal.ROUND_UP);
                        mapCount.put(symbol, count);
                    }
                    Thread.sleep(100);
                }catch (Exception e){
                    mapCount.put(symbol, null);
                }


            }


            BigDecimal executedQty = jsonObject.getBigDecimal("executedQty");
            if(isOk == 1){
                BigDecimal value = jsonObject.getBigDecimal("value");
                BigDecimal avgPx = jsonObject.getBigDecimal("avgPx");
                executedQty = value.divide(avgPx, 4, BigDecimal.ROUND_HALF_UP);
            }else if(isOk == 2){
                BigDecimal value = jsonObject.getBigDecimal("margin");
                BigDecimal avgPx = jsonObject.getBigDecimal("openAvgPx");
                executedQty = value.divide(avgPx, 4, BigDecimal.ROUND_HALF_UP);
            }
            if (mapCount.get(symbol) != null) {
                zongShu++;
                if (executedQty.multiply(beiShu).compareTo(mapCount.get(symbol)) >= 0) {
                    chengGong++;
                } else {

                    System.out.println();
                    System.out.println("失败的：" + symbol + ";" + executedQty.multiply(beiShu));

                    /*BigDecimal bei2 = executedQty.multiply(beiShu).multiply(new BigDecimal("2")).min(mapCount.get(symbol));
                    if (bei2.compareTo(mapCount.get(symbol)) >= 0) {
                        chengGong++;
                    } else {
                        System.out.println();
                        System.out.println("失败的：" + symbol + ";" + executedQty.multiply(beiShu));
                    }*/
                }
            }
        }
        System.out.println(zongShu);
        System.out.println(chengGong / zongShu);
    }

    public static void getOp(JSONArray list, String portfolioId, Long currentTime, int isOk) throws IOException, InterruptedException {
        if (list.size() > 299) {
            return;
        }
        if(isOk == 1){
            //概况里的操作记录
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            String url = "https://www.okx.com/priapi/v5/ecotrade/public/community/user/trade-records?instType=SWAP&limit=50&uniqueName=" + portfolioId + "&endModify=" + currentTime;
            Request request = new Request.Builder()
                    .url(url)
//                    .method("GET", body)
                    .addHeader("authority", "www.okx.com")
                    .addHeader("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"")
                    .addHeader("x-locale", "zh_CN")
                    .addHeader("x-cdn", "https://www.okx.com")
                    .addHeader("x-fptoken-signature", "{P1363}bQULTqCLrv0+mNx+BkwAhU6eHt5b4/okHrRd2wECSGFD6ug2eN3zEd9rbm/iyFqiOAzxzhgMFXtjT4CEVxYMkQ==")
                    .addHeader("x-site-info", "==QfxojI5RXa05WZiwiIMFkQPx0Rfh1SPJiOiUGZvNmIsICUKJiOi42bpdWZyJye")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .addHeader("x-request-timestamp", "1764160447061")
                    .addHeader("devid", "acf2ec83-51bb-4a21-95c0-8a82d657d690")
                    .addHeader("app-type", "web")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36")
//                .addHeader("x-fptoken", "eyJraWQiOiIxNjgzMzgiLCJhbGciOiJFUzI1NiJ9.eyJpYXQiOjE3NjQxNjA0MjUsImVmcCI6IjdXdVNta2FYTjRRNlRrSWh2SjVSb3lEWFh2dEtyK0tlaXRrOFIxSG5Ra0padGR5UklDMFVZRGZmeHJQVEVHQ0siLCJkaWQiOiJhY2YyZWM4My01MWJiLTRhMjEtOTVjMC04YTgyZDY1N2Q2OTAiLCJjcGsiOiJNRmt3RXdZSEtvWkl6ajBDQVFZSUtvWkl6ajBEQVFjRFFnQUUxOHg1eFIzdWVXU1J0Q0RzSklVUnQybnM1cVppWVgyUktvcE1kS05kdDYrWFNIUmd1RTVWeFRZOHN1elBobDF2K1JTZzdhUGx1WUNhcDcvMnZuc21rZz09In0.JCxGQH8da1YFsmEQM7pf-hKGrAm4DZbqxWsDJF8_ZIf-eNfgjXRguHHD4XgITzNcCcWkLHnZXaa2uuxLxx1oCQ")
                    .addHeader("accept", "application/json")
                    .addHeader("x-utc", "8")
                    .addHeader("x-simulated-trading", "undefined")
                    .addHeader("x-zkdex-env", "0")
                    .addHeader("x-id-group", "2140841604188600002-c-13")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("referer", "https://www.okx.com/zh-hans/copy-trading/account/E512EAA2C34FAF44?tab=trade")
                    .addHeader("accept-language", "zh-CN,zh;q=0.9")
//                .addHeader("cookie", "traceId=2140841604188600002; devId=acf2ec83-51bb-4a21-95c0-8a82d657d690; ok_site_info===QfxojI5RXa05WZiwiIMFkQPx0Rfh1SPJiOiUGZvNmIsICUKJiOi42bpdWZyJye; locale=zh_CN; ok-exp-time=1764160418868; ok_prefer_currency=0%7C1%7Cfalse%7CUSD%7C2%7C%24%7C1%7C1%7C%E7%BE%8E%E5%85%83; ok_prefer_cm=3; ok_prefer_udColor=0; ok_prefer_udTimeZone=0; __cf_bm=RwhDqY1fqhdNgjI4C7uO.rUYthFz6CMvy3kMDg6pmWg-1764160418-1.0.1.1-B7nAcyRBWQe8D0HmldAZx9grRCEUaJS82deEKrAkliTE6SkYEcxIzS.Lns.JSCf0k5dqnuDwq_EN4oZuXgxncQpmhWZSRnpXTGfa8kwyoXc; okg.currentMedia=xl; ok_global={%22okg_m%22:%22xl%22}; fingerprint_id=acf2ec83-51bb-4a21-95c0-8a82d657d690; ok-ses-id=ajuHiUWTm/zslROvEKPEt7qwLR9/BgOTcfqxRE/InVLeg5NOatR3+DkBjFgWh+vFJMIJtKLGLHYSf3YXggTP2lyDg//Rfy/Tr19SS+lOxNKIM3tpD/oPTZcscHME3SH0; tmx_session_id=8aew8z5ezvy_1764160423676; fp_s=0; _monitor_extras={\"deviceId\":\"Kmq_RLiCpxjbN9gjGusBsv\",\"eventId\":6,\"sequenceNumber\":6}")
                    .build();
            Response response = client.newCall(request).execute();
            String s = response.body().string();
            if (StringUtils.isNotBlank(s)) {
                JSONObject jsonObject = JSON.parseObject(s);
                if ("0".equals(jsonObject.getString("code"))) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (!CollectionUtils.isEmpty(jsonArray)) {
                        list.addAll(jsonArray);
                        JSONObject jsonObject1 = (JSONObject) jsonArray.get(jsonArray.size() - 1);
                        Thread.sleep(1000);
                        getOp(list, portfolioId, jsonObject1.getLong("fillTime"), isOk);
                    }
                }
            }

        }else if(isOk == 2){
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            String url = "https://www.okx.com/priapi/v5/ecotrade/public/position-history?size=50&instType=SWAP&uniqueName=" + portfolioId + "&after=" + currentTime;
            if(CollectionUtils.isEmpty(list)){
                url = "https://www.okx.com/priapi/v5/ecotrade/public/position-history?size=50&instType=SWAP&uniqueName=" + portfolioId;
            }
            Request request = new Request.Builder()
                    .url(url)
//                    .method("GET", body)
                    .addHeader("accept", "application/json")
                    .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8")
                    .addHeader("app-type", "web")
                    .addHeader("devid", "97b4e334-0b5d-47ba-8221-b2811645558e")
                    .addHeader("priority", "u=1, i")
                    .addHeader("referer", "https://www.okx.com/zh-hans/copy-trading/account/823664FB73B79E41?tab=swap")
                    .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"147\", \"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"147\"")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/147.0.0.0 Safari/537.36")
                    .addHeader("x-cdn", "https://www.okx.com")
                    .addHeader("x-fptoken", "eyJraWQiOiIxNjgzMzgiLCJhbGciOiJFUzI1NiJ9.eyJpYXQiOjE3NzU3ODY5MzcsImVmcCI6InBzSS9TOGtsUHVYNmNSN0pybXR1YUNtZzdPc0F6dnVTcmgxOUlBemRoSTRZdjBkRnNudy9GNGZ6Z3N2clhidFoiLCJkaWQiOiI5N2I0ZTMzNC0wYjVkLTQ3YmEtODIyMS1iMjgxMTY0NTU1OGUiLCJjcGsiOiJNRmt3RXdZSEtvWkl6ajBDQVFZSUtvWkl6ajBEQVFjRFFnQUVuTnUxQVF6MXlVaWlKUFBFZjdtWDJNMFozL2ZOTVcxdkFBTllQcEEvelZPRUxBYXcwUFp0T25KTnNlWkJsOXE0dW9qRU8vekptN2lyN3pheFNQeExlQT09In0.6Mk2TtULooEx6Bu5wdT6shpoZjKeqjHWXY9xhMOe-bGPVEdz_5zpTixaqS193EBNKNyondinROBL9kAzbk4qmw")
                    .addHeader("x-fptoken-signature", "{P1363}88up6jJzNy4PV1WA6VUSlI8ZlwrVHPwvZnhwh679nd6diE1LEmS8DYaiqAnvYO5voEKmJyVe06TpOmO+ZX5bqg==")
                    .addHeader("x-id-group", "2130157898613010004-c-11")
                    .addHeader("x-locale", "zh_CN")
                    .addHeader("x-request-timestamp", "1775789889585")
                    .addHeader("x-simulated-trading", "undefined")
                    .addHeader("x-site-info", "==QfxojI5RXa05WZiwiIMFkQPx0Rfh1SPJiOiUGZvNmIsIyRTJiOi42bpdWZyJye")
                    .addHeader("x-utc", "8")
                    .addHeader("x-zkdex-env", "0")
                    .addHeader("Cookie", "__cf_bm=BFKFdHBf_qYmVlTPUu5FHgi2DFIV4mgB8FkJRljW2t8-1775790044.186683-1.0.1.1-QBKwT1det0xHyHme6OtElNCaowQKBf4VXp2xblPcpTcOh5nCheGQzhIt0sbn8d2pBRubnDLjzm_0NvhAjAZ3eMvUiSszUUBU7L3vbhY5Bk44DTYXYBkD8WwP8sftp1ei")
                    .build();
            Response response = client.newCall(request).execute();
            String s = response.body().string();
            if (StringUtils.isNotBlank(s)) {
                JSONObject jsonObject = JSON.parseObject(s);
                if ("0".equals(jsonObject.getString("code"))) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (!CollectionUtils.isEmpty(jsonArray)) {
                        list.addAll(jsonArray);
                        JSONObject jsonObject1 = (JSONObject) jsonArray.get(jsonArray.size() - 1);
                        Thread.sleep(1000);
                        getOp(list, portfolioId, jsonObject1.getLong("id"), isOk);
                    }
                }
            }
        }else if(isOk == 3){
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://www.bmwweb.solutions/bapi/asset/v1/private/future/smart-money/profile/query-order-history?rows=50&topTraderId=4988811260243579393&marketType=UM&page=1")
//                    .method("GET", body)
                    .addHeader("accept", "*/*")
                    .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8")
                    .addHeader("bnc-uuid", "401c2a6e-6231-4f67-8aa4-9f13c995c1fb")
                    .addHeader("clienttype", "web")
                    .addHeader("content-type", "application/json")
                    .addHeader("csrftoken", PrivateConfig.genDan_token)
                    .addHeader("device-info", "eyJzY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA4MCIsImF2YWlsYWJsZV9zY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTAzMiIsInN5c3RlbV92ZXJzaW9uIjoiV2luZG93cyAxMCIsImJyYW5kX21vZGVsIjoidW5rbm93biIsInN5c3RlbV9sYW5nIjoiemgtQ04iLCJ0aW1lem9uZSI6IkdNVCswODowMCIsInRpbWV6b25lT2Zmc2V0IjotNDgwLCJ1c2VyX2FnZW50IjoiTW96aWxsYS81LjAgKFdpbmRvd3MgTlQgMTAuMDsgV2luNjQ7IHg2NCkgQXBwbGVXZWJLaXQvNTM3LjM2IChLSFRNTCwgbGlrZSBHZWNrbykgQ2hyb21lLzE0Ny4wLjAuMCBTYWZhcmkvNTM3LjM2IiwibGlzdF9wbHVnaW4iOiJQREYgVmlld2VyLENocm9tZSBQREYgVmlld2VyLENocm9taXVtIFBERiBWaWV3ZXIsTWljcm9zb2Z0IEVkZ2UgUERGIFZpZXdlcixXZWJLaXQgYnVpbHQtaW4gUERGIiwiY2FudmFzX2NvZGUiOiI1MDkzMGMzYiIsIndlYmdsX3ZlbmRvciI6Ikdvb2dsZSBJbmMuIChBTUQpIiwid2ViZ2xfcmVuZGVyZXIiOiJBTkdMRSAoQU1ELCBBTUQgUmFkZW9uKFRNKSBHcmFwaGljcyAoMHgwMDAwMTYzOCkgRGlyZWN0M0QxMSB2c181XzAgcHNfNV8wLCBEM0QxMSkiLCJhdWRpbyI6IjEyNC4wNDM0NzUyNzUxNjA3NCIsInBsYXRmb3JtIjoiV2luMzIiLCJ3ZWJfdGltZXpvbmUiOiJBc2lhL1NoYW5naGFpIiwiZGV2aWNlX25hbWUiOiJDaHJvbWUgVjE0Ny4wLjAuMCAoV2luZG93cykiLCJmaW5nZXJwcmludCI6ImJjZmFjZTRlYTY2MTc2MWYxZDM3MDBlMDE1YmI4MzdiIiwiZGV2aWNlX2lkIjoiIiwicmVsYXRlZF9kZXZpY2VfaWRzIjoiIn0=")
                    .addHeader("fvideo-id", "33b0efa389daa2cd9e34f7a7c92c7eed1b169daa")
                    .addHeader("if-none-match", "W/\"05769f2ade7ebfeff0fd4a1a710e83ac9\"")
                    .addHeader("lang", "zh-CN")
                    .addHeader("priority", "u=1, i")
                    .addHeader("referer", "https://www.bmwweb.solutions/zh-CN/smart-money/profile/4988811260243579393?rankingType=PNL&timeRange=30D")
                    .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"147\", \"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"147\"")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/147.0.0.0 Safari/537.36")
                    .addHeader("x-passthrough-token", "")
                    .addHeader("x-trace-id", "dba95fe4-4bb9-4e4b-aafc-1ea26b5341ae")
                    .addHeader("x-ui-request-trace", "dba95fe4-4bb9-4e4b-aafc-1ea26b5341ae")
                    .addHeader("cookie", PrivateConfig.genDan_cookie)
                    .build();
            Response response = client.newCall(request).execute();
            String s = response.body().string();
            if (StringUtils.isNotBlank(s)) {
                JSONObject jsonObject = JSON.parseObject(s);
                if ("000000".equals(jsonObject.getString("code"))) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (!CollectionUtils.isEmpty(jsonArray)) {
                        list.addAll(jsonArray.stream()
                                .limit(200)
                                .collect(JSONArray::new, JSONArray::add, JSONArray::addAll));
                        JSONObject jsonObject1 = (JSONObject) jsonArray.get(jsonArray.size() - 1);
                        Thread.sleep(1000);
                        getOp(list, portfolioId, jsonObject1.getLong("id"), isOk);
                    }
                }
            }
        }else {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"portfolioId\":\"" + portfolioId + "\",\"startTime\":0,\"pageSize\":50,\"endTime\":" + +currentTime + "}");
            Request request = new Request.Builder()
                    .url("https://www.binance.com/bapi/futures/v1/friendly/future/copy-trade/lead-portfolio/order-history")
                    .method("POST", body)
                    .addHeader("authority", "www.binance.com")
                    .addHeader("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"")
                    .addHeader("csrftoken", PrivateConfig.genDan_token)
                    .addHeader("bnc-time-zone", "Asia/Shanghai")
                    .addHeader("lang", "zh-CN")
                    .addHeader("device-info", "eyJzY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA4MCIsImF2YWlsYWJsZV9zY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA0MCIsInN5c3RlbV92ZXJzaW9uIjoiV2luZG93cyA4LjEiLCJicmFuZF9tb2RlbCI6InVua25vd24iLCJzeXN0ZW1fbGFuZyI6InpoLUNOIiwidGltZXpvbmUiOiJHTVQrMDg6MDAiLCJ0aW1lem9uZU9mZnNldCI6LTQ4MCwidXNlcl9hZ2VudCI6Ik1vemlsbGEvNS4wIChXaW5kb3dzIE5UIDYuMzsgV2luNjQ7IHg2NCkgQXBwbGVXZWJLaXQvNTM3LjM2IChLSFRNTCwgbGlrZSBHZWNrbykgQ2hyb21lLzk3LjAuNDY5Mi43MSBTYWZhcmkvNTM3LjM2IiwibGlzdF9wbHVnaW4iOiJQREYgVmlld2VyLENocm9tZSBQREYgVmlld2VyLENocm9taXVtIFBERiBWaWV3ZXIsTWljcm9zb2Z0IEVkZ2UgUERGIFZpZXdlcixXZWJLaXQgYnVpbHQtaW4gUERGIiwiY2FudmFzX2NvZGUiOiJ1bmtub3duIiwid2ViZ2xfdmVuZG9yIjoiR29vZ2xlIEluYy4gKEdvb2dsZSkiLCJ3ZWJnbF9yZW5kZXJlciI6IkFOR0xFIChHb29nbGUsIFZ1bGthbiAxLjIuMCAoU3dpZnRTaGFkZXIgRGV2aWNlIChTdWJ6ZXJvKSAoMHgwMDAwQzBERSkpLCBTd2lmdFNoYWRlciBkcml2ZXItNS4wLjApIiwiYXVkaW8iOiIxMjQuMDQzNDc1Mjc1MTYwNzQiLCJwbGF0Zm9ybSI6IldpbjMyIiwid2ViX3RpbWV6b25lIjoiQXNpYS9TaGFuZ2hhaSIsImRldmljZV9uYW1lIjoiQ2hyb21lIFY5Ny4wLjQ2OTIuNzEgKFdpbmRvd3MpIiwiZmluZ2VycHJpbnQiOiIzMWFiNmVkNjdmNTFhM2EyMzM5MDEwNmZjZmRmYTkwNyIsImRldmljZV9pZCI6IiIsInJlbGF0ZWRfZGV2aWNlX2lkcyI6IiJ9")
                    .addHeader("bnc-uuid", "3c9a9035-1af1-42f4-bd85-f77737265d5b")
                    .addHeader("fvideo-token", "gBgyZFQVmBd3iYLBpKrPnGyZlnDWDZG2NI9/QOelPJpeOt3q+VjFHYGWzCxyY+Gb9T4cYM8hsM3jMIn+setH2/NxJzuNfZcwBXOBbE6NxV/zRqPPrBK9dQLzNNyAbGB3OMT4wetjiEcRx450V5Sh1iEYy6trIrm4fAVjLcDuCS96+4zuNuL5UgjDTrK99WerQ=1f")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .addHeader("fvideo-id", "3397ebf940f4a2e29a02f0753a55a51b993ca093")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("x-ui-request-trace", "3d46e268-94b3-4c99-9635-a941dbaffd2b")
                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36")
                    .addHeader("x-trace-id", "3d46e268-94b3-4c99-9635-a941dbaffd2b")
                    .addHeader("bnc-location", "CN")
                    .addHeader("x-passthrough-token", "")
                    .addHeader("content-type", "application/json")
                    .addHeader("bnc-level", "0")
                    .addHeader("clienttype", "web")
                    .addHeader("accept", "*/*")
                    .addHeader("origin", "https://www.binance.com")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("referer", "https://www.binance.com/zh-CN/copy-trading/lead-details/4809112219484652032")
                    .addHeader("accept-language", "zh-CN,zh;q=0.9")
                    .addHeader("cookie", PrivateConfig.genDan_cookie)
                    .build();
            Response response = client.newCall(request).execute();

            String s = response.body().string();
            if (StringUtils.isNotBlank(s)) {
                JSONObject jsonObject = JSON.parseObject(s);
                if ("000000".equals(jsonObject.getString("code"))) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray jsonArray = data.getJSONArray("list");
                    if (!CollectionUtils.isEmpty(jsonArray)) {
                        list.addAll(jsonArray);
                        JSONObject jsonObject1 = (JSONObject) jsonArray.get(jsonArray.size() - 1);
                        Thread.sleep(5000);
                        getOp(list, portfolioId, jsonObject1.getLong("orderTime"), isOk);
                    }
                }
            }
        }


    }


}
