package com.example.bian.genDan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.bushu.PrivateConfig;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;

public class PostGet {

    public static String postPhone(RequestBody body, String url) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
            /*if("1".equals(PrivateConfig.ceShi)){
                System.out.println(PrivateConfig.genDan_token);
            }*/
        Request request = new Request.Builder()
                .url(url)
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

            /*if(PrivateConfig.ceShi.equals("1")){
                System.out.println("获取成功了");
            }*/
        return response.body().string();
    }

    public static String getPhone(String url) throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(url)
//                .method("GET", body)
                .addHeader("Host", "www.binance.com")
                .addHeader("x-token", PrivateConfig.genDan_token)
                .addHeader("bnc-req-src", "native")
                .addHeader("clienttype", "android")
                .addHeader("x-trace-id", "android_a8d8f652-e31f-4edd-8324-12e68c080a7e")
                .addHeader("fvideo-id", "2328e05fa9f2a07581f36886824c433bb98e40fe")
                .addHeader("fvideo-token", "594G41g/jw65DK5rnh+GjHwkk/R/DljLcPMyEkkAhNOjWK1FZMN32siTQw/Odj1skiEjmfkc0kVzGGTloN7YpUbQJYz5wWXMU0A56wTPD0ew9E/JZL/gwTD+NMJVsYK3QB7HF6aaQ46+DAWJWr6Wcf/tnZlF6cfQT7q0wPaQw3r8kKoR9n2eYFQaFLFJte5bs=48")
                .addHeader("lang", "zh-CN")
                .addHeader("versioncode", "29304")
                .addHeader("versionname", "2.93.4")
                .addHeader("isnight", "false")
                .addHeader("bnc-app-mode", "pro")
                .addHeader("bnc-uuid", "850ec9079879179b6eec1707dfe13c8e")
                .addHeader("bnc-time-zone", "Asia/Shanghai")
                .addHeader("bnc-app-channel", "binance")
                .addHeader("bnc-app-id", "1")
                .addHeader("device-info", "eyJkZXZpY2VfaWQiOiIiLCJhX2Jvb3Rsb2FkZXIiOiJ1bmtub3duIiwiYV9icmFuZCI6IlhpYW9taSIsImFfY3B1X2FiaSI6Ilthcm02NC12OGEsIGFybWVhYmktdjdhLCBhcm1lYWJpXSIsImFfZGV2aWNlX2xvZ2luX25hbWUiOiJlbGlzaCIsImRldmljZV9uYW1lIjoiTTIxMDVLODFBQyIsImFfZGlzcGxheSI6IlRLUTEuMjIxMDEzLjAwMiB0ZXN0LWtleXMiLCJhX2ZpbmdlcnByaW50IjoiWGlhb21pL2VsaXNoL2VsaXNoOjEzL1RLUTEuMjIxMDEzLjAwMi9WMTQuMC41LjAuVEtZQ05YTTp1c2VyL3JlbGVhc2Uta2V5cyIsImFfaG9zdCI6InBhbmd1LWJ1aWxkLWNvbXBvbmVudC1zeXN0ZW0tMTc2NzQ0LTBsbnM2LWZ3d25mLWs2djZ4IiwiYV9kZXZpY2VfdmVyc2lvbl9pZCI6IlRLUTEuMjIxMDEzLjAwMiIsImFfcHJvZHVjdCI6ImVsaXNoIiwiYV9zY3JlZW5IZWlnaHQiOiIyNTI0IiwiYV9zY3JlZW5XaWR0aCI6IjE2MDAiLCJhX3NkayI6IjMzIiwiYV9idWlsZF90aW1lIjoiMTY5NTE3NTM0NzAwMCIsImFfdXNlciI6ImJ1aWxkZXIiLCJicmFuZF9tb2RlbCI6IlhpYW9taU0yMTA1SzgxQUMiLCJhX2FwcF9pbnN0YWxsX2RhdGUiOiIxNzMwNjM4NzM0MTM5IiwianVkZ2Vfcm9vdCI6MCwic2NyZWVuX3Jlc29sdXRpb24iOiIxNjAwKjI1MjQiLCJzeXN0ZW1fbGFuZyI6InpoLUNOIiwic3lzdGVtX3ZlcnNpb24iOiIzMyIsInRpbWV6b25lIjoiR01UKzA4MDAifQ==")
                .addHeader("mclient-x-tag", "tvXLzOPgJFiMa8Omltoo")
                .addHeader("bnc-location", "BINANCE")
                .addHeader("bnc-currency", "USD")
                .addHeader("referer", "https://www.binance.com/")
                .addHeader("x-seccheck-sig", "a1.5.4#wQAAAJYAAACZAAAAaAAAAP60neXGFRGqtliI4CqPVb_6NPkuXhsXiJWGc5Es11C4iq29G1OFGL4oEY5tyNl8yP1I50QbjgHg9JyBwEgbBNgzQM3E85La_z1IwQWtN-D9QZtBohCUbgrHrHgWfXRF8jPjQ2zuXllumaDIHNsFQXgKyiP3Xs9oETMdoNwgcWyfNzvOHh5ZgKCFGfUbX2bZYkY_KOMFMsHEKZYAIO9qpS7eEGRPJDbaBQR_VGQFm9GEMbw8gxnynE8wkH5ZJ-YZkcI4FZQMEuG5kKKTBIOKPtIGcCAb")
                .addHeader("x-seccheck-token", "a1.5.4#6AAAAGECAABWAQAA-QAAAFxtKJ1gYYLxakh_0CVHA629LbuLL-V7fSCBnEsi65lgRCx3Dl0q7u3P1wAhQ2AgpFuBJomBSx55Ye0F54ytSS232Y66diBY_UBclLSbj3jlokGwL2tvBO-pBijKBiNMB4WI6qegPE5cIyf-Fs7krphc99l0xNCirebNZkqhP86msLKCv9YF6it3xmPOK8YSFquIK-lNTgY6obBV64MdWSF4FC_Ce_GbkMn7JiuZeUpOhxYApRpmo9MVD-uqXGPu09ytiTcHLbQc9_Fdiasj6wJgvjodAnDblglzUCsJQm0tCfgbfnp8UPboorWuCH2tiAkTFnptOA9KofnzQYSAEZ9dhs6ZIMU6pSbXsos1HPooUXwVJ0XObYxhwfmb7jWilbodmJ3cWYfdaKXNhG_PX8mIM5m9zyc9hB5VKOoa1UyK3dm3bvzgFOiaLvEF-6qv85xK3oZjIVawYYQdyVs0mS652mi6IejkhFbxTDLoik_G3yyPrZ-ZxQtPBUYs9SVFAc0jsT-_0uxUbSMYNfjhT_xhpzmi1NiCIXFIpKAjY5JByAnggPCdKwtIOgsEBURt9s21C20t-kOYnMCwFCzbBLwQY3ecVd0j6uueeOxKOC7129F0PXJhLTgXdOkQs0IlNZB0z2KzSxQAz4Bei7yJQW5WC8Ieg5g47TuGNcJ_qEwziP0F07d9MYzpgnBn-oA5wHakdPvWfn5yCTzL65_HYRJ2kk7_47dDOivjo-mkRy3OGcVDwWwPgJTUqugWu1HlPE1wK8tC0CIIA8Yo3h1uMhRi5x1wcOoHCNkv8G5SHQgLnGFhFx158jcVwo6lyYd8Xr6L-KMmXZYCbJHbMROwegMr2L7DOV7pi6uniyE3TOdwEgTbhsS2vtEPLfFMmC7Rg5IaJcDuK3ZDDECLcuPPow8kCfEhYYPZxsVe1YL3CT8WBT4Ptl-s9v1Rox_5mI3AdMgOLaKaqsjf4lA1CiOyNHCB1ry8-7ybt69fMSQJV4IKe3mpR78qRjCOq6d_KtQt6POj05weSlgiHKq_5Yp77iYQSPYsgGXlyR4c9OqEUIPvSwBxVA#D7F51ACC")
                .addHeader("bnc-cpk", "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEShviUa2a0hf6n4sjFo6BksqBhaKgzI5xn4Ck7Jn4WKLcmT7jJU+QFjyOIFDcql+A2digWX1CeD5mSByVTzqlkg==")
                .addHeader("user-agent", "okhttp/4.12.0")
                .addHeader("if-none-match", "W/\"0e7f09950351110e39093365fa8cc6a5c\"")
                .build();
        Response response = client.newCall(request).execute();

            /*if(PrivateConfig.ceShi.equals("1")){
                System.out.println("获取成功了");
            }*/
        return response.body().string();
    }


}
