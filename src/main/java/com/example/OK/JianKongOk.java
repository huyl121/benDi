package com.example.OK;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.OK.conn.client.APIClient;
import com.example.OK.conn.trade.TradeAPIService;
import com.example.OK.conn.trade.impl.TradeAPI;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import com.example.bian.client.model.trade.AccountInformation;
import com.example.bian.client.model.trade.Position;
import com.example.bian.genDan.*;
import com.example.bian.ok.GetOKPositions;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import static com.example.bian.client.bushu.PrivateConfig.genDan_genPortfolioId;
import static com.example.bian.client.bushu.PrivateConfig.genDans_genPortfolioId;

/**
 * 根据老师现有的持仓，自己分析他是加仓还是减仓
 */
public class JianKongOk {

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


        System.out.println("bnc-uuid=5207c285-8ded-465c-b32c-b8a179a931f9; source=organic; campaign=www.google.com.hk; BNC_FV_KEY=32cff046e95aad60abddf679ef4b55c5d7d3d0ee; se_gd=wIFFQW1ANGQVgBRAQU1ZgZZAxAQwUBZV1Qc9YUUZlNSWgBlNWUwX1; se_gsd=czM2CjBlIzM3UCMtJDU0FSojWgkLAwYTUl9CWlxSU1NUVFNT1; theme=dark; BNC-Location=BINANCE; changeBasisTimeZone=; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22416570327%22%2C%22first_id%22%3A%2217f356327f2170-06dc6dfab34654-3f3a5e08-2073600-17f356327f3728%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22%24device_id%22%3A%2217f356327f2170-06dc6dfab34654-3f3a5e08-2073600-17f356327f3728%22%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMThiYWYyY2QwMjQzMzAtMGQ5OWIwMTMxYTdhNTg4LTNmM2E1ZTA4LTIwNzM2MDAtMThiYWYyY2QwMjVkOGMiLCIkaWRlbnRpdHlfbG9naW5faWQiOiI0MTY1NzAzMjcifQ%3D%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%22416570327%22%7D%7D; userPreferredCurrency=USD_USD; futures-layout=pro; OptanonAlertBoxClosed=2024-02-01T13:12:40.082Z; logined=y; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22416570327%22%2C%22first_id%22%3A%2217f356327f2170-06dc6dfab34654-3f3a5e08-2073600-17f356327f3728%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22%24device_id%22%3A%2217f356327f2170-06dc6dfab34654-3f3a5e08-2073600-17f356327f3728%22%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMThiYWYyY2QwMjQzMzAtMGQ5OWIwMTMxYTdhNTg4LTNmM2E1ZTA4LTIwNzM2MDAtMThiYWYyY2QwMjVkOGMiLCIkaWRlbnRpdHlfbG9naW5faWQiOiI0MTY1NzAzMjcifQ%3D%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%22416570327%22%7D%7D; g_state={\"i_l\":4,\"i_p\":1711721974032}; BNC_FV_KEY_T=101-Q2swi1HnFxNqyf%2Fsv3%2FA8wuyksWx86uRVctpaCkJuRDMSB%2F0sc7p27b%2Fyi2XLZwO7Z8dE7%2FHueaV4Ra1jhH%2FRQ%3D%3D-p%2BpAKXRkYB1nnvWC1nllNw%3D%3D-1f; BNC_FV_KEY_EXPIRE=1709483362706; se_sd=wVSFRBwlSRXBBMMMXGhggZZCRBghQEUV1URZfVUJlhQVQB1NWU5Q1; cr00=5181467319C7B8339A604A12D116CA73; d1og=web.416570327.3817691A007925562E2FD223E811C0A2; r2o1=web.416570327.91FDE3C7199FF99EA68159CFD89207FA; f30l=web.416570327.C1175A820BD7475923667210579ACA4D; p20t=web.416570327.4C8ECBBEAF5304E118DCFC03233A1296; lang=zh-cn; OptanonConsent=isGpcEnabled=0&datestamp=Sun+Mar+03+2024+22%3A53%3A15+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202310.2.0&browserGpcFlag=0&isIABGlobal=false&hosts=&consentId=785d4232-2955-4abb-b9b2-ff78207a3cb6&interactionCount=3&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&AwaitingReconsent=false&geolocation=JP%3B13");
        args = new String[2];
        System.out.println("开始啦");
        args[0] = "E://code//biance";
        args[1] = "jianKong";
        PrivateConfig.before(args[0], args[1]);
        PrivateConfig.getJGXsw();
        PrivateConfig.xsw(true);




