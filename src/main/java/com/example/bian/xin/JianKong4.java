package com.example.bian.xin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.ChangeInitialLeverage;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import com.example.bian.client.model.enums.*;
import com.example.bian.client.model.trade.AccountInformation;
import com.example.bian.client.model.trade.Order;
import com.example.bian.client.model.trade.Position;
import com.example.bian.genDan.*;
import com.example.bian.ok.*;
import com.example.bian.ok.Constants;
import com.example.bian.xin1.GenTian5;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import static com.example.bian.client.bushu.PrivateConfig.*;

/**
 * 根据老师现有的持仓，自己分析他是加仓还是减仓
 */
public class JianKong4 {

    static Boolean isBaoJing = false;

    public static void main(String[] args) throws IOException, InterruptedException {

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
        args[1] = "jianKong";
        PrivateConfig.before(args[0], args[1]);
        PrivateConfig.getJGXsw();
        PrivateConfig.xsw(true);

//        PrivateConfig.fileLog.setLastModified(System.currentTimeMillis());

        genDans_genPortfolioId = "5090588047188778241";

        JianKong4 jianKong4 = new JianKong4();
        jianKong4.method(args, threadPoolExecutor, PrivateConfig.genDans_personInfoList);

    }


    JSONObject tianXH;
    // 币种的清仓次数
    Map<String, Integer> symbolMapQingCang = new HashMap<>();
    //币种的错误次数
    Map<String, Integer> symbolMapYouCuo = new HashMap<>();
    static Map<String, LogObject> logMap = new HashMap<>();//最多发10次邮件，2天过期


    BigDecimal ling02 = new BigDecimal("0.2");
    BigDecimal ling = new BigDecimal("0");
    public static boolean errorCookie = false;//获取老师订单时，如果有问题，不能立马清空
    public static int errorCount = 0;

    public static boolean needCheck = true;
    public static int checkCount = 0;

