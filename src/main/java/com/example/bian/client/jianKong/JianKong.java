package com.example.bian.client.jianKong;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import com.example.bian.client.model.trade.AccountInformation;
import com.example.bian.client.model.trade.Position;
import okhttp3.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;



/**
 * 根据老师现有的持仓，自己分析他是加仓还是减仓
 */
public class JianKong {

//    static String id349 = "24";//老的是id 126000  0.0004=50
  static String id349 = "35";//手术刀 11400 0.005=57




    public static void main(String[] args) throws IOException, InterruptedException {

        BigDecimal yingGaiYou = new BigDecimal("36859.6500000");
        BigDecimal you = new BigDecimal("36959");
        BigDecimal ling001 = new BigDecimal("0.005");

        if (yingGaiYou.subtract(you).abs().divide(you.abs(),5, BigDecimal.ROUND_HALF_UP).compareTo(ling001) > 0) {
            System.out.println("有差距了");
        }

        System.out.println("开始啦");
        JianKong genDan = new JianKong();
        genDan.method();

    }

    public void method() throws InterruptedException {
        try {
            List<JSONObject> listPersonInfo = PrivateConfig.getList();
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Response response = login(client);

            Request lsRequest = getLsRequest(response);
            System.setProperty("https.proxySet", "true");
            System.setProperty("https.proxyHost", "127.0.0.1");
            System.setProperty("https.proxyPort", "10819");

            BigDecimal ling001 = new BigDecimal("0.05");
            BigDecimal ling = new BigDecimal("0");
            Long count = 1L;
            int error = 1;//记录登录超时的次数，连续超过3次，报警
            while (true) {
                try {
                    System.out.println(getCurrentTime() + "正在监控");
                    Call callLs = client.newCall(lsRequest);
                    Response responseLs = callLs.execute();
                    error = 1;
                    JSONObject jsonLs = JSON.parseObject(responseLs.body().string());
                    JSONArray jsonArrayLs = ((JSONArray) jsonLs.get("positions"));
                    for (JSONObject personInfo : listPersonInfo) {

                        SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient));
                        AccountInformation accountInformation;
                        try {
                            accountInformation = syncRequestClient.getAccountInformation();
                        } catch (Exception e) {
                            e.printStackTrace();
                            error++;
                            if (error % 4 == 0) {
                                String msg = personInfo.getString(PrivateConfig.alias) + "的api访问失败";
                                System.out.println(msg);
                                System.out.println(e);
                                T5.sendMe(msg + e);
                                Thread.sleep(100);
                                error = 1;
                            }
                            continue;
                        }
                        List<Position> positionList = accountInformation.getPositions();

                        if (jsonArrayLs.size() == 0) {
                            Boolean hasProblem = false;
                            //老师没有持仓，我们有持仓时报错
                            for (Position position : positionList) {
                                if (position.getPositionAmt().abs().compareTo(ling) > 0) {
                                    String msg = personInfo.getString(PrivateConfig.alias) + "，老师没有" + position.getSymbol() + "，报警，手动平仓";
                                    System.out.println(msg);
                                    T5.sendMe(msg);
                                    Thread.sleep(100);
                                    hasProblem = true;
                                }
                            }
                            if (!hasProblem) {
                                System.out.println(personInfo.getString(PrivateConfig.alias) + "和老师都没有持仓");
//                                T5.sendMe("[呲牙][坏笑]" + "");
                            }
                        } else {
                            BigDecimal beiShu = new BigDecimal(personInfo.getString(PrivateConfig.beiShu));
                            List<String> symbolListLs = new ArrayList<>();
                            for (Object o : jsonArrayLs) {
                                JSONObject ls = (JSONObject) o;
                                String symbol = ls.getString("symbol");
                                symbolListLs.add(symbol);
                                //老师的数量乘以倍数就是我们应该有的数量
                                BigDecimal yingGaiYou = ls.getBigDecimal("positionAmt").multiply(beiShu).abs();
                                Boolean hasProblem = true;
                                for (Position position : positionList) {
                                    BigDecimal you = position.getPositionAmt().abs();
                                    if (symbol.equals(position.getSymbol()) && you.abs().compareTo(ling) > 0) {
                                        if (yingGaiYou.subtract(you).abs().divide(you.abs(), 5, BigDecimal.ROUND_HALF_UP).compareTo(ling001) > 0) {
                                            String msg = personInfo.getString(PrivateConfig.alias) + "，" + position.getSymbol() + "和老师个数不同" + "，应该有" + yingGaiYou + "个，现在有" + you + "个";
                                            System.out.println(msg);
                                            T5.sendMe(msg);
                                            Thread.sleep(100);
                                            hasProblem = false;
                                            break;
                                        } else {
                                            //和老师相同就退出
                                            hasProblem = false;
                                            System.out.println(personInfo.getString(PrivateConfig.alias) + "的" + symbol + "和老师相同，没有问题");
                                            break;
                                        }
                                    }
                                }
                                if (hasProblem) {
                                    String msg = personInfo.getString(PrivateConfig.alias) + "应该有" + yingGaiYou + "个" + symbol + "，现在没有。";
                                    System.out.println(msg);
                                    T5.sendMe(msg);
                                    Thread.sleep(100);
                                }
                            }
                            // 如果和老师都有，并且symbol不同时
                            for (Position position : positionList) {
                                BigDecimal you = position.getPositionAmt().abs();
                                if (you.abs().compareTo(ling) > 0) {
                                    if(!symbolListLs.contains(position.getSymbol())){
                                        String msg = personInfo.getString(PrivateConfig.alias) + "，老师没有" + position.getSymbol() + "，报警，手动平仓";
                                        System.out.println(msg);
                                        T5.sendMe(msg);
                                        Thread.sleep(100);
                                    }
                                }
                            }

                        }
                    }
                    error = 1;
                } catch (Exception e) {
                    error++;
                    if (error%4 == 0) {
                        T5.sendMe("监控程序出错了");
                        Thread.sleep(100);
                        error=1;
                    }
                    if (error%3 == 0) {
                        response = login(client);
                        lsRequest = getLsRequest(response);
                    }

                    System.out.println("获取老师信息错误错误");
                    e.printStackTrace();
                }
                Thread.sleep(1000 * 20);
                count++;
                if (count > 3 * 60 * 3) {
                    response = login(client);
                    lsRequest = getLsRequest(response);
                    count = 1L;
                }
            }
        }catch (Exception e){
            System.out.println("监控程序启动出错了");
            T5.sendMe("监控程序启动出错了");
            e.printStackTrace();
        }
    }

    public String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis())); // 时间戳转换日期
    }

    public Response login(OkHttpClient client) throws IOException {
        String urlLogin = "http://349assistant.com/doLogin";
        FormBody formBody = new FormBody.Builder()
                .add("userName", "jiaoyan01")
                .add("password", "654123")
                .build();

        Request request = new Request.Builder()
                .url(urlLogin)
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        Response response = call.execute();
        JSONObject json = JSON.parseObject(response.body().string());
        System.out.println(json);
        return response;
    }

    public static Request getLsRequest(Response response) {
        //获取老师的现状，可以根据保证金和数量判断咱们的跟单软件是否错误
        String getLsUrl = "http://349assistant.com/getLatestOrder";
        FormBody formBodyLs = new FormBody.Builder()
                .add("id", id349)
                .add("tradeTime", "12938129")
                .build();

        Request requestLs = new Request.Builder()
                .addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9,zh-TW;q=0.8")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Content-Length", "24")
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Host", "349assistant.com")
                .addHeader("Origin", "http://349assistant.com")
                .addHeader("Pragma", "no-cache")
                .addHeader("Proxy-Connection", "keep-alive")
                .addHeader("Referer", "http://349assistant.com/track")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4503.5 Safari/537.36")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Cookie", response.header("Set-Cookie"))
//                .headers(response.headers())
                .url(getLsUrl)
                .post(formBodyLs)
                .build();
        return requestLs;
    }


}