        JianKongOk jianKong4 = new JianKongOk();
        jianKong4.method(args, threadPoolExecutor, PrivateConfig.daiDanOk_personInfoList);

    }


    JSONObject tianXH;
    // 币种的清仓次数
    Map<String, Integer> symbolMapQingCang = new HashMap<>();
    //币种的错误次数
    Map<String, Integer> symbolMapYouCuo = new HashMap<>();
    static Map<String, LogObject> logMap = new HashMap<>();//最多发10次邮件，2天过期


    BigDecimal ling02 = new BigDecimal("0.2");
    BigDecimal ling = new BigDecimal("0");

    public static boolean needCheck = true;
    public static int checkCount = 0;

    public void method(String[] args, ThreadPoolExecutor threadPoolExecutor, List<JSONObject> personInfoList) throws InterruptedException {
        try {
            PrivateConfig.printLog("跟单开启监控：" + PrivateConfig.getCurrentTime());
            Thread.sleep(30 * 1000);

            int j = 0;
            int k = 0;
            while (true) {
                try {

                    PrivateConfig.xsw(false);
                    if (needCheck) {
                        //1分钟一次
                        //为什么把监控间隔放入方法里面？如果有错的话，应该立马再次确认，而不是1分钟之后再检查，如果放在外边可能是要等了
                        isError(threadPoolExecutor, personInfoList);
                    }

                    j++;
                    if (j > 10) {
                        j = 0;
                        System.out.println("jian kong");
                        needCheck = true;
                        checkCount = 0;
                    }

                    //第一次操作时设置倍数即可，其他时候只需要展示倍数
                    k++;
                    if (k > 1) {
                        k = 0;
                        for (JSONObject personInfo : personInfoList) {
                            System.out.println(personInfo.getString(PrivateConfig.alias)  + "：" + personInfo.getString(PrivateConfig.beiShu));
                        }
                    }



                } catch (Exception e) {
                    Thread.sleep(1000 * 6);
                    e.printStackTrace();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                if (!needCheck) {
                    if(PrivateConfig.ceShi.equals("1")){
                        Thread.sleep(1000 * 5);
                    }else {
                        Thread.sleep(1000 * 60);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("监控程序启动出错了");
            e.printStackTrace();
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
            } else if(PrivateConfig.daiDanOk_isDaiDanOk.equals("1")){
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
//            }

            //#1双向
            /*Map<String, Position> mapTian = new HashMap<>();
            for(Map.Entry<String, Position> s : mapTian1.entrySet()){
                mapTian.put(s.getKey().split("_")[0], s.getValue());
            }*/

            for (JSONObject personInfo : personInfoList) {
                List<Position> positionList = new ArrayList<>();
                TradeAPIService tradeAPIService = (TradeAPIService) personInfo.get(PrivateConfig.tradeAPIService);
                APIClient apiClient = (APIClient) personInfo.get(PrivateConfig.apiClient);
                TradeAPI tradeAPI = (TradeAPI) personInfo.get(PrivateConfig.tradeAPI);
                JSONObject result = tradeAPIService.getAccountAndPosition(apiClient, tradeAPI, "SWAP");
                if(result.getString("code").equals("0")) {
                    JSONArray data = result.getJSONArray("data");
                    JSONArray positions = data.getJSONObject(0).getJSONArray("posData");
                    if(!CollectionUtils.isEmpty(positions)){
                        for(Object o : positions){
                            JSONObject jsonObject = (JSONObject) o;
                            String sym = jsonObject.getString("instId").split("-")[0];
                            Position position = new Position();
                            position.setSymbol(sym + "USDT");
                            position.setPositionAmt(jsonObject.getBigDecimal("pos").divide(PrivateConfig.getOKZhangShu.getBigDecimal(sym)));
                            position.setPositionSide(jsonObject.getString("posSide").toUpperCase());
                            positionList.add(position);
                        }
                    }
                }



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
                            QingCangOk qingCang = new QingCangOk();
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
                                QingCangOk qingCang = new QingCangOk();
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

                                    String msg = "连续三次，有问题！" + personInfo.getString(PrivateConfig.alias) + "，" + position.getSymbol() + "和老师个数不同，应该有" + youLs.multiply(beiShu) + "个，现在有" + you + "个";

                                    //先自动减仓
                                    if (you.compareTo(youLs.multiply(beiShu)) > 0) {

                                        BigDecimal jianCangCount = you.subtract(youLs.multiply(beiShu)).setScale(PrivateConfig.getXSM(position.getSymbol()), BigDecimal.ROUND_HALF_UP);

                                        PrivateConfig.jianCangOk(position.getSymbol(), position.getPositionSide(), tradeAPIService, jianCangCount, apiClient, tradeAPI);

                                        //自动减仓是一个人一个人的减
                                        PrivateConfig.printLog(msg);
                                        T5.sendMe(msg);
                                        continue;
                                    }

                                    PrivateConfig.printLog(msg);

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
                                    Thread.sleep(10000);
                                    return;
                                }
                                hasProblem = false;
                                String msg = personInfo.getString(PrivateConfig.alias) + "的" + symbolSideLs + "没有，而老师有，可能是我们的挂单卖了，没有问题!";
                                System.out.println(msg);
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
                            String msg = personInfo.getString(PrivateConfig.alias) + "应该有" + youLs.multiply(beiShu) + "个" + symbolSideLs + "，现在没有。有问题！";
                            PrivateConfig.printLog(msg);
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
            Thread.sleep(1000*6);
        }else {
            Thread.sleep(1000 * 60);
        }
    }

    public static void sendEmail1(Map<String, LogObject> logMap, String msg) {
        if (logMap.containsKey(msg)) {
            if (logMap.get(msg).getCount() < 100) {
                logMap.get(msg).setCount(logMap.get(msg).getCount() + 1);
            }
        } else {
            LogObject logObject = new LogObject();
            logObject.setTime(System.currentTimeMillis());
            logObject.setCount(1);
            logMap.put(msg, logObject);
        }

        if (logMap.get(msg).getCount() < 6) {
            T5.searchAll(msg);
        }
    }

    public static Map<String, Position> getPositions(ThreadPoolExecutor threadPoolExecutor, String genPortfolioId) throws InterruptedException {
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
                                /*String positionSide = position.getString(PrivateConfig.positionSide);
                                if(positionSide.equals(PositionSide.BOTH.toString())) {
                                    //单向持仓时，转换为双向
                                    if (position.getBigDecimal(PrivateConfig.positionAmount).compareTo(PrivateConfig.ling) > 0) {
                                        positionSide = PositionSide.LONG.toString();
                                    } else {
                                        positionSide = PositionSide.SHORT.toString();
                                    }
                                }*/

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
    }

    public static Map<String, Position> getOKPositions(ThreadPoolExecutor threadPoolExecutor) throws InterruptedException {
        JSONArray jsonArray = GetOKPositions.getOrders(threadPoolExecutor, PrivateConfig.ok_genPortfolioId, true, false);
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
        return true;
    }

    public String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis())); // 时间戳转换日期
    }

}

