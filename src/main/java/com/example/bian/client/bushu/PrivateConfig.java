package com.example.bian.client.bushu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.binance.connector.client.impl.SpotClientImpl;
import com.example.OK.conn.ClosePositions;
import com.example.OK.conn.PlaceOrder;
import com.example.OK.conn.client.APIClient;
import com.example.OK.conn.client.APIConfiguration;
import com.example.OK.conn.client.I18nEnum;
import com.example.OK.conn.trade.TradeAPIService;
import com.example.OK.conn.trade.impl.TradeAPI;
import com.example.OK.conn.trade.impl.TradeAPIServiceImpl;
import com.example.OK.xiaDan;
import com.example.bian.client.RequestOptions;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.model.enums.NewOrderRespType;
import com.example.bian.client.model.enums.OrderSide;
import com.example.bian.client.model.enums.OrderType;
import com.example.bian.client.model.enums.PositionSide;
import com.example.bian.client.model.market.ExchangeInfoEntry;
import com.example.bian.client.model.market.ExchangeInformation;
import com.example.bian.client.model.market.MarkPrice;
import com.example.bian.client.model.trade.AccountInformation;
import com.example.bian.client.model.trade.Order;
import com.example.bian.client.model.trade.Position;
import com.example.bian.genDan.GetPositions;
import com.example.bian.ok.GetOKPositions;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

public class PrivateConfig {

    // #放开

    /*
    *  业务
    *

        cumQuote：下单金额，单位人民币。 下单的数量和价格的乘积，和USDT什么关系呢？
        像保证金不足的委托，代码里直接出错了，可能在系统里的历史委托中就找不到

        下单后撤销
            如果都完全成功的话
                它们是对同一个订单的操作，历史记录里只会有一条数据
                唯一的区别是，下单状态为new，撤销状态为CANCELED
            如果部分成功呢？疑问？
                下了的单部分成交了，此时状态为PARTIALLY_FILLED，此时还能撤销吗？撤销后原来的订单状态是什么，撤销是否会创建一个新的订单呢？

        撤单失败
            1、田下单没有成交，我们成交了，然后田撤单，我们会提示撤单失败
                买入：需要田那里保证他一定能买入（之前沟通的说是软件上有这个功能）
                卖出：这种情况小号已经卖了，那就不管啦

        下单失败
            1、此时应该是平仓的错误，平仓也叫下单，当第一次平仓时，田的没有成交，我们的成交了，此时田第二次平仓，我们就会下单失败，因为我们已经平过了。

        * 问题？？？
    *   下单时，不同的symbol，个数和价格的小数位有要求吗？对的
    *   市价单是不是不用设置价格？是的
    *
    * */

    /*
    buy：越大越容易成交
    sell：越小越容易成交c

        下面是双向操作的，也就是可以同时开多开空，此时ReduceOnly不能填写，注意是不能，传null才行。单向持仓时，不能同时持有
        * 做多
            买：side=BUY,positionSide=LONG
            卖：side=SELL,positionSide=LONG

        做空
            买：side=SELL,positionSide=SHORT
            卖：side=BUY,positionSide=SHORT

        单向持仓时，ReduceOnly必填， positionSide=both
        做多
            买：side=BUY,ReduceOnly=false
            卖：side=SELL,ReduceOnly=true

        做空
            买：side=SELL,ReduceOnly=false
            卖：side=BUY,ReduceOnly=true


        在AccountInformation中，positionAmt：做多为正数，做空位负数

        接口参数说明
        NEW	订单被交易引擎接受
        PARTIALLY_FILLED	部分订单被成交
        FILLED	订单完全成交
        CANCELED	用户撤销了订单
        PENDING_CANCEL	撤销中(目前并未使用)
        REJECTED	订单没有被交易引擎接受，也没被处理
        EXPIRED	订单被交易引擎取消, 比如  LIMIT FOK 订单没有成交        市价单没有完全成交        强平期间被取消的订单        交易所维护期间被取消的订单





        zhenShiBeiShu：自动计算的值再乘以此值，就是最终的倍数
        money：最大跟单比例
        beiShu：是变化的，根据这个值下单
    * */


    public static String symbol = "symbol";
    public static String side = "side";
    public static String positionSide = "positionSide";
    public static String con_zhisun = "con_zhisun";
    public static Boolean hsaOrder = false;
    public static Boolean isZhiSun = false;
    public static String positionAmount = "positionAmount";
    public static String qty = "qty";
    public static BigDecimal ling = new BigDecimal(0);

    public static final String duo_long = "LONG";
    public static final String kong_short = "SHORT";
    public static final String name = "name";
    public static final String alias = "alias";

    public static final String apiKey = "API_KEY";
    public static final String secretKey = "secretKey";
    public static final String passphrase = "passphrase";
    public static final String beiShu = "beiShu";
    public static final String zhenShiBeiShu = "zhenShiBeiShu";

    public static final String newMap = "newMap";
    public static final String cancleMap = "cancleMap";
    public static final String syncRequestClient = "syncRequestClient";
    public static final String tradeAPIService = "tradeAPIService";
    public static final String apiClient = "apiClient";
    public static final String tradeAPI = "tradeAPI";
    public static final String spotClient = "spotClient";

    //   public static String id349 = "24";//老的是id
//   public static String id349 = "35";//手术刀
//    public static String id349 = "43";//风火山林
//   public static String id349 = "44";//349-提款机
    public static String id349;
    public static String port = "10819";
    public static BigDecimal ling035;

    public static  String i = "1";
    public static  String time = "1";
    public static  String computer = "东京";
    public static  String linux = "1";

    public static String UMFUTURE_MAIN = "UMFUTURE_MAIN";
    public static String UMFUTURE_FUNDING = "UMFUTURE_FUNDING";
    public static String UMFUTURE_MARGIN = "UMFUTURE_MARGIN";
    public static String UMFUTURE_OPTION = "UMFUTURE_OPTION";
    public static String Leverage = "1";
    public static String daiLi = "1";
    public static String noLeverage = "1";
    public static String remove = "";//移除sol专用
    public static Integer LeverageCount = 8;
    public static  Integer isLinux = 0;
    public static  Integer zhiSunTime = 30;//默认30分钟
    public static  String daYin = "huYaLong";
    public static  String ceShi = "0";
    public static  String shiJian = "800";
    public static  String changeInitialLeverage = "0"; //是否自动设置杠杆
    public static  String buCang = "0"; //是否自动补仓
    public static  String jianCang = "0"; //是否自动减仓
    public static  String testGongNeng = "0"; //临时功能
    public static  String cmqUSDC = "0"; //是否跟聪明钱里的usdc
    public static  int getPon = 12000; //获取仓位次数，1分钟一次
    public static  String xiaoCangBeiShu; //当购买个数太小时，取最小购买个数
    public static String Leverage4164 = "ETHUSDT,BTCUSDT";
    public static  int LeverageCount4164 = 40;
    public static  String duo = "1.001";
    //当直接跟position时，这个就是带单员的号码，不会变；
    //当通过跟单记录跟单时，这个就是变化的，每次跟单就会生成一个新的
    public static  String biCoin_symName = "";
    public static  String biCoin_positionSide = "1";
    public static  String biCoin_isBiCoin = "0";
    public static  String biCoin_token = "1";
    public static  String biCoin_time2 = "1";
    public static  String biCoin_time3 = "1";
    public static  String biCoinTogether = "0";
    public static  String biCoinremove = "";//移除后，启动就能跟单了
    public static  String biCoins_isBiCoins = "0";
    public static  Integer biCoins_pageSize = 3;
    public static  String biCoins_token = "1";
    public static  String biCoins_time2 = "1";
    public static  String biCoins_time3 = "1";
    public static  String biCoins_remove = "";//移除后，启动就能跟单了
    public static  String ok_isOk = "0";
    public static  String ok_genPortfolioId = "0";
    public static volatile   String ok_position = "";//2：通过合约带单，此时必须有authorization
    public static volatile Map<String, Integer> personJiaCangMap = new HashMap<>();//每个人自动加仓的次数，不能太多，防止错误
    public static  String ok_zhuan = "0";//从概括转到带单
    public static  String ok_count = "";//空就是没有限制，不为空自己填写个限制的比例
    public static  String ok_authorization = "";
    public static  JSONArray ok_xianYou;
    public static  String ok_remove = "";//移除后，启动就能跟单了
    public static  Integer ok_pageSize = 3;
    public static  String genDan_genPortfolioId = "1";
    public static volatile String genDans_genPortfolioId = "";
    public static  String genDan_portfolioId = "1";
    public static  String genDan_money = "500";
    public static  String genDan_cookie = "";
    public static  String genDan_token = "";
    //1：直接查询老师的单子，如果不跟也能看到老师的单子，就用这个，此时token是可以不设置的；如果需要跟单才能看见，也是使用这个，因为是app的接口，此时token是必须的
    //3：和1一样也是直接查询带单人持仓，使用的是web端接口，需要提供cookie和token
    //2：表示使用网页版，要有cookie和token，0代表使用APP只需有token，意思是一样的，都是查询跟单人的信息
    //5和6是查询聪明钱的，5APP端，6web端
    public static  String genDan_position = "0";
    public static  String genDan_urls = "www.binance.com";
    public static  String genDan_url = "www.binance.com";
    public static  int genDan_urlIndex = 0;
    public static  String genDan_isGenDan = "0";
    public static  String genDans_isGenDans = "0";
    public static  String genDans_unrealizedProfit = "0";
    public static  String daiDanOk_isDaiDanOk = "0";
    public static  String analysis_isAnalysis = "0";
    public static  BigDecimal analysis_money = new BigDecimal("0");
    public static  String analysis_API_KEY = "";
    public static  String analysis_secretKey = "";
    public static  int analysis_peopleCount = 2;
    public static  String analysis_symbol = "ETHUSDT";
    public static  JSONArray genDan_xianYou;
    public static  JSONArray genDans_xianYou;
    public static  String daiDanOk_genXianYou = "0";
    public static  JSONArray genDan_musk;
    public static  String muskUrl = "aibc.mktx.org";
    public static  Long genDan_transferTime = 0L;

