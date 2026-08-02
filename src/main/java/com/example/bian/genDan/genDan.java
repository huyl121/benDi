package com.example.bian.genDan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import com.example.bian.client.model.enums.OrderSide;
import com.example.bian.client.model.enums.PositionSide;
import com.example.bian.client.model.trade.Order;
import com.example.bian.coin.genBiCoin;
import com.example.bian.xin.JianKong4;
import com.example.bian.xin.QingCang3;
import okhttp3.*;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

import static com.example.bian.client.bushu.PrivateConfig.*;

public class genDan {

    /*
     * 单向持仓时的下单记录
     * 做多买入 "side":"BUY","realizedProfit":0.00000000（买入时没有盈亏）,"qty":2601.00000000,"positionSide":"BOTH",
     * {"time":1704557420000,"symbol":"GMTUSDT","side":"BUY","quantity":867.43350,"quantityAsset":"USDT","realizedProfit":0.00000000,"qty":2601.00000000,"positionSide":"BOTH","activeBuy":true}
     * 做多卖出 "side":"SELL","realizedProfit":282.93366852（卖出时有盈亏，挣了是正数，赔了是负数）,
     * {"time":1704608070000,"symbol":"GMTUSDT","side":"SELL","quantity":1606.24640,"quantityAsset":"USDT","realizedProfit":282.93366852,"qty":3968.00000000,"positionSide":"BOTH","activeBuy":true}
     * 做空买入 "side":"SELL","realizedProfit":0.00000000,
     * {"time":1704469693000,"symbol":"SEIUSDT","side":"SELL","quantity":219.2520000,"quantityAsset":"USDT","realizedProfit":0.00000000,"qty":302.00000000,"positionSide":"BOTH","activeBuy":false}
     * 做空卖出 "side":"BUY","realizedProfit":10.69158117,
     * {"time":1704475064000,"symbol":"SEIUSDT","side":"BUY","quantity":3526.5360000,"quantityAsset":"USDT","realizedProfit":10.69158117,"qty":4953.00000000,"positionSide":"BOTH","activeBuy":false}
     *
     *position
     * 做多正在持仓
     * {"symbol":"OPUSDT","positionAmount":"506.9","leverage":20,"positionSide":"BOTH"}
     * 做空正在持仓
     * {"symbol":"UMAUSDT","positionAmount":"-57","leverage":20,"positionSide":"BOTH"}
     *
     *
     * 做多的历史持仓，已经全部清仓的 closed有时间，"status":"All Closed"，千万不要用"updateTime"，它是会改变的，不知道啥原因
     * {"symbol":"CELOUSDT","opened":1705095733741,"closed":1705102034801,"side":"Long","updateTime":1705102034949, "status":"All Closed"},
     * * 做多的历史持仓，已经部分清仓的 closed=null，"status":"Partially Closed"
     * {"symbol":"CELOUSDT","opened":1705095733741,"closed":,"side":"Long","updateTime":1705102034949, "status":"Partially Closed"},
     * 做空的历史持仓
     * {"symbol":"ENSUSDT","opened":1705059695456,"closed":1705073895126,"side":"Short","updateTime":1705073895763}
     *







     *
     * 双向持仓
     *      做多
            买：side=BUY,positionSide=LONG    qty":2778
            卖：side=SELL,positionSide=LONG   qty":2778
            做空
            买：side=SELL,positionSide=SHORT  qty":2778
            卖：side=BUY,positionSide=SHORT   qty":2778
     * {"time":1705751560000,"symbol":"UMAUSDT","side":"SELL","quantity":11239.788000,"realizedProfit":0.00000000,"qty":2778.00000000,"positionSide":"SHORT","activeBuy":false}
     *
     * position
     * 做多的正在持仓
     * {"symbol":"ORDIUSDT","positionAmount":"50.8","leverage":20,"positionSide":"LONG",}
     * 做空的持仓
     * {"symbol":"UMAUSDT","positionAmount":"-3471","leverage":20,"positionSide":"SHORT"}
     *
     * 做多的历史持仓（注意long和short是小写）
     * {"symbol":"UMAUSDT","opened":1705665671378,"closed":1705679892671,"side":"Long","updateTime":1705679892860}
     * 做空的历史持仓
     * {"symbol":"UMAUSDT","opened":1705749980512,"closed":1705750379058,"side":"Short","updateTime":1705750379223}
     *
     *
     *
     * */


