package com.example.bian.ok;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import com.example.bian.client.model.trade.Order;
import com.example.bian.genDan.MulPostOrders;
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

public class genOK {

    public static void main(String[] args) throws InterruptedException {


        args = new String[2];
        args[0] = "E://code//biance";
        args[1] = "0-genOK";
        PrivateConfig.init((args[0]));
        PrivateConfig.before(args[0], args[1]);
        PrivateConfig.printLog("开始啦");

        // 对https也开启代理

        System.out.println("开代理");
        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10819");


        PrivateConfig.getJGXsw();
        PrivateConfig.xsw(true);

        genOK genDan = new genOK();
        genDan.method();


    }


    public void method() throws InterruptedException {
        PrivateConfig.printLog("OK 开始时间为：" + PrivateConfig.getCurrentTime());

        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(5, 5, 10,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.DiscardPolicy());
        PrivateConfig.threadPoolExecutor = threadPoolExecutor;

        //启动监控线程
        Callable callable1 = new Callable() {
            @Override
            public String call() throws Exception {
                JianKong4 jianKong4 = new JianKong4();
                jianKong4.method(null, threadPoolExecutor, PrivateConfig.ok_personInfoList);
                return "";
            }
        };
        threadPoolExecutor.submit(callable1);

        int countErr = 0;
        //存储已经做过的单子
        //当一个挂单，分多次成交时，msgId会相同，此时需借助时间判断是否做过
        /* 例如
        1秒挂单【37.8万个】，成交【2.17万个】
        2秒挂单【37.8万个】，成交【3.8万个】
        3秒挂单【37.8万个】，成交【37.8万个】
         */
        Set<String> msgIdSet = new HashSet<>();

        //启动时，不跟单
        JSONArray oldOrders = GetOKPositions.getOrders(threadPoolExecutor, ok_genPortfolioId, false, false);
        for (Object entryNew : oldOrders) {
            JSONObject entity = (JSONObject) entryNew;
            String msgId = entity.getString("ordId");
            if (msgIdSet.contains(msgId)) {
                continue;
            }
            msgIdSet.add(msgId);
        }
        //想跟哪个单子就移除掉，移除几个跟几个
        if (StringUtils.isNotBlank(ok_remove)) {
            String[] removes = StringUtils.split(ok_remove, ",");
            for (String remove : removes) {
                msgIdSet.remove(remove);
            }
        }

        int countCycle = 0;
        int diaoYongCount = 0;
        long timeShangCi = System.currentTimeMillis();
        Map<Long, Integer> shiJianMap = new TreeMap<>();
        PrivateConfig.printLogJianKong();
        while (true) {
            try {
                diaoYongCount++;
                if (diaoYongCount > 10 * 15) {
                    diaoYongCount = 0;
                    PrivateConfig.printLogJianKong();
                }
                countCycle++;
                if (countCycle > 30 * 5) {
                    countCycle = 0;
                    PrivateConfig.printLog("ok" + PrivateConfig.getCurrentTime());

                    for (Map.Entry<Long, Integer> entry : shiJianMap.entrySet()) {
                        PrivateConfig.printLog(entry.getKey() + "：" + entry.getValue());
                    }
                }
                Thread.sleep(Long.parseLong(PrivateConfig.shiJian));
                if (countErr > 20) {
                    //调用出错了，清仓
                    T5.searchAll("ok，清仓了");
                    PrivateConfig.printLog("ok，清仓了");
                    QingCang3 qingCang = new QingCang3();
                    qingCang.qingCang(ok_personInfoList, null, null);
                    return;
                }
                JSONArray newOrders = GetOKPositions.getOrders(threadPoolExecutor, ok_genPortfolioId, false, false);
//                PrivateConfig.printLog(newOrders.toJSONString());
                if (CollectionUtils.isEmpty(newOrders)) {
                    countErr++;
                    T5.searchAll("ok，获取不到订单了");
                    Thread.sleep(1000 * 60);
                    continue;
                } else {
                    countErr = 0;
                    Long shiJianCha = (System.currentTimeMillis() - timeShangCi) / 1000;
                    timeShangCi = System.currentTimeMillis();
                    if (shiJianMap.containsKey(shiJianCha)) {
                        shiJianMap.put(shiJianCha, shiJianMap.get(shiJianCha) + 1);
                    } else {
                        shiJianMap.put(shiJianCha, 1);
                    }
                }

                List<Order> orderListNew = new ArrayList<>();
                JSONObject entityLog = null;
                for (Object entryNew : newOrders) {
                    JSONObject entity = (JSONObject) entryNew;
                    if(diaoYongCount == 1){
                        PrivateConfig.printLog(entity.toJSONString());
                    }
                    String msgId = entity.getString("ordId");
                    Long fillTime = entity.getLong("fillTime");
                    if (msgIdSet.contains(msgId)) {
                        continue;
                    }{
                        msgIdSet.add(msgId);
                    }

                    entityLog = entity;
                    String symbol = entity.getString("baseName") + "USDT";
                    String positionSide = entity.getString("posSide").toUpperCase();
                    String side = entity.getString("side").toUpperCase();

                    //如果时间过去了1分钟，通知一下，看看什么原因，也统计一下这种情况是否多
                    if (!PrivateConfig.compareTime(fillTime, 60)) {
                        PrivateConfig.printLog(entityLog.toJSONString());
                        PrivateConfig.printLog(PrivateConfig.getCurrentTime());
                        T5.sendMe(symbol + "，时间：" + (System.currentTimeMillis() - fillTime) / 1000);
                    }

                    //如果是手动想买的单子，就不判断时间了
                    if (!ok_remove.contains(msgId)) {
                        //买的时候，如果时间过去了1分钟，就不跟了
                        if (PrivateConfig.isMai(side, positionSide, null)) {
                            if (!PrivateConfig.compareTime(fillTime, 60)) {
                                continue;
                            }
                        }
                    }
                    //成交数量
                    BigDecimal value = entity.getBigDecimal("value");
                    BigDecimal avgPx = entity.getBigDecimal("avgPx");
                    BigDecimal count = value.divide(avgPx, 4, BigDecimal.ROUND_HALF_UP);

                    Order order = new Order();
                    order.setOrigQty(count);
                    order.setSide(side);
                    order.setSymbol(symbol);
                    order.setPositionSide(positionSide);
                    orderListNew.add(order);
                    if (entityLog != null) {
                        PrivateConfig.printLog(entityLog.toJSONString());
                    }
                }


                if (!orderListNew.isEmpty()) {
                    //下单
                    for (JSONObject person : ok_personInfoList) {

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