    public static Map<String, Integer> gangGanMap = new HashMap<>();
    public static String gangGanSheZhi = "0";
//    /*
//    "zhengQian": {
//		"shui": "",
//		"portfolioId": "4228255345010905601",
//		"investAmount": 10,
//		"copyModel": "FIXED_RATIO",
//		"定比": "FIXED_RATIO",
//		"定额": "FIXED_AMT",
//		"costPerOrder": 0
//	}
//
//    */
    public static  String zhengQian_shui = "隐毒素";
    public static  String zhengQian_portfolioId = "3763772820431316225";
    public static  int zhengQian_investAmount = 1000;
    public static  String zhengQian_copyModel = "FIXED_RATIO";
    public static  String zhengQian_inviteCode = "";
    public static  int zhengQian_costPerOrder = 0;

    public static  String shao = "0.999";
    public static  String genTian = "0";
    public static  String JianKongSol = "0";
    public static  String kuai = "0";//跟快单时，使用最大倍数
    public static  String both = "0";//开多开空测试用，默认双向
    public static  String zhiSun = "0";
    public static  String money = "0";
    public static  String startTime = "08:30";
    public static  String endTime = "23:30";
    public static JSONObject standard;
    public static JSONObject getXsw;
    public static JSONObject symbolLev;
    public static JSONObject getOKZhangShu;
    public static Map<String, Integer> jgXsw = new HashMap<>();//价格小数位
    public static Map<String, Integer> gsXsw = new HashMap<>();//个数小数位
    public static Map<String, BigDecimal> zuiDiMoney = new HashMap<>();//币种最低购买金额
    public static List<JSONObject> personInfoList;
    public static JSONArray niuRens = new JSONArray();
    public static List<JSONObject> genDan_personInfoList;
    public static List<JSONObject> genDans_personInfoList;
    public static List<JSONObject> daiDanOk_personInfoList;
    public static JSONArray genDans_genPortfolioIds;
    public static JSONArray analysis_genPortfolioIds;
    public static List<JSONObject> biCoin_personInfoList;
    public static List<JSONObject> biCoins_personInfoList = new ArrayList<>();
    public static List<JSONObject> ok_personInfoList = new ArrayList<>();
    public static Map<String, List<JSONObject>> biCoins_gendanMap = new HashMap<>();
    public static ThreadPoolExecutor threadPoolExecutor;
    public static Writer fileWriter;
    public static Writer fileWriterJianKong;
    public static String classPath;
    public static File fileLog;
    public static File fileLogJianKong;
    public static Boolean wangYe = false;
    public static String ip = "";
    public static String idCode = "https://coolco.vip/future/realfirm/39/43";
    public static String password = "sduroelejsyzbbac";

