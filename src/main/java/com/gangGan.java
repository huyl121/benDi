package com;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.model.market.PriceChangeTicker;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;

public class gangGan {




    /*
    *
    * https://www.binance.com/zh-CN/futures/trading-parameters/perpetual/leverage-margin
    *
    * */
    public static void main(String[] args) throws IOException {

        // 对https也开启代理

        System.out.println("开代理");
        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10809");

        JSONObject jsonObject = get();
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            Integer value = (Integer)entry.getValue();
            if(value > 20){
                value = 20;
            }
            System.out.println("\"" + key + "\":" + value + ",");
        }
    }

    public static JSONObject get() throws IOException {
        OkHttpClient client;
        client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{}");
        Request request = new Request.Builder()
                .url("https://www.binance.com/bapi/futures/v1/friendly/future/common/brackets")
                .method("POST", body)
                .addHeader("authority", "www.binance.com")
                .addHeader("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"")
//                .addHeader("csrftoken", "f46c5c9a76b901e1b71259a084f7cae5")
                .addHeader("bnc-time-zone", "Asia/Shanghai")
                .addHeader("lang", "zh-CN")
                .addHeader("device-info", "eyJzY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA4MCIsImF2YWlsYWJsZV9zY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA0MCIsInN5c3RlbV92ZXJzaW9uIjoiV2luZG93cyA4LjEiLCJicmFuZF9tb2RlbCI6InVua25vd24iLCJzeXN0ZW1fbGFuZyI6InpoLUNOIiwidGltZXpvbmUiOiJHTVQrMDg6MDAiLCJ0aW1lem9uZU9mZnNldCI6LTQ4MCwidXNlcl9hZ2VudCI6Ik1vemlsbGEvNS4wIChXaW5kb3dzIE5UIDYuMzsgV2luNjQ7IHg2NCkgQXBwbGVXZWJLaXQvNTM3LjM2IChLSFRNTCwgbGlrZSBHZWNrbykgQ2hyb21lLzk3LjAuNDY5Mi43MSBTYWZhcmkvNTM3LjM2IiwibGlzdF9wbHVnaW4iOiJQREYgVmlld2VyLENocm9tZSBQREYgVmlld2VyLENocm9taXVtIFBERiBWaWV3ZXIsTWljcm9zb2Z0IEVkZ2UgUERGIFZpZXdlcixXZWJLaXQgYnVpbHQtaW4gUERGIiwiY2FudmFzX2NvZGUiOiJ1bmtub3duIiwid2ViZ2xfdmVuZG9yIjoiR29vZ2xlIEluYy4gKEdvb2dsZSkiLCJ3ZWJnbF9yZW5kZXJlciI6IkFOR0xFIChHb29nbGUsIFZ1bGthbiAxLjIuMCAoU3dpZnRTaGFkZXIgRGV2aWNlIChTdWJ6ZXJvKSAoMHgwMDAwQzBERSkpLCBTd2lmdFNoYWRlciBkcml2ZXItNS4wLjApIiwiYXVkaW8iOiIxMjQuMDQzNDc1Mjc1MTYwNzQiLCJwbGF0Zm9ybSI6IldpbjMyIiwid2ViX3RpbWV6b25lIjoiQXNpYS9TaGFuZ2hhaSIsImRldmljZV9uYW1lIjoiQ2hyb21lIFY5Ny4wLjQ2OTIuNzEgKFdpbmRvd3MpIiwiZmluZ2VycHJpbnQiOiIzMWFiNmVkNjdmNTFhM2EyMzM5MDEwNmZjZmRmYTkwNyIsImRldmljZV9pZCI6IiIsInJlbGF0ZWRfZGV2aWNlX2lkcyI6IiJ9")
                .addHeader("bnc-uuid", "00078672-a427-4d69-86df-877509c40a5b")
                .addHeader("fvideo-token", "oi6XFbiM7lZQqsvLj/OdqVzRF/eGUk7nA6TmvYD398TtewdhlnHdab0CBfPpqM9MmIpWkopGQrGQF84QjnxuDtlITneeKTrUVAZlbVUe3gCCjFOqXsz2Cf+6IYKNAv2S1Sz8c/Z6XFiWW12i/5wqbr2qrFU1VpAF9yfAKMstokWSjxDa/dppFKNFsP9c+pvDg=7b")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("fvideo-id", "331e7153cc34a99c90c6ea8cd9ceabaa097a1ec3")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("x-ui-request-trace", "9a12bfe5-5b04-4cf3-8a31-d70af0aa0c9b")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36")
                .addHeader("x-trace-id", "9a12bfe5-5b04-4cf3-8a31-d70af0aa0c9b")
                .addHeader("bnc-location", "CN")
                .addHeader("x-passthrough-token", "eyJhbGciOiJSUzI1NiJ9.eyJpYXQiOjE3ODQ2NDU0NjMsImlzcyI6Imh0dHBzOi8vYmluYW5jZS5jb20iLCJzdWIiOiJ2YWxpZGF0aW9uX3Rva2VuIiwiZGV2aWNlSWQiOiIzRjZBMUZDMzRBQTFGQ0VDODJGN0M5MjlFOUE2RjMxRCIsImV4cCI6MTc4NDY0NTc2MywibmJmIjoxNzg0NjQ1NDYzfQ.D01iDAqO1e1Kg9SCM9L4LjDb7XOFZ7xrxBKqd2mwvjxWXhIZIj_AIhtAd3bs6eZ2x_I--sGT43-glzwElBJkoA7eceYwRoha9PjJMC3tEvm2UkGUseaR4ijm9vIP3AjrO0K-PTaiiqWKBhuUTCqWlXT8kwjf8rjlw4dNq6Fgmxs")
                .addHeader("content-type", "application/json")
                .addHeader("bnc-level", "0")
                .addHeader("clienttype", "web")
                .addHeader("accept", "*/*")
                .addHeader("origin", "https://www.binance.com")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("referer", "https://www.binance.com/zh-CN/futures/trading-parameters/perpetual/leverage-margin")
                .addHeader("accept-language", "zh-CN,zh;q=0.9")
//                .addHeader("cookie", "bnc-uuid=00078672-a427-4d69-86df-877509c40a5b; se_gd=1IDGhABgATGFF0AdbBFYgZZUACQlRBXWlpcNfVEVlNQVQBFNWWVU1; se_gsd=VgAhK0J2MCUnIwUiJQwiUyohBVBUDwoWV19EVVBVW1NWI1NT1; BNC_FV_KEY=331e7153cc34a99c90c6ea8cd9ceabaa097a1ec3; OptanonAlertBoxClosed=2026-06-26T09:06:13.491Z; r30t=1; BNC-Location=CN; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221229775668%22%2C%22first_id%22%3A%2219f032dd7ae1e-0fb0c6e6815e9b-5c123e18-2073600-19f032dd7af68d%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmMDMyZGQ3YWUxZS0wZmIwYzZlNjgxNWU5Yi01YzEyM2UxOC0yMDczNjAwLTE5ZjAzMmRkN2FmNjhkIiwiJGlkZW50aXR5X2xvZ2luX2lkIjoiMTIyOTc3NTY2OCJ9%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221229775668%22%7D%7D; changeBasisTimeZone=; g_state={\"i_l\":0,\"i_ll\":1783913004112,\"i_b\":\"xJrYvnJzD+28QTreC1LyfS7/IFckJ/y1wyWEYuAPetY\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1783913004112}; _gcl_au=1.1.1561541001.1782464934.-.-.1784641155.1552217228.1784641156.1784641155; futures-layout=pro; aws-waf-token=f920b627-7808-488a-bb59-e953426b28e3:BgoAtvNb8oBbAAAA:Q++igxq7htlCfFBpxzkDnSUr2dGEH8LyzNrGPAz/17MCKk7ry79fnAxJPvKzbxiN81JWs0sY9nv6Tnm07Y1mzSFYoNAlFesoqDnH+zEKc2cN80a+IrTTE1sG9HQfgtOCG5Q6SLN2LQmp7uKrz12NTr7cQ5NjWRioFaLaNYshQHEDRvHyb/xgv/ZT8sgwl8GrhcGxev0crnkgQauWPiNS9lBiLJo8JgMeLr6MoOFNRlRBzUeXTR4OmtdpaeHz+iAwjDGcpyskM+80; _gid=GA1.2.106431666.1787577534; BNC_FV_KEY_EXPIRE=1787667309599; BNC_FV_KEY_T=101-BkCRQLG7zFor7fLQmbodFnoFXmrLh1AU3VOqfObrY3azVXzYcVx9w%2FVBWBrOv7Ke6syZHfuKkXidk8Mar8J2UQ%3D%3D-w8%2BQuYPyWNFGdgs%2BqYw90Q%3D%3D-8c; theme=dark; se_sd=htRGQTQYFFKVw8WsJDBEgZZUgHVAAEUU1oJZfUEZlZSVQEVNWWdW1; r20t=web.1229775668.147038F0CE65DFE8AD150243804A24DB; cr00=639657A79B48F12DC7DE5A818ED3A72D; d1og=web.1229775668.1E4BA54F06CEC0AC00EB13858E8B8C7E; r2o1=web.1229775668.BF1FC0947EDD1117F8BAAFE3D688029C; f30l=web.1229775668.8C21816FF09552E99481277ABF534972; p20t=web.1229775668.4335ECD408650ADA6F1FC943E8B3843A; _h_desk_key=fa9c8a605258482eb2532cbc97ae48bc; OptanonConsent=isGpcEnabled=0&datestamp=Tue+Aug+25+2026+20%3A46%3A37+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&consentId=b9fdc09a-12a2-4698-8d79-339fead07500&interactionCount=2&isAnonUser=1&prevHadToken=0&landingPath=NotLandingPage&crTime=1782464777973&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&fclco=&lastConsentTs=1782464773&intType=1&geolocation=JP%3B13&AwaitingReconsent=false; _gat_UA-162512367-1=1; _uetsid=5f541d009fbe11f1b52b25e578c77762; _uetvid=a92ddaf0713e11f18a1b87d7629238c1; _ga=GA1.2.456841213.1782464780; _ga_3WP50LGEEC=GS2.1.s1787661707$o18$g1$t1787662041$j18$l0$h0")
                .build();
        Response response = client.newCall(request).execute();

        String s = response.body().string();
        JSONArray jsonArray = null;
        if (StringUtils.isNotBlank(s)) {
            JSONObject jsonObject = JSON.parseObject(s);
            if ("000000".equals(jsonObject.getString("code"))) {
                jsonArray = jsonObject.getJSONObject("data").getJSONArray("brackets");
            }
        }
        JSONObject symbolLev = null;
        if(!CollectionUtils.isEmpty(jsonArray)){
            symbolLev = new JSONObject();
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                String symbol = jsonObject.getString("symbol");
                JSONArray riskBrackets = jsonObject.getJSONArray("riskBrackets");
                int max = 0;
                for (Object o1 : riskBrackets) {
                    JSONObject jsonObject1 = (JSONObject) o1;
                    int maxOpenPosLeverage = jsonObject1.getInteger("maxOpenPosLeverage");
                    if(maxOpenPosLeverage > max){
                        max = maxOpenPosLeverage;
                    }
                }
                symbolLev.put(symbol, max);
            }
        }
        return symbolLev;
    }
}
