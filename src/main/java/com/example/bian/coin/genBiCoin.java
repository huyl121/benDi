package com.example.bian.coin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import com.example.bian.client.model.enums.OrderSide;
import com.example.bian.client.model.trade.Order;
import com.example.bian.genDan.MulPostOrders;
import com.example.bian.genDan.genDan;
import com.example.bian.xin.JianKong4;
import com.example.bian.xin.QingCang3;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

import static com.example.bian.client.bushu.PrivateConfig.*;

/*
*   买入开多
    卖出平多

    卖出开空
    买入平空
*
*
*
*
* */

public class genBiCoin {

    public static void main(String[] args) throws InterruptedException {


        args = new String[2];
        args[0] = "E://code//biance";
        args[1] = "0-genBiCoin";
        PrivateConfig.init((args[0]));
        PrivateConfig.before(args[0], args[1]);
        PrivateConfig.printLog("开始啦");

        // 对https也开启代理

        if(PrivateConfig.daiLi.equals("1")) {
            System.out.println("开代理");
            System.setProperty("https.proxySet", "true");
            System.setProperty("https.proxyHost", "127.0.0.1");
            System.setProperty("https.proxyPort", "10819");
        }


        PrivateConfig.getJGXsw();
        PrivateConfig.xsw(true);

        genBiCoin genDan = new genBiCoin();
        genDan.method(null);


    }


