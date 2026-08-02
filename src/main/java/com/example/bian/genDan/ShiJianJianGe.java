package com.example.bian.genDan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

public class ShiJianJianGe {

    public static void main(String[] args) throws InterruptedException, ParseException {

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


        ShiJianJianGe genDan = new ShiJianJianGe();
        genDan.method();
        System.out.println("结束");

    }
    static String genDan_portfolioId = null;
    static boolean 只统计钱 = true;

    public static void method() throws InterruptedException, ParseException {
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(1, 1, 1,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.DiscardPolicy());
        List<String> oldList = getOldList();
        List<String> list = new ArrayList<String>(Arrays.asList(


                "4751838302089254401",
                "4939006395945705729"



        ));
        for(String s : list) {
            if(oldList.contains(s)){
                continue;
            }

            genDan_portfolioId = s;
            System.out.println("-----------------------------------");
            System.out.println("谁：" + genDan_portfolioId);
            if(!只统计钱) {

                //查看下单数
                getOrders(threadPoolExecutor);
                System.out.println("总共多少单：" + tradeSet.size());
                System.out.println("小于30分钟的单子：" + count);
                System.out.println("小于1分钟的单子：" + count1Minite);
                System.out.println("总共多少天：" + tian);
                System.out.println("每天多少单：" + tradeSet.size() / (double) tian);
                System.out.println("小于1分钟的单子占有率：" + (double) count1Minite / tradeSet.size());
                System.out.println("小于30分钟的单子占有率：" + (double) count / tradeSet.size());
            }

            if(true) {

                //查看下单数
                JSONArray all = new JSONArray();
                for (int i = 1; i <= 1; i++) {
                    JSONArray jsonArray = getPositionHistorys(threadPoolExecutor, i, 200);
                    if(CollectionUtils.isNotEmpty(jsonArray)){
                        all.addAll(jsonArray);
                    }else {
                        break;
                    }
                    Thread.sleep(5000);
                }

                List<String> list1 = new ArrayList<>();
                list1.add("BTCUSDT");
//                list1.add("ETHUSDT");
                for(String symbol : list1){
                    symbolOp(all, symbol);
                }
            }


//            youMeiYouZaoJia(threadPoolExecutor);
            tian = 0L;
            tradeSet.clear();
            tradeMap.clear();
            oldTime1 = Long.MAX_VALUE;
            count = 0;
            count1Minite = 0;
        }
    }