    public void method(String[] args, ThreadPoolExecutor threadPoolExecutor, List<JSONObject> personInfoList) throws InterruptedException {
        try {
            PrivateConfig.printLog("跟单开启监控：" + PrivateConfig.getCurrentTime());
            //休息一下，等待主线程完成后，再监控
            if(PrivateConfig.ceShi.equals("1")){
                Thread.sleep(3 * 1000);
            }else {
                Thread.sleep(30 * 1000);
            }

            /*if (PrivateConfig.genDan_isGenDan == 1) {
                //添加现有持仓
                for (int i = 0; i < 5; i++) {
                    String s = getPosition(threadPoolExecutor);
                    if (StringUtils.isNotBlank(s)) {
                        JSONObject jsonObject = JSON.parseObject(s);
                        if ("000000".equals(jsonObject.getString("code"))) {
                            JSONArray jsonArrayNow = jsonObject.getJSONArray("data");
                            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(jsonArrayNow)) {
                                for (Object o1 : jsonArrayNow) {
                                    JSONObject position = (JSONObject) o1;
                                    if (position.getBigDecimal(PrivateConfig.positionAmount).abs().compareTo(PrivateConfig.ling) > 0) {
                                        Position positionNew = new Position();
                                        positionNew.setSymbol(position.getString(PrivateConfig.symbol));

                                        //#2
                                        String positionSide = position.getString(PrivateConfig.positionSide);
                                        if (positionSide.equals(PositionSide.BOTH.toString())) {
                                            //单向持仓时，转换为双向
                                            if (position.getBigDecimal(PrivateConfig.positionAmount).compareTo(ling) > 0) {
                                                positionSide = PositionSide.LONG.toString();
                                            } else {
                                                positionSide = PositionSide.SHORT.toString();
                                            }
                                        }
                                        positionNew.setPositionSide(positionSide);

                                        positionNew.setPositionAmt(position.getBigDecimal(PrivateConfig.positionAmount));
                                        positionNew.setTime(System.currentTimeMillis());
//                                        genDan.youCount.put(position.getString(PrivateConfig.symbol) + "_" + positionNew.getPositionSide(), positionNew);//#1双向
                                    }
                                }
                                break;
                            }
                        }
                    }
                    PrivateConfig.printLog("cookie过期");
                    Thread.sleep(2000);
//                return;
                }
            }*/

            //设置止损价
            /*for (JSONObject personInfo : personInfoList) {
                SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient));
                BigDecimal newMoney = syncRequestClient.getAccountInformation().getTotalMarginBalance();
                personInfo.put(PrivateConfig.con_zhisun, newMoney);
                Thread.sleep(1000);
            }*/

            int o = 0;
            int h = 250;
            int j = 0;
            int g = 0;
            int k = 0;
            double lastTime0 = System.currentTimeMillis();
            while (true) {
                try {

                    PrivateConfig.xsw(false);
                    PrivateConfig.printLog("ok的position：" + ok_position);
                    if (needCheck) {
                        //下单后，隔10秒再检查
                        if(PrivateConfig.ceShi.equals("1")){
                            Thread.sleep(3 * 1000);
                        }else {
                            Thread.sleep(Long.parseLong(PrivateConfig.shiJian) * 3);
                        }
                        //1分钟一次
                        //为什么把监控间隔放入方法里面？如果有错的话，应该立马再次确认，而不是1分钟之后再检查，如果放在外边可能是要等了
                        isError(threadPoolExecutor, personInfoList);
                    }

                    o++;
                    /*if(o>=PrivateConfig.zhiSunTime) {
                        //及时止损
                        o = 0;
                        if (!PrivateConfig.isZhiSun) {
                            for (JSONObject personInfo : personInfoList) {
                                SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient));
                                AccountInformation accountInformation = PrivateConfig.getAccountInformation(syncRequestClient, threadPoolExecutor);
                                if("0".equals(personInfo.getString("zhiSun"))){
                                    continue;
                                }
                                //及时止损
                                BigDecimal zuiGaoJia = personInfo.getBigDecimal(PrivateConfig.con_zhisun);
                                BigDecimal newJia = accountInformation.getTotalMarginBalance();
                                System.out.println(PrivateConfig.getCurrentTime() + personInfo.getString(PrivateConfig.alias) + "的最高价：" + zuiGaoJia + "，现价：" + newJia);
                                if (newJia.compareTo(zuiGaoJia.multiply(new BigDecimal(PrivateConfig.zhiSun))) < 0) {
                                    T5.searchAll("止损了，重大问题，抓紧报告");
                                    PrivateConfig.isZhiSun = true;
                                    Thread.sleep(5000);
                                    QingCang3 qingCang = new QingCang3();
                                    qingCang.qingCang(personInfoList, null, null);
                                    Thread.sleep(5000);
                                }
                                Thread.sleep(1000);
                            }
                        }
                    }*/

                    j++;
                    if (j > 10) {
                        j = 0;
//                        PrivateConfig.printLog(PrivateConfig.fileWriter,  "jian kong");
                        System.out.println("jian kong");
                        needCheck = true;
                        checkCount = 0;

                        //币安跟单时，及时移除盈利部分
                        /*if(PrivateConfig.genDan_isGenDan.equals("1")){
                            Map<String, BigDecimal> detailMap = JianKongTransfer.getGenDanDetail();
                            if(detailMap.get(PrivateConfig.genDan_genPortfolioId) != null){
                                BigDecimal money = new BigDecimal(PrivateConfig.genDan_money);
                                BigDecimal cha = detailMap.get(PrivateConfig.genDan_genPortfolioId).subtract(money).setScale(0, BigDecimal.ROUND_DOWN);
                                if(cha.compareTo(money.multiply(new BigDecimal("0.1"))) > 0){
                                    boolean jianCangOk = JianKongTransfer.jiaJianCang(PrivateConfig.genDan_genPortfolioId, String.valueOf(cha.longValue() * 0.89), "WITHDRAW");
                                    if (jianCangOk) {
                                        T5.sendMe("自动减仓" + cha.toString());
                                    }
                                }
                            }
                        }*/
                    }

                    /*k++;
                    if (k > 30) {
                        k = 0;

                    }*/

                    //监控缓存问题，下一个btc的单子，查看能否查到
                    h++;
                    if (h > 240) {
                        h = 0;
                        if (PrivateConfig.genTian.equals("1")) {
                            aVoid(personInfoList);

                            //设置一下倍数
                            PrivateConfig.setBeiShu(threadPoolExecutor);
                        }
//                        PrivateConfig.printLog(PrivateConfig.fileWriter,  "huan cun");
                        System.out.println("huan cun");

                    }

                    /*k++;
                    if (k > 300) {
                        k = 0;
                        double lastTime1 = System.currentTimeMillis();
                        double xiaoShi = (lastTime1 - lastTime0) / 1000 / 60 / 60;
                        BigDecimal two11 = new BigDecimal(xiaoShi);
                        T5.searchAll("用时" + two11.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "小时，超过6小时，有问题, " + PrivateConfig.getCurrentTime() + "：正在监控");
                        lastTime0 = lastTime1;

                    }*/

                    //设置倍数
                    k++;
                    if (k > 1) {
                        k = 0;
                        PrivateConfig.setBeiShu(threadPoolExecutor, personInfoList);
                    }

                    //设置杠杆
                    g++;
                    if (g > 2880) {
//                        PrivateConfig.printLog(PrivateConfig.fileWriter,  "gang gan");
                        System.out.println("gang gan");
                        g = 0;
                        if("1".equals(changeInitialLeverage)) {
                            ChangeInitialLeverage changeInitialLeverage = new ChangeInitialLeverage();
                            changeInitialLeverage.method(personInfoList);
                        }

                        //查看客户的转入转出
                        /*Transfer transfer = new Transfer();
                        transfer.method1(personInfoList);*/

                        Iterator<Map.Entry<String, LogObject>> iterator = logMap.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<String, LogObject> entry = iterator.next();
                            if (!PrivateConfig.compareTimeDay(entry.getValue().getTime(), 2)) {
                                iterator.remove();
                            }
                        }

                        //设置止损价
                        /*for (JSONObject personInfo : personInfoList) {
                            SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient));
                            BigDecimal oldMoney = personInfo.getBigDecimal(PrivateConfig.con_zhisun);
                            BigDecimal newMoney = syncRequestClient.getAccountInformation().getTotalMarginBalance();
                            if(newMoney.compareTo(oldMoney)>0){
                                personInfo.put(PrivateConfig.con_zhisun, newMoney);
                            }else {
                                personInfo.put(PrivateConfig.con_zhisun, oldMoney);
                            }
                            Thread.sleep(5000);
                        }*/
                    }

                } catch (Exception e) {
                    Thread.sleep(1000 * 6);
                    e.printStackTrace();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                if (!needCheck) {
                    if(PrivateConfig.ceShi.equals("1")){
                        Thread.sleep(1000 * 10);
                    }else {
                        Thread.sleep(1000 * 60 * 2);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("监控程序启动出错了");
            e.printStackTrace();
        }

    }



    //防止缓存出现问题，获取不到订单时的监控
    public void aVoid(List<JSONObject> personInfoList) {

        SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfoList.get(personInfoList.size() - 1).get(PrivateConfig.syncRequestClient));
        for (int i = 0; i < 2; i++) {
            try {
                Order myOrder;
                if(GenTian5.tianBoth.equals("1")){
                    myOrder = syncRequestClient.postOrder(
                            "BTCUSDT",
                            OrderSide.valueOf("BUY"),//买还是卖
                            PositionSide.valueOf("BOTH"),//做多还是做空 long SHORT both
                            OrderType.valueOf("LIMIT"),// 订单类型，limit：限价单；MARKET：市价单（想要成功买卖，使用这个）
                            TimeInForce.valueOf("GTC"),//,//成交为止，一直有效，不用管
                            "0.2",//跟单数量，需要大于5
                            "1000",//跟单单价，总价需要大于5（市价时，可以不填）
                            "FALSE",//order.getReduceOnly().toString(),
                            null,//order.getClientOrderId(),
                            null,//order.getStopPrice().toString(),
                            null,//WorkingType.valueOf(order.getWorkingType()),
                            NewOrderRespType.RESULT);
                }else {
                    myOrder = syncRequestClient.postOrder(
                            "BTCUSDT",
                            OrderSide.valueOf("BUY"),//买还是卖
                            PositionSide.valueOf("LONG"),//做多还是做空 long SHORT both
                            OrderType.valueOf("LIMIT"),// 订单类型，LIMIT：限价单；MARKET：市价单（想要成功买卖，使用这个）
                            TimeInForce.valueOf("GTC"),//,//成交为止，一直有效，不用管
                            "0.2",//跟单数量，需要大于5
                            "1000",//跟单单价，总价需要大于5（市价时，可以不填）
                            null,//order.getReduceOnly().toString(),
                            null,//order.getClientOrderId(),
                            null,//order.getStopPrice().toString(),
                            null,//WorkingType.valueOf(order.getWorkingType()),
                            NewOrderRespType.RESULT);
                }

                Thread.sleep(3000);
                List<Order> list = syncRequestClient.getAllOrders(null, null, null, null, 1);

                if (CollectionUtils.isEmpty(list)) {
                    T5.searchAll("下单失败，超过3次，抓紧联系胡亚龙12");
                    syncRequestClient.cancelOrder(myOrder.getSymbol(), myOrder.getOrderId(), myOrder.getClientOrderId());
                    Thread.sleep(1000);
                    continue;
                } else {
                    Order order = list.get(0);
                    Long currentTime = System.currentTimeMillis();
                    if (Math.abs(currentTime - order.getUpdateTime()) > 30000L) {//订单大于10秒时，不跟
                        T5.searchAll("下单失败，超过3次，抓紧联系胡亚龙123");
                        syncRequestClient.cancelOrder(myOrder.getSymbol(), myOrder.getOrderId(), myOrder.getClientOrderId());
                        Thread.sleep(1000);
                        continue;
                    }
                    try {
                        syncRequestClient.cancelOrder(myOrder.getSymbol(), myOrder.getOrderId(), myOrder.getClientOrderId());
                    } catch (Exception e1) {
                        Thread.sleep(3000);
                        syncRequestClient.cancelOrder(myOrder.getSymbol(), myOrder.getOrderId(), myOrder.getClientOrderId());
                    }
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void isError(ThreadPoolExecutor threadPoolExecutor, List<JSONObject> personInfoList) throws InterruptedException {
        try {

            System.out.println(getCurrentTime() + "正在监控");

            //key是symbol+positionSide
            Map<String, Position> mapTian = new HashMap<>();

//            if (!PrivateConfig.isZhiSun) {
            if(PrivateConfig.genDan_isGenDan.equals("1")){
                mapTian = getPositions(threadPoolExecutor, genDan_genPortfolioId);

            }else if(PrivateConfig.genDans_isGenDans.equals("1")){
                mapTian = getPositions(threadPoolExecutor, genDans_genPortfolioId);

            } else if(PrivateConfig.ok_isOk.equals("1")){
                mapTian = getOKPositions(threadPoolExecutor);
            }else {
                SyncRequestClient syncRequestClientTian = ((SyncRequestClient) PrivateConfig.standard.get(PrivateConfig.syncRequestClient));
                AccountInformation accountInformationTian = PrivateConfig.getAccountInformation(syncRequestClientTian, threadPoolExecutor);
                List<Position> positionListTian = accountInformationTian.getPositions();
                for (Position position : positionListTian) {
                    if (position.getPositionAmt().abs().compareTo(ling) > 0) {
                        position.setPositionSide(PrivateConfig.getPositionSide(position.getPositionSide(), position.getPositionAmt()));
                        mapTian.put(position.getSymbol() + "_" + position.getPositionSide(), position);//#1双向
                    }
                }
            }

            if(!mapTian.isEmpty()){
                PrivateConfig.gangGanJianKong(personInfoList, mapTian);
            }
//            }

            //#1双向
            /*Map<String, Position> mapTian = new HashMap<>();
            for(Map.Entry<String, Position> s : mapTian1.entrySet()){
                mapTian.put(s.getKey().split("_")[0], s.getValue());
            }*/

            for (JSONObject personInfo : personInfoList) {
                SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient));

                AccountInformation accountInformation = PrivateConfig.getAccountInformation(syncRequestClient, threadPoolExecutor);

                List<Position> positionList = accountInformation.getPositions();

                if (mapTian.isEmpty()) {
                    //老师没有持仓，我们有持仓时报错
                    for (Position position : positionList) {
                        String symbolSide = position.getSymbol() + "_" + position.getPositionSide();
                        if (position.getPositionAmt().abs().compareTo(ling) > 0) {
                            if (!hasErrorQingCang(symbolSide)) {
                                Thread.sleep(1000);
                                return;
                            }

                            // 先自动平仓解决
                            QingCang3 qingCang = new QingCang3();
                            qingCang.qingCang(personInfoList, position.getSymbol(), null);

                            if (position.getMaintMargin() != null) {
                                if (position.getMaintMargin().compareTo(new BigDecimal("0.5")) < 0) {
                                    // 如果保证金太少，证明是计算误差，清了就行啦，不用报警了
                                    return;
                                }
                            }

                            String msg = "连续三次，有问题，联系他手动平仓1，" + personInfo.getString(PrivateConfig.alias) + "，老师没有" + symbolSide;
                            PrivateConfig.printLog(msg);
                            PrivateConfig.printLog(position.toString());
                            T5.searchAll(msg);
                            //有错的话，报警后就return
                            Thread.sleep(1000 * 20);
                            return;
                        }
                    }
                    String msg = personInfo.getString(PrivateConfig.alias) + "和老师都没有持仓。没有问题！";
                    System.out.println(msg);
                } else {

                    // 如果和老师都有，并且symbol不同时，平仓
                    for (Position position : positionList) {
                        BigDecimal you = position.getPositionAmt().abs();
                        String symbolSide = position.getSymbol() + "_" + position.getPositionSide();
                        if (you.abs().compareTo(ling) > 0) {
                            if (!mapTian.containsKey(symbolSide)) {
                                if (!hasErrorQingCang(symbolSide)) {
                                    Thread.sleep(1000);
                                    return;
                                }
                                // 先自动平仓解决
                                QingCang3 qingCang = new QingCang3();
                                qingCang.qingCang(personInfoList, position.getSymbol(), position.getPositionSide());

                                if (position.getMaintMargin() != null) {
                                    if (position.getMaintMargin().compareTo(new BigDecimal("0.5")) < 0) {
                                        // 如果保证金太少，证明是计算误差，清了就行啦，不用报警了
                                        return;
                                    }
                                }

//                                PrivateConfig.printLog(position.toString());
                                String msg = "连续三次，有问题，联系他手动平仓2" + personInfo.getString(PrivateConfig.alias) + "，老师没有" + symbolSide;
//                                PrivateConfig.printLog(msg);
                                T5.searchAll(msg);
                                //有错的话，报警后就return
                                Thread.sleep(1000 * 20);
                                return;
                            }
                        }
                    }

                    BigDecimal beiShu = new BigDecimal(personInfo.getString(PrivateConfig.beiShu));
                    //只有倍数大时才校验个数是否正确
                    if (beiShu.compareTo(PrivateConfig.ling035) < 0) {
                        String msg = personInfo.getString(PrivateConfig.alias) + "的和老师相同，没有问题！";
                        PrivateConfig.printLog(msg);
                        continue;
                    }

                    for (Map.Entry<String, Position> entryLs : mapTian.entrySet()) {
                        String symbolSideLs = entryLs.getKey();
                        String symbolLs = entryLs.getKey().split("_")[0];
                        if (PrivateConfig.getXSM(symbolLs) <= 0.1) {
                            //小数位为0
                            ling02 = new BigDecimal("0.4");

                        }
                        //基准的数量除以倍数就是老师的数量
//                        BigDecimal youLs = entryLs.getValue().getPositionAmt().divide(beiShuMy, 5, BigDecimal.ROUND_HALF_UP).abs();
                        BigDecimal youLs = entryLs.getValue().getPositionAmt().abs();
                        Boolean hasProblem = true;
                        for (Position position : positionList) {
                            String symbolSide = position.getSymbol() + "_" + position.getPositionSide();
                            String symbol = position.getSymbol();
                            BigDecimal you = position.getPositionAmt().abs();
                            if (symbolSideLs.equals(symbolSide) && you.abs().compareTo(ling) > 0) {
                                BigDecimal youXueSheng = you.divide(beiShu, 5, BigDecimal.ROUND_HALF_UP);

                                //如果方向和老师的不同，上面已经清过仓了，这里不会有了
                                /*if (!entryLs.getValue().getPositionSide().toLowerCase().toString().equals(position.getPositionSide().toLowerCase().toString())) {
                                    if (!hasError(position.getSymbol())) {
                                        Thread.sleep(1000);
                                        return;
                                    }
                                    String msg = "连续三次，有问题" + personInfo.getString(PrivateConfig.alias) + "，" + position.getSymbol() + "和老师方向做反了。关闭软件，重新启动";
                                    PrivateConfig.printLog(msg);
//                                    PrivateConfig.printLog(PrivateConfig.fileWriter, msg);
                                    T5.searchAll(msg);
                                    //有错的话，报警后就return
                                    Thread.sleep(1000 * 20);
                                    return;
                                } else {*/
                                if (youLs.subtract(youXueSheng).divide(youLs, 5, BigDecimal.ROUND_HALF_UP).abs().compareTo(ling02) > 0 &&
                                        youXueSheng.subtract(youLs).divide(youXueSheng, 5, BigDecimal.ROUND_HALF_UP).abs().compareTo(ling02) > 0) {
                                    if (PrivateConfig.getXSM(symbol) <= 0.1 && youLs.multiply(beiShu).compareTo(new BigDecimal("5")) < 0) {
                                        //小数位为0，且应该有的个数小于5时，不校验
                                        hasProblem = false;
                                        String msg = personInfo.getString(PrivateConfig.alias) + "的" + symbolSideLs + "和老师相同，没有问题！";
                                        PrivateConfig.printLog(msg);
                                        continue;
                                    }
                                    if ("1".equals(PrivateConfig.genTian)) {
                                        //跟田时，田的多是正常的
                                        if (youLs.compareTo(youXueSheng) > 0) {
                                            if (!hasError(symbolSide)) {
                                                Thread.sleep(10000);
                                                return;
                                            }
                                            String msg = personInfo.getString(PrivateConfig.alias) + "的" + position.getSymbol() + "没有问题！此时田的多，我们的可能已经挂单卖了";
//                                                Thread.sleep(1000 * 30);
                                            System.out.println(msg);
                                            hasProblem = false;
//                                                PrivateConfig.printLog(PrivateConfig.fileWriter, msg);
                                            continue;
                                        }
                                    }
                                    if (!hasError(symbolSide)) {
                                        Thread.sleep(1000);
                                        return;
                                    }

                                    String msg = "连续2次，有问题！" + personInfo.getString(PrivateConfig.alias) + "，" + position.getSymbol() + "和老师个数不同，应该有" + youLs.multiply(beiShu).setScale(getXSM(entryLs.getValue().getSymbol()), RoundingMode.HALF_DOWN) + "个，现在有" + you + "个";

                                    //先自动减仓
                                    if (you.compareTo(youLs.multiply(beiShu)) > 0) {
                                        if ("1".equals(jianCang)) {
                                            BigDecimal jianCangCount = you.subtract(youLs.multiply(beiShu)).setScale(PrivateConfig.getXSM(position.getSymbol()), BigDecimal.ROUND_HALF_UP);
                                            /*String buy = "BUY";
                                            if(position.getPositionAmt().compareTo(ling) > 0){
                                                buy = OrderSide.SELL.toString();
                                            }*/
                                            PrivateConfig.jianCang(syncRequestClient, position.getSymbol(), position.getPositionSide(), jianCangCount.toString(), position.getPositionAmt());
//                                                postOrder(syncRequestClient, position.getSymbol(), buy, "TRUE", mai.toString());

                                            //自动减仓是一个人一个人的减
                                            PrivateConfig.printLog(msg);
//                                            PrivateConfig.printLog(PrivateConfig.fileWriter, msg);
                                            sendEmail1(logMap, msg);
                                            continue;
                                        }
                                    }else {
                                        if ("1".equals(buCang)) {
                                            Integer jiaCangCount = personJiaCangMap.get(personInfo.getString(name));
                                            if (jiaCangCount == null) {
                                                jiaCangCount = 0;
                                            }
                                            if (jiaCangCount < 3) {//等稳定后，这个次数可以放大
                                                jiaCangCount++;
                                                personJiaCangMap.put(personInfo.getString(name), jiaCangCount);
                                                //自动加仓，向下取整，肯定不能加多了
                                                BigDecimal jianCangCount = youLs.multiply(beiShu).subtract(you).setScale(PrivateConfig.getXSM(position.getSymbol()), BigDecimal.ROUND_DOWN);
                                                PrivateConfig.jiaCang(syncRequestClient, position.getSymbol(), position.getPositionSide(), jianCangCount.toString(), position.getPositionAmt());
                                            }

                                            //自动减仓是一个人一个人的减
                                            PrivateConfig.printLog(msg);
                                            sendEmail1(logMap, msg);
                                            continue;
                                        }
                                    }

                                    PrivateConfig.printLog(msg);
                                    sendEmail1(logMap, msg);
//                                        PrivateConfig.printLog(PrivateConfig.fileWriter, msg);

                                    hasProblem = false;
                                    //有错的话，报警后就return
                                    Thread.sleep(1000 * 20);
                                    continue;
                                } else {
                                    //和老师相同就退出
                                    hasProblem = false;
                                    PrivateConfig.printLog(personInfo.getString(PrivateConfig.alias) + "的" + symbolSideLs + "和老师相同，没有问题!");
                                }
//                                }
                            } else if (symbolSideLs.equals(symbolSide) && you.abs().compareTo(ling) == 0) {
                                if (!hasError(symbolSideLs)) {
                                    Thread.sleep(1000);
                                    return;
                                }
                                hasProblem = false;
                                String msg = personInfo.getString(PrivateConfig.alias) + "的" + symbolSideLs + "没有，而老师有，可能是我们的挂单卖了，没有问题!";
                                System.out.println(msg);
                                sendEmail1(logMap, msg);
                            }
                        }
                        if (hasProblem) {
                            if (PrivateConfig.getXSM(symbolLs) <= 0.1 && youLs.multiply(beiShu).compareTo(new BigDecimal("5")) < 0) {
                                //小数位为0，且应该有的个数小于5时，不校验
                                hasProblem = false;
                                String msg = personInfo.getString(PrivateConfig.alias) + "的" + symbolSideLs + "和老师相同，没有问题！";
                                PrivateConfig.printLog(msg);
                                continue;
                            }

                            if (!hasError(symbolSideLs)) {
                                Thread.sleep(1000);
                                return;
                            }
                            if ("1".equals(PrivateConfig.genTian)) {
                                String msg = personInfo.getString(PrivateConfig.alias) + "没有问题！";
                                PrivateConfig.printLog(msg);
                                continue;
                            }
                            String msg = personInfo.getString(PrivateConfig.alias) + "应该有" + youLs.multiply(beiShu).setScale(getXSM(entryLs.getValue().getSymbol()), RoundingMode.HALF_DOWN) + "个" + symbolSideLs + "，现在没有。有问题！";
                            PrivateConfig.printLog(msg);
                            sendEmail1(logMap, msg);
                            //有错的话，报警后就return
                            /*Thread.sleep(1000 * 20);
                            continue;*/
                        }
                    }
                }
                //学员之间的间隔
                Thread.sleep(3000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        // 能够走到这里，证明没有错
        symbolMapQingCang.clear();
        symbolMapYouCuo.clear();

        checkCount++;
        PrivateConfig.printLog("经过1次：" + checkCount);

        if (checkCount > 1) {
            needCheck = false;
            checkCount = 0;
        }
        //监控间隔
        if(PrivateConfig.ceShi.equals("1")){
            Thread.sleep(1000*10);
        }else {
            Thread.sleep(1000 * 60);
        }
    }

    public static void sendEmail1(Map<String, LogObject> logMap, String msg) {
        if (logMap.containsKey(msg)) {
            if (logMap.get(msg).getCount() < 4) {
                logMap.get(msg).setCount(logMap.get(msg).getCount() + 1);
            }
        } else {
            LogObject logObject = new LogObject();
            logObject.setTime(System.currentTimeMillis());
            logObject.setCount(1);
            logMap.put(msg, logObject);
        }

        if (logMap.get(msg).getCount() < 3) {
            T5.searchAll(msg);
        }
    }

    /*public static Map<String, Position> getPositions(ThreadPoolExecutor threadPoolExecutor, String genPortfolioId) throws InterruptedException {
        HashMap positionMap = new HashMap();
        int error121 = 0;
        for (int i = 0; i < 5; i++) {
            String s = GetPositions.getOrder(threadPoolExecutor, genPortfolioId);
            if (StringUtils.isNotBlank(s)) {
                JSONObject jsonObject = JSON.parseObject(s);
                if ("000000".equals(jsonObject.getString("code"))) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(jsonArray)) {
                        for (Object o1 : jsonArray) {
                            JSONObject position = (JSONObject) o1;
                            if (position.getBigDecimal(PrivateConfig.positionAmount).abs().compareTo(PrivateConfig.ling) > 0) {
                                Position positionNew = new Position();
                                positionNew.setSymbol(position.getString(PrivateConfig.symbol));

                                //#2
                                *//*String positionSide = position.getString(PrivateConfig.positionSide);
                                if(positionSide.equals(PositionSide.BOTH.toString())) {
                                    //单向持仓时，转换为双向
                                    if (position.getBigDecimal(PrivateConfig.positionAmount).compareTo(PrivateConfig.ling) > 0) {
                                        positionSide = PositionSide.LONG.toString();
                                    } else {
                                        positionSide = PositionSide.SHORT.toString();
                                    }
                                }*//*

                                positionNew.setPositionSide(PrivateConfig.getPositionSide(position.getString(PrivateConfig.positionSide), position.getBigDecimal(PrivateConfig.positionAmount)));
                                positionNew.setPositionAmt(position.getBigDecimal(PrivateConfig.positionAmount));
                                positionMap.put(positionNew.getSymbol() + "_" + positionNew.getPositionSide(), positionNew);//#1双向
                            }
                        }
                        return positionMap;
                    }
                }else {
                    T5.sendMe("抓紧联系我，" + jsonObject.getString("message"));
                    if(error121>2){
                        Thread.sleep(1000 * 60 * 10);
                    }
                    error121++;
                }
            }
            Thread.sleep(10000);
        }
        return new HashMap<>();
    }*/

    public static Map<String, Position> getPositions(ThreadPoolExecutor threadPoolExecutor, String genPortfolioId) throws InterruptedException {
        HashMap positionMap = new HashMap();
        JSONArray jsonArray = GetPositions.getOrders(threadPoolExecutor, genPortfolioId);
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(jsonArray)) {
            for (Object o1 : jsonArray) {
                JSONObject position = (JSONObject) o1;
                if (position.getBigDecimal(PrivateConfig.positionAmount).abs().compareTo(PrivateConfig.ling) > 0) {
                    Position positionNew = new Position();
                    positionNew.setSymbol(position.getString(PrivateConfig.symbol));
                    positionNew.setPositionSide(PrivateConfig.getPositionSide(position.getString(PrivateConfig.positionSide), position.getBigDecimal(PrivateConfig.positionAmount)));
                    positionNew.setPositionAmt(position.getBigDecimal(PrivateConfig.positionAmount));
                    positionNew.setLeverage(position.getBigDecimal("leverage"));
                    positionMap.put(positionNew.getSymbol() + "_" + positionNew.getPositionSide(), positionNew);//#1双向
                }
            }
        }
        return positionMap;
    }

    public static Map<String, Position> getOKPositions(ThreadPoolExecutor threadPoolExecutor) throws InterruptedException {
        JSONArray jsonArray = GetOKPositions.getOrders(threadPoolExecutor, PrivateConfig.ok_genPortfolioId, true, true);
        return PrivateConfig.buildPositionMap(jsonArray);
    }

    /**
     * 现有持仓
     * @param threadPoolExecutor
     * @return
     * @throws InterruptedException
     */
    public static String getPosition(ThreadPoolExecutor threadPoolExecutor) throws InterruptedException {

        //查询所有做过的symbol，不等于0的就是现在持仓的
        Callable callable = new Callable() {
            @Override
            public String call() throws Exception {
                return Postman.sendGet("https://" + PrivateConfig.genDan_url + "/bapi/futures/v1/friendly/future/copy-trade/lead-data/positions?portfolioId=" + PrivateConfig.genDan_portfolioId, PrivateConfig.genDan_cookie, PrivateConfig.genDan_token);
            }
        };
        int h = 0;
        int h10 = 0;
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
                    T5.searchAll("币安positions，连续3次，有问题！");
                    h10++;
                }
                if(h10>10){
                    return "错误太多了，立马报警3";
                }
            }
        }
    }





    public static String getHistoryFlag(JSONObject trade){//#1双向
        String flag = trade.getString(PrivateConfig.symbol) +
                "_" + trade.getString(PrivateConfig.side).toUpperCase() +
                "--" + trade.getString("closed")+
                "_" + trade.getString("status");
//                "_" + trade.getString("updateTime");
        return flag;
    }

    static int positionHistoryError = 0;
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
                    positionHistoryError = 0;
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray jsonArray = data.getJSONArray("list");
                    if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(jsonArray)) {
                        return jsonArray;
                    }
                } else {

                    T5.searchAll("position-history，连续3次，有问题！2");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        positionHistoryError++;
        if(positionHistoryError>3){
            //获取不到历史持仓时，证明程序出问题了，这时候果断清仓
            PrivateConfig.printLog("币安跟单-获取持仓有问题了，主动清仓1");
            T5.searchAll("position-history，主动清仓了，重要问题");
        }else {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            getPositionHistorys(threadPoolExecutor, pageNumber, pageSize);
        }
        return new JSONArray();
    }

    public static String getPositionHistory(ThreadPoolExecutor threadPoolExecutor, int pageNumber, int pageSize) throws InterruptedException {

        //查询所有类型的订单 可以根据symbol 查询是比特币的 还是其他币的
        Callable callable = new Callable() {
            @Override
            public String call() throws Exception {
                return Postman.sendPost("https://" + PrivateConfig.genDan_url + "/bapi/futures/v1/friendly/future/copy-trade/lead-portfolio/position-history",
                        "{\"pageNumber\":" + pageNumber + ",\"pageSize\":" + pageSize + ",\"portfolioId\":\"" + PrivateConfig.genDan_portfolioId + "\"}", PrivateConfig.genDan_cookie, PrivateConfig.genDan_token);
            }
        };
        int h = 0;
        int h10 = 0;
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
                    h10++;
                }
                if(h10>10){
                    return "错误太多了，立马报警2";
                }
            }
        }
    }


    /**
     * 第三次检查还有错，证明是真的有错了
     *
     * @param symbolLs
     * @return
     */
    public Boolean hasError(String symbolLs) {
        if (symbolMapYouCuo.get(symbolLs) == null) {
            symbolMapYouCuo.put(symbolLs, 0);
        }
        symbolMapYouCuo.put(symbolLs, symbolMapYouCuo.get(symbolLs) + 1);
        if (symbolMapYouCuo.get(symbolLs) < 3) {
            return false;
        }
        symbolMapYouCuo.remove(symbolLs);
        return true;
    }

    /**
     * 第三次检查还有错，证明是真的有错了
     *
     * @param symbolLs
     * @return
     */
    public Boolean hasErrorQingCang(String symbolLs) {
        if (symbolMapQingCang.get(symbolLs) == null) {
            symbolMapQingCang.put(symbolLs, 0);
        }
        symbolMapQingCang.put(symbolLs, symbolMapQingCang.get(symbolLs) + 1);
        if (symbolMapQingCang.get(symbolLs) < 3) {
            return false;
        }
        symbolMapQingCang.remove(symbolLs);
        return true;
    }

    public String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis())); // 时间戳转换日期
    }

}