    public static RequestOptions options = new RequestOptions();
    public static SyncRequestClient syncRequestClientHuyl = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, options);
    public static void before(String classPath1, String logName) {
        classPath = classPath1;

        getListNew(classPath + "//info.json");


    }

    public static List<Order> getOrders(SyncRequestClient syncRequestClientTiansc, ThreadPoolExecutor threadPoolExecutor) throws InterruptedException {

        //查询所有类型的订单 可以根据symbol 查询是比特币的 还是其他币的
        Callable callable = new Callable() {
            @Override
            public List<Order> call() throws Exception {
                return syncRequestClientTiansc.getAllOrders(null, null, null, null, 1);
                //                            return syncRequestClientTiansc.getAllOrders(null, null, currentTime - 10*1000, currentTime + 10*1000, 1);
//                            return syncRequestClientTiansc.getAllOrders(null, null, 1637931900000L, 1637932020000L, 5);
            }
        };
        int h = 0;
        while (true) {
            /*if(PrivateConfig.daYinLog){
                System.out.println("getOrders:" + getCurrentTime());
            }*/
            Future future = threadPoolExecutor.submit(callable);
            try {
                List<Order> list = (List<Order>) (future.get(3, TimeUnit.SECONDS));
                return list;
            } catch (TimeoutException e) {
                e.printStackTrace();
                Thread.sleep(3000);//前面有超时，歇2秒再跟
            }catch (Exception e){
                e.printStackTrace();
                Thread.sleep(3000);//前面有超时，歇2秒再跟
            }catch(Throwable t){
                t.printStackTrace();
                Thread.sleep(3000);//前面有超时，歇2秒再跟
            }finally {
                future.cancel(true);
                h++;
                if(h>5){
                    h=0;
                    T5.searchAll("连续5次，有问题了，跟田超时！");
                }
            }
        }
    }

    public static String getRemoveSide(String positionSide, BigDecimal amount) {

        if (positionSide.equals(PositionSide.SHORT.toString())) {
            return OrderSide.BUY.toString();
        } else if (positionSide.equals(PositionSide.LONG.toString())) {
            return OrderSide.SELL.toString();
        } else {
            if (amount.compareTo(ling) > 0) {
                return OrderSide.SELL.toString();
            } else {
                return OrderSide.BUY.toString();
            }
        }
    }

    public static String getPositionSide(String positionSide, BigDecimal amount){
        if(positionSide.equals(PositionSide.BOTH.toString())){
            if(amount.compareTo(ling)>0){
                positionSide = PositionSide.LONG.toString();
            }else {
                positionSide = PositionSide.SHORT.toString();
            }
        }

        return positionSide;
    }

    public static Position getOkPositionSide(JSONObject position){
        Position p = new Position();
        BigDecimal value = position.getBigDecimal("notionalUsd");
        BigDecimal markPx = position.getBigDecimal("markPx");
        BigDecimal count = value.divide(markPx, 4, BigDecimal.ROUND_HALF_UP);
        String positionSid = position.getString("posSide").toUpperCase();
        BigDecimal pos = position.getBigDecimal("pos");
        if(PrivateConfig.ok_position.equals("2")){
            pos = position.getBigDecimal("subPos");
            p.setUnrealizedProfit(position.getBigDecimal("pnlRatio").divide(position.getBigDecimal("lever"), 5, BigDecimal.ROUND_HALF_UP));
            p.setOpenTme(position.getLong("openTime"));
        }
        if(PositionSide.SHORT.toString().equals(positionSid)){
            count = count.negate();
        }else if(PositionSide.NET.toString().equals(positionSid)){
            positionSid = PositionSide.BOTH.toString();
            if(pos.compareTo(PrivateConfig.ling)<0){
                count = count.negate();
            }
        }

        p.setPositionSide(getPositionSide(positionSid, count));
        p.setPositionAmt(count);
        p.setSymbol(position.getString("posCcy") + "USDT");
        if(PrivateConfig.ok_position.equals("2")){
            if(StringUtils.isEmpty(position.getString("instId"))){
                p.setSymbol(null);
            }else {
                p.setSymbol(position.getString("instId").split("-")[0] + "USDT");
            }

        }
        p.setPos(pos);
        p.setLeverage(position.getBigDecimal("lever"));

        return p;
    }

    public static volatile Set<String> hasOrderSet = new TreeSet<>();
    /**
     * ok里有逐仓和全仓之分，这里合并到一起
     * @param jsonArray
     * @return
     */
    public static Map<String, Position> buildPositionMap(JSONArray jsonArray){
        Map<String, Position> positionMap = new HashMap<>();
        if(jsonArray != null){
            for (Object o : jsonArray) {
                JSONObject position = (JSONObject) o;
                String instType = position.getString("instType");
                if (!"SWAP".equals(instType)) {//只做永续合约
                    continue;
                }

                Position positionNew = getOkPositionSide(position);

                if(PrivateConfig.ok_position.equals("2")){
                    //先判断单子是否是想要的
                    if(StringUtils.isNotBlank(ok_count)){
                        //不为空时，校验是否挣钱
                        //带单员在挣钱，我们就不跟了：造成的问题是，带单员赔钱买，稍微挣钱就卖了，我想要的效果是一但买了，就等着一起卖
                        String hasOrderFlag = positionNew.getSymbol() + "_" + positionNew.getPositionSide() + "_" + positionNew.getOpenTme();
                        if(!hasOrderSet.contains(hasOrderFlag)){
                            BigDecimal unrealizedProfit = positionNew.getUnrealizedProfit();
                            if(unrealizedProfit.compareTo(new BigDecimal(ok_count)) >= 0){
                                continue;
                            }
                            hasOrderSet.add(hasOrderFlag);
                        }
                    }
                }


                String symBolPosition = positionNew.getSymbol() + "_" + positionNew.getPositionSide();
                if(positionMap.containsKey(symBolPosition)){
                    Position position1 = positionMap.get(symBolPosition);
                    position1.setPositionAmt(position1.getPositionAmt().add(positionNew.getPositionAmt()));
                    position1.setPos(position1.getPos().add(positionNew.getPos()));
                }else {
                    positionMap.put(symBolPosition, positionNew);
                }

            }
        }
        return positionMap;
    }


    public static String getOrderPositionSide(Order order){
        if(PositionSide.BOTH.toString().equals(order.getPositionSide())) {
            if (isDuo3(order)) {
                return PositionSide.LONG.toString();
            } else {
                return PositionSide.SHORT.toString();
            }
        }

        return order.getPositionSide();
    }

    public static Order getOrder(Order orderTian, SyncRequestClient syncRequestClientTiansc, ThreadPoolExecutor threadPoolExecutor) throws InterruptedException {
        Callable callable = new Callable() {
            @Override
            public Order call() throws Exception {
                return syncRequestClientTiansc.getOrder(orderTian.getSymbol(), orderTian.getOrderId(), orderTian.getClientOrderId());
            }
        };
        int h=0;
        while (true) {

            Future future = threadPoolExecutor.submit(callable);
            try {
                Order orderNew = (Order) (future.get(3, TimeUnit.SECONDS));
                return orderNew;
            } catch (TimeoutException e) {
                e.printStackTrace();
                Thread.sleep(3000);//前面有超时，歇2秒再跟
            }catch (Exception e){
                e.printStackTrace();
                Thread.sleep(3000);//前面有超时，歇2秒再跟
            }catch(Throwable t){
                t.printStackTrace();
                Thread.sleep(3000);//前面有超时，歇2秒再跟
            }finally {
                future.cancel(true);
                h++;
                if(h>5){
                    h=0;
                    T5.searchAll("连续5次，有问题了，跟田超时！");
                }
            }
        }
    }

    public static AccountInformation getAccountInformation(SyncRequestClient syncRequestClient, ThreadPoolExecutor threadPoolExecutor) throws InterruptedException {
        Callable callable = new Callable() {
            @Override
            public AccountInformation call() throws Exception {
                return syncRequestClient.getAccountInformation();
            }
        };
        int h = 0;
        while (true) {
            /*if(PrivateConfig.daYinLog){
                System.out.println("getAccountInformation:" + getCurrentTime());
            }*/
            Future future = threadPoolExecutor.submit(callable);
            try {
                AccountInformation accountInformation = (AccountInformation) (future.get(3, TimeUnit.SECONDS));
                return accountInformation;
            } catch (TimeoutException e) {
                e.printStackTrace();
                Thread.sleep(10000);//前面有超时，歇2秒再跟
            }catch (Exception e){
                e.printStackTrace();
                Thread.sleep(10000);//前面有超时，歇2秒再跟
            }catch(Throwable t){
                t.printStackTrace();
                Thread.sleep(10000);//前面有超时，歇2秒再跟
            }finally {
                future.cancel(true);
                h++;
                if(h>5){
                    h=0;
                    T5.searchAll("连续5次，有问题，getAccountInformation超时！");
                }
            }
        }
    }

    public static void jianCang(SyncRequestClient syncRequestClient, String symbol, String positionSide, String getOrigQty, BigDecimal positionAmt) {

        if(positionSide.equals(PositionSide.BOTH.toString())){
//            System.out.println("单向");
            if(positionAmt.compareTo(PrivateConfig.ling) > 0){
//                System.out.println("做的多");
                PrivateConfig.postOrder(syncRequestClient, symbol, OrderSide.SELL.toString(), PositionSide.BOTH.toString(), "TRUE", getOrigQty);
            }else {
//                System.out.println("做的空");
                PrivateConfig.postOrder(syncRequestClient, symbol, OrderSide.BUY.toString(), PositionSide.BOTH.toString(), "TRUE", getOrigQty);
            }
        }else {
//            System.out.println("双向");
            if(positionAmt.compareTo(PrivateConfig.ling) > 0){
//                System.out.println("做的多");
                PrivateConfig.postOrder(syncRequestClient, symbol, OrderSide.SELL.toString(), PositionSide.LONG.toString(), null, getOrigQty);
            }else {
//                System.out.println("做的空");
                PrivateConfig.postOrder(syncRequestClient, symbol, OrderSide.BUY.toString(), PositionSide.SHORT.toString(), null, getOrigQty);
            }
        }
    }

    public static void jiaCang(SyncRequestClient syncRequestClient, String symbol, String positionSide, String getOrigQty, BigDecimal positionAmt) {
            if(positionAmt.compareTo(PrivateConfig.ling) > 0){
                System.out.println("做的多");
                PrivateConfig.postOrder(syncRequestClient, symbol, OrderSide.BUY.toString(), PositionSide.LONG.toString(), null, getOrigQty);
            }else {
                System.out.println("做的空");
                PrivateConfig.postOrder(syncRequestClient, symbol, OrderSide.SELL.toString(), PositionSide.SHORT.toString(), null, getOrigQty);
            }
    }

    public static void jianCangOk(String symbol, String positionSide, TradeAPIService tradeAPIService, BigDecimal getOrigQty, APIClient client, TradeAPI tradeAPI) {
        if (PositionSide.LONG.toString().equals(positionSide)) {
            System.out.println("做的多");
            xiaDan.placeOrder(symbol, OrderSide.SELL.toString(), positionSide, tradeAPIService, getOrigQty, client, tradeAPI);
        } else {
            System.out.println("做的空");
            xiaDan.placeOrder(symbol, OrderSide.BUY.toString(), positionSide, tradeAPIService, getOrigQty, client, tradeAPI);
        }
    }

    /**
     * 市场单时，如果手里只有2个，卖5个，最后也会成功卖2个
     * 限价单时，如果手里只有2个，挂单卖5个，最后也会成功挂单2个
     * @param syncRequestClient
     * @param symbol
     * @param side
     * @param positionSide
     * @param reduceOnly
     * @param getOrigQty
     */
    /*public static void postOrder(SyncRequestClient syncRequestClient, String symbol, String side, String positionSide, String reduceOnly, String getOrigQty) {
        for (int i = 0; i < 1; i++) {
            try {
                syncRequestClient.postOrder(
                        symbol,
                        OrderSide.valueOf(side),//买还是卖，做多时，buy sell；做空时，sell buy
                        PositionSide.valueOf(positionSide),//双向：long short 单向：both
                        OrderType.valueOf("MARKET"),// 订单类型，limit：限价单；MARKET：市价单（想要成功买卖，使用这个）
                        null,//TimeInForce.valueOf("GTC"),//成交为止，一直有效，不用管
                        getOrigQty,//跟单数量（下单时，跟单个数一定是大于0的，通过AccountInformation查询到的，做空的个数小于0，做多的大于0）
                        null,//跟单单价，总价需要大于5（市价时，可以不填）
                        reduceOnly,//order.getReduceOnly().toString(), //双向持仓时，只能传null
                        null,//order.getClientOrderId(),
                        null,//order.getStopPrice().toString(),
                        null,//WorkingType.valueOf(order.getWorkingType()),
                        NewOrderRespType.RESULT);
                System.out.println("下单了：" + PrivateConfig.getCurrentTime()+"-" + symbol+"-"+side+"-"+positionSide+"-"+getOrigQty+"-"+reduceOnly);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }*/

    public static void postOrder(SyncRequestClient syncRequestClient, String symbol, String side, String positionSide, String reduceOnly, String getOrigQty) {
        if(PrivateConfig.ceShi.equals("1")){
            return;
        }
        for (int i = 0; i < 1; i++) {
            try {
                syncRequestClient.postOrder(
                        symbol,
                        OrderSide.valueOf(side),//买还是卖，做多时，buy sell；做空时，sell buy
                        PositionSide.valueOf(positionSide),//双向：long short 单向：both
                        OrderType.valueOf("MARKET"),// 订单类型，limit：限价单；MARKET：市价单（想要成功买卖，使用这个）
                        null,//TimeInForce.valueOf("GTC"),//成交为止，一直有效，不用管
                        getOrigQty,//跟单数量（下单时，跟单个数一定是大于0的，通过AccountInformation查询到的，做空的个数小于0，做多的大于0）
                        null,//跟单单价，总价需要大于5（市价时，可以不填）
                        reduceOnly,//order.getReduceOnly().toString(), //双向持仓时，只能传null
                        null,//order.getClientOrderId(),
                        null,//order.getStopPrice().toString(),
                        null,//WorkingType.valueOf(order.getWorkingType()),
                        NewOrderRespType.RESULT);
                System.out.println("下单了：" + PrivateConfig.getCurrentTime()+"-" + symbol+"-"+side+"-"+positionSide+"-"+getOrigQty+"-"+reduceOnly);
                return;
            } catch (Exception e) {
                try {
                    e.printStackTrace();
//                    PrivateConfig.printLog(fileWriter, e); -2019
                    BigDecimal count = new BigDecimal(getOrigQty);
                    boolean putAgain = false;
                    if(e.getMessage().contains("-2019")){
                        //保证金不够，调整杠杆买进去
//                        count = count.multiply(new BigDecimal("0.5")).setScale(PrivateConfig.getXSM(symbol), BigDecimal.ROUND_HALF_UP);
                        if(PrivateConfig.Leverage4164.contains(symbol)){
                            syncRequestClient.changeInitialLeverage(symbol, PrivateConfig.LeverageCount4164);
                        }else {
                            syncRequestClient.changeInitialLeverage(symbol, 20);
                        }
//                        T5.searchAll("保证金不足，看看是否购买成功");
                        Thread.sleep(1000);
                        putAgain = true;
                    } else if(e.getMessage().contains("-4164")) {
                        if ("1".equals(PrivateConfig.xiaoCangBeiShu)) {
//                            会存在一个问题，买入的太多，检测时，又自动减仓，没意义，不过这个逻辑已验证是对的，打开后，一定要关闭个数的判断
                            BigDecimal markPrice = syncRequestClient.getMarkPrice(symbol).get(0).getMarkPrice();
                            BigDecimal zuiDiCount = zuiDiMoney.get(symbol).divide(markPrice, getXSM(symbol), BigDecimal.ROUND_UP);
                            count = zuiDiCount;
//                            count = count.multiply(new BigDecimal(PrivateConfig.xiaoCangBeiShu)).min(zuiDiCount);
                        }
                    }

                    if(putAgain){
                        //只下单已知错误，未知错误不处理，是币安的锅
                        syncRequestClient.postOrder(
                                symbol,
                                OrderSide.valueOf(side),//买还是卖，做多时，buy sell；做空时，sell buy
                                PositionSide.valueOf(positionSide),//双向：long short 单向：both
                                OrderType.valueOf("MARKET"),// 订单类型，limit：限价单；MARKET：市价单（想要成功买卖，使用这个）
                                null,//TimeInForce.valueOf("GTC"),//成交为止，一直有效，不用管
                                count.toString(),//跟单数量（下单时，跟单个数一定是大于0的，通过AccountInformation查询到的，做空的个数小于0，做多的大于0）
                                null,//跟单单价，总价需要大于5（市价时，可以不填）
                                reduceOnly,//order.getReduceOnly().toString(), //双向持仓时，只能传null
                                null,//order.getClientOrderId(),
                                null,//order.getStopPrice().toString(),
                                null,//WorkingType.valueOf(order.getWorkingType()),
                                NewOrderRespType.RESULT);
                        System.out.println("下单了：" + PrivateConfig.getCurrentTime()+"-" + symbol+"-"+side+"-"+positionSide+"-"+count.toString()+"-"+reduceOnly);
                    }
                    return;
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

    public static boolean isMai(String side, String positionSide, BigDecimal realizedProfit){
        if (PositionSide.BOTH.toString().equals(positionSide)) {
            //单向
            if (realizedProfit.compareTo(PrivateConfig.ling) != 0) {
                return false;
            } else {
                return true;
            }
        }else {
            /*做多
            买：side=BUY,positionSide=LONG
            卖：side=SELL,positionSide=LONG

            做空
            买：side=SELL,positionSide=SHORT
            卖：side=BUY,positionSide=SHORT*/
            if((OrderSide.BUY.toString().equals(side) && PositionSide.LONG.toString().equals(positionSide))
                    || (OrderSide.SELL.toString().equals(side) && PositionSide.SHORT.toString().equals(positionSide))){
                return true;
            }else {
                return false;
            }

        }
    }

    public static boolean isDuo(String side, String positionSide, BigDecimal realizedProfit){
        if (PositionSide.BOTH.toString().equals(positionSide)) {
            //单向
            if ((realizedProfit.compareTo(PrivateConfig.ling) == 0 && OrderSide.BUY.toString().equals(side))
                    || realizedProfit.compareTo(PrivateConfig.ling) != 0 && OrderSide.SELL.toString().equals(side)) {
                return true;
            } else {
                return false;
            }
        }else {
            /*做多
            买：side=BUY,positionSide=LONG
            卖：side=SELL,positionSide=LONG

            做空
            买：side=SELL,positionSide=SHORT
            卖：side=BUY,positionSide=SHORT*/
            if(PositionSide.LONG.toString().equals(positionSide)){
                return true;
            }else {
                return false;
            }

        }
    }


    public  Map<String, String> getDuoKong(Boolean mai, Boolean duo) {
        String buy;
        String positionSide;
        Map map = new HashMap();
        if (mai) {
            if (duo) {
                buy = "BUY";
                positionSide = "LONG";
            } else {
                buy = "SELL";
                positionSide = "SHORT";
            }
        } else {
            if (duo) {
                buy = "SELL";
                positionSide = "LONG";
            } else {
                buy = "BUY";
                positionSide = "SHORT";
            }
        }
        map.put("buy", buy);
        map.put("positionSide", positionSide);
        return map;
    }

    private static Boolean jinTianDiaoYongGuo = false;
    public static void xsw(Boolean start){
        Long currentTime = System.currentTimeMillis();
        // 每天7到8点之间，调用一次
        if (start || (!jinTianDiaoYongGuo && (currentTime > getTodayStartTime(7, 0, 0) && currentTime < getTodayStartTime(8, 0, 0)))) {
            jinTianDiaoYongGuo = true;

            for (int i = 0; i < 3; i++) {
                try {
                    jgXsw.clear();
                    gsXsw.clear();
                    zuiDiMoney.clear();


                    RequestOptions options = new RequestOptions();
                    SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY,
                            options);
                    ExchangeInformation exchangeInformation = syncRequestClient.getExchangeInformation();
                    List<ExchangeInfoEntry> exchangeInfoEntryList = exchangeInformation.getSymbols();
                    System.out.println();
                    for(ExchangeInfoEntry entry : exchangeInfoEntryList) {

                        List<List<Map<String, String>>> filters = entry.getFilters();
                        String tick = "1";
                        for (List<Map<String, String>> filter : filters) {
                            for (Map<String, String> map : filter) {
                                if (map.keySet().contains("tickSize")) {
//                        System.out.println(entry.getSymbol());
                                    if ("1".equals(map.get("tickSize"))) {
                                        tick = "1";
                                    } else {
                                        tick = map.get("tickSize").split("1")[0] + "1";
                                    }
                                    break;
                                }
                            }

                            //查找每个币种最低购买金额
                            for (Map<String, String> map : filter) {
                                if (map.values().contains("MIN_NOTIONAL")) {
                                    for (Map<String, String> map1 : filter) {
                                        if(map1.get("notional") != null){
                                            zuiDiMoney.put(entry.getSymbol(), new BigDecimal(map1.get("notional")));
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                        jgXsw.put(entry.getSymbol(), get(tick));
                        gsXsw.put(entry.getSymbol(), Integer.parseInt(entry.getQuantityPrecision().toString()));
                    }
                    System.out.println("调用一次" + jgXsw.size() + ", " + getCurrentTime());
                    break;
                } catch (Exception e) {
                    try {
                        System.out.println(e.getMessage());
                        Thread.sleep(1000 * 3);
                    } catch (Exception e1) {

                    }
                }
            }
        }

        if(jgXsw.size()<=0){
            try {
                Thread.sleep(1000 * 3);
                if(jgXsw.size()<=0) {
                    T5.searchAll("获取小数位出错了，有问题");
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if(jinTianDiaoYongGuo && (currentTime > getTodayStartTime(9, 0, 0))){
            jinTianDiaoYongGuo = false;
        }

    }

    //个数小数位
    public static Integer getXSM(String symbol){
        if(gsXsw.get(symbol) != null){
            return gsXsw.get(symbol);
        }
        Integer xiaoShu = getXsw().get(symbol);
        if(xiaoShu == null){
            return 0;
        }else {
            return xiaoShu;
        }
    }

    //价格小数位
    public static Integer getJGXsw(String symbol) {
        if(jgXsw.get(symbol) != null){
            return jgXsw.get(symbol);
        }

        Map<String, Integer> map = new HashMap<>();
        if (getXsw != null) {
            Iterator it = getXsw.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
                map.put(entry.getKey(), Integer.parseInt(entry.getValue().toString().split(",")[0]));
            }
        }

        Integer xiaoShu = map.get(symbol);
        if(xiaoShu == null){
            return 0;
        }else {
            return xiaoShu;
        }
    }

    //价格小数位
    public static Map<String, Integer> getJGXsw() {

        Map<String, Integer> map = new HashMap<>();
        if (getXsw != null) {
            Iterator it = getXsw.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
                map.put(entry.getKey(), Integer.parseInt(entry.getValue().toString().split(",")[0]));
            }
        }

        return map;
    }

    //个数小数位
    public static Map<String, Integer> getXsw() {
//        https://www.binance.com/zh-CN/futures/XMR_USDT

        Map<String, Integer> map = new HashMap<>();

        if(getXsw != null){
            Iterator it =getXsw.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
                map.put(entry.getKey(), Integer.parseInt(entry.getValue().toString().split(",")[1]));
            }
        }

        return map;

    }

    /**
     * 获取当天的8点时间戳
     *
     * @return
     */
    public static long getTodayStartTime(int hour, int minute, int secont) {
        //设置时区
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, secont);
        return calendar.getTimeInMillis();
    }

    public static int get(String tick){
        int dcimalDigits = 0;
        int indexOf = tick.indexOf(".");
        if (indexOf > 0) {
            dcimalDigits = tick.length() - 1 - indexOf;
        }
        return dcimalDigits;
    }

    public static void logInit(String logName){
        try {
            logName = "log";
            String logPath = classPath + "//" + logName + ".txt";
            fileLog = new File(logPath);
            if (!fileLog.exists()) {
                fileLog.createNewFile();
            }
            fileWriter = new OutputStreamWriter(new FileOutputStream(fileLog, true),"gbk"); //gbk UTF-8

            String logPathJianKong = classPath + "//" + "JianKong.txt";
            fileLogJianKong = new File(logPathJianKong);
            if (!fileLogJianKong.exists()) {
                fileLogJianKong.createNewFile();
            }
            fileWriterJianKong = new OutputStreamWriter(new FileOutputStream(fileLogJianKong, true),"gbk"); //gbk UTF-8
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void init(String configPath) {

        JSONObject config = readJsonFile(configPath  + "//info.json");

        if (config.getString("daiLi") != null) {
            daiLi = config.getString("daiLi");
        }
        if(config.get("linux") != null){
            linux = config.getString("linux");
        }
        classPath = configPath;
        logInit("");
    }

    public static JSONArray readData() {

        JSONObject config = readJsonFile("E:/code/biance/data.json");

        return config.getJSONArray("data");
    }
    public static void getListNew(String configPath) {

        RequestOptions options = new RequestOptions();
        JSONObject config = readJsonFile(configPath);

        if(config.get("Leverage") != null){
            Leverage = config.getString("Leverage");
        }
        if(config.get("LeverageCount") != null){
            LeverageCount = config.getInteger("LeverageCount");
        }
        if(config.get("noLeverage") != null){
            noLeverage = config.getString("noLeverage");
        }
        if(config.get("remove") != null){
            remove = config.getString("remove");
        }
        if(config.get("isLinux") != null){
            isLinux = config.getInteger("isLinux");
        }
        if(config.get("zhiSunTime") != null){
            zhiSunTime = config.getInteger("zhiSunTime");
        }
        if(config.get("daYin") != null){
            daYin = config.getString("daYin");
        }
        if(config.get("ceShi") != null){
            ceShi = config.getString("ceShi");
        }
        if(config.get("time") != null){
            time = config.getString("time");
        }
        if(config.get("computer") != null){
            computer = config.getString("computer");
        }
        if(config.get("shiJian") != null){
            shiJian = config.getString("shiJian");
        }
        if(config.get("changeInitialLeverage") != null){
            changeInitialLeverage = config.getString("changeInitialLeverage");
        }if(config.get("buCang") != null){
            buCang = config.getString("buCang");
        }if(config.get("jianCang") != null){
            jianCang = config.getString("jianCang");
        }if(config.get("testGongNeng") != null){
            testGongNeng = config.getString("testGongNeng");
        }if(config.get("cmqUSDC") != null){
            cmqUSDC = config.getString("cmqUSDC");
        }if(config.get("gangGanSheZhi") != null){
            gangGanSheZhi = config.getString("gangGanSheZhi");
        }if(config.get("getPon") != null){
            getPon = config.getInteger("getPon");
        }if(config.get("xiaoCangBeiShu") != null){
            xiaoCangBeiShu = config.getString("xiaoCangBeiShu");
        }if(config.get("Leverage4164") != null){
            Leverage4164 = config.getString("Leverage4164");
        }if(config.get("LeverageCount4164") != null){
            LeverageCount4164 = config.getInteger("LeverageCount4164");
        }
        if(config.get("duo") != null){
            duo = config.getString("duo");
        }

        if(config.get("shao") != null){
            shao = config.getString("shao");
        }
        if(config.get("ip") != null){
            ip = config.getString("ip");
        }
        if(config.get("idCode") != null){
            idCode = config.getString("idCode");
        }
        if(config.get("password") != null){
            password = config.getString("password");
        }
        if(config.get("genTian") != null){
            genTian = config.getString("genTian");
        }
        if(config.get("JianKongSol") != null){
            JianKongSol = config.getString("JianKongSol");
        }
        if(config.get("kuai") != null){
            kuai = config.getString("kuai");
        }
        if(config.get("both") != null){
            both = config.getString("both");
        }
        if(config.get("zhiSun") != null){
            zhiSun = config.getString("zhiSun");
        }
        if(config.get("port") != null){
            port = config.getString("port");
        }

        if(config.get("startTime") != null){
            startTime = config.getString("startTime");
        }
        if(config.get("endTime") != null){
            endTime = config.getString("endTime");
        }
        getXsw = config.getJSONObject("getXsw");
        symbolLev = config.getJSONObject("symbolLev");
        getOKZhangShu = config.getJSONObject("getOKZhangShu");
        niuRens = config.getJSONArray("niuRens");
        standard = config.getJSONObject("standard");
        if (standard != null && standard.get(PrivateConfig.apiKey) != null) {
            standard.put(PrivateConfig.syncRequestClient, SyncRequestClient.create(standard.get(PrivateConfig.apiKey).toString(), standard.get(PrivateConfig.secretKey).toString(), options));
        }
        id349 = config.getString("id");

        ling035 = new BigDecimal(config.getString("ling035"));
        JSONArray personInfo = config.getJSONArray("personInfo");
        if(personInfo!=null && !CollectionUtils.isEmpty(personInfo)){
            List<JSONObject> list = new ArrayList<>();
            for(Object object : personInfo){
                JSONObject jsonObject = (JSONObject)object;
                jsonObject.put(PrivateConfig.syncRequestClient , SyncRequestClient.create(jsonObject.get(PrivateConfig.apiKey).toString(), jsonObject.get(PrivateConfig.secretKey).toString(), options));
                jsonObject.put(PrivateConfig.spotClient, new SpotClientImpl(jsonObject.get(PrivateConfig.apiKey).toString(), jsonObject.get(PrivateConfig.secretKey).toString()));
                jsonObject.put(PrivateConfig.newMap, new HashMap<>());
                jsonObject.put(PrivateConfig.cancleMap, new HashMap<>());
                list.add(jsonObject);
            }
            personInfoList = list;
        }


        if(config.getJSONObject("zhengQian") != null) {
            if (config.getJSONObject("zhengQian").getString("portfolioId") != null) {
                zhengQian_portfolioId = config.getJSONObject("zhengQian").getString("portfolioId");
            }
            if (config.getJSONObject("zhengQian").getString("costPerOrder") != null) {
                zhengQian_costPerOrder = config.getJSONObject("zhengQian").getInteger("costPerOrder");
            }
            if (config.getJSONObject("zhengQian").getString("investAmount") != null) {
                zhengQian_investAmount = config.getJSONObject("zhengQian").getInteger("investAmount");
            }
            if (config.getJSONObject("zhengQian").getString("copyModel") != null) {
                zhengQian_copyModel = config.getJSONObject("zhengQian").getString("copyModel");
            }if (config.getJSONObject("zhengQian").getString("inviteCode") != null) {
                zhengQian_inviteCode = config.getJSONObject("zhengQian").getString("inviteCode");
            }
            if (config.getJSONObject("zhengQian").getString("shui") != null) {
                zhengQian_shui = config.getJSONObject("zhengQian").getString("shui");
            }
        }

        if(config.getJSONObject("biCoin") != null) {
            if (config.getString("biCoinTogether") != null) {
                biCoinTogether = config.getString("biCoinTogether");
            }
            if (config.getJSONObject("biCoin").getString("symName") != null) {
                biCoin_symName = config.getJSONObject("biCoin").getString("symName");
            }if (config.getJSONObject("biCoin").getString("positionSide") != null) {
                biCoin_positionSide = config.getJSONObject("biCoin").getString("positionSide");
            }if (config.getJSONObject("biCoin").getString("isBiCoin") != null) {
                biCoin_isBiCoin = config.getJSONObject("biCoin").getString("isBiCoin");
            }
            if (StringUtils.isNotBlank(config.getJSONObject("biCoin").getString("token"))) {
                biCoin_token = config.getJSONObject("biCoin").getString("token");
            }
            if (config.getJSONObject("biCoin").getString("time2") != null) {
                biCoin_time2 = config.getJSONObject("biCoin").getString("time2");
            }
            if (config.getJSONObject("biCoin").getString("time3") != null) {
                biCoin_time3 = config.getJSONObject("biCoin").getString("time3");
            }

            if (config.getJSONObject("biCoin").getString("remove") != null) {
                biCoinremove = config.getJSONObject("biCoin").getString("remove");
            }

            JSONArray genDan_personInfo = config.getJSONObject("biCoin").getJSONArray("personInfo");
            List<JSONObject> genDan_list = new ArrayList<>();
            if(!CollectionUtils.isEmpty(genDan_personInfo)){
                for(Object object : genDan_personInfo){
                    JSONObject jsonObject = (JSONObject)object;
                    jsonObject.put(PrivateConfig.syncRequestClient , SyncRequestClient.create(jsonObject.get(PrivateConfig.apiKey).toString(), jsonObject.get(PrivateConfig.secretKey).toString(), options));
                    jsonObject.put(PrivateConfig.spotClient, new SpotClientImpl(jsonObject.get(PrivateConfig.apiKey).toString(), jsonObject.get(PrivateConfig.secretKey).toString()));
                    jsonObject.put(PrivateConfig.newMap, new HashMap<>());
                    jsonObject.put(PrivateConfig.cancleMap, new HashMap<>());
                    genDan_list.add(jsonObject);
                }
                biCoin_personInfoList = genDan_list;
            }
        }

        if(config.getJSONObject("biCoins") != null) {
            if (config.getJSONObject("biCoins").getString("isBiCoins") != null) {
                biCoins_isBiCoins = config.getJSONObject("biCoins").getString("isBiCoins");
            }if (config.getJSONObject("biCoins").getString("pageSize") != null) {
                biCoins_pageSize = config.getJSONObject("biCoins").getInteger("pageSize");
            }
            if (StringUtils.isNotBlank(config.getJSONObject("biCoins").getString("token"))) {
                biCoins_token = config.getJSONObject("biCoins").getString("token");
            }
            if (config.getJSONObject("biCoins").getString("time2") != null) {
                biCoins_time2 = config.getJSONObject("biCoins").getString("time2");
            }
            if (config.getJSONObject("biCoins").getString("time3") != null) {
                biCoins_time3 = config.getJSONObject("biCoins").getString("time3");
            }
            if (config.getJSONObject("biCoins").getString("remove") != null) {
                biCoins_remove = config.getJSONObject("biCoins").getString("remove");
            }

            JSONArray genDan_personInfo = config.getJSONObject("biCoins").getJSONArray("personInfo");
            if(!CollectionUtils.isEmpty(genDan_personInfo)){
                for(Object object : genDan_personInfo){
                    JSONObject jsonObject = (JSONObject)object;
                    jsonObject.put(PrivateConfig.syncRequestClient , SyncRequestClient.create(jsonObject.get(PrivateConfig.apiKey).toString(), jsonObject.get(PrivateConfig.secretKey).toString(), options));
                    jsonObject.put(PrivateConfig.spotClient, new SpotClientImpl(jsonObject.get(PrivateConfig.apiKey).toString(), jsonObject.get(PrivateConfig.secretKey).toString()));
                    biCoins_personInfoList.add(jsonObject);

                    String symName = jsonObject.getString("symName");
                    if(biCoins_gendanMap.containsKey(symName)){
                        List<JSONObject> symList = biCoins_gendanMap.get(symName);
                        symList.add(jsonObject);
                    }else {
                        List<JSONObject> symList = new ArrayList<>();
                        symList.add(jsonObject);
                        biCoins_gendanMap.put(symName, symList);
                    }
                }
            }
        }

        if(config.getJSONObject("ok") != null) {
            if (config.getJSONObject("ok").getString("isOk") != null) {
                ok_isOk = config.getJSONObject("ok").getString("isOk");
            }if (config.getJSONObject("ok").getString("pageSize") != null) {
                ok_pageSize = config.getJSONObject("ok").getInteger("pageSize");
            }
            if (config.getJSONObject("ok").getString("remove") != null) {
                ok_remove = config.getJSONObject("ok").getString("remove");
            }if (config.getJSONObject("ok").getString("genPortfolioId") != null) {
                ok_genPortfolioId = config.getJSONObject("ok").getString("genPortfolioId");
            }
            if (config.getJSONObject("ok").getString("position") != null) {
                ok_position = config.getJSONObject("ok").getString("position");
            }if (config.getJSONObject("ok").getString("zhuan") != null) {
                ok_zhuan = config.getJSONObject("ok").getString("zhuan");
            }
            if (config.getJSONObject("ok").getString("authorization") != null) {
                ok_authorization = config.getJSONObject("ok").getString("authorization");
            }
            if (config.getJSONObject("ok").getString("count") != null) {
                ok_count = config.getJSONObject("ok").getString("count");
            }

            ok_xianYou = config.getJSONObject("ok").getJSONArray("xianYou");

            JSONArray genDan_personInfo = config.getJSONObject("ok").getJSONArray("personInfo");
            if(!CollectionUtils.isEmpty(genDan_personInfo)){
                for(Object object : genDan_personInfo){
                    JSONObject jsonObject = (JSONObject)object;
                    jsonObject.put(PrivateConfig.syncRequestClient , SyncRequestClient.create(jsonObject.get(PrivateConfig.apiKey).toString(), jsonObject.get(PrivateConfig.secretKey).toString(), options));
                    jsonObject.put(PrivateConfig.spotClient, new SpotClientImpl(jsonObject.get(PrivateConfig.apiKey).toString(), jsonObject.get(PrivateConfig.secretKey).toString()));
                    ok_personInfoList.add(jsonObject);
                }
            }
        }

        if(config.getJSONObject("genDan") != null){
            if(config.getJSONObject("genDan").getString("portfolioId") != null){
                genDan_portfolioId = config.getJSONObject("genDan").getString("portfolioId");
            }

            if(config.getJSONObject("genDan").getString("genPortfolioId") != null){
                genDan_genPortfolioId = config.getJSONObject("genDan").getString("genPortfolioId");
            }if(config.getJSONObject("genDan").getString("money") != null){
                genDan_money = config.getJSONObject("genDan").getString("money");
            }

            if(StringUtils.isNotBlank(config.getJSONObject("genDan").getString("cookie"))){
                genDan_cookie = config.getJSONObject("genDan").getString("cookie");
            }

            if(StringUtils.isNotBlank(config.getJSONObject("genDan").getString("token") )){
                genDan_token = config.getJSONObject("genDan").getString("token");
            }
            if(config.getJSONObject("genDan").getString("position") != null){
                genDan_position = config.getJSONObject("genDan").getString("position");
            }
            if(config.getJSONObject("genDan").getString("urls") != null){
                genDan_urls = config.getJSONObject("genDan").getString("urls");
                genDan_url = genDan_urls.split(";")[genDan_urlIndex];
            }

            if(config.getJSONObject("genDan").getString("isGenDan") != null){
                genDan_isGenDan = config.getJSONObject("genDan").getString("isGenDan");
            }
            if(config.getJSONObject("genDan").getString("transferTime") != null){
                genDan_transferTime = config.getJSONObject("genDan").getLong("transferTime");
            }

            JSONArray genDan_personInfo = config.getJSONObject("genDan").getJSONArray("personInfo");
            List<JSONObject> genDan_list = new ArrayList<>();
            if(!CollectionUtils.isEmpty(genDan_personInfo)){
                for(Object object : genDan_personInfo){
                    JSONObject jsonObject = (JSONObject)object;
                    jsonObject.put(PrivateConfig.syncRequestClient , SyncRequestClient.create(jsonObject.get(PrivateConfig.apiKey).toString(), jsonObject.get(PrivateConfig.secretKey).toString(), options));
                    jsonObject.put(PrivateConfig.spotClient, new SpotClientImpl(jsonObject.get(PrivateConfig.apiKey).toString(), jsonObject.get(PrivateConfig.secretKey).toString()));
                    jsonObject.put(PrivateConfig.newMap, new HashMap<>());
                    jsonObject.put(PrivateConfig.cancleMap, new HashMap<>());
                    genDan_list.add(jsonObject);
                }
            }
            genDan_xianYou = config.getJSONObject("genDan").getJSONArray("xianYou");

            genDan_musk = config.getJSONArray("musk");
            muskUrl = config.getString("muskUrl");


            genDan_personInfoList = genDan_list;
        }

        if(config.getJSONObject("genDans") != null){
            if(config.getJSONObject("genDans").getString("isGenDans") != null){
                genDans_isGenDans = config.getJSONObject("genDans").getString("isGenDans");
            }

            if(config.getJSONObject("genDans").getString("unrealizedProfit") != null){
                genDans_unrealizedProfit = config.getJSONObject("genDans").getString("unrealizedProfit");
            }

            genDans_genPortfolioIds = config.getJSONObject("genDans").getJSONArray("genPortfolioIds");

            JSONArray genDan_personInfo = config.getJSONObject("genDans").getJSONArray("personInfo");
            List<JSONObject> genDans_list = new ArrayList<>();
            if(!CollectionUtils.isEmpty(genDan_personInfo)){
                for(Object object : genDan_personInfo){
                    JSONObject jsonObject = (JSONObject)object;
                    jsonObject.put(PrivateConfig.syncRequestClient , SyncRequestClient.create(jsonObject.get(PrivateConfig.apiKey).toString(), jsonObject.get(PrivateConfig.secretKey).toString(), options));
                    jsonObject.put(PrivateConfig.spotClient, new SpotClientImpl(jsonObject.get(PrivateConfig.apiKey).toString(), jsonObject.get(PrivateConfig.secretKey).toString()));
                    jsonObject.put(PrivateConfig.newMap, new HashMap<>());
                    jsonObject.put(PrivateConfig.cancleMap, new HashMap<>());
                    genDans_list.add(jsonObject);
                }
                genDans_personInfoList = genDans_list;
            }
            genDans_xianYou = config.getJSONObject("genDans").getJSONArray("xianYou");
        }

        if(config.getJSONObject("daiDanOk") != null){

            if(config.getJSONObject("daiDanOk").getString("isDaiDanOk") != null){
                daiDanOk_isDaiDanOk = config.getJSONObject("daiDanOk").getString("isDaiDanOk");
            }

            if(config.getJSONObject("daiDanOk").getString("genXianYou") != null){
                daiDanOk_genXianYou = config.getJSONObject("daiDanOk").getString("genXianYou");
            }

            JSONArray genDan_personInfo = config.getJSONObject("daiDanOk").getJSONArray("personInfo");
            List<JSONObject> genDans_list = new ArrayList<>();
            TradeAPIService tradeAPIService = new TradeAPIServiceImpl();
            if(!CollectionUtils.isEmpty(genDan_personInfo)){
                for(Object object : genDan_personInfo){
                    JSONObject jsonObject = (JSONObject)object;
                    APIClient client = new APIClient(config(jsonObject.getString(PrivateConfig.apiKey), jsonObject.getString(PrivateConfig.secretKey), jsonObject.getString(PrivateConfig.passphrase)));
                    jsonObject.put(PrivateConfig.tradeAPIService , tradeAPIService);
                    jsonObject.put(PrivateConfig.apiClient , client);
                    jsonObject.put(PrivateConfig.tradeAPI , client.createService(TradeAPI.class));

                    genDans_list.add(jsonObject);
                }
                daiDanOk_personInfoList = genDans_list;
            }
        }

        if(config.getJSONObject("analysis") != null){
            if(config.getJSONObject("analysis").getString("isAnalysis") != null){
                analysis_isAnalysis = config.getJSONObject("analysis").getString("isAnalysis");
            }
            if(config.getJSONObject("analysis").getString("API_KEY") != null){
                analysis_API_KEY = config.getJSONObject("analysis").getString("API_KEY");
            }
            if(config.getJSONObject("analysis").getString("secretKey") != null){
                analysis_secretKey = config.getJSONObject("analysis").getString("secretKey");
            }
            if(config.getJSONObject("analysis").getString("symbol") != null){
                analysis_symbol = config.getJSONObject("analysis").getString("symbol");
            }
            if(config.getJSONObject("analysis").getString("peopleCount") != null){
                analysis_peopleCount = config.getJSONObject("analysis").getInteger("peopleCount");
            }
            if(config.getJSONObject("analysis").getString("money") != null){
                analysis_money = config.getJSONObject("analysis").getBigDecimal("money");
            }
            analysis_genPortfolioIds = config.getJSONObject("analysis").getJSONArray("genPortfolioIds");

        }
    }

    //这个地方设置杠杆能够保证第一单成功，不用每次开新号设置杠杆了；但是当带单员从10-20-10，此时杠杆一直是20，正确的应该是10，所以还需要在监控里设置
    public static void gangGan(List<JSONObject> personInfoList, String symbol, BigDecimal gangGan){
        if("0".equals(gangGanSheZhi)){
            return;
        }
        if(gangGan == null){
            return;
        }
        if(gangGanMap.keySet().contains(symbol+gangGan.intValue())){
            return;
        }
        gangGanMap.put(symbol + gangGan, gangGan.intValue());
        try{
            for (JSONObject personInfo : personInfoList) {
                SyncRequestClient syncRequestClient = (SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient);
                syncRequestClient.changeInitialLeverage(symbol, gangGan.intValue());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void gangGanJianKong(List<JSONObject> personInfoList, Map<String, Position> mapTian){
        if("0".equals(gangGanSheZhi)){
            return;
        }
        try{

            for (JSONObject personInfo : personInfoList) {
                SyncRequestClient syncRequestClient = (SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient);
                for(Map.Entry<String, Position> entry : mapTian.entrySet()){
                    if(entry.getValue().getLeverage() != null){
                        syncRequestClient.changeInitialLeverage(entry.getValue().getSymbol(), entry.getValue().getLeverage().intValue());
                    }
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static APIConfiguration config(String apikey, String secretKey, String pass) {
        APIConfiguration config = new APIConfiguration();

        //传入https://www.okx.com 或 https://aws.okx.com
        //you can set the domain as https://www.okx.com or https://aws.okx.com
        config.setDomain("https://www.okx.com");


        config.setApiKey(apikey);
        config.setSecretKey(secretKey);
        config.setPassphrase(pass);

        //请求模拟盘的接口需要传入1，否则传入0
        //if you want to request the endpoint in demo trading,please input 1,otherwise,please input 0
        config.setxSimulatedTrading("0");

        //请求模拟盘的接口需要传入1，否则传入0
        //if you want to request the endpoint in demo trading,please input 1,otherwise,please input 0
        config.setxSimulatedTrading("0");


        config.setPrint(true);
        /* config.setI18n(I18nEnum.SIMPLIFIED_CHINESE);*/
        config.setI18n(I18nEnum.ENGLISH);
        return config;
    }

    /**
     *
     * @param personInfo
     * @param order 这是田的order
     */
    public static void cancleMap(JSONObject personInfo, Order order){
        //已创建的订单
        Map<String, Order> newMap = (Map<String, Order>) personInfo.get(PrivateConfig.newMap);
        //已撤销的订单
        Map<String, Order> cancleMap = (Map<String, Order>) personInfo.get(PrivateConfig.cancleMap);
        SyncRequestClient syncRequestClient = (SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient);
        if (!cancleMap.containsKey(personInfo.getString(PrivateConfig.name) + order.getOrderId())) {
            // 撤单，如何做到撤单成功？
            Long cancleId = order.getOrderId();
            Order myOrder = newMap.get(personInfo.getString(PrivateConfig.name) + cancleId);
            try {
                if (myOrder == null) {
                    cancleMap.put(personInfo.getString(PrivateConfig.name) + cancleId, null);
                    return;
                }
                Order myCancleOrder = syncRequestClient.cancelOrder(myOrder.getSymbol(), myOrder.getOrderId(), myOrder.getClientOrderId());
                cancleMap.put(personInfo.getString(PrivateConfig.name) + cancleId, myCancleOrder);
                print(personInfo, Thread.currentThread().getName() + "---撤单成功，没有问题！" + currentTime());
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getName() + "---有问题了3，撤单失败：" + e.toString() + "..." + myOrder.toString());
//                            T5.searchAll("撤单失败，有问题！" + e.getMessage());
            }
        }
    }

    public static void print(JSONObject personInfo, String msg){
        if(PrivateConfig.ceShi.equals("1")){
            System.out.println(personInfo.getString(PrivateConfig.alias) + msg);
        }
    }
    private static String currentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd = sdf.format(new Date(System.currentTimeMillis())); // 时间戳转换日期
        return sd;
    }


    public static Boolean isGua(Order order){

        if(order.getPrice() == null){
            return false;
        }

        //市价的价格是0，如果价格大于0，可以认为是挂单
        // 只要成交了，就认为是市价，所以挂单在我们这里状态必须是NEW
        // 当获取的频率很高时，市价也可能是NEW
        if(order.getPrice().compareTo(new BigDecimal("0"))>0 && "NEW".contains(order.getStatus())){
            return true;
        }
        return false;
    }

    public static Boolean isDuo3(Order order){
        if(PositionSide.BOTH.toString().equals(order.getPositionSide())){
            if(!order.getReduceOnly() && "BUY".equals(order.getSide())){
                return true;
            }
            if(order.getReduceOnly() && "SELL".equals(order.getSide())){
                return true;
            }
            return false;
        }else {
            if(PositionSide.LONG.toString().equals(order.getPositionSide())){
                return true;
            }else {
                return false;
            }

        }
    }

    public static void printLog(String msg) {
        System.out.println(msg);

        if(linux.equals("0")){
            PrivateConfig.fileLog.setLastModified(System.currentTimeMillis());
            try {
                fileWriter.write(getCurrentTime() + "\n" + msg + "\n");
                fileWriter.flush();
                PrivateConfig.fileLog.setLastModified(System.currentTimeMillis());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }
    public static void printLogJianKong() {

        PrivateConfig.fileLogJianKong.setLastModified(System.currentTimeMillis());
        try {
            fileWriterJianKong.write(getCurrentTime() + "\n" + "DiaoYong" + "\n");
            fileWriterJianKong.flush();
            PrivateConfig.fileLogJianKong.setLastModified(System.currentTimeMillis());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    public static void printLog1(String msg){
        if(PrivateConfig.ceShi.equals("1")){
            System.out.println(msg);
        }
    }

    public static void setBeiShu(ThreadPoolExecutor threadPoolExecutor) throws InterruptedException {
        SyncRequestClient syncRequestClientTian = ((SyncRequestClient) PrivateConfig.standard.get(PrivateConfig.syncRequestClient));
        AccountInformation accountInformationTian = PrivateConfig.getAccountInformation(syncRequestClientTian, threadPoolExecutor);
        List<Position> positionListTian = accountInformationTian.getPositions();
        boolean hasOrder = false;
        for (Position position : positionListTian) {
            if (position.getPositionAmt().abs().compareTo(ling) > 0) {
                hasOrder = true;
            }
        }
        if(!hasOrder) {
            BigDecimal tianMoney = accountInformationTian.getTotalMarginBalance();
            for (JSONObject personInfo : personInfoList) {
                BigDecimal myMoney = new BigDecimal(personInfo.getString("money"));
                personInfo.put(PrivateConfig.beiShu, myMoney.divide(tianMoney, 3, RoundingMode.HALF_UP));
                System.out.println(personInfo.getString(PrivateConfig.alias) + personInfo.getString(PrivateConfig.beiShu));
            }
        }
    }


    public static void setBeiShu(ThreadPoolExecutor threadPoolExecutor, BigDecimal laoShiMoney, List<JSONObject> personInfoList) throws InterruptedException {

        for (JSONObject personInfo : personInfoList) {
            try{
                SyncRequestClient syncRequestClient = (SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient);
                AccountInformation my = PrivateConfig.getAccountInformation(syncRequestClient, threadPoolExecutor);
                BigDecimal myMoney = my.getTotalMarginBalance();
                BigDecimal sheZhiMoney = personInfo.getBigDecimal("money");

                BigDecimal zhenShiBeiShu = personInfo.getBigDecimal(PrivateConfig.zhenShiBeiShu);
                BigDecimal dangQianBiLi = myMoney.multiply(zhenShiBeiShu).divide(laoShiMoney, 10, RoundingMode.HALF_UP);
                personInfo.put(PrivateConfig.beiShu, sheZhiMoney.min(dangQianBiLi));
            }catch (Exception e){
                PrivateConfig.printLog(e.getMessage());
            }
            Thread.sleep(100);
        }
        for (JSONObject personInfo : personInfoList) {
            System.out.println(personInfo.getString(PrivateConfig.alias) + personInfo.getString(PrivateConfig.beiShu));
        }
    }

    public static void setStartBeiShu(List<JSONObject> personInfoList, JSONObject startBeiShu){

        for (JSONObject personInfo : personInfoList) {
            try{
                String apikey = personInfo.getString(apiKey);
                String dangQianBiLi = startBeiShu.getString(apikey);
                if(StringUtils.isNotBlank(dangQianBiLi)){
                    personInfo.put(PrivateConfig.beiShu, dangQianBiLi);
                }
            }catch (Exception e){
                PrivateConfig.printLog(e.getMessage());
            }
        }

        for (JSONObject personInfo : personInfoList) {
            System.out.println(personInfo.getString(PrivateConfig.alias) + personInfo.getString(PrivateConfig.beiShu));
        }
    }

    public static void setBeiShuOk(BigDecimal laoShiMoney, List<JSONObject> personInfoList) throws InterruptedException {

        for (JSONObject personInfo : personInfoList) {
            try{
                TradeAPIService tradeAPIService = (TradeAPIService) personInfo.get(PrivateConfig.tradeAPIService);
                APIClient apiClient = (APIClient) personInfo.get(PrivateConfig.apiClient);
                TradeAPI tradeAPI = (TradeAPI) personInfo.get(PrivateConfig.tradeAPI);
                JSONObject result = tradeAPIService.getAccountAndPosition(apiClient, tradeAPI, "SWAP");
                if(result.getString("code").equals("0")) {
                    JSONArray data = result.getJSONArray("data");
                    BigDecimal myMoney = data.getJSONObject(0).getJSONArray("balData").getJSONObject(0).getBigDecimal("eq");
                    BigDecimal sheZhiMoney = personInfo.getBigDecimal("money");

                    BigDecimal zhenShiBeiShu = personInfo.getBigDecimal(PrivateConfig.zhenShiBeiShu);
                    BigDecimal dangQianBiLi = myMoney.multiply(zhenShiBeiShu).divide(laoShiMoney, 10, RoundingMode.HALF_UP);
                    personInfo.put(PrivateConfig.beiShu, sheZhiMoney.min(dangQianBiLi));
                }
            }catch (Exception e){
                PrivateConfig.printLog(e.getMessage());
            }
            Thread.sleep(100);
        }
    }

    public static JSONArray reBuiltJsonArray(JSONArray array){
        if("0".equals(cmqUSDC)){
            return array;
        }
        for (int i = 0; i < array.size(); i++) {
            JSONObject item = array.getJSONObject(i);
            if(item.getString(symbol).endsWith("USDC")){
                item.put(symbol, item.getString(symbol).replace("USDC", "USDT"));
            }
            item.put(positionSide, getPositionSide(item.getString(positionSide), item.getBigDecimal(positionAmount)));
        }
        Map<String, JSONObject> map = new HashMap<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject item = array.getJSONObject(i);
            String symbolPosition = item.getString(symbol) + "_" + item.getString(positionSide);
            if(map.keySet().contains(symbolPosition)){
                JSONObject itemOld = map.get(symbolPosition);
                itemOld.put(positionAmount, item.getBigDecimal(positionAmount).add(itemOld.getBigDecimal(positionAmount)));
            }else {
                map.put(symbolPosition, new JSONObject(item));
            }
        }
        JSONArray jsonArray = new JSONArray();
        for(Map.Entry<String, JSONObject> entry : map.entrySet()){
            jsonArray.add(entry.getValue());
        }
        return jsonArray;
    }

    public static void setBeiShu(ThreadPoolExecutor threadPoolExecutor, List<JSONObject> personInfoList) {
        try {
            BigDecimal laoShiMoney = null;
            if (PrivateConfig.genDan_isGenDan.equals("1")) {
                JSONArray jsonArray = GetPositions.getOrders(threadPoolExecutor, genDan_genPortfolioId);
                if (CollectionUtils.isEmpty(jsonArray)) {
                    laoShiMoney = com.example.bian.genDan.Constants.getLaoShiMoney(genDan_genPortfolioId);
                }
            } else if (PrivateConfig.ok_isOk.equals("1")) {
                JSONArray jsonArray = GetOKPositions.getOrders(threadPoolExecutor, ok_genPortfolioId, true, false);
                if (CollectionUtils.isEmpty(jsonArray)) {
                    laoShiMoney = com.example.bian.ok.Constants.getLaoShiMoney();
                }
            }
            if (laoShiMoney != null) {
                PrivateConfig.setBeiShu(threadPoolExecutor, laoShiMoney, personInfoList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (JSONObject personInfo : personInfoList) {
            System.out.println(personInfo.getString(PrivateConfig.alias) + personInfo.getString(PrivateConfig.beiShu));
        }
    }


    public static void printLog(Writer fileWriter, Exception e) {
        /*try {
            fileWriter.write(getCurrentTime());
            if(e.getMessage() != null){
                fileWriter.write(e.getMessage());
            }
            for (int i1 = 0; i1 < e.getStackTrace().length; i1++) {
                fileWriter.write(e.getStackTrace()[i1].getLineNumber());
            }
            fileWriter.write("\n");
            fileWriter.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }*/
    }

    public static void printLog(Writer fileWriter, String text) {
//        PrivateConfig.fileLog.setLastModified(System.currentTimeMillis());
        /*try {
            fileWriter.write(getCurrentTime() + "\n" + text + "\n");
            fileWriter.flush();
            PrivateConfig.fileLog.setLastModified(System.currentTimeMillis());
        } catch (IOException e1) {
            e1.printStackTrace();
        }*/
    }

    public static void printLog1(Writer fileWriter, String text) {
        /*try {
            fileWriter.write(text + "\n");
            fileWriter.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }*/
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis())); // 时间戳转换日期
    }


    // 测试用 胡亚龙
    public static final String API_KEY = "fz5Wl4Jg6VYrxoyyHcu4imA0RYT77TEncjf1CYLqVZBCyXYswjRg5YP9CQALtrTV";
    public static final String SECRET_KEY = "uDGTrk1C30hXEiQr3xXjg9PxW0Yv11s4eXoFH9KOesRKfjhhNJmJRbFvhWH7Totg";

    // 杰哥
   /* public static final String API_KEY = "oVRZJW4Q7DBaMNWiCizyyEhyiM8KPeqZztb4Xaq3jfsnC72MlGX2THGOMcsFwC5Z";
    public static final String SECRET_KEY = "MVZuD9h6w3Q2aqa16x6XP2Qwd5NMxKWJrVFFGFF9TX9wJGfMJQhkIuCdnxfsPjuu";*/


    /**
     * 读取json文件，返回json串
     * @param fileName
     * @return
     */
    public static JSONObject readJsonFile(String fileName) {
        String jsonStr = "";
        try {
//            fileWriter.write("du qu json" + "\n");
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }

            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            JSONObject jobj = JSON.parseObject(jsonStr);
            return jobj;
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public static List<JSONObject> getList(){
        RequestOptions options = new RequestOptions();

        for(JSONObject jsonObject : list){
            jsonObject.put(PrivateConfig.syncRequestClient , SyncRequestClient.create(jsonObject.get(PrivateConfig.apiKey).toString(), jsonObject.get(PrivateConfig.secretKey).toString(), options));
            jsonObject.put(PrivateConfig.newMap, new HashMap<>());
            jsonObject.put(PrivateConfig.cancleMap, new HashMap<>());
        }

        return list;
    }

    private static List<JSONObject> list = new ArrayList<JSONObject>(){
        private static final long serialVersionUID = 6275850885615832771L;
        {

            /*田书成 小号*/
            add(PrivateConfig.get1("tianShuChengXiaoHao",
                    "田书成 小号",
                    "yJ7jbmQMysHTEnDXXFCnwR80XMuT7RUg8QErnWTRv8YpZmGbeua1DvVq7xmFvSUK",
                    "tLlmJ2aEW66jQdEFwznImR6y4kR4U79jtAFEl9PAiA4WNSBLhbKearKtSht32656" ,
                    "0.3"));

            /*田书成 大号*/
            add(PrivateConfig.get1("tianShuCheng",
                    "田书成 大号",
                    "ia2szD89gbEN1GVuz1jTqCDakk5yOdVrYcJpvUjrhbSmhH4Zw4JkyxYm6OygXwmM",
                    "3j90FA0D8blQMAwZk9HJzDn1lSoMsH3h5RkZrpTlz45otceBL99lvqSEvGdAfBbI" ,
                    "0.3"));

        }};

    public static JSONObject get1(String... args){
        JSONObject j1 = new JSONObject();
        j1.put(PrivateConfig.name, args[0]);
        j1.put(PrivateConfig.alias, args[1]);
        j1.put(PrivateConfig.apiKey, args[2]);
        j1.put(PrivateConfig.secretKey, args[3]);
        j1.put(PrivateConfig.beiShu, args[4]);
        return j1;
    }

    /**
     * 判断时间是否在多少秒内
     * @param time
     * @param second
     * @return
     */
    public static boolean compareTime(Long time, int second){
        if(System.currentTimeMillis() - time < second * 1000){
            return true;
        }else {
            return false;
        }
    }

    public static boolean compareTimeDay(Long time, int day){
        if(System.currentTimeMillis() - time < day * 86400 * 1000){
            return true;
        }else {
            return false;
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {

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

        /*ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(5, 5, 10,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.DiscardPolicy());

        setBeiShu(threadPoolExecutor, PrivateConfig.genDan_personInfoList);*/


        RequestOptions options = new RequestOptions();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, options);

        postOrder(syncRequestClient, "USDCUSDT", OrderSide.SELL.toString(), PositionSide.LONG.toString(), null, "5");

        System.out.println();
    }

};

