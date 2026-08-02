package com.example.bian.ok;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import com.example.bian.genDan.Postman;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.concurrent.*;

public class GenDanZhengQian {

    /*
    * https://www.binancezh.info/bapi/futures/v1/private/future/copy-trade/copy-portfolio/create
    *定额
    * {
    "agreeShare": true,
    "leverageMode": "FOLLOW_LEAD",
    "marginMode": "FOLLOW_LEAD",
    "investAmount": 11,
    "costPerOrder": 10,
    "copyModel": "FIXED_AMT",
    "investAsset": "USDT",
    "leadPortfolioId": "3899711535133359873"
}
* 定比
{
    "agreeShare": true,
    "leverageMode": "FOLLOW_LEAD",
    "marginMode": "FOLLOW_LEAD",
    "investAmount": 10,
    "copyModel": "FIXED_RATIO",
    "costPerOrder": 0,
    "investAsset": "USDT",
    "leadPortfolioId": "3899711535133359873"
}
*
* {
    "code": "000000",
    "message": null,
    "messageDetail": null,
    "data": {
        "copyPortfolioId": "3901214144038380545",
        "status": "ACTIVE"
    },
    "success": true
}
    *
    * */

    public static void main(String[] args) throws InterruptedException, IOException {

        // 对https也开启代理
        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10819");

        args = new String[2];
        PrivateConfig.printLog("开始啦");
        args[0] = "E://code//biance";
        args[1] = "0-genDan";
        PrivateConfig.before(args[0], args[1]);

//        PrivateConfig.getJGXsw();
//        PrivateConfig.xsw(true);


        GenDanZhengQian genDan = new GenDanZhengQian();
        genDan.method("a100111");

    }


