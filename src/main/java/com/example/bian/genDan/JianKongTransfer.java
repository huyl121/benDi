package com.example.bian.genDan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class JianKongTransfer {

    public static void main(String[] args) throws InterruptedException, IOException {

        // 对https也开启代理
        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10819");

        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(1,
                        1,
                        10,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.DiscardPolicy());

        args = new String[2];
        System.out.println("开始啦");
        args[0] = "E://code//biance";
        args[1] = "0-genDan";
        PrivateConfig.before(args[0], args[1]);
        PrivateConfig.getJGXsw();
        PrivateConfig.xsw(true);
        JianKongTransfer jianKongTransfer = new JianKongTransfer();
//        jianKongTransfer.method(threadPoolExecutor);
        while (true){
            jianKongTransfer.methods(threadPoolExecutor);
            jianKongTransfer.getGenDanDetail();
        }



    }

    /**
     * 监控老师转入转出
     * @param threadPoolExecutor
     */
    public static void method(ThreadPoolExecutor threadPoolExecutor) {

        JSONArray jsonArray = getTransfers(threadPoolExecutor);
        for (Object o : jsonArray) {
            JSONObject transfer = (JSONObject) o;
            if(!PrivateConfig.genDan_transferTime.equals(transfer.getLong("time"))){
                Long amount = transfer.getLong("amount");
                if(transfer.getString("from").contains("Lead")){
                    T5.searchAll("老师转出" + amount + "，联系胡亚龙" + PrivateConfig.getCurrentTime());
                }else {
                    T5.searchAll("老师转入" + amount + "，联系胡亚龙" + PrivateConfig.getCurrentTime());
                }
                return;
            }
        }

        System.out.println("监控转移");
    }

    public static JSONArray getTransfers(ThreadPoolExecutor threadPoolExecutor)  {
        for(int i=0; i<3; i++){
            String s = getTransfer(threadPoolExecutor);
            if (StringUtils.isNotBlank(s)) {
                JSONObject jsonObject = JSON.parseObject(s);
                if ("000000".equals(jsonObject.getString("code"))) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray jsonArray = data.getJSONArray("list");
                    if (CollectionUtils.isNotEmpty(jsonArray)) {
                        return jsonArray;
                    }
                }
            }
        }
        System.out.println("币安跟单-获取订单有问题了");
        T5.searchAll("币安跟单-获取订单有问题了，连续5次收到，联系胡亚龙！");
        return new JSONArray();
    }

    public static String getTransfer(ThreadPoolExecutor threadPoolExecutor) {

        //订单的顺序：第一个就是最近的一个
        Callable callable = new Callable() {
            @Override
            public String call() throws Exception {
                return Postman.sendPost("https://" + PrivateConfig.genDan_url + "/bapi/futures/v1/friendly/future/copy-trade/lead-portfolio/transfer-history",
                        "{\"pageNumber\":" + 1 + ",\"pageSize\":" + 1 + ",\"portfolioId\":\"" + PrivateConfig.genDan_portfolioId + "\"}", PrivateConfig.genDan_cookie, PrivateConfig.genDan_token);
            }
        };

        int h = 0;
        int h10 = 0;
        while (true) {
            try {
                Future future = threadPoolExecutor.submit(callable);
                try {
                    String s = (String) (future.get(3, TimeUnit.SECONDS));
                    return s;
                } catch (TimeoutException e) {
                    Thread.sleep(3000);//前面有超时，歇2秒再跟
                } catch (Exception e) {
                    Thread.sleep(3000);//前面有超时，歇2秒再跟
                } catch (Throwable t) {
                    Thread.sleep(3000);//前面有超时，歇2秒再跟
                } finally {
                    future.cancel(true);
                    h++;
                    if (h > 5) {
                        h = 0;
                        System.out.println("币安跟单-获取订单超时了1");
                        T5.searchAll("币安跟单-获取订单超时，有问题了，连续5次收到，联系胡亚龙！");
                        h10++;
                    }
                    if(h10>10){
                        return "错误太多了，立马报警1";
                    }
                }
            } catch (Exception e1) {
            }

        }
    }

    /**
     * 监控老师转入转出
     * @param threadPoolExecutor
     */
    public static void methods(ThreadPoolExecutor threadPoolExecutor) throws InterruptedException, IOException {

        Map<String, BigDecimal> detailMap = getGenDanDetail();

        for(Object o1 : PrivateConfig.niuRens){
            JSONObject niuRen = (JSONObject) o1;
            String portfolioId = niuRen.getString("portfolioId");
            String genPortfolioId = niuRen.getString("genPortfolioId");
            String name = niuRen.getString("name");
            String time = niuRen.getString("time");
            String money = niuRen.getString("money");
            String jianCang = niuRen.getString("jianCang");//盈利了就减仓，不跟随带单员
            int inCount = niuRen.getInteger("inCount");//转入次数，自动的话，最多加1次

            if("1".equals(jianCang)){
                if(detailMap.get(genPortfolioId) != null){
                    Long cha = detailMap.get(genPortfolioId).subtract(new BigDecimal(money)).setScale(0, BigDecimal.ROUND_DOWN).longValue();
                    boolean jianCangOk = jiaJianCang(genPortfolioId, String.valueOf(cha*0.89), "WITHDRAW");
                    if (jianCangOk || cha <= 0) {
                        niuRen.put("jianCang", "0");
                    }
                    System.out.println(name + "自动减仓：" + cha.toString());
                    /*if(detailMap.get(genPortfolioId).compareTo(new BigDecimal(money).multiply(new BigDecimal("1.1")) )>  0){

                    }*/
                }
            }else {
                JSONArray jsonArray = getTransferss(threadPoolExecutor, portfolioId);
                for (Object o : jsonArray) {
                    JSONObject transfer = (JSONObject) o;
                    if(!time.equals(transfer.getString("time"))){
                        Long amount = transfer.getLong("amount");
                        if(transfer.getString("from").contains("Lead")){
//                            T5.searchAll(name + "老师转出" + amount + "，" + PrivateConfig.getCurrentTime());
                            //重新设置转出时间
                            niuRen.put("time", transfer.getString("time"));

                            if(detailMap.get(genPortfolioId) != null){
                                if(detailMap.get(genPortfolioId).compareTo(new BigDecimal(money) )>  0){
                                    Long cha = detailMap.get(genPortfolioId).subtract(new BigDecimal(money)).setScale(0, BigDecimal.ROUND_DOWN).longValue();
                                    boolean jianCangOk = jiaJianCang(genPortfolioId, String.valueOf(cha*8.8/10), "WITHDRAW");
                                    System.out.println(name + "，自动减仓：" + cha.toString());
                                    if(!jianCangOk){
                                        niuRen.put("jianCang", "1");
                                    }
                                }
                            }

                        }else {
//                        T5.sendMe(name + "老师转入" + amount + "，" + PrivateConfig.getCurrentTime());
                            //重新设置转入时间
                            niuRen.put("time", transfer.getString("time"));

                            if(detailMap.get(genPortfolioId) != null){
                                if(detailMap.get(genPortfolioId).compareTo(new BigDecimal(money) ) <  0){
                                    Long cha = new BigDecimal(money).subtract(detailMap.get(genPortfolioId)).setScale(0, BigDecimal.ROUND_DOWN).longValue();
                                    Long ban = Long.parseLong(money)/2;
                                    if(cha > ban){
                                        cha = ban;
                                    }
                                    if(inCount==0) {
                                        jiaJianCang(genPortfolioId, cha.toString(), "DEPOSIT");
                                        System.out.println(name + "自动加仓：" + cha.toString());
                                        niuRen.put("inCount", 1);
                                    }
                                }
                            }
                        }
                    }
                }

            }


            Thread.sleep(2000);
        }


    }

    public static boolean jiaJianCang(String portfolioId, String amount, String direction) throws IOException {

        MediaType mediaType = MediaType.parse("application/json");
        JSONObject param = new JSONObject();
        param.put("portfolioId", portfolioId);
        param.put("asset", "USDT");
        param.put("amount", amount);
        param.put("direction", direction);
        RequestBody body = RequestBody.create(mediaType, param.toJSONString());
        String s = PostGet.postPhone(body, "https://www.binance.com/bapi/futures/v1/private/future/copy-trade/copy-portfolio/transfer");
        System.out.println(s);
        if (StringUtils.isNotBlank(s)) {
            JSONObject jsonObject = JSON.parseObject(s);
            if ("000000".equals(jsonObject.getString("code"))) {
                return true;
            }
        }
        return false;

    }

    public static Map<String , BigDecimal> getGenDanDetail () throws IOException {

        String s = PostGet.getPhone("https://www.binance.com/bapi/futures/v1/private/future/copy-trade/copy-portfolio/detail-list?ongoing=true");
        Map<String, BigDecimal> map = new HashMap<>();
        if (StringUtils.isNotBlank(s)) {
//                System.out.println(s);
            JSONObject jsonObject = JSON.parseObject(s);
            if ("000000".equals(jsonObject.getString("code"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (Object o : jsonArray) {
                    JSONObject detail = (JSONObject) o;
                    String copyPortfolioId = detail.getString("copyPortfolioId");
                    BigDecimal marginBalance = detail.getBigDecimal("marginBalance");
                    map.put(copyPortfolioId, marginBalance);
                }
            }
        }
        return map;
    }

    public static JSONArray getTransferss(ThreadPoolExecutor threadPoolExecutor, String portfolioId)  {
        for(int i=0; i<3; i++){
            String s = getTransfers(threadPoolExecutor, portfolioId);
            if (StringUtils.isNotBlank(s)) {
                JSONObject jsonObject = JSON.parseObject(s);
                if ("000000".equals(jsonObject.getString("code"))) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray jsonArray = data.getJSONArray("list");
                    if (CollectionUtils.isNotEmpty(jsonArray)) {
                        return jsonArray;
                    }
                }
            }
        }
        System.out.println("币安跟单-获取订单有问题了");
        T5.searchAll("币安跟单-获取订单有问题了，连续5次收到，联系胡亚龙！");
        return new JSONArray();
    }

    public static String getTransfers(ThreadPoolExecutor threadPoolExecutor, String portfolioId) {

        //订单的顺序：第一个就是最近的一个
        Callable callable = new Callable() {
            @Override
            public String call() throws Exception {
                return Postman.sendPost("https://" + PrivateConfig.genDan_url + "/bapi/futures/v1/friendly/future/copy-trade/lead-portfolio/transfer-history",
                        "{\"pageNumber\":" + 1 + ",\"pageSize\":" + 1 + ",\"portfolioId\":\"" + portfolioId + "\"}", PrivateConfig.genDan_cookie, PrivateConfig.genDan_token);
            }
        };

        int h = 0;
        int h10 = 0;
        while (true) {
            try {
                Future future = threadPoolExecutor.submit(callable);
                try {
                    String s = (String) (future.get(3, TimeUnit.SECONDS));
                    return s;
                } catch (TimeoutException e) {
                    Thread.sleep(3000);//前面有超时，歇2秒再跟
                } catch (Exception e) {
                    Thread.sleep(3000);//前面有超时，歇2秒再跟
                } catch (Throwable t) {
                    Thread.sleep(3000);//前面有超时，歇2秒再跟
                } finally {
                    future.cancel(true);
                    h++;
                    if (h > 5) {
                        h = 0;
                        System.out.println("币安跟单-获取订单超时了1");
                        T5.searchAll("币安跟单-获取订单超时，有问题了，连续5次收到，联系胡亚龙！");
                        h10++;
                    }
                    if(h10>10){
                        return "错误太多了，立马报警1";
                    }
                }
            } catch (Exception e1) {
            }

        }
    }

}