    public static String con_realizedProfit = "realizedProfit";




    public static void main(String[] args) throws InterruptedException, IOException {


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

        JSONArray jsonArrayNew = new JSONArray();
        JSONObject test = JSONObject.parseObject("");
        jsonArrayNew.add(test);
        genDan genDan = new genDan();
        genDan.method(jsonArrayNew);


    }


    public void method(JSONArray test) throws InterruptedException, IOException {
        PrivateConfig.printLog("跟单开始时间为：" + PrivateConfig.getCurrentTime());

        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(5, 5, 10,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.DiscardPolicy());
        PrivateConfig.threadPoolExecutor = threadPoolExecutor;

        /*Callable callable2 = new Callable() {
            @Override
            public String call() throws Exception {
                JianKongLog jianKong4 = new JianKongLog();
                jianKong4.method(PrivateConfig.genDan_personInfoList);
                return "";
            }
        };
        threadPoolExecutor.submit(callable2);*/



        //启动监控线程
        Callable callable1 = new Callable() {
            @Override
            public String call() throws Exception {
                JianKong4 jianKong4 = new JianKong4();
                jianKong4.method(null, threadPoolExecutor, PrivateConfig.genDan_personInfoList);
                return "";
            }
        };
        threadPoolExecutor.submit(callable1);

        //设置倍数
        PrivateConfig.setBeiShu(threadPoolExecutor, PrivateConfig.genDan_personInfoList);
        Set<String> tradeSet = new LinkedHashSet<>();
        int countErr = 0;
        int countErrLarge = 0;
        Map<String, Order> oldOrders = getOrders1(threadPoolExecutor, true);
        int diaoYongCount = 0;
        PrivateConfig.printLogJianKong();
        while (true) {

            try {
//                PrivateConfig.printLog("执行1次：" + PrivateConfig.getCurrentTime());

                Thread.sleep(Long.parseLong(PrivateConfig.shiJian));

                /*if(PrivateConfig.isZhiSun){
                    Thread.sleep(1000*60*30);
                    T5.searchAll("止损了，重大问题，抓紧报告");
                    System.out.println("止损了，重大问题，抓紧报告");
                    continue;
                }*/

                Map<String, Order> newOrders = getOrders1(threadPoolExecutor, false);
                if(newOrders == null){
                    T5.searchAll("停止跟单了，抓紧报告");
                    return;
                }

                diaoYongCount++;
                if(diaoYongCount > 100){
                    diaoYongCount = 0;
                    PrivateConfig.printLogJianKong();
                    PrivateConfig.printLog("newOrders");
                    for(Map.Entry<String, Order> entryNew : newOrders.entrySet()){
                        PrivateConfig.printLog(entryNew.getKey());
                        PrivateConfig.printLog(entryNew.getValue().toString());
                    }
                    PrivateConfig.printLog("oldOrders");
                    for(Map.Entry<String, Order> entryNew : oldOrders.entrySet()){
                        PrivateConfig.printLog(entryNew.getKey());
                        PrivateConfig.printLog(entryNew.getValue().toString());
                    }
                }

                List<Order> orderListNew = new ArrayList<>();

                for(Map.Entry<String, Order> entryNew : newOrders.entrySet()){
                    String key = entryNew.getKey();
                    if(!oldOrders.containsKey(key)){
                        //有新单子了
                        String symbolPoSide = key.split("__")[0];
                        for(Map.Entry<String, Order> entryOld : oldOrders.entrySet()){
                            if(entryOld.getKey().contains(symbolPoSide)){

                                Order orderNew = new Order();
                                orderNew.setSymbol(entryNew.getValue().getSymbol());
                                orderNew.setPositionSide(getPositionSide(entryNew.getValue().getPositionSide(), entryNew.getValue().getOrigQty()));
                                orderNew.setRemoveSide(getRemoveSide(entryNew.getValue().getPositionSide(), entryNew.getValue().getOrigQty()));
                                orderNew.setRemovePositionSide(entryNew.getValue().getPositionSide());

                                BigDecimal oldCount = entryOld.getValue().getOrigQty();
                                BigDecimal newCount = entryNew.getValue().getOrigQty();
                                BigDecimal result = newCount.subtract(oldCount);
                                orderNew.setOrigQty(result.abs());

                                orderNew.setSide(OrderSide.BUY.toString());
                                if(result.compareTo(ling)<0){
                                    orderNew.setSide(OrderSide.SELL.toString());
                                }
                                orderListNew.add(orderNew);

                                //更新旧单子（当数量从1变成2时，数量1的订单移除，所以再从2变成1时，又是一个新单子了）
                                oldOrders.remove(entryOld.getKey());
                                oldOrders.put(key, entryNew.getValue());
                                break;
                            }
                        }

                        if(!oldOrders.containsKey(key)){
                            //有新币种的单子了
                            Order orderNew = new Order();
                            orderNew.setSymbol(entryNew.getValue().getSymbol());
                            orderNew.setOrigQty(entryNew.getValue().getOrigQty().abs());

                            orderNew.setPositionSide(getPositionSide(entryNew.getValue().getPositionSide(), entryNew.getValue().getOrigQty()));
                            orderNew.setRemoveSide(getRemoveSide(entryNew.getValue().getPositionSide(), entryNew.getValue().getOrigQty()));
                            orderNew.setRemovePositionSide(entryNew.getValue().getPositionSide());

                            orderNew.setSide(OrderSide.BUY.toString());
                            if(entryNew.getValue().getOrigQty().compareTo(ling)<0){
                                orderNew.setSide(OrderSide.SELL.toString());
                            }
                            orderListNew.add(orderNew);
                            //更新旧单子
                            oldOrders.put(key, entryNew.getValue());
                        }
                    }
                }

                //处理清仓的币种
                Iterator<Map.Entry<String, Order>> iterator = oldOrders.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Order> entryOld = iterator.next();
                    String symbolPoSide = entryOld.getKey().split("__")[0];
                    boolean qingCang = true;
                    for(Map.Entry<String, Order> entryNew : newOrders.entrySet()){
                        if(entryNew.getKey().contains(symbolPoSide)){
                            //现在还有
                            qingCang = false;
                            break;
                        }
                    }
                    //现在没有了
                    if(qingCang){
                        QingCang3 qingCang3 = new QingCang3();
                        qingCang3.qingCang(PrivateConfig.genDan_personInfoList, entryOld.getValue().getSymbol(), getPositionSide(entryOld.getValue().getPositionSide(), entryOld.getValue().getOrigQty()));
                        //删除旧订单
                        iterator.remove();
                    }
                }


                /*for(Map.Entry<String, Order> entryOld : oldOrders.entrySet()){
                    System.out.println(entryOld.getKey());
                }*/


                countErr = 0;

                if (!orderListNew.isEmpty()) {
                    //下单
                    for (JSONObject person : PrivateConfig.genDan_personInfoList) {

                        try {
                            for (Order order : orderListNew) {
                                //太贵的单子不跟
                                if(PrivateConfig.remove.contains(order.getSymbol().toUpperCase())){
                                    //移除跟單
                                    remove(order, PrivateConfig.genDan_genPortfolioId);
                                }else {
                                    MulPostOrders mulGetAllOrders = new MulPostOrders(person, order);
                                    threadPoolExecutor.submit(mulGetAllOrders);//启动一般的线程
                                    //两个账号之间执行的间隔
                                    Thread.sleep(50);
                                }

                            }

                        } catch (Exception e) {
                            PrivateConfig.printLog(e.getMessage());
                            T5.searchAll("连续三次，有问题，关闭软件，重新启动33。" + e.getMessage());
                        }
                    }
                    JianKong4.needCheck = true;
                    JianKong4.checkCount = 0;
//                    PrivateConfig.printLog("下单了1：" + PrivateConfig.getCurrentTime());
//                    Thread.sleep(2000);//前面有跟单，歇2秒再跟
                }



            } catch (Exception e) {
                countErr++;
                if (countErr > 3) {
                    countErr = 0;
                    PrivateConfig.printLog(e.getMessage());
                    T5.searchAll("连续三次，有问题，关闭软件，重新启动34。" + e.getMessage());
                    countErrLarge++;
                }
                if (countErrLarge > 5) {
                    countErrLarge = 0;
                    //更换Url地址
                    PrivateConfig.genDan_urlIndex++;
                    if (PrivateConfig.genDan_urlIndex >= PrivateConfig.genDan_urls.split(";").length) {
                        PrivateConfig.genDan_urlIndex = 0;
                    }
                    PrivateConfig.genDan_url = PrivateConfig.genDan_urls.split(";")[PrivateConfig.genDan_urlIndex];
                    System.out.println("正在访问的域名：" + PrivateConfig.genDan_url);
                }
            }

            if (tradeSet.size() > 1000) {
                //删除最后100个元素
                for (int i = 0; i < 90; i++) {
                    tradeSet.remove(900);
                }
            }
        }
    }

    public void remove(Order order, String genPortfolioId) throws IOException, InterruptedException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject remove = new JSONObject();
        remove.put("symbol", order.getSymbol());
        remove.put("type", "MARKET");
        remove.put("side", order.getRemoveSide());
        remove.put("positionSide", order.getRemovePositionSide());
        remove.put("quantity", order.getOrigQty()+"");
        remove.put("isolated", false);
        remove.put("newOrderRespType", "RESULT");
        remove.put("placeType", "position");
        remove.put("copyTradeType", "COPY");
        remove.put("portfolioId", genPortfolioId);
        if(PositionSide.BOTH.toString().equals(order.getRemovePositionSide())){
            remove.put("reduceOnly", true);
        }
        if("1".equals(PrivateConfig.ceShi)){
            System.out.println("移除"+remove.toJSONString());
        }
        RequestBody body = RequestBody.create(mediaType, remove.toJSONString());

        /*Request request = new Request.Builder()
                .url("https://www.binance.com/bapi/futures/v1/private/future/order/place-order")
                .method("POST", body)
                .addHeader("Host", "www.binance.com")
                .addHeader("x-token", PrivateConfig.genDan_token)
                .addHeader("bnc-req-src", "native")
                .addHeader("clienttype", "android")
                .addHeader("x-trace-id", "android_7ee5cfef-96cf-4b8e-976b-1419de511820")
                .addHeader("fvideo-id", "2328e05fa9f2a07581f36886824c433bb98e40fe")
                .addHeader("fvideo-token", "EP5GTaPzjPXBgHCd8y9s9jrnCh4c8uPlVQJ4dxMnRw0/fPRI4lZIigQ59N2kEcbBltbpiIKAE4ou6RMEWfepUbxbdt+XC1OH9WguZhvO2s3PYHsN5G8UQlDy7SRBhJD7b0FI3/BPlO8gbsD6/X3sMNosN8HVxfVYAUXzt8cNbWUnbG109VSnt6ODkWUxBImw4=48")
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
                .addHeader("mclient-x-tag", "Z73vs0d3eu67rDqMCbSH")
                .addHeader("bnc-location", "BINANCE")
                .addHeader("bnc-currency", "USD")
                .addHeader("referer", "https://www.binance.com/")
                .addHeader("x-seccheck-sig", "a1.5.4#TgAAABoAAAA6AAAAYwAAAC3JzWBooNdCfCcdX1qa_cffRDOFiTbv9-mNGhUvru8XCIe0bsbKE9yx25GAmF4NX0l2F7Kn-a_putA6934SAgWK0c4zq8YSW8LD0qLC2E418vB0s7NoYUd7CIWOZDHUbizgEmN62utyuy27sg2aoDTXLItS119m6fSE7aOBcC6S569JFQZ2lvZ0cUZOS67u5MHR0LGrGttGrdkyQOvvtu3TQFBlY2_sIkDUyEOeGQiFU2lIup2vPyh2a0yATXhZWGOEdSzQLBPaVndcD0qkAgxCng74")
                .addHeader("x-seccheck-token", "a1.5.4#fgAAAHoBAABqAQAAQwIAAFxtKJ1gYYLxakh_0CVHA62t6jN25Vb74HDPxbPDvvA9qdkBNXbYT-m-c_xGmXwITUi7pP5MnBKs0jrhwi1H_0Zen_8e9BxaKf9OlNqLhrg1WB2JIfMtNm4Tlv-icuNYywSoK4hEI5EsGen9dvl4RMWNvLFvMCboPSC2ostemX67X8gAt-gFqSDwMgOVft0VCdux4Vx-FrCEVrc34P1BK2YCyT9SRCngffl3Y4midkTkk268Z-ugPWC8hUhSRgcZKc6wnU9BXqM-SyZi8PbDmWBxdJoP0nuIt6gcVE3JZxN1arkYOJ0V8t8G19c2hx6K6j89vmxd6yP0gqTT2EnfGY7FvWyBcR4uz9atiNG42eamwCJpGJmbHt2Dl_mkkxw9CdC7J7tzJuEx7bn1seXTINVvqa9HFLNG350e6SHGvrnUD-p6DI3rfYLjtCtuJQwc8BF2JQEz0K5Pvn5DfM7FVfE8oj5ORQvRVr9DalNwLZ7AqwaqZX0ohL73wHqg5tCxa6G-u_N3aXM0ALSaos7D1w5fA-QrYyC8_YqX9NDtpkzL_NO9RgGsEAO74DrYgEOjH-ns09pKsEBD9bH0rgqrOquINJhNLytw8JLZEngl7b2Pe0zOCKGnDZVDCow1mVlWV31q5cA-UslShIJIt9c0vcTBDrNW3wpXOmp04RcPEdTwHX7y0_T7_kVKHbDAsJrtIIP0NT1zQ6Vp1ejjRUvHHrdb7jX2k73JF3WmL1w8gyVNPBKKvrjD9_Z7mqMAmGU9TulBbUOkGlIG2fJZ2QLUWKb1WvKuS3SDMK0iAIJ0E85OV-KvTZ_Z78KSop43G6Ug2MO2-FVprp8Q2xshuI8wkHsLBbZ_SDrpgwlmbT7ETRmRJZA3UbMf9_T_gQxQb5BVVE-D5OjqzrdFEKDtprVyhAWhgEDqIhtf3Qx3HJSizvYnnFAqyYn8-7HrpU9NZ2vuNVqWvUDtKnbjYCfUB_QCEkrGfC0SliO5avGVEyqbZ3yV2hQTSH6_CtsjgxtEgXES15xWi3Ya9HHh4lvvp4PUWY_nx_IA-xu6t2CCWFpkUAScv58wrw#6033A180")
                .addHeader("bnc-cpk", "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEsOT9uXR+pkT+pwt5yOUMrtl17pNeji0auJ1jURmjLV5QED8dC5qczpEi0PuHN1LFfGXSnzX4AJzbvlOzkoL35Q==")
                .addHeader("content-type", "application/json")
                .addHeader("user-agent", "okhttp/4.12.0")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonObject = JSON.parseObject(response.body().string());*/
        JSONObject jsonObject = JSON.parseObject(PostGet.postPhone(body, "https://www.binance.com/bapi/futures/v1/private/future/order/place-order"));
        if(PrivateConfig.ceShi.equals("1")){
            System.out.println(jsonObject.toJSONString());
        }
        if (!"000000".equals(jsonObject.getString("code"))) {
            T5.sendMe(jsonObject.toJSONString());
        }
    }






    public Map<String, Order> getOrders1(ThreadPoolExecutor threadPoolExecutor, boolean old) throws InterruptedException {
        Map<String, Order> map = new HashMap<>();
        JSONArray jsonArray;
        if(old && !CollectionUtils.isEmpty(PrivateConfig.genDan_xianYou)){
            //从json文件中读取现有持仓， 这样就可以随时添加客户
            /*
             * 现有订单：MASKUSDT_1104.00000000_LONG_1717553618000
             * 现有订单：1000PEPEUSDT_-13371307.00000000_SHORT_1710610697000
             * */
            jsonArray = PrivateConfig.genDan_xianYou;
        }else {
            jsonArray = GetPositions.getOrders(threadPoolExecutor, genDan_genPortfolioId);
        }
        if(daYin.equals("1")){
            if(jsonArray != null){
                printLog(jsonArray.toJSONString());
            }else {
                printLog("获取订单失败了");
            }
        }

//        System.out.println("订单：" + jsonArray.toJSONString());
        if(jsonArray != null){
            for (Object o : jsonArray) {
                JSONObject trade = (JSONObject) o;
                String symbol = trade.getString(PrivateConfig.symbol);
                String positionSide = trade.getString(PrivateConfig.positionSide);
                BigDecimal qty = trade.getBigDecimal(PrivateConfig.positionAmount);

                Order order = new Order();
                order.setSymbol(symbol);
                order.setPositionSide(positionSide);
                order.setOrigQty(qty);
                map.put(flg1(trade), order);
            }
        }else {
            return null;
        }


        return map;
    }

    public String flg1(JSONObject trade){
        String symbol = trade.getString(PrivateConfig.symbol);
        String positionSide = trade.getString(PrivateConfig.positionSide);
        BigDecimal qty = trade.getBigDecimal(PrivateConfig.positionAmount);
        return new StringBuilder().append(symbol).append("_").append(positionSide).append("__").append(qty).toString();
    }




}




