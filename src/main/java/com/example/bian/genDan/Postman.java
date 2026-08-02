package com.example.bian.genDan;

import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.xin.JianKong4;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Postman {
    public static void main(String[] args) throws InterruptedException, IOException {

        // 准备工作
        // 对https也开启代理
        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10819");

        args = new String[2];
        System.out.println("开始啦");
        args[0] = "E://code//biance";
        args[1] = "jianKong";
        PrivateConfig.before(args[0], args[1]);

        Postman postman = new Postman();
        postman.ceShi();

    }

    public  void ceShi() throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(1,
                        1,
                        10,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.DiscardPolicy());


        /*System.out.println(JianKongTransfer.getTransfer(threadPoolExecutor));
        System.out.println(genDan.getOrder(threadPoolExecutor));
        System.out.println(JianKong4.getPositionHistory(threadPoolExecutor, 1, 1));
        System.out.println(JianKong4.getPosition(threadPoolExecutor));*/
    }

    public static String sendPostPhone(String param) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, param);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://www.mokexapp.tv/bapi/futures/v1/private/future/copy-trade/copy-portfolio/create")
                .method("POST", body)
                .addHeader("Host", "www.mokexapp.tv")
                .addHeader("x-token", PrivateConfig.genDan_token)
                .addHeader("bnc-req-src", "native")
                .addHeader("clienttype", "android")
                .addHeader("x-trace-id", "android_8fbeff66-8458-4073-b7e8-c6189a12627f")
                .addHeader("fvideo-id", "2328e05fa9f2a07581f36886824c433bb98e40fe")
                .addHeader("fvideo-token", "n1DuQdrUY+fDx6RFHTkRfdIGYdqgfIbP1RjGPQAhfhZS1ZmZnglNGJAKtfB+nYanSFFt+KJh8o35+WkQOH82ch7KVvMCIgVlx0zgkr6dm4o05EnhzCWUa/gm0WvC0Gy9V3dSCE4ge5WFLquBMgxE7HIu3e7yf7WwizqpdrA08isyfna4+Ivxzsnu4a4HbpseY=48")
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
                .addHeader("device-info", "eyJkZXZpY2VfaWQiOiIiLCJhX2Jvb3Rsb2FkZXIiOiJ1bmtub3duIiwiYV9icmFuZCI6IlhpYW9taSIsImFfbG9jYXRpb25fY2l0eSI6InVua25vd24iLCJhX2NwdV9hYmkiOiJbYXJtNjQtdjhhLCBhcm1lYWJpLXY3YSwgYXJtZWFiaV0iLCJhX2RldmljZV9sb2dpbl9uYW1lIjoiZWxpc2giLCJkZXZpY2VfbmFtZSI6Ik0yMTA1SzgxQUMiLCJhX2Rpc3BsYXkiOiJUS1ExLjIyMTAxMy4wMDIgdGVzdC1rZXlzIiwiYV9maW5nZXJwcmludCI6IlhpYW9taS9lbGlzaC9lbGlzaDoxMy9US1ExLjIyMTAxMy4wMDIvVjE0LjAuNS4wLlRLWUNOWE06dXNlci9yZWxlYXNlLWtleXMiLCJhX2hvc3QiOiJwYW5ndS1idWlsZC1jb21wb25lbnQtc3lzdGVtLTE3Njc0NC0wbG5zNi1md3duZi1rNnY2eCIsImFfZGV2aWNlX3ZlcnNpb25faWQiOiJUS1ExLjIyMTAxMy4wMDIiLCJhX2ltZWkiOiJ1bmtub3duIiwiYV9yb21fc2l6ZSI6IjIzMSwyMjFNQiIsImFfbWFjX2FkZHJlc3MiOiJ1bmtub3duIiwiYV9nZXRfbGluZV9udW1iZXIiOiJ1bmtub3duIiwiYV9wcm9kdWN0IjoiZWxpc2giLCJhX3JhbV9zaXplICI6IjcsNjA5TUIiLCJhX3NjcmVlbkhlaWdodCI6IjI1MjQiLCJhX3NjcmVlbldpZHRoIjoiMTYwMCIsImFfc2RrIjoiMzMiLCJhX3NlcmlhbF9pbmZvIjoidW5rbm93biIsImFfc2ltX3NlcmlhbF9udW1iZXIiOiJ1bmtub3duIiwiYV9idWlsZF90aW1lIjoiMTY5NTE3NTM0NzAwMCIsImFfdXNlciI6ImJ1aWxkZXIiLCJicmFuZF9tb2RlbCI6IlhpYW9taU0yMTA1SzgxQUMiLCJhX2FwcF9pbnN0YWxsX2RhdGUiOiIxNzMwNjM4NzM0MTM5IiwianVkZ2Vfcm9vdCI6MSwic2NyZWVuX3Jlc29sdXRpb24iOiIxNjAwKjI1MjQiLCJzeXN0ZW1fbGFuZyI6InpoLUNOIiwic3lzdGVtX3ZlcnNpb24iOiIzMyIsInN5c3RlbV92ZXJzaW9uX25hbWUiOiIxMyIsInRpbWV6b25lIjoiR01UKzA4MDAifQ==")
                .addHeader("mclient-x-tag", "tfph2mpTPAuwxbiMHoQc")
                .addHeader("bnc-location", "CN")
                .addHeader("bnc-currency", "USD")
                .addHeader("referer", "https://www.binance.com/")
                .addHeader("bnc-level", "0")
                .addHeader("x-seccheck-sig", "a1.6.3#pQAAAI8AAACuAAAAEwAAAL-GQadyFBZgfrCe4xqmN1lHF0QT7BAr2tDxe7mAzEfsGxlJ4WWKW-a36Vtc-be_pmnwePjeiUItmSnxXREFCV7_ch6fmjhQjRmUa95z6dQ8PVehLZ1FkAkXtHsGSr8_PlcAbx2xN9ULQT20EKTEQkATmkipBtpYbxn6t132A4ZLbbsQygF-B-foB2LAXT6YjzJ8hcbNBGDZcri8FY8FyeFI14OWcaXca3rB4OuAAK62FqJSYu9fk8QxcQRz1vFCtNQpMiQLksW8sLw-ef5YgIsnZPpb")
                .addHeader("x-seccheck-token", "a1.6.3#5QEAAE8CAACeAgAA8wAAALjbUwGyD2n6Pj-a4M5vDiKzXoqOLIGHUOj4fbbKgfCxLg61mfMTF9gYQUMpyLmV9EdW_x1cpi-0Acu6EGd5oUw7VRyHN3RX4ofQmrhgrPl0knK7hrfE1Q0yo_ycve6GD0IImcjTsHh7MQ3InNd84pHwwjQVlyc4dMCwHh1BCvcYxULFx7X3Vs71JXLGSUpb7F_JFe-zbqZCwEM_alR7DgQgxRBfPcvssjo2UQpXf9NtfGRVqqLBLt09OLc3CHE2rZo6hlFfA2gyQqyciXdrYkeE-pJ8jjTF-WKA2Se9FiwsopA_Vy_7DHXp4GpcwZ-14F5dk_MaT3gokLLseFJkiTGx_6lBbJp8FiLpCReDGQ9CMwNjhRk-V73rj4vHGU1FLcxMztkAgdJJe7dKw7fjmMlVjZiWNmMgDB39J8fPVP7lMe7m1ab8f1PR8-GoT3O8RQEpUFworVdiji9cpDj5M9qAb6O28uc7VWDrw-aLsfq84bswHMlFFD_K72V8U6sPiqZJkIWd8QSMXF9FQN0MDS92hOuy4LZhcnyWkTN4lWcJIE2bvpgp7uQeN33ldS3uB1qJ_8AU51b0ip2tzu34hM9bk4NjeCypLoDBQuWztCkZlGgwc5EXWzcgU5IUO3KPcZF1Ms6n5SHiAPVZwJtG3zABZuNWpJnzlHpLPbHNefSY_8Fh9wf9DbHpkAZiU2ueFRm5-8YmbnyUizEN7wmKx0krJLw4jIeV8hKlq8nCP_-G59xjTJXJDxPpQEgKiKgZOfxrc4eP41Xu3M3SIOo_HU9_i6Z1yVRFfpJOerOVB7g5opqbkPK7winyP0KtYnR683SUbHH6Dr_vDkXHc3Htj6bA3TIiDNYgXzYP3piWJwh-XwV3y5pUWlRVtvxXuYaeWiEQ3mV86-fVelePwxhY6K95c5ckptgMdV-tunthoO1CT_OIRzvJVxZKRiKH_d-4hVE315SPmLFEA_g-QiUlGEO7sjcys8C6e9KNF29ZTYv6REdmPX3dWpIbvoDA_8831jI441v2ohracgb2BaZcLhJlBlnf2bXtUjbIYEIARr323z_AeQ#B03677CD")
                .addHeader("bnc-cpk", "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEfzODU3jqzIdimXv8QGwoGPqraz+5Mi86/TYD4FSl1rpYHdIdtS6imV4IP5A1FhdzZWIucuZf2u7IYuhin1N3Rw==")
                .addHeader("content-type", "application/json")
                .addHeader("user-agent", "okhttp/4.12.0")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String sendPost(String url, String param, String cookie, String token) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, param);
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("authority", PrivateConfig.genDan_url)
                .addHeader("accept", "*/*")
                .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8")
                .addHeader("bnc-uuid", "60cf9e75-1f53-49d9-91b8-d67edbee73db")
                .addHeader("clienttype", "web")
                .addHeader("content-type", "application/json")
                .addHeader("device-info", "eyJzY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA4MCIsImF2YWlsYWJsZV9zY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA1MCIsInN5c3RlbV92ZXJzaW9uIjoiV2luZG93cyAxMCIsImJyYW5kX21vZGVsIjoidW5rbm93biIsInN5c3RlbV9sYW5nIjoiemgtQ04iLCJ0aW1lem9uZSI6IkdNVCswODowMCIsInRpbWV6b25lT2Zmc2V0IjotNDgwLCJ1c2VyX2FnZW50IjoiTW96aWxsYS81LjAgKFdpbmRvd3MgTlQgMTAuMDsgV2luNjQ7IHg2NCkgQXBwbGVXZWJLaXQvNTM3LjM2IChLSFRNTCwgbGlrZSBHZWNrbykgQ2hyb21lLzEyMC4wLjAuMCBTYWZhcmkvNTM3LjM2IiwibGlzdF9wbHVnaW4iOiJQREYgVmlld2VyLENocm9tZSBQREYgVmlld2VyLENocm9taXVtIFBERiBWaWV3ZXIsTWljcm9zb2Z0IEVkZ2UgUERGIFZpZXdlcixXZWJLaXQgYnVpbHQtaW4gUERGIiwiY2FudmFzX2NvZGUiOiIyNzAzMDQxNCIsIndlYmdsX3ZlbmRvciI6Ikdvb2dsZSBJbmMuIChJbnRlbCkiLCJ3ZWJnbF9yZW5kZXJlciI6IkFOR0xFIChJbnRlbCwgSW50ZWwoUikgSEQgR3JhcGhpY3MgNjMwICgweDAwMDA1OTEyKSBEaXJlY3QzRDExIHZzXzVfMCBwc181XzAsIEQzRDExKSIsImF1ZGlvIjoiMTI0LjA0MzQ3NTI3NTE2MDc0IiwicGxhdGZvcm0iOiJXaW4zMiIsIndlYl90aW1lem9uZSI6IkFzaWEvU2hhbmdoYWkiLCJkZXZpY2VfbmFtZSI6IkNocm9tZSBWMTIwLjAuMC4wIChXaW5kb3dzKSIsImZpbmdlcnByaW50IjoiZDI4YWFjZmYxOTgyOTk4Njk3YmM3YmUwYzAxOTA2YTkiLCJkZXZpY2VfaWQiOiIiLCJyZWxhdGVkX2RldmljZV9pZHMiOiIsIn0=")
                .addHeader("fvideo-id", "3303ee55ac7eac53af446a458e1a9aad2f1ba95f")
                .addHeader("fvideo-token", "7dU9Tw0tSuq7hkfbmLiI7on5Gd3Yu1hepRqhNnGSdnrGSQj9N7eeVPLU2EkZHI+3l4Z/zRdylPdKiO9y13TL9CYV0hhaRgeyVjt0bglFU9LIRX7tUIsBZHDiwnODeu5nCHBxyWOPuXI4SE7sJDbYsoKhRndNPguG+3MnIhCdOouBfH7469Q0AWW/2bHw/oMAE=64")
                .addHeader("lang", "zh-CN")
                .addHeader("origin", "https://" + PrivateConfig.genDan_url)
                .addHeader("referer", "https://" + PrivateConfig.genDan_url + "/zh-CN/copy-trading/lead-details/3965785765605235713?timeRange=7D")
                .addHeader("sec-ch-ua", "\"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .addHeader("x-passthrough-token", "")
                .addHeader("x-trace-id", "476dba14-da14-4f75-879b-bbb4b3bea712")
                .addHeader("x-ui-request-trace", "476dba14-da14-4f75-879b-bbb4b3bea712")
//                .addHeader("Cookie", "aliyungf_tc=8fcec5e314ea024908a84684e2c2adbb69723c99c9b28b95cb005aa48a5b3e8e")
                .build();

                if(StringUtils.isNotBlank(cookie)){
                    request  = new Request.Builder()
                            .url(url)
                            .method("POST", body)
                            .addHeader("authority", PrivateConfig.genDan_url)
                            .addHeader("accept", "*/*")
                            .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8")
                            .addHeader("bnc-uuid", "60cf9e75-1f53-49d9-91b8-d67edbee73db")
                            .addHeader("clienttype", "web")
                            .addHeader("content-type", "application/json")
                            .addHeader("device-info", "eyJzY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA4MCIsImF2YWlsYWJsZV9zY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA1MCIsInN5c3RlbV92ZXJzaW9uIjoiV2luZG93cyAxMCIsImJyYW5kX21vZGVsIjoidW5rbm93biIsInN5c3RlbV9sYW5nIjoiemgtQ04iLCJ0aW1lem9uZSI6IkdNVCswODowMCIsInRpbWV6b25lT2Zmc2V0IjotNDgwLCJ1c2VyX2FnZW50IjoiTW96aWxsYS81LjAgKFdpbmRvd3MgTlQgMTAuMDsgV2luNjQ7IHg2NCkgQXBwbGVXZWJLaXQvNTM3LjM2IChLSFRNTCwgbGlrZSBHZWNrbykgQ2hyb21lLzEyMC4wLjAuMCBTYWZhcmkvNTM3LjM2IiwibGlzdF9wbHVnaW4iOiJQREYgVmlld2VyLENocm9tZSBQREYgVmlld2VyLENocm9taXVtIFBERiBWaWV3ZXIsTWljcm9zb2Z0IEVkZ2UgUERGIFZpZXdlcixXZWJLaXQgYnVpbHQtaW4gUERGIiwiY2FudmFzX2NvZGUiOiIyNzAzMDQxNCIsIndlYmdsX3ZlbmRvciI6Ikdvb2dsZSBJbmMuIChJbnRlbCkiLCJ3ZWJnbF9yZW5kZXJlciI6IkFOR0xFIChJbnRlbCwgSW50ZWwoUikgSEQgR3JhcGhpY3MgNjMwICgweDAwMDA1OTEyKSBEaXJlY3QzRDExIHZzXzVfMCBwc181XzAsIEQzRDExKSIsImF1ZGlvIjoiMTI0LjA0MzQ3NTI3NTE2MDc0IiwicGxhdGZvcm0iOiJXaW4zMiIsIndlYl90aW1lem9uZSI6IkFzaWEvU2hhbmdoYWkiLCJkZXZpY2VfbmFtZSI6IkNocm9tZSBWMTIwLjAuMC4wIChXaW5kb3dzKSIsImZpbmdlcnByaW50IjoiZDI4YWFjZmYxOTgyOTk4Njk3YmM3YmUwYzAxOTA2YTkiLCJkZXZpY2VfaWQiOiIiLCJyZWxhdGVkX2RldmljZV9pZHMiOiIsIn0=")
                            .addHeader("fvideo-id", "3303ee55ac7eac53af446a458e1a9aad2f1ba95f")
                            .addHeader("fvideo-token", "7dU9Tw0tSuq7hkfbmLiI7on5Gd3Yu1hepRqhNnGSdnrGSQj9N7eeVPLU2EkZHI+3l4Z/zRdylPdKiO9y13TL9CYV0hhaRgeyVjt0bglFU9LIRX7tUIsBZHDiwnODeu5nCHBxyWOPuXI4SE7sJDbYsoKhRndNPguG+3MnIhCdOouBfH7469Q0AWW/2bHw/oMAE=64")
                            .addHeader("lang", "zh-CN")
                            .addHeader("origin", "https://" + PrivateConfig.genDan_url)
                            .addHeader("referer", "https://" + PrivateConfig.genDan_url + "/zh-CN/copy-trading/lead-details/3965785765605235713?timeRange=7D")
                            .addHeader("sec-ch-ua", "\"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"")
                            .addHeader("sec-ch-ua-mobile", "?0")
                            .addHeader("sec-ch-ua-platform", "\"Windows\"")
                            .addHeader("sec-fetch-dest", "empty")
                            .addHeader("sec-fetch-mode", "cors")
                            .addHeader("sec-fetch-site", "same-origin")
                            .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                            .addHeader("x-passthrough-token", "")
                            .addHeader("x-trace-id", "476dba14-da14-4f75-879b-bbb4b3bea712")
                            .addHeader("x-ui-request-trace", "476dba14-da14-4f75-879b-bbb4b3bea712")
//                            .addHeader("Cookie", "aliyungf_tc=8fcec5e314ea024908a84684e2c2adbb69723c99c9b28b95cb005aa48a5b3e8e")
                            .addHeader("cookie", cookie)
                            .addHeader("csrftoken", token)
                            .build();
                }

        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public static String sendGet(String url, String cookie, String token) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(url)
//                .method("GET", body)
                .addHeader("authority", PrivateConfig.genDan_url)
                .addHeader("accept", "*/*")
                .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8")
                .addHeader("bnc-uuid", "60cf9e75-1f53-49d9-91b8-d67edbee73db")
                .addHeader("clienttype", "web")
                .addHeader("content-type", "application/json")
//                .addHeader("cookie", cookie)
//                .addHeader("csrftoken", token)
                .addHeader("device-info", "eyJzY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA4MCIsImF2YWlsYWJsZV9zY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA1MCIsInN5c3RlbV92ZXJzaW9uIjoiV2luZG93cyAxMCIsImJyYW5kX21vZGVsIjoidW5rbm93biIsInN5c3RlbV9sYW5nIjoiemgtQ04iLCJ0aW1lem9uZSI6IkdNVCswODowMCIsInRpbWV6b25lT2Zmc2V0IjotNDgwLCJ1c2VyX2FnZW50IjoiTW96aWxsYS81LjAgKFdpbmRvd3MgTlQgMTAuMDsgV2luNjQ7IHg2NCkgQXBwbGVXZWJLaXQvNTM3LjM2IChLSFRNTCwgbGlrZSBHZWNrbykgQ2hyb21lLzEyMC4wLjAuMCBTYWZhcmkvNTM3LjM2IiwibGlzdF9wbHVnaW4iOiJQREYgVmlld2VyLENocm9tZSBQREYgVmlld2VyLENocm9taXVtIFBERiBWaWV3ZXIsTWljcm9zb2Z0IEVkZ2UgUERGIFZpZXdlcixXZWJLaXQgYnVpbHQtaW4gUERGIiwiY2FudmFzX2NvZGUiOiIyNzAzMDQxNCIsIndlYmdsX3ZlbmRvciI6Ikdvb2dsZSBJbmMuIChJbnRlbCkiLCJ3ZWJnbF9yZW5kZXJlciI6IkFOR0xFIChJbnRlbCwgSW50ZWwoUikgSEQgR3JhcGhpY3MgNjMwICgweDAwMDA1OTEyKSBEaXJlY3QzRDExIHZzXzVfMCBwc181XzAsIEQzRDExKSIsImF1ZGlvIjoiMTI0LjA0MzQ3NTI3NTE2MDc0IiwicGxhdGZvcm0iOiJXaW4zMiIsIndlYl90aW1lem9uZSI6IkFzaWEvU2hhbmdoYWkiLCJkZXZpY2VfbmFtZSI6IkNocm9tZSBWMTIwLjAuMC4wIChXaW5kb3dzKSIsImZpbmdlcnByaW50IjoiZDI4YWFjZmYxOTgyOTk4Njk3YmM3YmUwYzAxOTA2YTkiLCJkZXZpY2VfaWQiOiIiLCJyZWxhdGVkX2RldmljZV9pZHMiOiIsIn0=")
                .addHeader("fvideo-id", "3303ee55ac7eac53af446a458e1a9aad2f1ba95f")
                .addHeader("fvideo-token", "7JVquN5wstsGIXZgqkdDKqdDXBnnUcc3+wzIhmJxL4Koxl/+M8khvGiS/1yGBrORqbWybJvys5cxUd1y6LBpzaSdaMQbxMAOvJ5zEIBD9kpOnNk43Py6QAGlYofTnIG4HgeoemDbwPBkGc0053Z7RizD2g5Xgl0XnWPZqWpdE9bce+LIJi3hjwvoyG9PvYg3k=73")
                .addHeader("lang", "zh-CN")
                .addHeader("referer", "https://" + PrivateConfig.genDan_url + "/zh-CN/copy-trading/lead-details/3966748461221672193?timeRange=7D")
                .addHeader("sec-ch-ua", "\"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .addHeader("x-passthrough-token", "")
                .addHeader("x-trace-id", "8d881471-27fb-467e-bb5f-e227fc50034e")
                .addHeader("x-ui-request-trace", "8d881471-27fb-467e-bb5f-e227fc50034e")
                .build();

        if(StringUtils.isNotBlank(cookie)){
            request = new Request.Builder()
                    .url(url)
//                .method("GET", body)
                    .addHeader("authority", PrivateConfig.genDan_url)
                    .addHeader("accept", "*/*")
                    .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8")
                    .addHeader("bnc-uuid", "60cf9e75-1f53-49d9-91b8-d67edbee73db")
                    .addHeader("clienttype", "web")
                    .addHeader("content-type", "application/json")
                .addHeader("cookie", cookie)
                .addHeader("csrftoken", token)
                    .addHeader("device-info", "eyJzY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA4MCIsImF2YWlsYWJsZV9zY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA1MCIsInN5c3RlbV92ZXJzaW9uIjoiV2luZG93cyAxMCIsImJyYW5kX21vZGVsIjoidW5rbm93biIsInN5c3RlbV9sYW5nIjoiemgtQ04iLCJ0aW1lem9uZSI6IkdNVCswODowMCIsInRpbWV6b25lT2Zmc2V0IjotNDgwLCJ1c2VyX2FnZW50IjoiTW96aWxsYS81LjAgKFdpbmRvd3MgTlQgMTAuMDsgV2luNjQ7IHg2NCkgQXBwbGVXZWJLaXQvNTM3LjM2IChLSFRNTCwgbGlrZSBHZWNrbykgQ2hyb21lLzEyMC4wLjAuMCBTYWZhcmkvNTM3LjM2IiwibGlzdF9wbHVnaW4iOiJQREYgVmlld2VyLENocm9tZSBQREYgVmlld2VyLENocm9taXVtIFBERiBWaWV3ZXIsTWljcm9zb2Z0IEVkZ2UgUERGIFZpZXdlcixXZWJLaXQgYnVpbHQtaW4gUERGIiwiY2FudmFzX2NvZGUiOiIyNzAzMDQxNCIsIndlYmdsX3ZlbmRvciI6Ikdvb2dsZSBJbmMuIChJbnRlbCkiLCJ3ZWJnbF9yZW5kZXJlciI6IkFOR0xFIChJbnRlbCwgSW50ZWwoUikgSEQgR3JhcGhpY3MgNjMwICgweDAwMDA1OTEyKSBEaXJlY3QzRDExIHZzXzVfMCBwc181XzAsIEQzRDExKSIsImF1ZGlvIjoiMTI0LjA0MzQ3NTI3NTE2MDc0IiwicGxhdGZvcm0iOiJXaW4zMiIsIndlYl90aW1lem9uZSI6IkFzaWEvU2hhbmdoYWkiLCJkZXZpY2VfbmFtZSI6IkNocm9tZSBWMTIwLjAuMC4wIChXaW5kb3dzKSIsImZpbmdlcnByaW50IjoiZDI4YWFjZmYxOTgyOTk4Njk3YmM3YmUwYzAxOTA2YTkiLCJkZXZpY2VfaWQiOiIiLCJyZWxhdGVkX2RldmljZV9pZHMiOiIsIn0=")
                    .addHeader("fvideo-id", "3303ee55ac7eac53af446a458e1a9aad2f1ba95f")
                    .addHeader("fvideo-token", "7JVquN5wstsGIXZgqkdDKqdDXBnnUcc3+wzIhmJxL4Koxl/+M8khvGiS/1yGBrORqbWybJvys5cxUd1y6LBpzaSdaMQbxMAOvJ5zEIBD9kpOnNk43Py6QAGlYofTnIG4HgeoemDbwPBkGc0053Z7RizD2g5Xgl0XnWPZqWpdE9bce+LIJi3hjwvoyG9PvYg3k=73")
                    .addHeader("lang", "zh-CN")
                    .addHeader("referer", "https://" + PrivateConfig.genDan_url + "/zh-CN/copy-trading/lead-details/3966748461221672193?timeRange=7D")
                    .addHeader("sec-ch-ua", "\"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .addHeader("x-passthrough-token", "")
                    .addHeader("x-trace-id", "8d881471-27fb-467e-bb5f-e227fc50034e")
                    .addHeader("x-ui-request-trace", "8d881471-27fb-467e-bb5f-e227fc50034e")
                    .build();
        }
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
