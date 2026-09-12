package com;

import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.example.bian.client.bushu.PrivateConfig.shiJian;

public class test {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("开代理");
        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10809");

        args = new String[2];
        args[0] = "E://code//benDi";
        args[1] = "0-genDan";
        PrivateConfig.before(args[0], "0-" + args[1]);
        PrivateConfig.xsw(true);

        while (true){
            Thread.sleep(Long.parseLong(shiJian));
            String msg = getOrder(null);
            if (msg != null && msg.contains("请求过于频繁")) {
                shiJian = String.valueOf(Integer.parseInt(shiJian) + 500);
                PrivateConfig.printLog("请求时间增加到：" + shiJian);
                Thread.sleep(1000 * 60);
            }
            System.out.println(msg);
        }



    }

    private static String getOrder(String genPortfolioId) {

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
                Future future = PrivateConfig.threadPoolExecutor.submit(callable);
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

    public static String getPosition(String genPortfolioId) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"portfolioId\":\"5075296293924315648\",\"pageSize\":1,\"pageNumber\":1,\"status\":\"FAIL\",\"startTime\":1781366400000,\"endTime\":1789228799999}");
        Request request = new Request.Builder()
                .url("https://www.binance.com/bapi/futures/v1/private/future/copy-trade/copy-portfolio/order-history")
                .method("POST", body)
                .addHeader("authority", "www.binance.com")
                .addHeader("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"")
                .addHeader("bnc-level", "0")
                .addHeader("csrftoken", "d245886c85409603622eb2e59598c81e")
                .addHeader("bnc-time-zone", "Asia/Shanghai")
                .addHeader("lang", "zh-CN")
                .addHeader("fvideo-id", "33da7e181c2ca9589f6ba07b2213be4b03fec0b1")
                .addHeader("device-info", "eyJzY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA4MCIsImF2YWlsYWJsZV9zY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA0MCIsInN5c3RlbV92ZXJzaW9uIjoiV2luZG93cyA4LjEiLCJicmFuZF9tb2RlbCI6InVua25vd24iLCJzeXN0ZW1fbGFuZyI6InpoLUNOIiwidGltZXpvbmUiOiJHTVQrMDg6MDAiLCJ0aW1lem9uZU9mZnNldCI6LTQ4MCwidXNlcl9hZ2VudCI6Ik1vemlsbGEvNS4wIChXaW5kb3dzIE5UIDYuMzsgV2luNjQ7IHg2NCkgQXBwbGVXZWJLaXQvNTM3LjM2IChLSFRNTCwgbGlrZSBHZWNrbykgQ2hyb21lLzk3LjAuNDY5Mi43MSBTYWZhcmkvNTM3LjM2IiwibGlzdF9wbHVnaW4iOiJQREYgVmlld2VyLENocm9tZSBQREYgVmlld2VyLENocm9taXVtIFBERiBWaWV3ZXIsTWljcm9zb2Z0IEVkZ2UgUERGIFZpZXdlcixXZWJLaXQgYnVpbHQtaW4gUERGIiwiY2FudmFzX2NvZGUiOiJ1bmtub3duIiwid2ViZ2xfdmVuZG9yIjoiR29vZ2xlIEluYy4gKEdvb2dsZSkiLCJ3ZWJnbF9yZW5kZXJlciI6IkFOR0xFIChHb29nbGUsIFZ1bGthbiAxLjIuMCAoU3dpZnRTaGFkZXIgRGV2aWNlIChTdWJ6ZXJvKSAoMHgwMDAwQzBERSkpLCBTd2lmdFNoYWRlciBkcml2ZXItNS4wLjApIiwiYXVkaW8iOiIxMjQuMDQzNDc1Mjc1MTYwNzQiLCJwbGF0Zm9ybSI6IldpbjMyIiwid2ViX3RpbWV6b25lIjoiQXNpYS9TaGFuZ2hhaSIsImRldmljZV9uYW1lIjoiQ2hyb21lIFY5Ny4wLjQ2OTIuNzEgKFdpbmRvd3MpIiwiZmluZ2VycHJpbnQiOiIzMWFiNmVkNjdmNTFhM2EyMzM5MDEwNmZjZmRmYTkwNyIsImRldmljZV9pZCI6IiIsInJlbGF0ZWRfZGV2aWNlX2lkcyI6IiJ9")
                .addHeader("bnc-uuid", "06794427-5f45-457b-a40d-9bbe448af1db")
                .addHeader("fvideo-token", "iAPphb0FtP/vQzG/2qC4Ei5rew3CcPsLuAHQrY/4VL12K1zmQKG6ZBSQnyV9OibRTyZg/UENd2AnaOblWcXLEZbPrg7mKrclLIrdyagc44byuDcZxFqduxkM30hlhrJqlV6yNekr/kg8CeQsJJRWvhsd96l5u2/IV4/9bFwSF2zTItSitet++3AP0mYWayVBc=37")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("x-trace-id", "0ec0ec44-1a08-43af-a2ea-561df4b0227b")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("x-ui-request-trace", "0ec0ec44-1a08-43af-a2ea-561df4b0227b")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36")
                .addHeader("content-type", "application/json")
                .addHeader("bnc-location", "")
                .addHeader("x-passthrough-token", "eyJhbGciOiJSUzI1NiJ9.eyJpYXQiOjE3ODc4MzkzODIsImlzcyI6Imh0dHBzOi8vYmluYW5jZS5jb20iLCJzdWIiOiJ2YWxpZGF0aW9uX3Rva2VuIiwiZGV2aWNlSWQiOiIwOEIyNDZEOTU3NDM5MTBDOURFNjg2M0E1MkJBQkQ0RSIsImV4cCI6MTc4NzgzOTY4MiwibmJmIjoxNzg3ODM5MzgyfQ.GIVzgwthItmEksPwk0ObYOdAYccZYOrouISMTXXPXZWJg2CurzHyFNQA9uSwo9L34pQppFpRJcVtuTjlOZQBvf8-WP0CzN8JZT2xyWTHHx-tLqrt58UnZRnHb8zWzf-8OPj5BphZP6_U1ROT9_3-SlyU09XZGhaNIuL5D5t_0ME")
                .addHeader("baggage", "sentry-environment=prod,sentry-release=20260911-bebd9d89-8796,sentry-public_key=af78a307ddf943f1b2b9d61200bd034b,sentry-trace_id=970d10eaa0d6498b8a7e9ceb4d0c770e")
                .addHeader("sentry-trace", "970d10eaa0d6498b8a7e9ceb4d0c770e-ae30033ea25f4dd6")
                .addHeader("clienttype", "web")
                .addHeader("accept", "*/*")
                .addHeader("origin", "https://www.binance.com")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("referer", "https://www.binance.com/zh-CN/copy-trading/copy-management")
                .addHeader("accept-language", "zh-CN,zh;q=0.9")
                .addHeader("cookie", "bnc-uuid=06794427-5f45-457b-a40d-9bbe448af1db; BNC_FV_KEY=33da7e181c2ca9589f6ba07b2213be4b03fec0b1; OptanonAlertBoxClosed=2026-06-26T04:32:10.950Z; se_gd=FEaBgQVAXRIVlYXcGAAYgZZAxUA1aBZVVoO9bUUJlNQVwVVNWWUN1; se_gsd=USUiAStvNis3NyM7IAMyJCYEGRVXBwoIUlhLV1xRW1NWJFNT1; changeBasisTimeZone=; lang=zh-CN; _ga_3WP50LGEEC=deleted; userPreferredCurrency=USD_USD; currentAccount=; BNC-Location=; logined=y; _gcl_au=1.1.1877311437.1782448337.-.-.1785930280.1486059394.1788613116.1788616574; _gid=GA1.2.834818184.1788750737; g_state={\"i_l\":0,\"i_ll\":1788754335810,\"i_b\":\"TWlKXHnPAyvYV8xT1GuHoh077MGgk3yKK90E0RteoD0\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1788754335810}; r30t=1; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221207218528%22%2C%22first_id%22%3A%2219f0232ff4017-009227c0825a51e8-5c123e18-2073600-19f0232ff4125c%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmMDIzMmZmNDAxNy0wMDkyMjdjMDgyNWE1MWU4LTVjMTIzZTE4LTIwNzM2MDAtMTlmMDIzMmZmNDEyNWMiLCIkaWRlbnRpdHlfbG9naW5faWQiOiIxMjA3MjE4NTI4In0%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221207218528%22%7D%7D; aws-waf-token=0ec667a2-8c2d-4418-b64d-e10b5e38f54d:AQoAbvoXfnkBAAAA:y8DGq/MNN00XdG5/6D79O9Ohod9arNWxAy0Wx9xwl+1MX+PX7ZvGYkvRPU7mnZyqlg0UBAWp8OoshCmvo9BeH9lVGWHM+cwUk9GJUBvSkWqSiCoFCY2hZlfNVvWV/zKUNiX2aKYoudMD3PqydeuCfj15YPwngaIUKeLLJSbUaB7KI9Ng6//3n/haFoQiS9s6E7PWUTjLk4Q1pkf5uOAm/eEFwxB+DAtuij7riznbRY+xTbh22vdBojOZHjSCC8KoKSSNmhM=; futures-layout=pro; _ga_3WP50LGEEC=deleted; r20t=web.1207218528.C8C8871D8E609623CFE0B65D2D7C2ED4; cr00=A355E6F218E7AF38A1E69A6EDD279000; d1og=web.1207218528.3CAF6AE8E1BD06D07BA018A1301A36FA; r2o1=web.1207218528.55F6D20B3DEE5241E898921AF39541AE; f30l=web.1207218528.1D560BA2A11684DABCC972DFD9811672; p20t=web.1207218528.B52217AE06805E3E9127804D09A1FF70; theme=dark; OptanonConsent=isGpcEnabled=0&datestamp=Sat+Sep+12+2026+16%3A00%3A03+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&consentId=ad3f0059-b7f2-4fd5-944c-dbca20ecf064&interactionCount=1&isAnonUser=1&prevHadToken=0&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&fclco=&lastConsentTs=1782448330&intType=1&crTime=1782448332280&geolocation=JP%3B13&AwaitingReconsent=false; _gat_UA-162512367-1=1; _ga_3WP50LGEEC=GS2.1.s1789196528$o344$g1$t1789200013$j48$l0$h0; _ga=GA1.1.1490553504.1782448338; BNC_FV_KEY_T=101-wmQhkjbVEGP%2FJMjqXehZfIw3q%2BST8oxvolUs7cLrynCOmRmnPZ%2BPa4E9ykBo7rQx1DSzIaTSbiDKtIYO%2B2%2FWlA%3D%3D-mpNLePsJu9BbG%2FFeuqNr%2Bw%3D%3D-91; BNC_FV_KEY_EXPIRE=1789221614375; _uetsid=dce84df0aa7011f1bf44693b5558687f; _uetvid=04e41d70711811f187b8d9ff36446842")
                .build();
        return PrivateConfig.getResponse(request);
    }


}