    public void method(ThreadPoolExecutor threadPoolExecutor) throws InterruptedException {
        PrivateConfig.printLog("BiCoin 开始时间为：" + PrivateConfig.getCurrentTime());

        if(threadPoolExecutor == null){
            threadPoolExecutor =
                    new ThreadPoolExecutor(5, 5, 10,
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(),
                            Executors.defaultThreadFactory(),
                            new ThreadPoolExecutor.DiscardPolicy());
            PrivateConfig.threadPoolExecutor = threadPoolExecutor;
        }

        //启动监控线程
        /*Callable callable1 = new Callable() {
            @Override
            public String call() throws Exception {
                JianKongCoin jianKong4 = new JianKongCoin();
                jianKong4.method(PrivateConfig.biCoin_personInfoList);
                return "";
            }
        };
        threadPoolExecutor.submit(callable1);*/

        /*Callable callable2 = new Callable() {
            @Override
            public String call() throws Exception {
                JianKongLog jianKong4 = new JianKongLog();
                jianKong4.method(PrivateConfig.biCoin_personInfoList);
                return "";
            }
        };
        threadPoolExecutor.submit(callable2);*/

        int countErr = 0;
        //存储已经做过的单子
        //当一个挂单，分多次成交时，msgId会相同，此时需借助时间判断是否做过
        /* 例如
        1秒挂单【37.8万个】，成交【2.17万个】
        2秒挂单【37.8万个】，成交【3.8万个】
        3秒挂单【37.8万个】，成交【37.8万个】
         */
        Map<String, Map<String, BigDecimal>> msgIdTimeMap = new HashMap<>();

        //启动时，不跟单
        JSONArray oldOrders = GetPositions.getOrders(threadPoolExecutor);
        for (Object entryNew : oldOrders) {
            JSONObject entity = (JSONObject) entryNew;
            String msgId = entity.getString("msgId");
            String content = entity.getString("content");
            Long cTime = entity.getLong("cTime");
            String contentTime = content + cTime;
            if(msgIdTimeMap.containsKey(msgId) && msgIdTimeMap.get(msgId).containsKey(contentTime)){
                continue;
            }

            Map<String, BigDecimal> timeMap;
            if(msgIdTimeMap.containsKey(msgId)){
                timeMap = msgIdTimeMap.get(msgId);
            }else {
                timeMap = new HashMap<>();
                msgIdTimeMap.put(msgId, timeMap);
            }
            timeMap.put(contentTime, null);

        }
        //想跟哪个单子就移除掉，移除几个跟几个
        msgIdTimeMap.remove("90fd92a7cd8b4d479a0d91b9a3df2ccf");
        if(StringUtils.isNotBlank(biCoinremove)){
            String[] removes = StringUtils.split(biCoinremove, ",");
            for(String remove : removes){
                msgIdTimeMap.remove(remove);
            }
        }

        int countCycle = 0;
        int diaoYongCount = 0;
        long timeShangCi = System.currentTimeMillis();
        Map<Long, Integer> shiJianMap = new TreeMap<>();//统计调用时间差的
        PrivateConfig.printLogJianKong();
        boolean usdc = false;
        while (true) {
            try {
                diaoYongCount++;
                if(diaoYongCount > 15){
                    diaoYongCount = 0;
                    PrivateConfig.printLogJianKong();
                }
                countCycle++;
                if(countCycle>30*5){
                    countCycle = 0;
                    PrivateConfig.printLog("coin"+PrivateConfig.getCurrentTime());

                    for(Map.Entry<Long, Integer> entry : shiJianMap.entrySet()){
                        PrivateConfig.printLog(entry.getKey() + "：" + entry.getValue());
                    }
                }
                Thread.sleep(Long.parseLong(PrivateConfig.shiJian));
                if (countErr > getPon) {
                    //调用出错了，清仓
                    T5.searchAll("币coin，清仓了");
                    PrivateConfig.printLog("币coin，清仓了");
                    QingCang3 qingCang = new QingCang3();
                    qingCang.qingCang(PrivateConfig.biCoin_personInfoList, null, null);
                    return;
                }
                JSONArray newOrders = GetPositions.getOrders(threadPoolExecutor);
//                PrivateConfig.printLog(newOrders.toJSONString());
                if(CollectionUtils.isEmpty(newOrders)){
                    countErr++;
                    T5.searchAll("币coin，获取不到订单了");
                    Thread.sleep(1000 * 60);
                    continue;
                }else {
                    countErr=0;
                    Long shiJianCha = (System.currentTimeMillis() - timeShangCi)/1000;
                    timeShangCi = System.currentTimeMillis();
                    if(shiJianMap.containsKey(shiJianCha)){
                        shiJianMap.put(shiJianCha, shiJianMap.get(shiJianCha) + 1);
                    }else {
                        shiJianMap.put(shiJianCha, 1);
                    }
                }

                List<Order> orderListNew = new ArrayList<>();
                JSONObject entityLog = null;
                for (Object entryNew : newOrders) {
                    JSONObject entity = (JSONObject)entryNew;
                    if(diaoYongCount == 1){
                        PrivateConfig.printLog(entity.toJSONString());
                    }

                    String symName = entity.getString("symName");
                    if(StringUtils.isNotBlank(PrivateConfig.biCoin_symName) && !PrivateConfig.biCoin_symName.equals(symName)){
                        continue;//指定跟单人，不想指定时，这个值为空就行
                    }
                    String msgId = entity.getString("msgId");

                    String content = entity.getString("content");
                    Long cTime = entity.getLong("cTime");
                    String contentTime = content + cTime;
                    if(msgIdTimeMap.containsKey(msgId) && msgIdTimeMap.get(msgId).containsKey(contentTime)){
                        continue;
                    }

                    Map<String, BigDecimal> timeMap;
                    if(msgIdTimeMap.containsKey(msgId)){
                        timeMap = msgIdTimeMap.get(msgId);
                    }else {
                        timeMap = new HashMap<>();
                        msgIdTimeMap.put(msgId, timeMap);
                    }



                    entityLog = entity;
                    String symbol = entity.getString("sym");
                    //兼容ok
                    if(symbol.contains("-")){
                        symbol = symbol.split("-")[0] + "USDT";
                    }
                    //兼容USDC
                    if(symbol.contains("USDC")){
                        usdc = true;
                        symbol = symbol.replace("USDC", "USDT");
                    }else {
                        usdc = false;
                    }
                    String labelSub = entity.getString("labelSub");

                    String positionSide = "LONG";
                    if(labelSub.contains("空")){
                        positionSide = "SHORT";
                    }
                    String side = "BUY";
                    if(labelSub.contains("平多") || (labelSub.contains("开空"))){
                        side = "SELL";
                    }
                    //如果时间过去了1分钟，通知一下，看看什么原因，也统计一下这种情况是否多
                    if(!PrivateConfig.compareTime(cTime, 60)){
                        PrivateConfig.printLog(entityLog.toJSONString());
                        PrivateConfig.printLog(PrivateConfig.getCurrentTime());
                        T5.sendMe(symbol + "，时间：" + (System.currentTimeMillis() - cTime)/1000);
                    }
                    BigDecimal count;

                    if(usdc){
                        if(PrivateConfig.isMai(side, positionSide, null)){
                            //如果是手动想买的单子，就不判断时间了
                            if(!biCoinremove.contains(msgId)){
                                //买的时候，如果时间过去了1分钟，就不跟了
                                if(biCoin_time3.contains(symbol)){
                                    if(!PrivateConfig.compareTime(cTime, 3*60)){
                                        timeMap.put(contentTime, null);
                                        continue;
                                    }
                                }else if(biCoin_time2.contains(symbol)) {
                                    if (!PrivateConfig.compareTime(cTime, 2*60)) {
                                        timeMap.put(contentTime, null);
                                        continue;
                                    }
                                }else {
                                    if(!PrivateConfig.compareTime(cTime, 60)){
                                        timeMap.put(contentTime, null);
                                        continue;
                                    }
                                }
                            }

                            //成交数量
                            String contentChengJiao = entity.getString("content").split("成交【")[1].split("张】")[0];
                            BigDecimal countChengJiao;
                            if(contentChengJiao.contains("万")){
                                contentChengJiao = contentChengJiao.replace("万", "");
                                countChengJiao = new BigDecimal(contentChengJiao).multiply(new BigDecimal("10000"));
                            }else {
                                countChengJiao = new BigDecimal(contentChengJiao);
                            }
                            //挂单数量
                            String contentGuaDan = entity.getString("content").split("挂单【")[1].split("张】，")[0];
                            BigDecimal countGuaDan;
                            if(contentGuaDan.contains("万")){
                                contentGuaDan = contentGuaDan.replace("万", "");
                                countGuaDan = new BigDecimal(contentGuaDan).multiply(new BigDecimal("10000"));
                            }else {
                                countGuaDan = new BigDecimal(contentGuaDan);
                            }


                            BigDecimal countYiMai = new BigDecimal("0");

                            Map<String, BigDecimal> timeTemp = msgIdTimeMap.get(msgId);
                            for(Map.Entry<String, BigDecimal> entry : timeTemp.entrySet()){
                                countYiMai = countYiMai.add(entry.getValue());
                            }
                            count = countChengJiao.subtract(countYiMai);

                            /*if(countGuaDan.compareTo(countChengJiao) == 0){
                                //相等时，成交数量减去之前已经购买的数量
                                Map<String, BigDecimal> timeTemp = msgIdTimeMap.get(msgId);
                                for(Map.Entry<String, BigDecimal> entry : timeTemp.entrySet()){
                                    countYiMai = countYiMai.add(entry.getValue());
                                }
                                count = countChengJiao.subtract(countYiMai);
                            }else {
                                //成交数量和挂单数量不等，已成交为主
                                count = countChengJiao;
                            }*/

                        }else {
                            //卖的时候按照提交数量
                            String contentGuaDan = entity.getString("content").split("挂单【")[1].split("张】，")[0];
                            if(contentGuaDan.contains("万")){
                                contentGuaDan = contentGuaDan.replace("万", "");
                                count = new BigDecimal(contentGuaDan).multiply(new BigDecimal("10000"));
                            }else {
                                count = new BigDecimal(contentGuaDan);
                            }
                        }
                    }else {
                        if(PrivateConfig.isMai(side, positionSide, null)){
                            //如果是手动想买的单子，就不判断时间了
                            if(!biCoinremove.contains(msgId)){
                                //买的时候，如果时间过去了1分钟，就不跟了
                                if(biCoin_time3.contains(symbol)){
                                    if(!PrivateConfig.compareTime(cTime, 3*60)){
                                        timeMap.put(contentTime, null);
                                        continue;
                                    }
                                }else if(biCoin_time2.contains(symbol)) {
                                    if (!PrivateConfig.compareTime(cTime, 2*60)) {
                                        timeMap.put(contentTime, null);
                                        continue;
                                    }
                                }else {
                                    if(!PrivateConfig.compareTime(cTime, 60)){
                                        timeMap.put(contentTime, null);
                                        continue;
                                    }
                                }
                            }

                            //成交数量
                            String contentChengJiao = entity.getString("content").split("成交【")[1].split("个】")[0];
                            BigDecimal countChengJiao;
                            if(contentChengJiao.contains("万")){
                                contentChengJiao = contentChengJiao.replace("万", "");
                                countChengJiao = new BigDecimal(contentChengJiao).multiply(new BigDecimal("10000"));
                            }else {
                                countChengJiao = new BigDecimal(contentChengJiao);
                            }
                            //挂单数量
                            String contentGuaDan = entity.getString("content").split("挂单【")[1].split("个】，")[0];
                            BigDecimal countGuaDan;
                            if(contentGuaDan.contains("万")){
                                contentGuaDan = contentGuaDan.replace("万", "");
                                countGuaDan = new BigDecimal(contentGuaDan).multiply(new BigDecimal("10000"));
                            }else {
                                countGuaDan = new BigDecimal(contentGuaDan);
                            }


                            BigDecimal countYiMai = new BigDecimal("0");

                            Map<String, BigDecimal> timeTemp = msgIdTimeMap.get(msgId);
                            for(Map.Entry<String, BigDecimal> entry : timeTemp.entrySet()){
                                countYiMai = countYiMai.add(entry.getValue());
                            }
                            count = countChengJiao.subtract(countYiMai);

                            /*if(countGuaDan.compareTo(countChengJiao) == 0){
                                //相等时，成交数量减去之前已经购买的数量
                                Map<String, BigDecimal> timeTemp = msgIdTimeMap.get(msgId);
                                for(Map.Entry<String, BigDecimal> entry : timeTemp.entrySet()){
                                    countYiMai = countYiMai.add(entry.getValue());
                                }
                                count = countChengJiao.subtract(countYiMai);
                            }else {
                                //成交数量和挂单数量不等，已成交为主
                                count = countChengJiao;
                            }*/

                        }else {
                            //卖的时候按照提交数量
                            String contentGuaDan = entity.getString("content").split("挂单【")[1].split("个】，")[0];
                            if(contentGuaDan.contains("万")){
                                contentGuaDan = contentGuaDan.replace("万", "");
                                count = new BigDecimal(contentGuaDan).multiply(new BigDecimal("10000"));
                            }else {
                                count = new BigDecimal(contentGuaDan);
                            }
                        }
                    }

                    timeMap.put(contentTime, count);


                    Order order = new Order();
                    order.setOrigQty(count);
                    order.setSide(side);
                    order.setSymbol(symbol);
                    order.setPositionSide(positionSide);
                    orderListNew.add(order);
                    if(entityLog != null){
                        PrivateConfig.printLog(entityLog.toJSONString());
                    }
                }


                if (!orderListNew.isEmpty()) {
                    //下单
                    for (JSONObject person : PrivateConfig.biCoin_personInfoList) {

                        try {
                            for (Order order1 : orderListNew) {
                                MulPostOrders mulGetAllOrders = new MulPostOrders(person, order1);
                                threadPoolExecutor.submit(mulGetAllOrders);//启动一般的线程
                                PrivateConfig.printLog(order1.toString());
                                //两个账号之间执行的间隔
                                Thread.sleep(50);

                            }

                        } catch (Exception e) {
                            PrivateConfig.printLog(e.getMessage());
                            T5.searchAll("连续三次，有问题，关闭软件，重新启动33。" + e.getMessage());
                        }
                    }
                }


            } catch (Exception e) {
                countErr++;
                PrivateConfig.printLog(e.getMessage());
            }
        }
    }

}
