package com.example.bian;

import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import com.example.bian.client.model.enums.NewOrderRespType;
import com.example.bian.client.model.enums.OrderSide;
import com.example.bian.client.model.enums.OrderType;
import com.example.bian.client.model.enums.PositionSide;
import com.example.bian.client.model.trade.AccountInformation;
import com.example.bian.client.model.trade.Order;
import com.example.bian.client.model.trade.Position;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据老师现有的持仓，自己分析他是加仓还是减仓
 */
public class JianKong5BeiFen0301 {

    static Boolean isBaoJing = false;

    public static void main(String[] args) throws IOException, InterruptedException {

        System.setProperty("https.proxySet", "true");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyPort", "10819");


        args = new String[1];
        System.out.println("开始啦");
        args[0] = "E://baidutongbu//baidutongbu//tongbu//bian//bian";

        JianKong5BeiFen0301 genDan = new JianKong5BeiFen0301();
        genDan.method(args);

    }

    List<JSONObject> listPersonInfo;
    JSONObject tianXH;
    // 币种的清仓次数
    Map<String, Integer> symbolMapQingCang = new HashMap<>();
    //币种的错误次数
    Map<String, Integer> symbolMapYouCuo = new HashMap<>();

    BigDecimal ling02 = new BigDecimal("0.2");
    BigDecimal ling = new BigDecimal("0");
    int error = 1;//记录api超时的次数，连续超过3次，报警

    public void method(String[] args) throws InterruptedException {
        try {
            PrivateConfig.getListNew(args[0] + "//info.json");
            listPersonInfo = PrivateConfig.personInfoList;
            /*for (Map.Entry<String, Integer> entry : PrivateConfig.getXsw().entrySet()) {
                symbolMapQingCang.put(entry.getKey(), 0);
                symbolMapYouCuo.put(entry.getKey(), 0);
            }*/

            while (true) {
                try {
                    isError();
                } catch (Exception e) {
                    Thread.sleep(1000 * 6);
                    e.printStackTrace();
                } catch (Throwable t) {
                    t.printStackTrace();
                }

            }
        } catch (Exception e) {
            System.out.println("监控程序启动出错了");
            e.printStackTrace();
        }
    }