    public static void symbolOp(JSONArray jsonArray, String symbolOp) {

        int countTotal = 0;//总单数
        int countWin = 0;//盈利单数
        int countXiao5 = 0;//时间间隔小于5分钟的单子个数
        int countXiao30 = 0;//时间间隔小于30分钟的单子个数
        int countXiao120 = 0;//时间间隔小于30分钟的单子个数
        int countXiao240 = 0;//时间间隔小于30分钟的单子个数
        System.out.println("------------------------------------------------------------");
        System.out.println("下面是" + symbolOp + "的数据");
        Set<BigDecimal> jinE = new TreeSet<>();
        for(Object o : jsonArray){
            JSONObject jsonObject = (JSONObject) o;
            String status = jsonObject.getString("status");
            if(!status.equals("All Closed")){
                continue;
            }
            String symbol = jsonObject.getString("symbol");
            if(!symbolOp.equals(symbol)){
                continue;
            }
            countTotal++;



            BigDecimal kaiCangJiaGe = jsonObject.getBigDecimal("avgCost");
            BigDecimal count = jsonObject.getBigDecimal("maxOpenInterest");
            BigDecimal pingCangJiaGe = jsonObject.getBigDecimal("avgClosePrice");
            BigDecimal yingLv = pingCangJiaGe.subtract(kaiCangJiaGe).divide(kaiCangJiaGe, 4, RoundingMode.HALF_UP);

            jinE.add(kaiCangJiaGe.multiply(count).setScale(0, RoundingMode.DOWN));

            BigDecimal yingLi = jsonObject.getBigDecimal("closingPnl");
            if(yingLi.compareTo(PrivateConfig.ling) > 0){
                countWin++;
//                System.out.println(yingLv.abs());
            }else {
//                System.out.println("-" + yingLv.abs());
            }
            Long kaiShiShijian = jsonObject.getLong("opened");
            Long jieShuShijian = jsonObject.getLong("closed");
            Long jianGe = (jieShuShijian - kaiShiShijian)/1000/60;
            if(jianGe <= 5){
                countXiao5++;
            }
            if(jianGe <= 30){
                countXiao30++;
            }
            if(jianGe >= 120){
                countXiao120++;
            }
            if(jianGe >= 240){
                countXiao240++;
            }
        }
        for(BigDecimal bigDecimal : jinE){
            System.out.println(bigDecimal);
        }

        System.out.println("总单数：" + countTotal);
        System.out.println("盈利单数：" + countWin);
        System.out.println("盈利单数比：" + countWin*1.0/countTotal);
        System.out.println("小于5分钟的单数比：：" + countXiao5*1.0/countTotal);
        System.out.println("小于30分钟的单数比：：" + countXiao30*1.0/countTotal);
        System.out.println("大于120分钟的单数比：：" + countXiao120*1.0/countTotal);
        System.out.println("大于240分钟的单数比：：" + countXiao240*1.0/countTotal);


    }


    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //详细做单信息
    public static void youMeiYouZaoJia(ThreadPoolExecutor threadPoolExecutor) throws InterruptedException, ParseException {
        int error = 0;
        List<String> jiaCangTimeList1 = new ArrayList<>();
        List<Long> jiaCangTimeList = new ArrayList<>();
        for(String jiaCangOrder : jiaCangTimeList1){
            jiaCangTimeList.add(Long.valueOf(dateToStamp(jiaCangOrder)));
        }
        List<String> jiaCangOrderList = new ArrayList<>();
        BigDecimal total = PrivateConfig.ling;
        int totalCount = 0;
        long time = 0;
        BigDecimal shouXuFei = PrivateConfig.ling;
        for (int i = 1; i <= 120; i++) {
            if(error>=3){
                break;
            }
            JSONArray jsonArray = getPositionHistorys(threadPoolExecutor, i, 50);
            if(CollectionUtils.isEmpty(jsonArray)){
                i--;
                error++;
            }else {
                error = 0;
                for (Object o : jsonArray) {
                    totalCount++;
                    JSONObject trade = (JSONObject) o;
                    BigDecimal qian = trade.getBigDecimal("closingPnl").setScale(2, RoundingMode.DOWN);
                    BigDecimal maxOpenInterest = trade.getBigDecimal("maxOpenInterest");//最大持仓量
                    BigDecimal avgCost = trade.getBigDecimal("avgCost");//开仓价格
                    BigDecimal avgClosePrice = trade.getBigDecimal("avgClosePrice");//平仓价格

                    shouXuFei = shouXuFei.add(avgCost.multiply(maxOpenInterest).multiply(new BigDecimal("0.0004")))
                            .add(avgClosePrice.multiply(maxOpenInterest).multiply(new BigDecimal("0.0004")));

                    total = total.add(qian);

                    Long openedTime = trade.getLong("opened");//开仓时间
                    tian = (System.currentTimeMillis() - openedTime) / 24 / 60 / 60 / 1000;

                    Long closedTime = trade.getLong("closed");//平仓时间
                    for(Long jiaCangTime : jiaCangTimeList){
                        if(openedTime<jiaCangTime && jiaCangTime < closedTime){
                            jiaCangOrderList.add(trade.getString(PrivateConfig.symbol)
                                    + "/" + sdf.format(new Date(openedTime))
                                    + "/" + sdf.format(new Date(closedTime))
                                    + "/" + trade.getString("avgCost")
                                    + "/" + trade.getString("maxOpenInterest")
                                    + "/" + trade.getString("side")
                                    + "/" + qian
                            );
                        }
                    }

                    //时间间隔，小时
                    Long timeCha1 = (closedTime-openedTime)/1000/60/60;

                    String side = trade.getString("side");
                    //价格差赢率
                    BigDecimal yingLv = (avgClosePrice.subtract(avgCost).divide(avgCost,3,BigDecimal.ROUND_HALF_UP));
                    if("Short".equals(side)){
                        yingLv = yingLv.multiply(new BigDecimal("-1"));
                    }

                    System.out.println(sdf.format(new Date(openedTime)) + "/" + qian + "/" + timeCha1+ "/" + yingLv+ "/"  + trade.getString(PrivateConfig.symbol));


                    if(closedTime != null){
                        Long timeCha = (closedTime-openedTime)/1000/60;
                        time+=timeCha;
                    }
                }
            }
            Thread.sleep(3000);
        }
        /*for(String sol : SOLUSDTList){
            System.out.println(sol);.
        }*/

        System.out.println("总共挣了：" + total);
        System.out.println("手续费：" + shouXuFei);
        System.out.println("平均每单多少分钟：" + time/totalCount);

    }


    public static List<String> getOldList(){
        List<String> list = new ArrayList<String>(Arrays.asList(



        ));

        return list;
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }


    public static void youMeiYouJiaCang(ThreadPoolExecutor threadPoolExecutor) throws InterruptedException {
        int error = 0;

        for (int i = 1; i <= 1000; i++) {
            if(error>=3){
                break;
            }
            JSONArray jsonArray = getPositionHistorys(threadPoolExecutor, i, 50);
            if(CollectionUtils.isEmpty(jsonArray)){
                i--;
                error++;
            }else {
                error = 0;
                for (Object o : jsonArray) {
                    JSONObject trade = (JSONObject) o;

                }
            }
            Thread.sleep(3000);
        }


    }

