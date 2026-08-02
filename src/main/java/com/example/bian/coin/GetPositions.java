package com.example.bian.coin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import com.example.bian.genDan.PostGet;
import okhttp3.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.example.bian.client.bushu.PrivateConfig.ling;

public class GetPositions {
    public static JSONArray getOrders(ThreadPoolExecutor threadPoolExecutor) throws InterruptedException {

        /*if(PrivateConfig.ceShi.equals("1")){
            JSONArray jsonArray = PrivateConfig.readData();
            if(CollectionUtils.isNotEmpty(jsonArray)){
                return jsonArray;
            }
        }*/
        for (int i = 0; i < 3; i++) {
            String s = getOrder(threadPoolExecutor);
            if (StringUtils.isNotBlank(s)) {
//                System.out.println(s);
                JSONObject jsonObject = JSON.parseObject(s);
                if ("0".equals(jsonObject.getString("code"))) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    return jsonArray;
                } else {
                    PrivateConfig.printLog(jsonObject.getString("message"));
                    T5.sendMe("抓紧联系我，" + jsonObject.getString("message"));
                    Thread.sleep(3000);
                }
            }
        }
        PrivateConfig.printLog("币安跟单-获取订单有问题了1");
        T5.sendMe("币coin，连续5次，有问题！2");
        Thread.sleep(1000 * 60);
        return null;
    }

    public static String getOrder(ThreadPoolExecutor threadPoolExecutor) {

        //订单的顺序：第一个就是最近的一个
        Callable callable = new Callable() {
            @Override
            public String call() throws Exception {
                return getPosition();
            }
        };

        int h = 0;
        while (true) {
            try {
                Future future = threadPoolExecutor.submit(callable);
                try {
                    String s = (String) (future.get(10, TimeUnit.SECONDS));
                    return s;
                }  catch (Exception e) {
                    PrivateConfig.printLog("超时了1："+PrivateConfig.getCurrentTime());
//                    e.printStackTrace();
                    Thread.sleep(3000);//前面有超时，歇2秒再跟
                } catch (Throwable t) {
                    PrivateConfig.printLog("超时了2："+PrivateConfig.getCurrentTime());
                    t.printStackTrace();
                    Thread.sleep(3000);//前面有超时，歇2秒再跟
                } finally {
                    future.cancel(true);
                    h++;
                    if (h > 5) {
                        h = 0;
                        PrivateConfig.printLog("币安跟单-获取订单超时了");
                        T5.searchAll("订单失败，连续5次，有问题！4");
                    }
                }
            } catch (Exception e1) {
                PrivateConfig.printLog("超时了3："+PrivateConfig.getCurrentTime());
                e1.printStackTrace();
            }

        }
    }

    public static String getPosition() throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://i.bicoin.com.cn/msgHis/listHisMsg?typeStr=15&reqPage=1&pageSize=3")
                .method("GET", null)
                .addHeader("Host", "i.bicoin.com.cn")
                .addHeader("accept", "application/json,application/xml,application/xhtml+xml,text/html;q=0.9,image/webp,*/*;q=0.8")
                .addHeader("accept-language", "zh-CN,zh")
                .addHeader("appversion", "4.0.4")
                .addHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("from", "Android")
                .addHeader("fromandroid", "bicoin")
                .addHeader("mobilid", "dervice_id")
                .addHeader("mobilkey", "C9FF5A57CF0DCC68901C9BF69246E87E")
                .addHeader("redrisegreendown", "2")
                .addHeader("token", PrivateConfig.biCoin_token)
                .addHeader("user-agent", "Mozilla/5.0 (Linux; U; Android 9; zh-cn; V1916A Build/PQ3B.190801.07101020) AppleWebKit/533.1 (KHTML, like Gecko) Version/5.0 Mobile Safari/533.1")
                .addHeader("usertempid", "")
                .build();
        Response response = client.newCall(request).execute();

        return response.body().string();

    }



}