    public void isError() throws InterruptedException {
        try {
            System.out.println(getCurrentTime() + "正在监控");
            SyncRequestClient syncRequestClientTian = ((SyncRequestClient) PrivateConfig.standard.get(PrivateConfig.syncRequestClient));
            BigDecimal beiShuMy = new BigDecimal(PrivateConfig.standard.getString(PrivateConfig.beiShu));
            AccountInformation accountInformationTian;
            try {
                accountInformationTian = syncRequestClientTian.getAccountInformation();
            } catch (Exception e) {
                e.printStackTrace();
                error++;
                Thread.sleep(10000);
                if (error > 3) {
                    String msg = "api访问失败，有问题！如果连续三次报这个问题，抓紧联系胡亚龙解决";
                    System.out.println(msg);
                    System.out.println(e);
                    T5.searchAll(msg + e);
                    Thread.sleep(2000);
                    error = 1;
                }
                return;
            }
            List<Position> positionListTian = accountInformationTian.getPositions();
            Map<String, Position> mapTian = new HashMap<>();
            for (Position position : positionListTian) {
                if (position.getPositionAmt().abs().compareTo(ling) > 0) {
                    mapTian.put(position.getSymbol(), position);
                }
            }
            for (JSONObject personInfo : listPersonInfo) {
                SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient));
                AccountInformation accountInformation = syncRequestClient.getAccountInformation();

                List<Position> positionList = accountInformation.getPositions();

                if (mapTian.isEmpty()) {
                    Boolean hasProblem = false;
                    //老师没有持仓，我们有持仓时报错
                    for (Position position : positionList) {
                        if (position.getPositionAmt().abs().compareTo(ling) > 0) {
                            if (symbolMapQingCang.get(position.getSymbol()) == null) {
                                symbolMapQingCang.put(position.getSymbol(), 0);
                            }
                            symbolMapQingCang.put(position.getSymbol(), symbolMapQingCang.get(position.getSymbol()) + 1);
                            if (symbolMapQingCang.get(position.getSymbol()) < 3) {
                                Thread.sleep(1000);
                                return;
                            }
                            // 先自动平仓解决
                            QingCang qingCang = new QingCang();
                            qingCang.qingCang(listPersonInfo, position.getSymbol());

                            if(position.getMaintMargin().compareTo(new BigDecimal("0.5")) < 0){
                                // 如果保证金太少，证明是计算误差，清了就行啦，不用报警了
                                return;
                            }
                            System.out.println(position.toString());

                            String msg = personInfo.getString(PrivateConfig.alias) + "，老师没有" + position.getSymbol() + "。有问题！如果连续三次报这个问题，请联系他手动平仓1";
                            System.out.println(msg);
                            T5.searchAll(msg);
                            hasProblem = true;
                            //有错的话，报警后就return
                            Thread.sleep(1000 * 20);
                            return;
                        }
                    }
                    if (!hasProblem) {
                        System.out.println(personInfo.getString(PrivateConfig.alias) + "和老师都没有持仓。没有问题！");
//                                T5.sendMe("[呲牙][坏笑]" + "");
                    }
                } else {

                    // 如果和老师都有，并且symbol不同时，平仓
                    for (Position position : positionList) {
                        BigDecimal you = position.getPositionAmt().abs();
                        if (you.abs().compareTo(ling) > 0) {
                            if (!mapTian.keySet().contains(position.getSymbol())) {
                                if (symbolMapQingCang.get(position.getSymbol()) == null) {
                                    symbolMapQingCang.put(position.getSymbol(), 0);
                                }
                                symbolMapQingCang.put(position.getSymbol(), symbolMapQingCang.get(position.getSymbol()) + 1);
                                if (symbolMapQingCang.get(position.getSymbol()) < 3) {
                                    Thread.sleep(1000);
                                    return;
                                }
                                // 先自动平仓解决
                                QingCang qingCang = new QingCang();
                                qingCang.qingCang(listPersonInfo, position.getSymbol());

                                if(position.getMaintMargin().compareTo(new BigDecimal("0.5")) < 0){
                                    // 如果保证金太少，证明是计算误差，清了就行啦，不用报警了
                                    return;
                                }
                                System.out.println(position.toString());
                                String msg = personInfo.getString(PrivateConfig.alias) + "，老师没有" + position.getSymbol() + "。有问题！如果连续三次报这个问题，请联系他手动平仓2";
                                System.out.println(msg);
                                T5.searchAll(msg);
                                //有错的话，报警后就return
                                Thread.sleep(1000 * 20);
                                return;
                            }
                        }
                    }

                    BigDecimal beiShu = new BigDecimal(personInfo.getString(PrivateConfig.beiShu));
                    //只有倍数大时才校验个数是否正确
                    if(beiShu.compareTo(PrivateConfig.ling035) < 0) {
                        System.out.println(personInfo.getString(PrivateConfig.alias) + "的和老师相同，没有问题！");
                        continue;
                    }
                    for (Map.Entry<String, Position> entryLs : mapTian.entrySet()) {
                        String symbolLs = entryLs.getKey();
                        if(PrivateConfig.getXSM(symbolLs) <= 0.1){
                            //小数位为0
                            ling02 = new BigDecimal("0.4");

                        }
                        //基准的数量除以倍数就是老师的数量
                        BigDecimal youLs = entryLs.getValue().getPositionAmt().divide(beiShuMy, 5, BigDecimal.ROUND_HALF_UP).abs();
                        Boolean hasProblem = true;
                        for (Position position : positionList) {

                            BigDecimal you = position.getPositionAmt().abs();
                            if (symbolLs.equals(position.getSymbol()) && position.getPositionSide().equals(entryLs.getValue().getPositionSide()) && you.abs().compareTo(ling) > 0) {
                                BigDecimal youXueSheng = you.divide(beiShu, 5, BigDecimal.ROUND_HALF_UP);
                                if (!entryLs.getValue().getPositionSide().toLowerCase().toString().equals(position.getPositionSide().toLowerCase().toString())) {
                                    if (symbolMapYouCuo.get(position.getSymbol()) == null) {
                                        symbolMapYouCuo.put(position.getSymbol(), 0);
                                    }
                                    symbolMapYouCuo.put(position.getSymbol(), symbolMapYouCuo.get(position.getSymbol()) + 1);
                                    if (symbolMapYouCuo.get(position.getSymbol()) < 3) {
                                        Thread.sleep(1000);
                                        return;
                                    }
                                    String msg = personInfo.getString(PrivateConfig.alias) + "，" + position.getSymbol() + "和老师方向做反了。有问题！如果连续三次报这个问题，请联系胡亚龙解决";
                                    System.out.println(msg);
                                    T5.sendMe(msg);
                                    //有错的话，报警后就return
                                    Thread.sleep(1000 * 20);
                                    return;
                                } else {
                                    if (youLs.subtract(youXueSheng).divide(youLs, 5, BigDecimal.ROUND_HALF_UP).abs().compareTo(ling02) > 0 &&
                                            youXueSheng.subtract(youLs).divide(youXueSheng, 5, BigDecimal.ROUND_HALF_UP).abs().compareTo(ling02) > 0) {
                                        if (PrivateConfig.getXSM(symbolLs) <= 0.1 && youLs.multiply(beiShu).compareTo(new BigDecimal("5")) < 0) {
                                            //小数位为0，且应该有的个数小于5时，不校验
                                            hasProblem = false;
                                            System.out.println(personInfo.getString(PrivateConfig.alias) + "的" + symbolLs + "和老师相同，没有问题！");
                                            continue;
                                        }
                                        if ("1".equals(PrivateConfig.genTian)) {
                                            //跟田时，田的多是正常的
                                            if (youLs.compareTo(youXueSheng) > 0) {
                                                String msg = personInfo.getString(PrivateConfig.alias) + "的"  + position.getSymbol() +  "没有问题！此时田的多，我们的可能已经挂单卖了";
                                                System.out.println(msg);
//                                                Thread.sleep(1000 * 30);
                                                hasProblem = false;
                                                T5.sendMe(msg);
                                                continue;
                                            }
                                        }
                                        if (symbolMapYouCuo.get(position.getSymbol()) == null) {
                                            symbolMapYouCuo.put(position.getSymbol(), 0);
                                        }
                                        symbolMapYouCuo.put(position.getSymbol(), symbolMapYouCuo.get(position.getSymbol()) + 1);
                                        if (symbolMapYouCuo.get(position.getSymbol()) < 3) {
                                            Thread.sleep(1000);
                                            return;
                                        }
                                        String msg = personInfo.getString(PrivateConfig.alias) + "，" + position.getSymbol() + "和老师个数不同" + "，应该有" + youLs.multiply(beiShu) + "个，现在有" + you + "个。有问题！";

                                        //自动减仓
                                        if(you.compareTo(youLs.multiply(beiShu)) > 0){

                                            BigDecimal mai = you.subtract(youLs.multiply(beiShu)).setScale(PrivateConfig.getXSM(position.getSymbol()), BigDecimal.ROUND_HALF_UP);
                                            String buy = "SELL";
                                            if(PrivateConfig.kong_short.equals(position.getPositionSide())){
                                                buy = "BUY";
                                            }
                                            postOrder(syncRequestClient, position.getSymbol(), buy, position.getPositionSide(), mai.toString());
                                        }

                                        System.out.println(msg);
                                        T5.sendMe(msg);
                                        hasProblem = false;
                                        //有错的话，报警后就return
                                        Thread.sleep(1000);
                                        return;
                                    } else {
                                        //和老师相同就退出
                                        hasProblem = false;
                                        System.out.println(personInfo.getString(PrivateConfig.alias) + "的" + symbolLs + "和老师相同，没有问题!");
                                    }
                                }
                            }else if (symbolLs.equals(position.getSymbol()) && position.getPositionSide().equals(entryLs.getValue().getPositionSide()) && you.abs().compareTo(ling) == 0) {

                                hasProblem = false;
                                String msg = personInfo.getString(PrivateConfig.alias) + "的" + symbolLs + "没有，而老师有，可能是我们的挂单卖了，没有问题!";
                                System.out.println(msg);
                                T5.sendMe(msg);
                            }
                        }
                        if (hasProblem) {
                            if(PrivateConfig.getXSM(symbolLs) <= 0.1 && youLs.multiply(beiShu).compareTo(new BigDecimal("5"))<0) {
                                //小数位为0，且应该有的个数小于5时，不校验
                                hasProblem = false;
                                System.out.println(personInfo.getString(PrivateConfig.alias) + "的" + symbolLs + "和老师相同，没有问题！");
                                continue;
                            }
                            if (symbolMapYouCuo.get(symbolLs) == null) {
                                symbolMapYouCuo.put(symbolLs, 0);
                            }
                            symbolMapYouCuo.put(symbolLs, symbolMapYouCuo.get(symbolLs) + 1);
                            if (symbolMapYouCuo.get(symbolLs) < 3) {
                                Thread.sleep(1000);
                                return;
                            }
                            if("1".equals(PrivateConfig.genTian)){
                                String msg = personInfo.getString(PrivateConfig.alias) + "没有问题！";
                                System.out.println(msg);
//                                Thread.sleep(1000 * 30);
                                continue;
                            }
                            String msg = personInfo.getString(PrivateConfig.alias) + "应该有" + youLs.multiply(beiShu) + "个" + symbolLs + "，现在没有。有问题！";
                            System.out.println(msg);
                            T5.sendMe(msg);
                            //有错的话，报警后就return
                            Thread.sleep(1000 * 20);
                            return;
                        }
                    }
                }
                //学员之间的间隔
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }catch (Throwable t){
            t.printStackTrace();
        }
        // 能够走到这里，证明没有错
        symbolMapQingCang.clear();
        symbolMapYouCuo.clear();
        /*for (Map.Entry<String, Integer> entry : PrivateConfig.getXsw().entrySet()) {
            symbolMapQingCang.put(entry.getKey(), 0);
            symbolMapYouCuo.put(entry.getKey(), 0);
        }*/
        //监控间隔
        Thread.sleep(1000 * 30);
    }

    /**
     * 第三次检查还有错，证明是真的有错了
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
        return true;
    }

    public String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis())); // 时间戳转换日期
    }
    public void postOrder(SyncRequestClient syncRequestClient, String symbol, String buy, String positionSide, String getOrigQty) {
        System.out.println("自动减仓");
        Order myOrder = syncRequestClient.postOrder(
                symbol,
                OrderSide.valueOf(buy),//买还是卖
                PositionSide.valueOf(positionSide),//做多还是做空 long short both
                OrderType.valueOf("MARKET"),// 订单类型，limit：限价单；MARKET：市价单（想要成功买卖，使用这个）
                null,//TimeInForce.valueOf("GTC"),//成交为止，一直有效，不用管
                getOrigQty,//跟单数量，需要大于5
                null,//跟单单价，总价需要大于5（市价时，可以不填）
                null,//order.getReduceOnly().toString(),
                null,//order.getClientOrderId(),
                null,//order.getStopPrice().toString(),
                null,//WorkingType.valueOf(order.getWorkingType()),
                NewOrderRespType.RESULT);
        System.out.println(myOrder.toString());
    }
}