    public static JSONArray getPositionHistorys(ThreadPoolExecutor threadPoolExecutor, int pageNumber, int pageSize)  {
        for (int i = 0; i < 3; i++) {
            String s = null;
            try {
                s = getPositionHistory(threadPoolExecutor, pageNumber, pageSize);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (StringUtils.isNotBlank(s)) {
                JSONObject jsonObject = JSON.parseObject(s);
                if ("000000".equals(jsonObject.getString("code"))) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray jsonArray = data.getJSONArray("list");
                    if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(jsonArray)) {
                        return jsonArray;
                    }
                } else {
                    T5.searchAll("position-history，连续3次，有问题！1");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return new JSONArray();
    }

    public static String getPositionHistory(ThreadPoolExecutor threadPoolExecutor, int pageNumber, int pageSize) throws InterruptedException {

        //查询所有类型的订单 可以根据symbol 查询是比特币的 还是其他币的
        Callable callable = new Callable() {
            @Override
            public String call() throws Exception {
                return Postman.sendPost("https://www.binance.com/bapi/futures/v1/public/future/copy-trade/lead-portfolio/position-history",
                        "{\"pageNumber\":" + pageNumber + ",\"pageSize\":" + pageSize + ",\"portfolioId\":\"" + genDan_portfolioId + "\"}", PrivateConfig.genDan_cookie, PrivateConfig.genDan_token);
            }
        };
        int h = 0;
        while (true) {
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
                    PrivateConfig.printLog("币安跟单-positions超时了12");
                    T5.searchAll("position-history，连续5次，有问题！");
                }
            }
        }
    }






    public static JSONArray getOrders(ThreadPoolExecutor threadPoolExecutor) throws InterruptedException {
        JSONArray all = new JSONArray();
        for (int i = 1; i <= 20; i++) {
            String s = getOrder(threadPoolExecutor, i, 50);
            if (StringUtils.isNotBlank(s)) {
                JSONObject jsonObject = JSON.parseObject(s);
                if ("000000".equals(jsonObject.getString("code"))) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray jsonArray = data.getJSONArray("list");
                    if (CollectionUtils.isNotEmpty(jsonArray)) {
                        print(jsonArray);
                    }else {
                        return all;
                    }
                }else if ("11012005".equals(jsonObject.getString("code"))) {
//                    System.out.println("获取第" + i + jsonObject.getString("message"));
                    i--;
                }
            }else {
                return all;
            }
            Thread.sleep(3000);
        }
        return all;
    }
    static Set<String> tradeSet = new HashSet<>();
    static Map<String, Long> tradeMap = new HashMap<>();
    static Long oldTime1 = Long.MAX_VALUE;
    static int count = 0;
    static int count1Minite = 0;
    static Long tian = 0L;
    public static void print(JSONArray all) {

        for (Object o : all) {
            JSONObject trade = (JSONObject) o;
            String symbol = trade.getString(PrivateConfig.symbol);
            String side = trade.getString(PrivateConfig.side);
            String positionSide = trade.getString(PrivateConfig.positionSide);
            Long newTime = trade.getLong("time");
            String flag = symbol + "-" + side + "-" + positionSide + "-" + newTime;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time1 = sdf.format(new Date(newTime)); // 时间戳转换日期
            if (!tradeSet.contains(flag)) {
                if (oldTime1 - newTime <= 2000) {
                    //前后相差太短，认为是一个单子
                    continue;
                }
                tradeSet.add(flag);
                String cha = "";
                if (tradeMap.containsKey(symbol)) {
                    Long oldTime = tradeMap.get(symbol);
                    cha = sdf.format(new Date(oldTime - newTime - 28800000)); // 时间戳转换日期

                    if (oldTime - newTime < 30 * 60 * 1000) {
                        count++;
//                        System.out.println(time1 + ":" + flag + "; 与上次操作相差多久：" + cha);
                    }

                    if (oldTime - newTime < 60 * 1000) {
                        count1Minite++;
                    }
                }
                tradeMap.put(symbol, newTime);
//                System.out.println(time1 + ":" + flag + "; 与上次操作相差多久：" + cha);
            }
            oldTime1 = newTime;
        }

        tian = (System.currentTimeMillis() - oldTime1) / 24 / 60 / 60 / 1000;

    }

    public static String getOrder(ThreadPoolExecutor threadPoolExecutor, int pageNumber, int pageSize) {

        //订单的顺序：第一个就是最近的一个
        Callable callable = new Callable() {
            @Override
            public String call() throws Exception {
                return Postman.sendPost("https://www.binance.com/bapi/futures/v1/public/future/copy-trade/lead-portfolio/trade-history",
                        "{\"pageNumber\":" + pageNumber + ",\"pageSize\":" + pageSize + ",\"portfolioId\":\"" + genDan_portfolioId + "\"}", PrivateConfig.genDan_cookie, PrivateConfig.genDan_token);
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
                        PrivateConfig.printLog("币安跟单-获取订单超时了");
                        T5.searchAll("订单失败，连续5次，有问题！2");
                    }
                }
            } catch (Exception e1) {
            }

        }
    }


}
