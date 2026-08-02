package com.example.bian.ok;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import okhttp3.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.example.bian.client.bushu.PrivateConfig.*;


public class GetOKPositions {
    public static JSONArray getOrders(ThreadPoolExecutor threadPoolExecutor, String genPortfolioId, boolean currentPosition, boolean fromJianKong) throws InterruptedException {
        int n = 10;
        if(fromJianKong || "2".equals(ok_position)){
            n = getPon;
        }
        int errorCount = 0;
        for (int i = 0; i < n; i++) {
            try{
                String s = getOrder(threadPoolExecutor, genPortfolioId, currentPosition);
                if (StringUtils.isNotBlank(s)) {
//                System.out.println(s);
                    JSONObject jsonObject = JSON.parseObject(s);
                    if ("0".equals(jsonObject.getString("code"))) {
                        JSONArray jsonArray;
                        if(currentPosition){
                            if(PrivateConfig.ok_position.equals("2")){
                                jsonArray = jsonObject.getJSONArray("data");
                                //判断是否能够查出来是哪个币种，查不出来，就是token过期了
                                boolean token = true;
                                for (Object o : jsonArray) {
                                    JSONObject position = (JSONObject) o;
                                    if(StringUtils.isEmpty(position.getString("instId"))){
                                        PrivateConfig.printLog("ok的token过期了");
                                        T5.searchAll("ok的token过期了");
//                                    Thread.sleep(5000);
                                        Thread.sleep(1000 * 60);
                                        token = false;
                                        break;
                                    }
                                }
                                if(!token){
                                    continue;
                                }
                            }else {
                                jsonArray = ((JSONObject)jsonObject.getJSONArray("data").get(0)).getJSONArray("posData");
                            }
                        }else {
                            jsonArray = jsonObject.getJSONArray("data");
                        }
                        return jsonArray;

                    }else if("50113".equals(jsonObject.getString("code"))) {
                        PrivateConfig.printLog(jsonObject.toJSONString());
                        T5.searchAll("token已过期，" + jsonObject.toJSONString());
                        Thread.sleep(1000 * 60);
                    }else {
                        PrivateConfig.printLog(jsonObject.toJSONString());
                        T5.searchAll("ok抓紧联系我ok1，" + jsonObject.toJSONString());
                        Thread.sleep(1000 * 60);
                    }
                }else {
                    T5.searchAll("ok出问题了，抓紧联系我ok2");
                    Thread.sleep(1000 * 60);
                }
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
                errorCount++;
                if(errorCount>3){
                    T5.searchAll("概况关闭了");
                    errorCount = 0;
                }

                Thread.sleep(1000 * 60);
            }catch (Exception e){
                e.printStackTrace();
                T5.searchAll("概况关闭了");
                Thread.sleep(1000 * 60);
            }
        }
        PrivateConfig.printLog("ok-获取订单有问题了1");
        return null;
    }

    private static String getOrder(ThreadPoolExecutor threadPoolExecutor, String genPortfolioId, boolean currentPosition) {

        //订单的顺序：第一个就是最近的一个
        Callable callable = new Callable() {
            @Override
            public String call() throws Exception {
                return getPosition(genPortfolioId, currentPosition);
            }
        };

        int h = 0;
        while (true) {
            try {
                Future future = threadPoolExecutor.submit(callable);
                try {
                    String s = (String) (future.get(30, TimeUnit.SECONDS));
                    return s;
                }  catch (Exception e) {
                    if(PrivateConfig.ceShi.equals("1")){
                        System.out.println("这里出错了2");
                    }
                    e.printStackTrace();
                    Thread.sleep(3000);//前面有超时，歇2秒再跟
                } catch (Throwable t) {
                    if(PrivateConfig.ceShi.equals("1")){
                        System.out.println("这里出错了3");
                    }
                    t.printStackTrace();
                    Thread.sleep(3000);//前面有超时，歇2秒再跟
                } finally {
                    future.cancel(true);
                    h++;
                    if (h > 5) {
                        h = 0;
                        PrivateConfig.printLog("币安跟单-获取订单超时了");
                        T5.searchAll("订单失败，连续5次，有问题！5");
                    }
                }
            } catch (Exception e1) {
                if(PrivateConfig.ceShi.equals("1")){
                    System.out.println("这里出错了4");
                }
                e1.printStackTrace();
            }

        }
    }

