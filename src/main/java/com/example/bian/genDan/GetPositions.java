package com.example.bian.genDan;

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
import java.util.concurrent.*;

import static com.example.bian.client.bushu.PrivateConfig.getPon;
import static com.example.bian.client.bushu.PrivateConfig.ling;

public class GetPositions {

    public static void main(String[] args) throws IOException, InterruptedException {


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

        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(5, 5, 10,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.DiscardPolicy());
        PrivateConfig.threadPoolExecutor = threadPoolExecutor;

//        getPosition("4777921357644221697");
        System.out.println(getOrders(threadPoolExecutor, "4918666677208285440"));


    }
    public static JSONArray getOrders(ThreadPoolExecutor threadPoolExecutor, String genPortfolioId) throws InterruptedException {
        for (int i = 0; i < getPon; i++) {
            String s = getOrder(threadPoolExecutor, genPortfolioId);
            if (StringUtils.isNotBlank(s)) {
//                System.out.println(s);
                JSONObject jsonObject = JSON.parseObject(s);
                if ("000000".equals(jsonObject.getString("code"))) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    return PrivateConfig.reBuiltJsonArray(jsonArray);
                } else if ("100002001".equals(jsonObject.getString("code"))) {
                    PrivateConfig.printLog(jsonObject.getString("message"));
                    T5.searchAll("抓紧联系我1，" + jsonObject.getString("message"));
                    Thread.sleep(1000 * 60);
                }else if ("100002002".equals(jsonObject.getString("code"))) {
                    PrivateConfig.printLog(jsonObject.getString("message"));
                    T5.searchAll("抓紧联系我2，" + jsonObject.getString("message"));
                    Thread.sleep(1000 * 60);
                }else {
                    PrivateConfig.printLog(jsonObject.getString("message"));
                    T5.searchAll("抓紧联系我3，" + jsonObject.getString("message"));
                    Thread.sleep(1000 * 60);
                }
            }
            Thread.sleep(1000 * 60);
        }
        PrivateConfig.printLog("币安跟单-获取订单有问题了1");
        return null;
    }

    public static String getOrder(ThreadPoolExecutor threadPoolExecutor, String genPortfolioId) {

        //订单的顺序：第一个就是最近的一个
        Callable callable = new Callable() {
            @Override
            public String call() throws Exception {
                return getPosition(genPortfolioId);
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

    public static String getPosition(String genPortfolioId) throws IOException, InterruptedException {

        if (PrivateConfig.genDan_position.equals("0")) {
//            System.out.println("获取id：" + PrivateConfig.genDan_genPortfolioId);
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"portfolioId\":\" " + genPortfolioId + "\",\"copyTradeType\":\"COPY\"}");
            /*if("1".equals(PrivateConfig.ceShi)){
                System.out.println(PrivateConfig.genDan_token);
            }*/
            return PostGet.postPhone(body, "https://www.binance.com/bapi/futures/v6/private/future/user-data/user-position");
            /*Request request = new Request.Builder()
                    .url("https://www.binance.com/bapi/futures/v6/private/future/user-data/user-position")
                    .method("POST", body)
                    .addHeader("Host", "www.binance.com")
                    .addHeader("x-token", PrivateConfig.genDan_token)
                    .addHeader("bnc-req-src", "native")
                    .addHeader("clienttype", "android")
                    .addHeader("x-trace-id", "android_d7850d5c-6ec0-45de-83a2-449f9a0ff3af")
                    .addHeader("fvideo-id", "2328e05fa9f2a07581f36886824c433bb98e40fe")
                    .addHeader("fvideo-token", "IuNC8r7N/hWNE6pCi8uDHozvcZunn+THMykKUV2F4RTDHgTlg3W9o/B8J8tynyK2Pt/a4a90nf7G9E8MQw58FuVFvRpZ9m2Z5KY2uTdsN0+Wr+mjGMJWpjoZg7kzD2lG3CmhTUZWkPET35CriQwldOG4PbGdnIGVzeXtHrrLbPtPe6F0zmELVsHfVi3NvPq/A=76")
                    .addHeader("lang", "zh-CN")
                    .addHeader("versioncode", "29202")
                    .addHeader("versionname", "2.92.2")
                    .addHeader("isnight", "false")
                    .addHeader("bnc-app-mode", "pro")
                    .addHeader("bnc-uuid", "850ec9079879179b6eec1707dfe13c8e")
                    .addHeader("bnc-time-zone", "Asia/Shanghai")
                    .addHeader("bnc-app-channel", "binance")
                    .addHeader("bnc-app-id", "1")
                    .addHeader("device-info", "eyJkZXZpY2VfaWQiOiIiLCJhX2Jvb3Rsb2FkZXIiOiJ1bmtub3duIiwiYV9icmFuZCI6IlhpYW9taSIsImFfY3B1X2FiaSI6Ilthcm02NC12OGEsIGFybWVhYmktdjdhLCBhcm1lYWJpXSIsImFfZGV2aWNlX2xvZ2luX25hbWUiOiJlbGlzaCIsImRldmljZV9uYW1lIjoiTTIxMDVLODFBQyIsImFfZGlzcGxheSI6IlRLUTEuMjIxMDEzLjAwMiB0ZXN0LWtleXMiLCJhX2ZpbmdlcnByaW50IjoiWGlhb21pL2VsaXNoL2VsaXNoOjEzL1RLUTEuMjIxMDEzLjAwMi9WMTQuMC41LjAuVEtZQ05YTTp1c2VyL3JlbGVhc2Uta2V5cyIsImFfaG9zdCI6InBhbmd1LWJ1aWxkLWNvbXBvbmVudC1zeXN0ZW0tMTc2NzQ0LTBsbnM2LWZ3d25mLWs2djZ4IiwiYV9kZXZpY2VfdmVyc2lvbl9pZCI6IlRLUTEuMjIxMDEzLjAwMiIsImFfcHJvZHVjdCI6ImVsaXNoIiwiYV9zY3JlZW5IZWlnaHQiOiIyNTI0IiwiYV9zY3JlZW5XaWR0aCI6IjE2MDAiLCJhX3NkayI6IjMzIiwiYV9idWlsZF90aW1lIjoiMTY5NTE3NTM0NzAwMCIsImFfdXNlciI6ImJ1aWxkZXIiLCJicmFuZF9tb2RlbCI6IlhpYW9taU0yMTA1SzgxQUMiLCJhX2FwcF9pbnN0YWxsX2RhdGUiOiIxNzMwNjM4NzM0MTM5IiwianVkZ2Vfcm9vdCI6MCwic2NyZWVuX3Jlc29sdXRpb24iOiIxNjAwKjI1MjQiLCJzeXN0ZW1fbGFuZyI6InpoLUNOIiwic3lzdGVtX3ZlcnNpb24iOiIzMyIsInRpbWV6b25lIjoiR01UKzA4MDAifQ==")
                    .addHeader("mclient-x-tag", "Z73vs0d3eu67rDqMCbSH")
                    .addHeader("bnc-location", "BINANCE")
                    .addHeader("bnc-currency", "CNY")
                    .addHeader("referer", "https://www.binance.com/")
                    .addHeader("x-seccheck-sig", "a1.5.4#rAAAACgAAAByAAAAcwAAAGicDX_5LHH31d6-qCwIDxZqJ5lZz3-swk5quRJsQb02LcwiMKU6cG0oUzKNVT77tm_FkbvzQIhfulMU5mrlOBdHy4mdbyd0MSTuiOoVXTYW1idRIpOX9Y9-ygGxT41yJp6ugql1P967ve31u3CRqvwnunJzKkR_C0w0FnOUkYd3wNv9eBCO8IgdUfcx8XInYWpF0L9UYkwrW2vHfWNnJhUzRd4Ua4wzcTpYaQms2PGPomKZu-GDaUc2O6rwZGna17-Z75SCvHHyHWx413Cym0Q1EVJO")
                    .addHeader("x-seccheck-token", "a1.5.4#rAIAAGgBAAByAAAAwwAAAKPs68HMOB4q6WYIhIHehw3AZ37oizyinQCM1T7DIHMWTqSCm9vIFXPBZ0zW7PWjo_urBiUjWAkP9y4Sd-g2ZLFDI7D-Ay8258TQQZGOI3DiQ8xrDNKoJpKeMGNZe56NGobthfePTqbpSO-qwpcrhE51AHLcLtXIpiuL_VwZO_JgDnHkMVlC2X0yotuTR0-5OhR6OsPNb38pFgODEbOG3W4I5TeK4hvrkmRYpkqUGBCdchanWMTrwIzWG46Y9OTqcdx-P8Md6UAfTTzWaPAT0fi0rrymESDx7kohmTNMOAvfZIv0f5Yf3Zk_1Mq_8lKte01Kl_ED1rJyBFzE2Mn07AYapO7nNDPRSbTIdORezud0ZBB5hRfxhUXB6gl77vOt6JAtKZYMpiz1QBU-DNMJFsD01JFzc0tGpoFbsBUr79R3CPa-BxbGqUh4pD3RW7cIzVVR4xWfRruE4MAOOc7ymJzUnGX_67H41mgVxY91ilZ5nEIq-gbBdZdQCOAeS_RxjdtqIJH-6mrKSmGosoNq8jGizsZLFRAeC38MVsIGtbDlUj0YW1roArd4jMOykTp0ImSMS4bmBcmNMWmxx7D14HpmtFJoRdusxiKcvSmyuu7upXjg8iKKm4aZ2ewj0E366zshYsGFF3MGFi75OuppErXE_5n47q9VQjuP9VyXGvP1TW6jTJuyHwz7tgK9jRWljtCtW0jG3A-E9itY9-HCWjL5sBGX1ZRu2EcOxqk3kUNzlD0YPVM42Rwm80Vc8SDCoXC_PTMy_5HXpUQgKHb2wnOAgc0yNrxwKmFYt6P80CvODQzz-44w8T1OjlA-qFRpUBmkOhQxKXb_lHZyQ5t2283cwbEdXCvNQQ9ucs7y-R73W3xJ-7IMIq456cY2DxHn3jhi4NwrwU5dnGdB0KyWknQXbwHzMmylXKl_i6CbmiziOWLFrWKd-HBNpngx_i5hkO5Zxd4v0P9ex7bEZhDx5_ul66Mg8emhtCEYXnnDVJUG4NM6LKoDaaXtWzraf2BVB_r1OwVQJv9PedRqof1R0JTxIdRDa1HxWm_d90VgRJ-rUGtlkg#55922C1A")
                    .addHeader("bnc-cpk", "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAERHCIrq8ochHqaQ5iPa/Q6itIt3wJmf3CDC5w9vTBVpqszoVnHrtUva1RK2ouYMvZxFoymvyfNW8ajQHc6Ae2Fg==")
                    .addHeader("content-type", "application/json")
                    .addHeader("user-agent", "okhttp/4.12.0")
                    .build();
            Response response = client.newCall(request).execute();

            *//*if(PrivateConfig.ceShi.equals("1")){
                System.out.println("获取成功了");
            }*//*
            return response.body().string();*/

        } else if (PrivateConfig.genDan_position.equals("2")) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"copyTradeType\":\"COPY\",\"portfolioId\":\"" + genPortfolioId + "\"}");
            Request request = new Request.Builder()
                    .url("https://" + PrivateConfig.genDan_url + "/bapi/futures/v6/private/future/user-data/user-position")
                    .method("POST", body)
                    .addHeader("accept", "*/*")
                    .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6")
                    .addHeader("bnc-location", "BINANCE")
                    .addHeader("bnc-uuid", "d56eb723-020e-4546-b979-2482e1c8114b")
                    .addHeader("clienttype", "web")
                    .addHeader("content-type", "application/json")
                    .addHeader("cookie", PrivateConfig.genDan_cookie)
                    .addHeader("csrftoken", PrivateConfig.genDan_token)
                    .addHeader("device-info", "eyJzY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA4MCIsImF2YWlsYWJsZV9zY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA0MCIsInN5c3RlbV92ZXJzaW9uIjoiV2luZG93cyAxMCIsImJyYW5kX21vZGVsIjoidW5rbm93biIsInN5c3RlbV9sYW5nIjoiemgtQ04iLCJ0aW1lem9uZSI6IkdNVCswODowMCIsInRpbWV6b25lT2Zmc2V0IjotNDgwLCJ1c2VyX2FnZW50IjoiTW96aWxsYS81LjAgKFdpbmRvd3MgTlQgMTAuMDsgV2luNjQ7IHg2NCkgQXBwbGVXZWJLaXQvNTM3LjM2IChLSFRNTCwgbGlrZSBHZWNrbykgQ2hyb21lLzEzMC4wLjAuMCBTYWZhcmkvNTM3LjM2IEVkZy8xMzAuMC4wLjAiLCJsaXN0X3BsdWdpbiI6IlBERiBWaWV3ZXIsQ2hyb21lIFBERiBWaWV3ZXIsQ2hyb21pdW0gUERGIFZpZXdlcixNaWNyb3NvZnQgRWRnZSBQREYgVmlld2VyLFdlYktpdCBidWlsdC1pbiBQREYiLCJjYW52YXNfY29kZSI6IjIxOWYxMTU3Iiwid2ViZ2xfdmVuZG9yIjoiR29vZ2xlIEluYy4gKE5WSURJQSkiLCJ3ZWJnbF9yZW5kZXJlciI6IkFOR0xFIChOVklESUEsIE5WSURJQSBHZUZvcmNlIEdUWCAxMDUwIFRpICgweDAwMDAxQzgyKSBEaXJlY3QzRDExIHZzXzVfMCBwc181XzAsIEQzRDExKSIsImF1ZGlvIjoiMTI0LjA0MzQ3NTI3NTE2MDc0IiwicGxhdGZvcm0iOiJXaW4zMiIsIndlYl90aW1lem9uZSI6IkFzaWEvU2hhbmdoYWkiLCJkZXZpY2VfbmFtZSI6IkVkZ2UgVjEzMC4wLjAuMCAoV2luZG93cykiLCJmaW5nZXJwcmludCI6IjFkZDNmOGIwMjA3YjVmYTRkZWFhMDk2YjQ3MmQ1NjVkIiwiZGV2aWNlX2lkIjoiIiwicmVsYXRlZF9kZXZpY2VfaWRzIjoiIn0=")
                    .addHeader("fvideo-id", "337b5f104434a720be7bc601990ab06d34ae359c")
                    .addHeader("fvideo-token", "mJzzyif1iMzADF5/DgZWOx3vgAjffpE6333YrHUyfAiKhUIahm0Mj3xNbhxknuLlgc0Y/NtQ59xwK89B16ZPhTWRbf+l0CV7iazgHoYg3mXuIb5wBgRlbfYCwmNsMLEUcwpTcORSpxjTmd1Q2a/Ao0hQYYgF4fWKn73BjlvHNL5QeykiAGOcVWXZB7twRb2nc=3c")
                    .addHeader("lang", "zh-CN")
                    .addHeader("origin", "https://www.suitechsui.io")
                    .addHeader("priority", "u=1, i")
                    .addHeader("referer", "https://www.suitechsui.io/zh-CN/copy-trading/copy-management")
                    .addHeader("sec-ch-ua", "\"Chromium\";v=\"130\", \"Microsoft Edge\";v=\"130\", \"Not?A_Brand\";v=\"99\"")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36 Edg/130.0.0.0")
                    .addHeader("x-passthrough-token", "")
                    .addHeader("x-trace-id", "679352f7-75e9-4f9e-9c21-b71b85e0d71b")
                    .addHeader("x-ui-request-trace", "679352f7-75e9-4f9e-9c21-b71b85e0d71b")
                    .build();
            Response response = client.newCall(request).execute();

            return response.body().string();
        }else if (PrivateConfig.genDan_position.equals("3")) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("https://www.binance.com/bapi/futures/v1/friendly/future/copy-trade/lead-data/positions?portfolioId=" + genPortfolioId)
//                    .method("GET", body)
                    .addHeader("authority", "www.binance.com")
                    .addHeader("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"")
                    .addHeader("csrftoken", PrivateConfig.genDan_token)
                    .addHeader("cookie", PrivateConfig.genDan_cookie)
                    .addHeader("bnc-time-zone", "Asia/Shanghai")
                    .addHeader("lang", "zh-CN")
                    .addHeader("device-info", "eyJzY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA4MCIsImF2YWlsYWJsZV9zY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA0MCIsInN5c3RlbV92ZXJzaW9uIjoiV2luZG93cyA4LjEiLCJicmFuZF9tb2RlbCI6InVua25vd24iLCJzeXN0ZW1fbGFuZyI6InpoLUNOIiwidGltZXpvbmUiOiJHTVQrMDg6MDAiLCJ0aW1lem9uZU9mZnNldCI6LTQ4MCwidXNlcl9hZ2VudCI6Ik1vemlsbGEvNS4wIChXaW5kb3dzIE5UIDYuMzsgV2luNjQ7IHg2NCkgQXBwbGVXZWJLaXQvNTM3LjM2IChLSFRNTCwgbGlrZSBHZWNrbykgQ2hyb21lLzk3LjAuNDY5Mi43MSBTYWZhcmkvNTM3LjM2IiwibGlzdF9wbHVnaW4iOiJQREYgVmlld2VyLENocm9tZSBQREYgVmlld2VyLENocm9taXVtIFBERiBWaWV3ZXIsTWljcm9zb2Z0IEVkZ2UgUERGIFZpZXdlcixXZWJLaXQgYnVpbHQtaW4gUERGIiwiY2FudmFzX2NvZGUiOiJ1bmtub3duIiwid2ViZ2xfdmVuZG9yIjoiR29vZ2xlIEluYy4gKEdvb2dsZSkiLCJ3ZWJnbF9yZW5kZXJlciI6IkFOR0xFIChHb29nbGUsIFZ1bGthbiAxLjIuMCAoU3dpZnRTaGFkZXIgRGV2aWNlIChTdWJ6ZXJvKSAoMHgwMDAwQzBERSkpLCBTd2lmdFNoYWRlciBkcml2ZXItNS4wLjApIiwiYXVkaW8iOiIxMjQuMDQzNDc1Mjc1MTYwNzQiLCJwbGF0Zm9ybSI6IldpbjMyIiwid2ViX3RpbWV6b25lIjoiQXNpYS9TaGFuZ2hhaSIsImRldmljZV9uYW1lIjoiQ2hyb21lIFY5Ny4wLjQ2OTIuNzEgKFdpbmRvd3MpIiwiZmluZ2VycHJpbnQiOiIzMWFiNmVkNjdmNTFhM2EyMzM5MDEwNmZjZmRmYTkwNyIsImRldmljZV9pZCI6IiIsInJlbGF0ZWRfZGV2aWNlX2lkcyI6IiJ9")
                    .addHeader("bnc-uuid", "3c9a9035-1af1-42f4-bd85-f77737265d5b")
                    .addHeader("fvideo-token", "1wJRCEhITeqnMToICpfYBnoaoGjA9TJg2mYBEl+iCWsnYQ2PYPY0P2JZc8QmzpnrK+fON+lUPJBybcOULqa/Dk+340Cg8llyo4zC0Qw5fGLidZXoiJ4fJfs8m9cOeBzpwGWbTDl4Clxy71TFzKw3k04dlZzrMGp8k0putzoYOWrdOK77LvaWC13YUTrS0HZEc=3b")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .addHeader("fvideo-id", "3397ebf940f4a2e29a02f0753a55a51b993ca093")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("x-ui-request-trace", "d8b75337-7a6f-4e66-b281-453324cd138b")
                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36")
                    .addHeader("x-trace-id", "d8b75337-7a6f-4e66-b281-453324cd138b")
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
                    .addHeader("if-none-match", "W/\"091cdf704efe278938b3c5a6634bee7c1\"")
                    .build();
            Response response = client.newCall(request).execute();
            String s = response.body().string();
            if (StringUtils.isNotBlank(s)) {
                JSONObject jsonObject = JSON.parseObject(s);
                if ("000000".equals(jsonObject.getString("code"))) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if(CollectionUtils.isEmpty(jsonArray)){
                        jsonObject.put("code", "100002001");
                        jsonObject.put("message", "登录状态失效！或币安仓位出问题了");
                        return jsonObject.toJSONString();
                    }
                    JSONArray jsonArrayNew = new JSONArray();
                    for (Object o : jsonArray) {
                        JSONObject trade = (JSONObject) o;
                        BigDecimal qty = trade.getBigDecimal(PrivateConfig.positionAmount);
                        if(qty.compareTo(ling) == 0){
                            continue;
                        }
                        jsonArrayNew.add(trade);
                    }
                    jsonObject.put("data", jsonArrayNew);
                    return jsonObject.toJSONString();
                }
            }
            return s;
        }else if (PrivateConfig.genDan_position.equals("5") || PrivateConfig.genDan_position.equals("6")) {
            return getCongMingQian(genPortfolioId);
        }else {
            return getPostionPrivate(genPortfolioId);
        }


    }

    public static String getCongMingQian(String genPortfolioId) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request;
        if (PrivateConfig.genDan_position.equals("5")){
            request = new Request.Builder()
                    .url("https://" + PrivateConfig.genDan_url + "/bapi/asset/v1/private/future/smart-money/profile/query-positions?marketType=UM&page=1&rows=50&topTraderId=" + genPortfolioId)
                    .addHeader("Host", "www.binance.com")
                    .addHeader("bnc-req-src", "mp")
                    .addHeader("x-token", PrivateConfig.genDan_token)
                    .addHeader("content-type", "application/json")
                    .addHeader("clienttype", "android")
                    .addHeader("x-trace-id", "android_25be455c-286a-45ff-899f-f19f3ad40d34")
                    .addHeader("fvideo-id", "2328e05fa9f2a07581f36886824c433bb98e40fe")
                    .addHeader("fvideo-token", "BRXLZlwwNAWDc+KUWbLT8kGP5rxpAiv/YZF7g8rvvtGgc16UnCT8RjoKxHm9HFZIBtdBRklr3IUfFkRj5JqbAcvA02GJFg50OrZxSMILj87i7qQp1jhmgRT/hv9eDWTMai/zVudlmdjpROvglXg3RVBBCTrMmorZ3jiSoVOQVj44zn4eihDJxhu4SU6iCXG+w=37")
                    .addHeader("lang", "zh-CN")
                    .addHeader("versioncode", "301102")
                    .addHeader("versionname", "3.11.2")
                    .addHeader("isnight", "false")
                    .addHeader("bnc-neo-theme", "light_glacier")
                    .addHeader("bnc-app-mode", "pro")
                    .addHeader("bnc-uuid", "850ec9079879179b6eec1707dfe13c8e")
                    .addHeader("bnc-time-zone", "Asia/Shanghai")
                    .addHeader("bnc-app-channel", "binance")
                    .addHeader("bnc-app-id", "1")
                    .addHeader("device-info", "eyJkZXZpY2VfaWQiOiIiLCJhX2Jvb3Rsb2FkZXIiOiJ1bmtub3duIiwiYV9icmFuZCI6IlhpYW9taSIsImFfbG9jYXRpb25fY2l0eSI6InVua25vd24iLCJhX2NwdV9hYmkiOiJbYXJtNjQtdjhhLCBhcm1lYWJpLXY3YSwgYXJtZWFiaV0iLCJhX2RldmljZV9sb2dpbl9uYW1lIjoiZWxpc2giLCJkZXZpY2VfbmFtZSI6Ik0yMTA1SzgxQUMiLCJhX2Rpc3BsYXkiOiJUS1ExLjIyMTAxMy4wMDIgdGVzdC1rZXlzIiwiYV9maW5nZXJwcmludCI6IlhpYW9taS9lbGlzaC9lbGlzaDoxMy9US1ExLjIyMTAxMy4wMDIvVjE0LjAuNS4wLlRLWUNOWE06dXNlci9yZWxlYXNlLWtleXMiLCJhX2hvc3QiOiJwYW5ndS1idWlsZC1jb21wb25lbnQtc3lzdGVtLTE3Njc0NC0wbG5zNi1md3duZi1rNnY2eCIsImFfZGV2aWNlX3ZlcnNpb25faWQiOiJUS1ExLjIyMTAxMy4wMDIiLCJhX2ltZWkiOiJ1bmtub3duIiwiYV9yb21fc2l6ZSI6IjIzMSwyMjFNQiIsImFfbWFjX2FkZHJlc3MiOiJ1bmtub3duIiwiYV9nZXRfbGluZV9udW1iZXIiOiJ1bmtub3duIiwiYV9wcm9kdWN0IjoiZWxpc2giLCJhX3JhbV9zaXplICI6IjcsNjA5TUIiLCJhX3NjcmVlbkhlaWdodCI6IjE1NjQiLCJhX3NjcmVlbldpZHRoIjoiMTI4MiIsImFfc2RrIjoiMzMiLCJhX3NlcmlhbF9pbmZvIjoidW5rbm93biIsImFfc2ltX3NlcmlhbF9udW1iZXIiOiJ1bmtub3duIiwiYV9idWlsZF90aW1lIjoiMTY5NTE3NTM0NzAwMCIsImFfdXNlciI6ImJ1aWxkZXIiLCJicmFuZF9tb2RlbCI6IlhpYW9taU0yMTA1SzgxQUMiLCJhX2FwcF9pbnN0YWxsX2RhdGUiOiIxNzMwNjM4NzM0MTM5IiwianVkZ2Vfcm9vdCI6MSwic2NyZWVuX3Jlc29sdXRpb24iOiIxMjgyKjE1NjQiLCJzeXN0ZW1fbGFuZyI6InpoLUNOIiwic3lzdGVtX3ZlcnNpb24iOiIzMyIsInN5c3RlbV92ZXJzaW9uX25hbWUiOiIxMyIsInRpbWV6b25lIjoiR01UKzA4MDAifQ==")
                    .addHeader("mclient-x-tag", "tfph2mpTPAuwxbiMHoQc")
                    .addHeader("bnc-location", "CN")
                    .addHeader("bnc-currency", "USD")
                    .addHeader("referer", "https://www.binance.com/")
                    .addHeader("bnc-level", "0")
                    .addHeader("x-seccheck-sig", "a1.6.3#PwAAADMAAAApAAAAowAAALOY1I1yPOLHeKdGClDpwWk_YZFcp593kaIYRo0MbjsEAQCPnKBjMvf_KUvZM2dJLLendDOhw5ZVELoafmEt_j-B7MbV97_O3NWyoZ2yi8vgkR8oGBoDE4UPlMkKo4U0C8ZV2lNu0RBsb0VddoOSSPQlKIpeX78VIQNmgWN6q7yBtGv4mttG4A91AxfVTpfParQ2tyThmJusxLNZ0ym_y3ReNo2j2rpihv7VYBSBBFGzasplzZPowX7QcqunIDi4J5aYDFi24w6sueHP_ZGcNfQCmfjW")
                    .addHeader("x-seccheck-token", "a1.6.3#7wEAAPMBAAA5AAAAwwEAALjbUwGyD2n6Pj-a4M5vDiL9dPwsTqFL8nUOlklYV4_npfVC2h77YMZQP8bb4tA9UNEvOu5Xxy0_PTnqx_8G1BO-5oQjynJkw20v4wgYxPAS9o1pYgDINVMQG_B4IpCV21VcCBlq4O__ucIRLWy0F-U7a6FUdmlpBrfNbGNy4U6kBwYj6DaB-LdimiuwxF1PAy_NpAdBNuAUKU2fUkF_39Z5cSVaa-sUIIFFo7TbomrbddN1H3JvtTWshAJHWSm5gU-700JKYFl-6ehV9z0Hx8wXcm2-TdkmBuD9HoNrynsAsZTUCMa6nQnXpf8oWBoYYpOKw8kGJzCL-vEKJUelZ-mvFQLemcz2fw9OaicsAp0v-a8EHcR1raJIR9KLQQDurqJKP9Lx4Mi8UFXy3e6IMl5_VJaVIL3e8VMLi0_1-hDhS48Yb9ZbABS4pFU7BbvfgVGSGtOyS1v1KnYmJ_kiGl-rA-BUcK1dyc_N1i5RBcMfAiBoeM0t0UBbYevFWdSwP1_l-cV8jZk0gHteGoXh-Rif6J3Gj_Xl_8vvjsjmAsMXQPdwPybMKq0rHVfKjSCvpaa33UVoL_3isftRRE_V6BpUvRfDNT-fuZxvjoOMxu0cAsJEWXM5Sbg8LVrfQSUupQxtBM39dwpQGuxbnHYBRO_toJPzJQK8xLmb7NhUyN9Oq8XTPRsgIAx7QOSBvoDmv4EFaEUiseXuoyoiso8-NcK4RAIM94Ndaa3kxTmDNf5a9L_3wHvejX_hhZUi7iKKfLYBUwcG3V4uRkKlIz264qfTVIxmFT7JeE2cW3pNqu4omWrj6kuxSQdtwzIKRk3NiWj6VNi_suIYUMj1BmnUvvDC-xZJcSCIqcDa_iMAqmSAO3a56r9KJv68QPEN2x3xWWhLb0TLWUw1jRpXwwVad7SrFIC3uau4xueDXngm6naKD9EzbJu42c_gBPrHnfNGJoCqtQtS-LsppcYIZaPApawFrK1U03bF0tc1jHdf3RjjfkpENeZ0Fv-DAgKQE7P-6-1sApj2QY9qmAdLIj9OV2B5r4ztZ1Yxwkqu4M21pIfIyZKJjg#FD783A8F")
                    .addHeader("bnc-cpk", "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEvUegQuM677hyjBTfgJCF4p80Me88P3XcYGF8COssL9xTnCwxvBF7lR7QRMz/5T97Htpl7rWcVzFZS2Exxbn+Hw==")
                    .addHeader("user-agent", "okhttp/4.12.0")
                    .addHeader("if-none-match", "W/\"006a8776828ba91f2db5511abd58ed837\"")
                    .build();
        }else{
            request = new Request.Builder()
                    .url("https://" + PrivateConfig.genDan_url + "/bapi/asset/v1/private/future/smart-money/profile/query-positions?marketType=UM&page=1&rows=50&topTraderId=" + genPortfolioId)
                    .addHeader("authority", "www.binance.com")
                    .addHeader("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"")
                    .addHeader("csrftoken", PrivateConfig.genDan_token)
                    .addHeader("bnc-time-zone", "Asia/Shanghai")
                    .addHeader("lang", "zh-CN")
                    .addHeader("device-info", "eyJzY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA4MCIsImF2YWlsYWJsZV9zY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA0MCIsInN5c3RlbV92ZXJzaW9uIjoiV2luZG93cyA4LjEiLCJicmFuZF9tb2RlbCI6InVua25vd24iLCJzeXN0ZW1fbGFuZyI6InpoLUNOIiwidGltZXpvbmUiOiJHTVQrMDg6MDAiLCJ0aW1lem9uZU9mZnNldCI6LTQ4MCwidXNlcl9hZ2VudCI6Ik1vemlsbGEvNS4wIChXaW5kb3dzIE5UIDYuMzsgV2luNjQ7IHg2NCkgQXBwbGVXZWJLaXQvNTM3LjM2IChLSFRNTCwgbGlrZSBHZWNrbykgQ2hyb21lLzk3LjAuNDY5Mi43MSBTYWZhcmkvNTM3LjM2IiwibGlzdF9wbHVnaW4iOiJQREYgVmlld2VyLENocm9tZSBQREYgVmlld2VyLENocm9taXVtIFBERiBWaWV3ZXIsTWljcm9zb2Z0IEVkZ2UgUERGIFZpZXdlcixXZWJLaXQgYnVpbHQtaW4gUERGIiwiY2FudmFzX2NvZGUiOiJ1bmtub3duIiwid2ViZ2xfdmVuZG9yIjoiR29vZ2xlIEluYy4gKEdvb2dsZSkiLCJ3ZWJnbF9yZW5kZXJlciI6IkFOR0xFIChHb29nbGUsIFZ1bGthbiAxLjIuMCAoU3dpZnRTaGFkZXIgRGV2aWNlIChTdWJ6ZXJvKSAoMHgwMDAwQzBERSkpLCBTd2lmdFNoYWRlciBkcml2ZXItNS4wLjApIiwiYXVkaW8iOiIxMjQuMDQzNDc1Mjc1MTYwNzQiLCJwbGF0Zm9ybSI6IldpbjMyIiwid2ViX3RpbWV6b25lIjoiQXNpYS9TaGFuZ2hhaSIsImRldmljZV9uYW1lIjoiQ2hyb21lIFY5Ny4wLjQ2OTIuNzEgKFdpbmRvd3MpIiwiZmluZ2VycHJpbnQiOiIzMWFiNmVkNjdmNTFhM2EyMzM5MDEwNmZjZmRmYTkwNyIsImRldmljZV9pZCI6IiIsInJlbGF0ZWRfZGV2aWNlX2lkcyI6IiJ9")
                    .addHeader("bnc-uuid", "06794427-5f45-457b-a40d-9bbe448af1db")
                    .addHeader("fvideo-token", "ZTXniVKvHXlnKmdOjlDiL+ncjeZz2blV4UoXp7/Ueux2SwWRAtZN5GLxeib5c+cwtzq2mZ8SJ5/t5l5ks+nTpwqph1TaiaYiF0JOKzujvBoGPG6w6teHH6l01GHoSEDdZ6kTQVRkKV/XjISxEml9yVPWYUxd3Zh/zedye9rsi3B6nye46tdNwTTk9I6IUx0Kk=46")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .addHeader("fvideo-id", "33da7e181c2ca9589f6ba07b2213be4b03fec0b1")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("x-ui-request-trace", "ff2c0cf6-213d-4934-a2d8-a384fda9c69b")
                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36")
                    .addHeader("x-trace-id", "ff2c0cf6-213d-4934-a2d8-a384fda9c69b")
                    .addHeader("bnc-location", "CN")
                    .addHeader("x-passthrough-token", "eyJhbGciOiJSUzI1NiJ9.eyJpYXQiOjE3ODMzODk4MzMsImlzcyI6Imh0dHBzOi8vYmluYW5jZS5jb20iLCJzdWIiOiJ2YWxpZGF0aW9uX3Rva2VuIiwiZGV2aWNlSWQiOiIwOEIyNDZEOTU3NDM5MTBDOURFNjg2M0E1MkJBQkQ0RSIsImV4cCI6MTc4MzM5MDEzMywibmJmIjoxNzgzMzg5ODMzfQ.geZZzczloaYDyvCP5d6lO6QVJdwQEVDXiunYQGBc9aWdJKcHo1EvPj2uqOJEked6xAKLCemk7HeLA4pO_ZcRX7SHc3VrM9cHGlZ-z2PxFgb1yXpiunf2ONtnkTTYeayjMOSWlHalHQSyM4hzpJNUkROAxy5xte5fd2JIz5uCXk8")
                    .addHeader("content-type", "application/json")
                    .addHeader("bnc-level", "0")
                    .addHeader("clienttype", "web")
                    .addHeader("accept", "*/*")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("referer", "https://www.binance.com/zh-CN/smart-money/profile/5082050984257986817")
                    .addHeader("accept-language", "zh-CN,zh;q=0.9")
                    .addHeader("cookie", PrivateConfig.genDan_cookie)
                    .addHeader("if-none-match", "W/\"0a03b50b211c0b2173ecc06aa2554b319\"")
                    .build();
        }

        Response response = client.newCall(request).execute();

        String s = response.body().string();
        if (StringUtils.isNotBlank(s)) {
            JSONObject jsonObject = JSON.parseObject(s);
            if ("000000".equals(jsonObject.getString("code"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if(CollectionUtils.isEmpty(jsonArray)){
                    return s;
                }
                JSONArray jsonArrayNew = new JSONArray();
                for (Object o : jsonArray) {
                    JSONObject trade = (JSONObject) o;
                    trade.put(PrivateConfig.positionAmount, trade.getBigDecimal("amount"));//持仓数量
                    trade.put(PrivateConfig.positionSide, trade.getString("side"));
                    trade.put("unrealizedProfit", trade.getBigDecimal("pnl"));//当前亏损金额
                    trade.put("notionalValue", trade.getBigDecimal("amount").multiply(trade.getBigDecimal("markPrice")));//持仓金额
                    jsonArrayNew.add(trade);
                }
                jsonObject.put("data", jsonArrayNew);
                return jsonObject.toJSONString();
            }
        }
        return s;
    }

    /**
     * 单子是公开的，不用跟单，就能直接看到，也就不用token了
     * 如果跟单后才能看，此时需要有token
     * @return
     * @throws IOException
     */
    public static String getPostionPrivate(String genPortfolioId) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://www.binance.com/bapi/futures/v1/friendly/future/copy-trade/lead-data/positions?portfolioId=" + genPortfolioId)
                .method("GET", null)
                .addHeader("Host", "www.binance.com")
                .addHeader("x-token", PrivateConfig.genDan_token)
                .addHeader("clienttype", "android")
                .addHeader("x-trace-id", "android_f8fb7a94-6094-4a58-b4d7-c1d9607384d9")
                .addHeader("fvideo-id", "23e8a53dbdc4a3bf84be24d98d4cbf090ccfaf2a")
                .addHeader("fvideo-token", "6bMHgl2fOtM2jGVjWLtPq/brTgeL6TKQcyly8iWOGDWRQeWvMUSwoCUf3+20t+fOjU9G+urw3+eDwf53VqqfjqNK2+DnLkPyzWb7rdHBbxXudY8cA8k1feSWBniJPZD0XHWeqTI1xI8fjq5fJS52Gtox5quV3feddLp2d7aNqe33lKbV6GKSWRWQ3xrGjB6/k=26")
                .addHeader("lang", "zh-CN")
                .addHeader("versioncode", "28604")
                .addHeader("versionname", "2.86.4")
                .addHeader("isnight", "false")
                .addHeader("bnc-app-mode", "pro")
                .addHeader("bnc-uuid", "2ac40994-f482-4915-810b-3cfa284ab3b7")
                .addHeader("bnc-time-zone", "Asia/Shanghai")
                .addHeader("bnc-app-channel", "binance")
                .addHeader("bnc-app-id", "1")
                .addHeader("device-info", "eyJkZXZpY2VfaWQiOiIiLCJhX2Jvb3Rsb2FkZXIiOiJ1bmtub3duIiwiYV9icmFuZCI6InNhbXN1bmciLCJhX2xvY2F0aW9uX2NpdHkiOiJ1bmtub3duIiwiYV9jcHVfYWJpIjoiW3g4Nl82NCwgeDg2LCBhcm02NC12OGEsIGFybWVhYmktdjdhLCBhcm1lYWJpXSIsImFfZGV2aWNlX2xvZ2luX25hbWUiOiJzdGFyMnFsdGVjaG4iLCJkZXZpY2VfbmFtZSI6IlNNLUc5NzMwIiwiYV9kaXNwbGF5IjoiUFEzQi4xOTA4MDEuMDcxMDEwMjAgcmVsZWFzZS1rZXlzIiwiYV9maW5nZXJwcmludCI6InNhbXN1bmcvc3RhcjJxbHRlemgvc3RhcjJxbHRlY2huOjkvUFEzQi4xOTA4MDEuMDcxMDEwMjAvRzk2NTBaSFUyQVJDNjp1c2VyL3JlbGVhc2Uta2V5cyIsImFfaG9zdCI6ImRldiIsImFfZGV2aWNlX3ZlcnNpb25faWQiOiJQUTNCLjE5MDgwMS4wNzEwMTAyMCIsImFfaW1laSI6InVua25vd24iLCJhX3JvbV9zaXplIjoiMjUsMTY0TUIiLCJhX21hY19hZGRyZXNzIjoiMDA6ZGI6ZTI6ZDM6ZGY6MDUiLCJhX2dldF9saW5lX251bWJlciI6InVua25vd24iLCJhX3Byb2R1Y3QiOiJTTS1HOTczMCIsImFfcmFtX3NpemUgIjoiMywwMDJNQiIsImFfc2NyZWVuSGVpZ2h0IjoiMTkyMCIsImFfc2NyZWVuV2lkdGgiOiIxMDgwIiwiYV9zZGsiOiIyOCIsImFfc2VyaWFsX2luZm8iOiJ1bmtub3duIiwiYV9zaW1fc2VyaWFsX251bWJlciI6InVua25vd24iLCJhX2J1aWxkX3RpbWUiOiIxNzIwNTc4MDI3MDAwIiwiYV91c2VyIjoiYnVpbGQiLCJicmFuZF9tb2RlbCI6InNhbXN1bmdTTS1HOTczMCIsImFfYXBwX2luc3RhbGxfZGF0ZSI6IjE3MjE5NzMwNjM3NTYiLCJqdWRnZV9yb290IjowLCJzY3JlZW5fcmVzb2x1dGlvbiI6IjEwODAqMTkyMCIsInN5c3RlbV9sYW5nIjoiemgtQ04iLCJzeXN0ZW1fdmVyc2lvbiI6IjI4Iiwic3lzdGVtX3ZlcnNpb25fbmFtZSI6IjkiLCJ0aW1lem9uZSI6IkdNVCswODAwIn0=")
                .addHeader("mclient-x-tag", "Z73vs0d3eu67rDqMCbSH")
                .addHeader("bnc-location", "BINANCE")
                .addHeader("bnc-currency", "CNY")
                .addHeader("referer", "https://www.binance.com/")
                .addHeader("x-seccheck-sig", "a1.5.4#awAAACsAAAAUAAAAagAAAFzqo45WTS0NZNMKzYz4zYP4Lsc9FGAPAFjDdDK_0nGaO1k-WHHV99Rbd0IruJKgSIEyhcAzqfwZaNfRSClWDbaWxyXf1FZMjCB3lDp8d3Vkj0pwEN1a449p9OABUyB6PzNgEeWitjb6pnNqa00wK_blC3d9CCIkJyS7h9J_qQRtkACZSdoBShfAH3AN0hjfTmxP_nUg7yPO7gRlw17Owo-7QM8DzFMl82ybSdyAmhtUmxxSy4NyzR7IiH_z5MdW288g-QJ4hhJFsQj95ajQ9qw0Q1Gl")
                .addHeader("x-seccheck-token", "a1.5.4#WwIAAEsAAAC0AQAAWgEAAP5sRicPBrooIWMlQx6Qh4XIEZzgAZMjKe2hkNGW5jVEQwsn7QlimouQPtzzQSIQk3k51dHV0R0QrB-LkvIzCGTHN-RMgLs5f_QjNEs1aIciNcGvyFIMoRMNbA3A-dRTurYseD-hiq0LSJD9ibTRQDXQTPDvcMm76EFWfgmxdVW0oGhSGtNU-wwrlq-MYMZuzKOGWhRqkGmJIpG3ml9X5VJgKHwrg1wDL_VzhyVs8EsIddBnvhqXd9nryUwf-p5kXf-Sy5qXz0YLNAd0k4h342r-cL0njXl3bFOfer8eADouM4nXB4wPetDjlmvztvC2WqFcKpTVLRNiSZnQwAWCUd4NsMwaconIh9fdCeRmgr46CI6rEKTJFOkV2jrrL4LmBtMzuzknfb0i4t8-hQW6S_w2zqQeubsewxXvF8gQV_U5-OarOTI7LYtfoYpB_lCYpEcDI3cdbLDwa2paSF7iRjXajlgxRfBwvU5FRfxgNV7VSW4hgmkgvMOh4i_Wlac3qyu-XvDs9ctPkoLYqLHq_vGyr89vClHShbi-bhz0tokt9ZXfZrdJCqhxjBffgzWx6R-m-Ee0SlBJpStNPLiNz8bXK8WSmjW9hG3-7GpPpJqzEyakjlRNFFa6vROkCMwAaZQIMcQV6MZMwXNnaMzzayW-UNWtGtqjWZbNqPn8CCMMl_PMSkX1Ssq_hbeGeKPCfid7YE6mDbHls1mfSUAlwRlYHC42dkhxX0LUrYhaAtTTtKZEzucSn6_vI4u86Yadb5tDUlVOJBCmz6QJAfxxGbLzTPYYPvvmDA02n1tZX3Lkv2GHz5sEBRTPLgbrmZXmOq83K41rK4bdqytHF06jX9TDpa-J0Oa3DkQsl1g9eKWHGRGp1sH0GkHlxMQxirEl-QMjv1aSijanb0_qEYaI2Xn7K_Fp6Cu9F8U7PBVDztZsY-fQMIpTubYxKPrzWA6y22Mg93-J-LMOYQnyWsZ8ZM7gvqjY0rRm2Vh3bOnUT2kEEj7jfffoMbZPHRuneZq6W73wr5rOrw2ByFccU_kJ0WzBdJ2X#5FF6EA87")
                .addHeader("bnc-cpk", "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEuCwEXWqP/G+TnJkgIlfyYOY2wOGkjkO4qmFxDP7XzxdJmc+UFrWN33RbSa+K9vOv1Kwb8/w+NMk2AWa7FBPB9A==")
                .addHeader("content-type", "application/json")
                .addHeader("user-agent", "okhttp/4.11.0")
                .build();
        Response response = client.newCall(request).execute();
        String s = response.body().string();
        if (StringUtils.isNotBlank(s)) {
            JSONObject jsonObject = JSON.parseObject(s);
            if ("000000".equals(jsonObject.getString("code"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if(CollectionUtils.isEmpty(jsonArray)){
                    jsonObject.put("code", "100002001");
                    jsonObject.put("message", "登录状态失效！！或币安仓位出问题了");
                    return jsonObject.toJSONString();
                }
                JSONArray jsonArrayNew = new JSONArray();
                for (Object o : jsonArray) {
                    JSONObject trade = (JSONObject) o;
                    BigDecimal qty = trade.getBigDecimal(PrivateConfig.positionAmount);
                    if(qty.compareTo(ling) == 0){
                        continue;
                    }
                    jsonArrayNew.add(trade);
                }
                jsonObject.put("data", jsonArrayNew);
                return jsonObject.toJSONString();
            }
        }
        return s;
    }

}