    public static void method(String time) throws InterruptedException, IOException {

        if(time.contains("a")){
            int tian = Integer.parseInt(time.replace("a", ""));
            if(System.currentTimeMillis() > (1733903329000L + 86400000L * tian)){
                System.out.println("license过期了");
                return;
            }
        }else {
            if(System.currentTimeMillis() > (Long.parseLong("1733907902000") + 86400000L)){
                System.out.println("license过期了");
                return;
            }
        }
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(1, 1, 1,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.DiscardPolicy());
        boolean chengGong = false;
        int i = 60;

        while (true){
            try{
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\"traderUniqueName\":\""+PrivateConfig.zhengQian_portfolioId+"\",\"copyMode\":\"SMART_COPY\",\"initialAmount\":\""+PrivateConfig.zhengQian_investAmount+"\",\"replicationRequired\":\"0\"}");
                Request request = new Request.Builder()
                        .url("https://www.okx.com/priapi/v5/ecotrade/copier/first-settings?t=1763988442810")
                        .method("POST", body)
                        .addHeader("accept", "application/json")
                        .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8")
                        .addHeader("app-type", "web")
                        .addHeader("authorization", PrivateConfig.ok_authorization)
                        .addHeader("content-type", "application/json")
                        .addHeader("cookie", "devId=0eb6d2c5-d324-4fb4-b334-d1e6d00e178c; locale=zh_CN; ok-exp-time=1763987559340; ok_prefer_currency=0%7C1%7Cfalse%7CUSD%7C2%7C%24%7C1%7C1%7C%E7%BE%8E%E5%85%83; ok_prefer_udColor=0; okg.currentMedia=xl; ok_global={%22okg_m%22:%22xl%22}; fingerprint_id=0eb6d2c5-d324-4fb4-b334-d1e6d00e178c; fp_s=0; intercom-id-ny9cf50h=d6eb02b5-b2f1-4d2f-be28-02021591a59b; intercom-device-id-ny9cf50h=a706b86e-5be0-46f7-ace0-d0931ce93627; first_ref=https%3A%2F%2Fwww.okx.com%2Fzh-hans; _c_WBKFRo=cOyKGvdFnP7iPKCtvZ8pyzYQOsmlGDNB39qtouOo; _nb_ioWEgULi=; ftID=undefined; x-lid=undefined; g_state={\"i_l\":0,\"i_ll\":1763987909129,\"i_b\":\"qa6rpkPn+ECySgbVYMN/ckR70HB4OM72W5JkVq1AXEA\"}; tmx_session_id=109f49n32h6_1763987909530; finger_test_cookie=1763987915001; token=eyJraWQiOiIxMzYzODYiLCJhbGciOiJFUzI1NiJ9.eyJqdGkiOiJleDExMDE3NjM5ODgxNjEyMDM2NzMxRjM3Rjc2Njg4NjgxMVlta1MiLCJ1aWQiOiJlTTVmZVdCWnZ0d0xKK2xwZWRUNXV3PT0iLCJzdGEiOjAsIm1pZCI6ImVNNWZlV0JadnR3TEorbHBlZFQ1dXc9PSIsInBpZCI6IlBUeUE4VzA5ekZVSkJHSjZZUk5HWXc9PSIsIm5kZSI6MCwiaWF0IjoxNzYzOTg4MTYxLCJleHAiOjE3NjUxOTc3NjEsImJpZCI6MCwiZG9tIjoid3d3Lm9reC5jb20iLCJlaWQiOjE0LCJpc3MiOiJva2NvaW4iLCJkaWQiOiJtbnNIMTB1TU5wVW92S25aYTArQXVBblNyR2tUT0RYd3RPZEd3KzhmY3Vha2VFQlNsSzZONlQ2eEdYQitKaEgxIiwiZmlkIjoibW5zSDEwdU1OcFVvdktuWmEwK0F1QW5TckdrVE9EWHd0T2RHdys4ZmN1YWtlRUJTbEs2TjZUNnhHWEIrSmhIMSIsImxpZCI6ImVNNWZlV0JadnR3TEorbHBlZFQ1dXc9PSIsInVmYiI6IlBUeUE4VzA5ekZVSkJHSjZZUk5HWXc9PSIsInVwYiI6ImlCcmEyVmhOb2t5UmloeGlKLzN6RXc9PSIsImt5YyI6Miwia3lpIjoic1ZrUEh4ak1Hb2Fhc2o2Z3RXMVB4N2RUcENaS2c1LzZLbjFteGFpclpDbE84cWtiMWJMdGFmMklSVUtrTDd4RTd5ZEYvWU5DR1FXLzV5aTRWQnpUM1E9PSIsImNwayI6ImhCdjNtSEZjb0lETG5TckZ6dEdTTlpMT29aU2s1bUE4SHBQcE9MOFE1TlZSc2R0a2J5NmJwUk8rL3BWZ0J2UDVEbFNUMFUyTmRLMVl2ZHlveVJjV21ncWVYYXpVMkhmMENGYXZUcE9KN2pvRGhZb0h5TjlvTEVDS1FUS2FSd1FHMVJZL2VMNU9PbU03L0phbUNvT0daTnd6SDR0RlNwMWhxcFoyaHBYTDlWdz0iLCJ2ZXIiOjEsImNsdCI6MiwidXVkIjoiWE1MMEtKZC9pT1VGZENRQld0cWRXVUhNKytDNWNZVUQ2V21yNVI4R2cxdz0ifQ.fFTyd42qPmKGik-p7uNct5rDUnwSJjgUX3H1qTIG6r0a4lwIUTm6ZyFril8yRXsFHPjg_OyCfWDrF2f4667iFw; isLogin=1; ok_site_info==0HNxojI5RXa05WZiwiIMFkQPx0Rfh1SPJiOiUGZvNmIsICUKJiOi42bpdWZyJye; _tk=gqqKYvOtvhxJ78CE3gAB8A==; ok_login_type=OKX_GLOBAL; ok_prefer_udTimeZone=1; preferLocale=zh_CN; __cf_bm=Huj5KLOH0TH0bii2CUFusjqZDsk156c50g40o.qZk5o-1763988193-1.0.1.1-4gQbfA_SiRH4UF2LG0PfPbUAIsfb1VIjyBGHDOhpx4eALKhZtDEfiEVE1sZMXmTvZvlPhlY9FwEzRbI_e.yRiHcpfAleu_LqjZkHWrz767In5nPtyidiEhvZz_ZzRIhs; im-token=hello; intercom-session-ny9cf50h=K2J6RkpWNnpKaFV0VGNWMUs3dGZ0am80YlNLemV5U2xUajhHU0xsUHZKUTh5cDQrNnExYzRScGlOUkI1RUhiVUhxcDNmZFQ0d1lYTVpDbnJLUFhVbkFyTlgyaVd3dG5ZUlhtb2kzVitNNEk9LS0wck1wb1FDbXp0djJhVnpIRlRlY25nPT0=--5aa8e76e283be5695eb9a9a0e8f16696fcd821e9; traceId=2130939884177240004; _monitor_extras={\"deviceId\":\"xw693k3SolFu1Yb9CofXDc\",\"eventId\":177,\"sequenceNumber\":177}; ok-ses-id=XnrhAUbEUGfQSEwI1w3CXl5+jrN/cm8AmTs7YGWHzylPc88xv2Qn+uEDs0UNanQq0EQqAzM2ESBrWWeTsx214KzHAtd9p72Omc5kvOVoXSzfTuLLhcvqZ6v8LM9njmr3; __cf_bm=ApUq1yfoQUQrc2RnVEXV.jz8j2p8u3osWtDakf_4QB8-1763989183-1.0.1.1-fxB5mOAQ27rSaxiJThcbkhkAhxu7jFmFhrl24iU6Tkj6Yyhk8PE0sbu7lfTKCSWPeGBa_Ec6tjyVSUzUDcraZRSy0RcLZdWEU_qmK5ioVtKLhQuH3xH3isvLRc9OEqLq")
                        .addHeader("devid", "0eb6d2c5-d324-4fb4-b334-d1e6d00e178c")
                        .addHeader("origin", "https://www.okx.com")
                        .addHeader("priority", "u=1, i")
                        .addHeader("referer", "https://www.okx.com/zh-hans/copy-trading/account/E0A10911B2872132?tab=swap")
                        .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"131\", \"Chromium\";v=\"131\", \"Not_A Brand\";v=\"24\"")
                        .addHeader("sec-ch-ua-mobile", "?0")
                        .addHeader("sec-ch-ua-platform", "\"Windows\"")
                        .addHeader("sec-fetch-dest", "empty")
                        .addHeader("sec-fetch-mode", "cors")
                        .addHeader("sec-fetch-site", "same-origin")
                        .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36")
                        .addHeader("x-cdn", "https://www.okx.com")
                        .addHeader("x-client-signature", "{P1363}vpXgGjYRIUgF963XFWr9FPWsCqZNqUdop36vvBdMTHaom/0ZlYko/81/SZsWHTRHpQ1LK9q5JMNpTZ0k4YO7bA==")
                        .addHeader("x-client-signature-version", "1.3")
                        .addHeader("x-id-group", "2130939884177240004-c-20")
                        .addHeader("x-locale", "zh_CN")
                        .addHeader("x-request-timestamp", "1763988442810")
                        .addHeader("x-simulated-trading", "undefined")
                        .addHeader("x-site-info", "=0HNxojI5RXa05WZiwiIMFkQPx0Rfh1SPJiOiUGZvNmIsICUKJiOi42bpdWZyJye")
                        .addHeader("x-utc", "8")
                        .addHeader("x-zkdex-env", "0")
                        .build();
                Response response = client.newCall(request).execute();
                String s = response.body().string();
                i++;
                if (StringUtils.isNotBlank(s)) {
                    JSONObject jsonObject = JSON.parseObject(s);
                    if ("0".equals(jsonObject.getString("code"))) {
                        chengGong = true;
                        break;
                    }else {
                        if(i > 60){
                            i=0;
                            System.out.println(jsonObject.getString("msg"));
                        }
                    }
                    Thread.sleep(Long.parseLong(PrivateConfig.shiJian));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        System.out.println(PrivateConfig.zhengQian_shui + "：跟单成功了");
        T5.sendMe(PrivateConfig.zhengQian_shui + "：跟单成功了");
        Thread.sleep(1000 * 60);
        System.out.println(PrivateConfig.zhengQian_shui + "：跟单成功了");
        T5.sendMe(PrivateConfig.zhengQian_shui + "：跟单成功了");
        Thread.sleep(1000 * 60);
        System.out.println(PrivateConfig.zhengQian_shui + "：跟单成功了");
        T5.sendMe(PrivateConfig.zhengQian_shui + "：跟单成功了");

    }

    public static String getOrder(ThreadPoolExecutor threadPoolExecutor) throws InterruptedException {

        String s1 = "{\"leverageMode\": \"FOLLOW_LEAD\",\"marginMode\": \"FOLLOW_LEAD\",\"investAmount\": "
                + PrivateConfig.zhengQian_investAmount + ",\"costPerOrder\":"
                + PrivateConfig.zhengQian_costPerOrder + ",\"copyModel\":\""
                + PrivateConfig.zhengQian_copyModel + "\",\"investAsset\": \"USDT\",\"leadPortfolioId\": \""
                + PrivateConfig.zhengQian_portfolioId + "\"}";
        //订单的顺序：第一个就是最近的一个
        Callable callable = new Callable() {
            @Override
            public String call() throws Exception {
                return Postman.sendPost("https://" + PrivateConfig.genDan_url + "/bapi/futures/v1/private/future/copy-trade/copy-portfolio/create",
                        s1, PrivateConfig.genDan_cookie, PrivateConfig.genDan_token);
            }
        };

        int h = 0;
        while (true) {
            try {
                Future future = threadPoolExecutor.submit(callable);
                try {
                    String s = (String) (future.get(3, TimeUnit.SECONDS));
                    return s;
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    Thread.sleep(3000);//前面有超时，歇2秒再跟
                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.sleep(3000);//前面有超时，歇2秒再跟
                } catch (Throwable t) {
                    t.printStackTrace();
                    Thread.sleep(3000);//前面有超时，歇2秒再跟
                } finally {
                    future.cancel(true);
                    h++;
                    if (h > 5) {
                        h = 0;
                        PrivateConfig.printLog("添加跟单失败");
                        T5.searchAll("添加跟单失败，有问题！");
                    }
                }
            } catch (Exception e1) {
            }
            Thread.sleep(2000);
        }
    }




}