    /**
     *
     * @param genPortfolioId
     * @param currentPosition true：查询持仓；false：查询操作记录
     * @return
     * @throws IOException
     */
    public static String getPosition(String genPortfolioId, boolean currentPosition) throws IOException {

        if(currentPosition){
            if(PrivateConfig.ok_position.equals("2")){
                return get2(genPortfolioId);
            }else {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = RequestBody.create(mediaType, "");
                Request request = new Request.Builder()
                        .url("https://www.okx.com/priapi/v5/ecotrade/public/community/user/position-current?uniqueName=" + genPortfolioId)
//                    .method("GET", body)
                        .addHeader("authority", "www.okx.com")
                        .addHeader("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"")
                        .addHeader("x-locale", "zh_CN")
                        .addHeader("x-cdn", "https://www.okx.com")
                        .addHeader("x-fptoken-signature", "{P1363}EOUsfY+Jud2h3Cv0AVVNRQXB8jntK5rBrsAMVd28uAARbEpRdvOP9WGqCbBpU4LO9zhFk1FCIaUW1CLY2Rf1ug==")
                        .addHeader("x-site-info", "==QfxojI5RXa05WZiwiIMFkQPx0Rfh1SPJiOiUGZvNmIsICUKJiOi42bpdWZyJye")
                        .addHeader("sec-ch-ua-platform", "\"Windows\"")
                        .addHeader("x-request-timestamp", "1764161635081")
                        .addHeader("devid", "acf2ec83-51bb-4a21-95c0-8a82d657d690")
                        .addHeader("app-type", "web")
                        .addHeader("sec-ch-ua-mobile", "?0")
                        .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36")
                        .addHeader("accept", "application/json")
                        .addHeader("x-utc", "8")
                        .addHeader("x-simulated-trading", "undefined")
                        .addHeader("x-zkdex-env", "0")
                        .addHeader("x-id-group", "2140841604188600002-c-16")
                        .addHeader("sec-fetch-site", "same-origin")
                        .addHeader("sec-fetch-mode", "cors")
                        .addHeader("sec-fetch-dest", "empty")
                        .addHeader("referer", "https://www.okx.com/zh-hans/copy-trading/account/E512EAA2C34FAF44?tab=trade")
                        .addHeader("accept-language", "zh-CN,zh;q=0.9")
                        .addHeader("Cookie", "__cf_bm=4tSypJ9B2AL4Ql6gcQiv3jhP5s6FEaVpP1hx7yjfb84-1764162223-1.0.1.1-9V54zQ2stbvRNqauWsWzaxJ6pX7ewClHjAGw643OnGrCtLk7I88a1BkkAJv9Zb9uJPX2TzUrG5L0KXVx1Q02x8B5ZzlYWxLICdJOtYN.tzQ")
                        .build();
                Response response = client.newCall(request).execute();
                return response.body().string();
            }
        }else {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            String url = "https://www.okx.com/priapi/v5/ecotrade/public/community/user/trade-records?instType=SWAP&uniqueName=" + genPortfolioId + "&limit=" + PrivateConfig.ok_pageSize;
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
            return response.body().string();
        }


    }

    public static String get2(String genPortfolioId) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://www.okx.com/priapi/v5/ecotrade/public/trader/position-detail?instType=SWAP&uniqueName=" + genPortfolioId)
//                        .method("GET", body)
                .addHeader("authority", "www.okx.com")
                .addHeader("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"")
                .addHeader("x-locale", "zh_CN")
                .addHeader("x-cdn", "https://www.okx.com")
                .addHeader("authorization", PrivateConfig.ok_authorization)
                .addHeader("x-client-signature-version", "1.3")
                .addHeader("x-site-info", "=0HNxojI5RXa05WZiwiIMFkQPx0Rfh1SPJiOiUGZvNmIsIyRTJiOi42bpdWZyJye")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("x-request-timestamp", "1776055134807")
                .addHeader("devid", "5f5ed41b-f63b-4b3f-b900-52bf404fdb6a")
                .addHeader("app-type", "web")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36")
                .addHeader("accept", "application/json")
                .addHeader("x-utc", "8")
                .addHeader("x-simulated-trading", "undefined")
                .addHeader("x-client-signature", "{P1363}w+kZ7e9177LFopzbQbm+rMREGENUT0WJLiZmnJxueZjaPZIjx8AFl+ut/Dud/0epirz7CTvcjktrgPqOX48AVA==")
                .addHeader("x-zkdex-env", "0")
                .addHeader("x-id-group", "2130560550595260001-c-35")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("referer", "https://www.okx.com/zh-hans/copy-trading/account/823664FB73B79E41?tab=swap")
                .addHeader("accept-language", "zh-CN,zh;q=0.9")
                .addHeader("Cookie", "__cf_bm=Wq3W0A6sLiG3r5_S_iEkHGqpqAe6J9wAsMVkWBJZ.go-1776065990.153579-1.0.1.1-eevgV1vBuiDVvRTLXeY63fp33KAWaq82hOrm1c703RYNvwL4Ew66pbYqn_kGr5lbiXzFOsyBimBdviXUGoSK.brKPRpg2JXhbtTKvPnsVIe.XtWVjyb8rh5ihYIX1KYJ")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static void main(String[] args) throws IOException {
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

        String s = GetOKPositions.getPosition("2831B237B23802D3", true);
//        String s = GetOKPositions.getPosition("777930890675843907", false);
//        String s = GetOKPositions.getPosition("24D8CE79A97FD35D", false);
        if (StringUtils.isNotBlank(s)) {
            JSONObject jsonObject = JSON.parseObject(s);
            if ("0".equals(jsonObject.getString("code"))) {
                JSONArray jsonArray;
                jsonArray = jsonObject.getJSONArray("data");
                Set<Long> fullTimeSet = new TreeSet<>();
                Set<Long> uTimeSet = new TreeSet<>();

                for (Object entryNew : jsonArray) {
                    JSONObject entity = (JSONObject) entryNew;
                    Long cTime = entity.getLong("cTime");
                    Long fillTime = entity.getLong("fillTime");
                    Long uTime = entity.getLong("uTime");
                    fullTimeSet.add((fillTime - cTime)/1000);
                    uTimeSet.add((uTime - cTime)/1000);

                }
                System.out.println("fullTimeSet");
                for(Long fullTime : fullTimeSet){
                    System.out.println(fullTime);
                }

                System.out.println("uTimeSet");
                for(Long fullTime : uTimeSet){
                    System.out.println(fullTime);
                }
            }
        }

    }

}
