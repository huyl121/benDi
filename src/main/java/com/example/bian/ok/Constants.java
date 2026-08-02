package com.example.bian.ok;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.impl.utils.JsonWrapper;
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

        getLaoShiMoney();
    }

    public static BigDecimal getLaoShiMoney() throws IOException {


        if(PrivateConfig.ok_position.equals("2")){
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            Request request = new Request.Builder()
                    .url("https://www.okx.com/priapi/v5/ecotrade/public/trader/trade-data?latestNum=0&bizType=SWAP&uniqueName=" + PrivateConfig.ok_genPortfolioId)
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
                    .addHeader("x-fptoken", "eyJraWQiOiIxNjgzMzgiLCJhbGciOiJFUzI1NiJ9.eyJpYXQiOjE3NzYzMDY5NTUsImVmcCI6IndFUVFjWFlQSStSc1RSOHIvYndjQlNtZzdPc0F6dnVTcmgxOUlBemRoSTRZdjBkRnNudy9GNGZ6Z3N2clhidFoiLCJkaWQiOiI5N2I0ZTMzNC0wYjVkLTQ3YmEtODIyMS1iMjgxMTY0NTU1OGUiLCJjcGsiOiJNRmt3RXdZSEtvWkl6ajBDQVFZSUtvWkl6ajBEQVFjRFFnQUVuTnUxQVF6MXlVaWlKUFBFZjdtWDJNMFozL2ZOTVcxdkFBTllQcEEvelZPRUxBYXcwUFp0T25KTnNlWkJsOXE0dW9qRU8vekptN2lyN3pheFNQeExlQT09In0.MDEwhEVl1sZQP3ZQy5FhNetfTEz_aHxBhD-k1oHiZY4xGKwVCZxir0NQbhdWbdvpRLaAG_NoUXLtKiILZSY3pw")
                    .addHeader("x-fptoken-signature", "{P1363}Gk+TE5JsONpqL9codV5Is4s22dUfnSaQdP3rJNIWk40n7lAwBpe+7+i32cWUiZbvLq+qW9nEOYZ75tbkmFJ2Kg==")
                    .addHeader("x-id-group", "2130663090845380001-c-6")
                    .addHeader("x-locale", "zh_CN")
                    .addHeader("x-request-timestamp", "1776309086543")
                    .addHeader("x-simulated-trading", "undefined")
                    .addHeader("x-site-info", "==QfxojI5RXa05WZiwiIMFkQPx0Rfh1SPJiOiUGZvNmIsIyRTJiOi42bpdWZyJye")
                    .addHeader("x-utc", "8")
                    .addHeader("x-zkdex-env", "0")
                    .addHeader("Cookie", "__cf_bm=_D4KeoApgAfW6vRxL7GLU9Bns8RxGMSpUr3NPYK36PA-1776308417.2027771-1.0.1.1-xan292d2RH.7RtY9bWd0_FIbEbFzzezVhYqbyHWvNUakSoGKpwAFzcBIxElBqeeUfWB.ePaJ9OZYy_N3o08fBGgZw0tuCiCgR_tkKzY2oQDBdc5IeHVDZL26AmACm_oE")
                    .build();
            Response response = client.newCall(request).execute();
            String s = response.body().string();
            if (StringUtils.isNotBlank(s)) {
                JSONObject jsonObject = JSON.parseObject(s);
                if ("0".equals(jsonObject.getString("code"))) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data").getJSONObject(0).getJSONArray("nonPeriodicPart");
                    for (Object entryNew : jsonArray) {
                        JSONObject entity = (JSONObject) entryNew;
                        String currency = entity.getString("functionId");
                        if("asset".equals(currency)){
                            return entity.getBigDecimal("value");
                        }
                    }
                }
            }
        }else {
            OkHttpClient client;
            client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            Request request = new Request.Builder()
                    .url("https://www.okx.com/priapi/v5/ecotrade/public/community/user/asset?uniqueName=" + PrivateConfig.ok_genPortfolioId)
//                .method("GET", body)
                    .addHeader("authority", "www.okx.com")
                    .addHeader("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"")
                    .addHeader("x-locale", "zh_CN")
                    .addHeader("x-cdn", "https://www.okx.com")
                    .addHeader("authorization", "eyJraWQiOiIxMzYzODYiLCJhbGciOiJFUzI1NiJ9.eyJqdGkiOiJleDExMDE3NjUxOTU5NTYxNjc4QjI2NjM4MzlCRkE1NzQ4MVhqdFkiLCJ1aWQiOiJlTTVmZVdCWnZ0d0xKK2xwZWRUNXV3PT0iLCJzdGEiOjAsIm1pZCI6ImVNNWZlV0JadnR3TEorbHBlZFQ1dXc9PSIsInBpZCI6IlBUeUE4VzA5ekZVSkJHSjZZUk5HWXc9PSIsIm5kZSI6MCwiaWF0IjoxNzY2ODI2MTcyLCJleHAiOjE3NjgwMzU3NzIsImJpZCI6MCwiZG9tIjoid3d3Lm9reC5jb20iLCJlaWQiOjE0LCJpc3MiOiJva2NvaW4iLCJkaWQiOiJqV1RxaHlZOWtEaEFlRjlLd1JkTzJ6aGJWbmh0OGg2UkliOXN4aWFBK0VyS29QWElOcFZYdjA4Q3FSK2Y1NGYvIiwiZmlkIjoialdUcWh5WTlrRGhBZUY5S3dSZE8yemhiVm5odDhoNlJJYjlzeGlhQStFcktvUFhJTnBWWHYwOENxUitmNTRmLyIsImxpZCI6ImVNNWZlV0JadnR3TEorbHBlZFQ1dXc9PSIsInVmYiI6IlBUeUE4VzA5ekZVSkJHSjZZUk5HWXc9PSIsInVwYiI6ImlCcmEyVmhOb2t5UmloeGlKLzN6RXc9PSIsImt5YyI6Miwia3lpIjoic1ZrUEh4ak1Hb2Fhc2o2Z3RXMVB4N2RUcENaS2c1LzZLbjFteGFpclpDbE84cWtiMWJMdGFmMklSVUtrTDd4RTd5ZEYvWU5DR1FXLzV5aTRWQnpUM1E9PSIsImNwayI6ImhCdjNtSEZjb0lETG5TckZ6dEdTTlpMT29aU2s1bUE4SHBQcE9MOFE1TldIdzdnNURWWUtFNVZGcWNrU2hwSWtyc0ZYd1dhMzZINDRQQU5PeVRta0RZMk93UnM1WCs4TkRWellHd0EvbloxUHRXVVJZOUxBdmZzc1RMcE54anl4MFJ4YUJ2aUtZTHVXeE9vTEd6N2xZUVA2Z2ZXODZqRWtVOHFaOEo2b2s0MD0iLCJ2ZXIiOjEsImNsdCI6MiwidXVkIjoiWE1MMEtKZC9pT1VGZENRQld0cWRXVUhNKytDNWNZVUQ2V21yNVI4R2cxdz0ifQ.CIPmOIt1b844AaPoAfDo3nzyNY9J3vcN40hLQRsMZYXftYXj0VdhjaUC51QhN8D7Ubx8ZBn8Fh6rlXdkuLRawQ")
                    .addHeader("x-client-signature-version", "1.3")
                    .addHeader("x-site-info", "=0HNxojI5RXa05WZiwiIMFkQPx0Rfh1SPJiOiUGZvNmIsIyRTJiOi42bpdWZyJye")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .addHeader("x-request-timestamp", "1766829224564")
                    .addHeader("devid", "29b06fad-95d8-4bc3-8dbe-eb2301a88a53")
                    .addHeader("app-type", "web")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36")
                    .addHeader("accept", "application/json")
                    .addHeader("x-utc", "8")
                    .addHeader("x-simulated-trading", "undefined")
                    .addHeader("x-client-signature", "{P1363}1VzXkVLHxBmL4oTQGMkCMk4GQ5jZsSlgoCqIPYBXmFfYJk0K9/dy6FepC5FRp3ILDXMjT23M2QniDS3h4Vj3dw==")
                    .addHeader("x-zkdex-env", "0")
                    .addHeader("x-id-group", "2140168270313090011-c-17")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("referer", "https://www.okx.com/zh-hans/copy-trading/account/E512EAA2C34FAF44?tab=trade")
                    .addHeader("accept-language", "zh-CN,zh;q=0.9")
                    .addHeader("cookie", "devId=29b06fad-95d8-4bc3-8dbe-eb2301a88a53; locale=zh_CN; ok_prefer_udColor=0; _gcl_au=1.1.1992442658.1764048054; _ym_d=1764048067; _ym_uid=1764048067422820891; intercom-device-id-ny9cf50h=a653c72f-0941-4962-8f9a-2c0e972b4f81; intercom-id-ny9cf50h=e3f590e4-e5ee-4c45-940f-ed5564137fab; _gcl_gs=2.1.k1$i1764048124$u3801134; ok_prefer_cm=3; fingerprint_id=29b06fad-95d8-4bc3-8dbe-eb2301a88a53; OptanonAlertBoxClosed=2025-12-02T08:50:47.468Z; ok_prefer_currency=0%7C1%7Cfalse%7CUSD%7C2%7C%24%7C1%7C1%7C%E7%BE%8E%E5%85%83; OptanonConsent=isGpcEnabled=0&datestamp=Tue+Dec+02+2025+16%3A55%3A56+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202405.1.0&browserGpcFlag=0&isIABGlobal=false&hosts=&consentId=aa27c081-88e4-4749-a4b6-868862120e5b&interactionCount=1&isAnonUser=1&landingPath=NotLandingPage&groups=C0004%3A1%2CC0002%3A1%2CC0003%3A1%2CC0001%3A1&intType=1&geolocation=SG%3B&AwaitingReconsent=false; g_state={\"i_l\":0,\"i_ll\":1765195794218,\"i_b\":\"FjkeLJkxVurg+xqttiV20ynM5e6xO23cE7vf7t1vImk\"}; isLogin=1; _tk=gqqKYvOtvhxJ78CE3gAB8A==; ok_login_type=OKX_GLOBAL; ok_prefer_udTimeZone=1; preferLocale=zh_CN; ok_site_info==0HNxojI5RXa05WZiwiIMFkQPx0Rfh1SPJiOiUGZvNmIsIyRTJiOi42bpdWZyJye; ok_global={%22okg_m%22:%22xl%22}; okg.currentMedia=xl; _gid=GA1.2.1381400353.1766733534; first_ref=https%3A%2F%2Fwww.okx.com%2Fzh-hans; _ym_isad=2; ok-exp-time=1766826170776; token=eyJraWQiOiIxMzYzODYiLCJhbGciOiJFUzI1NiJ9.eyJqdGkiOiJleDExMDE3NjUxOTU5NTYxNjc4QjI2NjM4MzlCRkE1NzQ4MVhqdFkiLCJ1aWQiOiJlTTVmZVdCWnZ0d0xKK2xwZWRUNXV3PT0iLCJzdGEiOjAsIm1pZCI6ImVNNWZlV0JadnR3TEorbHBlZFQ1dXc9PSIsInBpZCI6IlBUeUE4VzA5ekZVSkJHSjZZUk5HWXc9PSIsIm5kZSI6MCwiaWF0IjoxNzY2ODI2MTcyLCJleHAiOjE3NjgwMzU3NzIsImJpZCI6MCwiZG9tIjoid3d3Lm9reC5jb20iLCJlaWQiOjE0LCJpc3MiOiJva2NvaW4iLCJkaWQiOiJqV1RxaHlZOWtEaEFlRjlLd1JkTzJ6aGJWbmh0OGg2UkliOXN4aWFBK0VyS29QWElOcFZYdjA4Q3FSK2Y1NGYvIiwiZmlkIjoialdUcWh5WTlrRGhBZUY5S3dSZE8yemhiVm5odDhoNlJJYjlzeGlhQStFcktvUFhJTnBWWHYwOENxUitmNTRmLyIsImxpZCI6ImVNNWZlV0JadnR3TEorbHBlZFQ1dXc9PSIsInVmYiI6IlBUeUE4VzA5ekZVSkJHSjZZUk5HWXc9PSIsInVwYiI6ImlCcmEyVmhOb2t5UmloeGlKLzN6RXc9PSIsImt5YyI6Miwia3lpIjoic1ZrUEh4ak1Hb2Fhc2o2Z3RXMVB4N2RUcENaS2c1LzZLbjFteGFpclpDbE84cWtiMWJMdGFmMklSVUtrTDd4RTd5ZEYvWU5DR1FXLzV5aTRWQnpUM1E9PSIsImNwayI6ImhCdjNtSEZjb0lETG5TckZ6dEdTTlpMT29aU2s1bUE4SHBQcE9MOFE1TldIdzdnNURWWUtFNVZGcWNrU2hwSWtyc0ZYd1dhMzZINDRQQU5PeVRta0RZMk93UnM1WCs4TkRWellHd0EvbloxUHRXVVJZOUxBdmZzc1RMcE54anl4MFJ4YUJ2aUtZTHVXeE9vTEd6N2xZUVA2Z2ZXODZqRWtVOHFaOEo2b2s0MD0iLCJ2ZXIiOjEsImNsdCI6MiwidXVkIjoiWE1MMEtKZC9pT1VGZENRQld0cWRXVUhNKytDNWNZVUQ2V21yNVI4R2cxdz0ifQ.CIPmOIt1b844AaPoAfDo3nzyNY9J3vcN40hLQRsMZYXftYXj0VdhjaUC51QhN8D7Ubx8ZBn8Fh6rlXdkuLRawQ; tmx_session_id=ii40jfavsm_1766826172926; fp_s=0; _ga=GA1.2.1670802556.1764048054; _ga_G0EKWWQGTZ=GS2.1.s1766826171$o40$g1$t1766827033$j58$l0$h0; ok-ses-id=baGxvUiYZd0CYCMEnXr5Zy+T1+jjz0XgXn4AtqgScmHHAnqV9VJ7nyvFGs7zKUg5L6mv+/i9/GVi2CxM+13cqTJX07v+bOwLDXtITBu+WilB1X1HL3672TpTsAftz99d; traceId=2130368292181130001; ok_prefer_exp=1; _monitor_extras={\"deviceId\":\"ouC_6eUGRTYo3OeW0pTRgO\",\"eventId\":887,\"sequenceNumber\":887}; __cf_bm=enLoseAQ6rjeB3pr9Uhci38xttIWaho03B2f6ge32Mk-1766829224.1857493-1.0.1.1-e_HCEC5GMVv9RWkWl6T05Up_a4_JAu1DYXEEp5cfglFh6mvwlID81uKSfOi9wrvoQb3WtsdOTleHl8wZEykmWVoBpsTr4yvIrqK6tarP9eXWa5nXIJ.k8fKSk6XRzidT; __cf_bm=00RgDfJBxLqv0CNgO2td4Rvp2d1rg49_Mbm6Wo913YI-1766829813.66723-1.0.1.1-nMQAJm2I3ojBFnnOSGqjMMnaAY6lRNYXeQVTQHYCzdD52xUKLYiiT5Xno4Z0sxTGdKOSKv4jZVzMJI.KhUO3Q7cXgIpJl.34pO_YvNVKriPXxbRKDJDj5wx.sKZja_DW")
                    .build();
            Response response = client.newCall(request).execute();
            String s = response.body().string();
            if (StringUtils.isNotBlank(s)) {
                JSONObject jsonObject = JSON.parseObject(s);
                if ("0".equals(jsonObject.getString("code"))) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (Object entryNew : jsonArray) {
                        JSONObject entity = (JSONObject) entryNew;
                        String currency = entity.getString("currency");
                        if("USDT".equals(currency)){
                            return entity.getBigDecimal("amount");
                        }
                    }
                }
            }
        }

        return null;

    }
}
