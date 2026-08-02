package com.example.bian.xin;

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
public class GuanBi3 {

    static Boolean isBaoJing = false;

    public static void main(String[] args) throws IOException, InterruptedException {

        System.setProperty("https.proxySet", "true");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyPort", "10819");


        args = new String[2];
        System.out.println("开始啦");
        args[0] = "E://code//biance";
        args[1] = "jianKong";
        PrivateConfig.before(args[0], args[1]);
        PrivateConfig.getJGXsw();

        GuanBi3 jianKong3 = new GuanBi3();
        jianKong3.method(args, PrivateConfig.genDan_personInfoList);

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

    public void method(String[] args,List<JSONObject> listPersonInfo) throws InterruptedException {
        try {
            PrivateConfig.getListNew(args[0] + "//info.json");
            this.listPersonInfo = listPersonInfo;
            if(PrivateConfig.genTian.equals("1")){
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
            }else {
                System.out.println("查看是否有单子");
                for (JSONObject personInfo : listPersonInfo) {
                    try {
                        SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient));
                        AccountInformation accountInformation = syncRequestClient.getAccountInformation();
                        List<Position> positionList = accountInformation.getPositions();

                        for (Position position : positionList) {
                            if (position.getPositionAmt().abs().compareTo(ling) > 0) {
                                String msg = personInfo.getString(PrivateConfig.alias) + "：有" + position.getSymbol() + position.getPositionAmt() + "个";
                                System.out.println(msg);
                            }
                        }
                        System.out.println(personInfo.getString(PrivateConfig.alias) + "查询完毕，如果没有持仓记录，就是没有持仓");
                    }catch (Exception e){
                        System.out.println(personInfo.getString(PrivateConfig.alias) + "没有查出来");
                        e.printStackTrace();
                    }
                }
            }


        } catch (Exception e) {
            System.out.println("监控程序启动出错了");
            e.printStackTrace();
        }
    }

    public void isError() throws InterruptedException {
        try {
//            System.out.println(getCurrentTime() + "正在监控");
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
                    String msg = "api访问失败，有问题！如果连续三次报这个问题，切换个节点，再不行就关闭软件5分钟！";
                    System.out.println(msg);
                    Thread.sleep(2000);
                    error = 1;
                }
                return;
            }
            PrivateConfig.xsw(false);
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
                            if(!hasErrorQingCang(position.getSymbol())){
                                Thread.sleep(1000);
                                return;
                            }

                            if(position.getMaintMargin().compareTo(new BigDecimal("0.5")) < 0){
                                // 如果保证金太少，证明是计算误差，清了就行啦，不用报警了
                                return;
                            }

                            String msg = personInfo.getString(PrivateConfig.alias) + "，老师没有" + position.getSymbol() + "。有问题！如果连续三次报这个问题，请联系他手动平仓1！不能关闭！";
                            System.out.println(msg);
                            //有错的话，报警后就return
                            Thread.sleep(1000 * 20);
                            return;
                        }
                    }
                    if (!hasProblem) {
                        System.out.println(personInfo.getString(PrivateConfig.alias) + "和老师都没有持仓。没有问题！可以关闭！");
                    }
                } else {

                    // 如果和老师都有，并且symbol不同时，平仓
                    for (Position position : positionList) {
                        BigDecimal you = position.getPositionAmt().abs();
                        if (you.abs().compareTo(ling) > 0) {
                            if (!mapTian.keySet().contains(position.getSymbol())) {
                                if(!hasErrorQingCang(position.getSymbol())){
                                    Thread.sleep(1000);
                                    return;
                                }

                                if(position.getMaintMargin().compareTo(new BigDecimal("0.5")) < 0){
                                    // 如果保证金太少，证明是计算误差，清了就行啦，不用报警了
                                    return;
                                }
                                String msg = personInfo.getString(PrivateConfig.alias) + "，老师没有" + position.getSymbol() + "。有问题！如果连续三次报这个问题，请联系他手动平仓2！不能关闭！";
                                System.out.println(msg);
                                //有错的话，报警后就return
                                Thread.sleep(1000 * 20);
                                return;
                            }
                        }
                    }

                    BigDecimal beiShu = new BigDecimal(personInfo.getString(PrivateConfig.beiShu));
                    //只有倍数大时才校验个数是否正确
                    if(beiShu.compareTo(PrivateConfig.ling035) < 0) {
                        System.out.println(personInfo.getString(PrivateConfig.alias) + "的和老师相同，没有问题！不能关闭！");
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
                                    if (!hasError(position.getSymbol())) {
                                        Thread.sleep(1000);
                                        return;
                                    }
                                    String msg = personInfo.getString(PrivateConfig.alias) + "，" + position.getSymbol() + "和老师方向做反了。有问题！如果连续三次报这个问题，关闭软件，重新启动！不能关闭！";
                                    System.out.println(msg);
                                    //有错的话，报警后就return
                                    Thread.sleep(1000 * 20);
                                    return;
                                } else {
                                    if (youLs.subtract(youXueSheng).divide(youLs, 5, BigDecimal.ROUND_HALF_UP).abs().compareTo(ling02) > 0 &&
                                            youXueSheng.subtract(youLs).divide(youXueSheng, 5, BigDecimal.ROUND_HALF_UP).abs().compareTo(ling02) > 0) {
                                        if (PrivateConfig.getXSM(symbolLs) <= 0.1 && youLs.multiply(beiShu).compareTo(new BigDecimal("5")) < 0) {
                                            //小数位为0，且应该有的个数小于5时，不校验
                                            hasProblem = false;
                                            System.out.println(personInfo.getString(PrivateConfig.alias) + "的" + symbolLs + "和老师相同，没有问题！不能关闭！");
                                            continue;
                                        }
                                        if ("1".equals(PrivateConfig.genTian)) {
                                            //跟田时，田的多是正常的
                                            if (youLs.compareTo(youXueSheng) > 0) {
                                                if (!hasError(position.getSymbol())) {
                                                    Thread.sleep(1000);
                                                    return;
                                                }
                                                String msg = personInfo.getString(PrivateConfig.alias) + "的"  + position.getSymbol() +  "没有问题！此时田的多，我们的可能已经挂单卖了！不能关闭！";
                                                System.out.println(msg);
                                                hasProblem = false;
                                                continue;
                                            }
                                        }
                                        if (!hasError(position.getSymbol())) {
                                            Thread.sleep(1000);
                                            return;
                                        }

                                        String msg = personInfo.getString(PrivateConfig.alias) + "，" + position.getSymbol() + "和老师个数不同" + "，应该有" + youLs.multiply(beiShu) + "个，现在有" + you + "个。如果连续三次收到，就有问题了！不能关闭！";

                                        System.out.println(msg);
                                        //有错的话，报警后就return
                                        Thread.sleep(1000 * 20);
                                        return;
                                    } else {
                                        //和老师相同就退出
                                        hasProblem = false;
                                        System.out.println(personInfo.getString(PrivateConfig.alias) + "的" + symbolLs + "和老师相同，没有问题!不能关闭！");
                                    }
                                }
                            }else if (symbolLs.equals(position.getSymbol()) && position.getPositionSide().equals(entryLs.getValue().getPositionSide()) && you.abs().compareTo(ling) == 0) {
                                if(!hasError(symbolLs)) {
                                    Thread.sleep(1000);
                                    return;
                                }
                                hasProblem = false;
                                String msg = personInfo.getString(PrivateConfig.alias) + "的" + symbolLs + "没有，而老师有，可能是我们的挂单卖了，没有问题!可以关闭！";
                                System.out.println(msg);
                            }
                        }
                        if (hasProblem) {
                            if(PrivateConfig.getXSM(symbolLs) <= 0.1 && youLs.multiply(beiShu).compareTo(new BigDecimal("5"))<0) {
                                //小数位为0，且应该有的个数小于5时，不校验
                                hasProblem = false;
                                System.out.println(personInfo.getString(PrivateConfig.alias) + "的" + symbolLs + "和老师相同，没有问题！不能关闭！");
                                continue;
                            }

                            if(!hasError(symbolLs)) {
                                Thread.sleep(1000);
                                return;
                            }
                            if("1".equals(PrivateConfig.genTian)){
                                String msg = personInfo.getString(PrivateConfig.alias) + "没有问题！不能关闭！";
                                System.out.println(msg);
                                continue;
                            }
                            String msg = personInfo.getString(PrivateConfig.alias) + "应该有" + youLs.multiply(beiShu) + "个" + symbolLs + "，现在没有。有问题！不能关闭！";
                            System.out.println(msg);
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

    /**
     * 第三次检查还有错，证明是真的有错了
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
        return true;
    }

    public String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis())); // 时间戳转换日期
    }

}

