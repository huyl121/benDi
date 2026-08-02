package com.example.bian.genDan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.bushu.PrivateConfig;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;

public class Constants {

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

        System.out.println(getLaoShiMoney(""));
    }

    public static BigDecimal getLaoShiMoney(String genPortfolioId) throws IOException {

        //聪明钱里的老师金额
        if (PrivateConfig.genDan_position.equals("5") || PrivateConfig.genDan_position.equals("6")) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
//            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://www.binance.com/bapi/asset/v1/friendly/future/smart-money/profile?topTraderId=" + genPortfolioId)
//                    .method("GET", body)
                    .addHeader("authority", "www.binance.com")
                    .addHeader("fvideo-id", "3355c946ae9ea22e88615bbef8ba71903006905e")
//                    .addHeader("csrftoken", "c15884b92065ea6f22b274fd6f4b0a5c")
                    .addHeader("x-ui-request-trace", "fefc9c9e-59d9-430c-8894-c28eba53e6f3")
                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36")
                    .addHeader("x-trace-id", "fefc9c9e-59d9-430c-8894-c28eba53e6f3")
                    .addHeader("lang", "zh-CN")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("x-passthrough-token", "")
                    .addHeader("content-type", "application/json")
                    .addHeader("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"")
                    .addHeader("device-info", "eyJzY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA4MCIsImF2YWlsYWJsZV9zY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA0MCIsInN5c3RlbV92ZXJzaW9uIjoiV2luZG93cyA4LjEiLCJicmFuZF9tb2RlbCI6InVua25vd24iLCJzeXN0ZW1fbGFuZyI6InpoLUNOIiwidGltZXpvbmUiOiJHTVQrMDg6MDAiLCJ0aW1lem9uZU9mZnNldCI6LTQ4MCwidXNlcl9hZ2VudCI6Ik1vemlsbGEvNS4wIChXaW5kb3dzIE5UIDYuMzsgV2luNjQ7IHg2NCkgQXBwbGVXZWJLaXQvNTM3LjM2IChLSFRNTCwgbGlrZSBHZWNrbykgQ2hyb21lLzk3LjAuNDY5Mi43MSBTYWZhcmkvNTM3LjM2IiwibGlzdF9wbHVnaW4iOiJQREYgVmlld2VyLENocm9tZSBQREYgVmlld2VyLENocm9taXVtIFBERiBWaWV3ZXIsTWljcm9zb2Z0IEVkZ2UgUERGIFZpZXdlcixXZWJLaXQgYnVpbHQtaW4gUERGIiwiY2FudmFzX2NvZGUiOiJ1bmtub3duIiwid2ViZ2xfdmVuZG9yIjoiR29vZ2xlIEluYy4gKEdvb2dsZSkiLCJ3ZWJnbF9yZW5kZXJlciI6IkFOR0xFIChHb29nbGUsIFZ1bGthbiAxLjIuMCAoU3dpZnRTaGFkZXIgRGV2aWNlIChTdWJ6ZXJvKSAoMHgwMDAwQzBERSkpLCBTd2lmdFNoYWRlciBkcml2ZXItNS4wLjApIiwiYXVkaW8iOiIxMjQuMDQzNDc1Mjc1MTYwNzQiLCJwbGF0Zm9ybSI6IldpbjMyIiwid2ViX3RpbWV6b25lIjoiQXNpYS9TaGFuZ2hhaSIsImRldmljZV9uYW1lIjoiQ2hyb21lIFY5Ny4wLjQ2OTIuNzEgKFdpbmRvd3MpIiwiZmluZ2VycHJpbnQiOiIzMWFiNmVkNjdmNTFhM2EyMzM5MDEwNmZjZmRmYTkwNyIsImRldmljZV9pZCI6IiIsInJlbGF0ZWRfZGV2aWNlX2lkcyI6IiJ9")
                    .addHeader("bnc-uuid", "9ca859e2-89ff-448c-9874-2f0f653bdf4b")
                    .addHeader("clienttype", "web")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .addHeader("accept", "*/*")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("referer", "https://www.binance.com/zh-CN/smart-money/profile/4585345507763225344?rankingType=PNL&timeRange=30D")
                    .addHeader("accept-language", "zh-CN,zh;q=0.9")
//                    .addHeader("cookie", "BNC-Location=CN; bnc-uuid=9ca859e2-89ff-448c-9874-2f0f653bdf4b; BNC_FV_KEY=3355c946ae9ea22e88615bbef8ba71903006905e; OptanonAlertBoxClosed=2026-02-24T02:06:41.462Z; se_gd=xIWFBAAURRQDQ8R4FARlgZZUwHVFTBQVFoQVZVkNVBdVwEVNWVUS1; se_gsd=BiQiPEplNjMlIDcxJCYiGioyFFYLAwsGVVlAV1ZTVFBbN1NT1; _gcl_au=1.1.1514292412.1771942146; r30t=1; userPreferredCurrency=USD_USD; changeBasisTimeZone=; g_state={\"i_l\":0,\"i_ll\":1773142886216,\"i_b\":\"4OTkD53toSFjIHSHbnjAdTz8zKIPDG9AWr6G7SACeOY\",\"i_e\":{\"enable_itp_optimization\":0}}; lang=en; currentAccount=; logined=y; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221207218528%22%2C%22first_id%22%3A%2219c8d65ee8d25f-08a1e33452e00b-5c123e18-2073600-19c8d65ee8e697%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTljOGQ2NWVlOGQyNWYtMDhhMWUzMzQ1MmUwMGItNWMxMjNlMTgtMjA3MzYwMC0xOWM4ZDY1ZWU4ZTY5NyIsIiRpZGVudGl0eV9sb2dpbl9pZCI6IjEyMDcyMTg1MjgifQ%3D%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221207218528%22%7D%7D; futures-layout=pro; aws-waf-token=9095e419-d0a7-49cc-ae79-0184d3bf696a:AQoAsrlcbuMOAAAA:VipCEHjIg4i243ccDKWoY8HusJXQxQc6NT3fZeOVeLiRyGgUrMot25RhouyhFmJ+Y3pJlPyJK4rK3uhDZ96/rkvDc5BQqhOrd7ZnqWHL5FvO0hnZsk8TrHyqFdXpBdRdQtS+5w1H0w62eIMqo9J7rRdbd1swgryP6BJLSHcuBOp5pH2Uo/c2i9dS8V4mZr/8JbM=; _gid=GA1.2.805739230.1773580375; r20t=web.3986DEE0BA02553F16BBD89A49D0DF58; cr00=3C06DD20858AFDDA931D932466263F74; d1og=web.1207218528.C11AE3100DE3D8562DCFA38B5D7C45FB; r2o1=web.1207218528.D510476D44569950DBB3FC20CD4616BE; f30l=web.1207218528.07A0F495646DE74C90DD604B2F7A9D0B; p20t=web.1207218528.79576DB0240BF68A8E7FAB23C1B5EB57; _uetvid=61c39960118a11f194a7af1704c05639; theme=dark; BNC_FV_KEY_T=101-MKswyVG4%2BlNgPZlJv6OtfKk2T7BKI5KTRwiWfRNLX%2BXoGgLWLtyrdaiRayoNbArvUcYMzX7N1jhArWtVhCaZSw%3D%3D-KtX93v6qNda7oxgS8%2FSglQ%3D%3D-01; BNC_FV_KEY_EXPIRE=1773691417132; _ga_3WP50LGEEC=GS2.1.s1773669819$o62$g0$t1773669819$j60$l0$h0; OptanonConsent=isGpcEnabled=0&datestamp=Mon+Mar+16+2026+22%3A03%3A45+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202506.1.0&browserGpcFlag=0&isIABGlobal=false&hosts=&consentId=697c3094-e434-4237-aed1-1e6fe4c240e8&interactionCount=1&isAnonUser=1&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&intType=1&geolocation=SG%3B&AwaitingReconsent=false; _ga=GA1.2.226209271.1771898802")
                    .addHeader("if-none-match", "W/\"01d8ab2e52514cb7390d2510f40b1e9a8\"")
                    .build();
            Response response = client.newCall(request).execute();
            String s = response.body().string();
            if (StringUtils.isNotBlank(s)) {
                JSONObject jsonObject = JSON.parseObject(s);
                if ("000000".equals(jsonObject.getString("code"))) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    return data.getBigDecimal("umMarginBalance");
                }
            }

        }else {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://www.binance.com/bapi/futures/v1/friendly/future/copy-trade/lead-portfolio/detail?portfolioId=" + genPortfolioId)
//                    .method("GET", body)
                    .addHeader("authority", "www.binance.com")
                    .addHeader("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"")
                    .addHeader("bnc-time-zone", "Asia/Shanghai")
                    .addHeader("lang", "zh-CN")
//                    .addHeader("cookie", PrivateConfig.genDan_cookie)
//                    .addHeader("csrftoken", PrivateConfig.genDan_token)
                    .addHeader("device-info", "eyJzY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA4MCIsImF2YWlsYWJsZV9zY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA0MCIsInN5c3RlbV92ZXJzaW9uIjoiV2luZG93cyA4LjEiLCJicmFuZF9tb2RlbCI6InVua25vd24iLCJzeXN0ZW1fbGFuZyI6InpoLUNOIiwidGltZXpvbmUiOiJHTVQrMDg6MDAiLCJ0aW1lem9uZU9mZnNldCI6LTQ4MCwidXNlcl9hZ2VudCI6Ik1vemlsbGEvNS4wIChXaW5kb3dzIE5UIDYuMzsgV2luNjQ7IHg2NCkgQXBwbGVXZWJLaXQvNTM3LjM2IChLSFRNTCwgbGlrZSBHZWNrbykgQ2hyb21lLzk3LjAuNDY5Mi43MSBTYWZhcmkvNTM3LjM2IiwibGlzdF9wbHVnaW4iOiJQREYgVmlld2VyLENocm9tZSBQREYgVmlld2VyLENocm9taXVtIFBERiBWaWV3ZXIsTWljcm9zb2Z0IEVkZ2UgUERGIFZpZXdlcixXZWJLaXQgYnVpbHQtaW4gUERGIiwiY2FudmFzX2NvZGUiOiJ1bmtub3duIiwid2ViZ2xfdmVuZG9yIjoiR29vZ2xlIEluYy4gKEdvb2dsZSkiLCJ3ZWJnbF9yZW5kZXJlciI6IkFOR0xFIChHb29nbGUsIFZ1bGthbiAxLjIuMCAoU3dpZnRTaGFkZXIgRGV2aWNlIChTdWJ6ZXJvKSAoMHgwMDAwQzBERSkpLCBTd2lmdFNoYWRlciBkcml2ZXItNS4wLjApIiwiYXVkaW8iOiIxMjQuMDQzNDc1Mjc1MTYwNzQiLCJwbGF0Zm9ybSI6IldpbjMyIiwid2ViX3RpbWV6b25lIjoiQXNpYS9TaGFuZ2hhaSIsImRldmljZV9uYW1lIjoiQ2hyb21lIFY5Ny4wLjQ2OTIuNzEgKFdpbmRvd3MpIiwiZmluZ2VycHJpbnQiOiIzMWFiNmVkNjdmNTFhM2EyMzM5MDEwNmZjZmRmYTkwNyIsImRldmljZV9pZCI6IiIsInJlbGF0ZWRfZGV2aWNlX2lkcyI6IiJ9")
                    .addHeader("bnc-uuid", "3c9a9035-1af1-42f4-bd85-f77737265d5b")
                    .addHeader("fvideo-token", "EeM4MfYEi28BDoU0dnHpAv0r9riZxfherxM2i3sNcAPuCi5D7gjoVLDxU83HfgwoT/5+zVdI0ZsebQF58LyQZGJqmLnr2Nl90GXCz5NhCbMI3mD6JTv7O89Vzm/bjXNXT6+G91BErmaiJ8Z4DYlie6MuzIsEmLRs46hIwp7UQQOqZaX9Nj/641IjBjO9Kd9h8=7c")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .addHeader("fvideo-id", "3397ebf940f4a2e29a02f0753a55a51b993ca093")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("x-ui-request-trace", "f60a1166-960f-4319-a42d-9be74a27b31b")
                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36")
                    .addHeader("x-trace-id", "f60a1166-960f-4319-a42d-9be74a27b31b")
                    .addHeader("bnc-location", "CN")
                    .addHeader("x-passthrough-token", "")
                    .addHeader("content-type", "application/json")
                    .addHeader("bnc-level", "0")
                    .addHeader("clienttype", "web")
                    .addHeader("accept", "*/*")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("referer", "https://www.binance.com/zh-CN/copy-trading/lead-details/4777921357644221697?timeRange=30D&isSmartFilter=true")
                    .addHeader("accept-language", "zh-CN,zh;q=0.9")
                    .addHeader("if-none-match", "W/\"0ca5eb252541a54aced210106fcc88759\"")
                    .build();
            Response response = client.newCall(request).execute();


            String s = response.body().string();
            if (StringUtils.isNotBlank(s)) {
                JSONObject jsonObject = JSON.parseObject(s);
                if ("000000".equals(jsonObject.getString("code"))) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    return data.getBigDecimal("marginBalance");
                }
            }
        }

        return null;

    }
}
