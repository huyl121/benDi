package com.example.bian;

import com.alibaba.fastjson.JSONArray;
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
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import static com.example.bian.BianController.fileWriter;

import static com.example.bian.client.bushu.PrivateConfig.getXSM;

/**
 * Created by adimn on 2021/7/30.
 */
@RestController
@CrossOrigin
@Controller
public class BianController {

    static List<JSONObject> listPersonInfo;
    static ThreadPoolExecutor threadPoolExecutor;
    static FileWriter fileWriter;
    static BigDecimal ling = new BigDecimal(0);
    static BigDecimal ling05 = new BigDecimal("0.05");
    static Map<String, BigDecimal> lsChiYou = new HashMap<>();
    static Map<String, Boolean> lsDuoKong = new HashMap<>();
    static String classPath;
    static String classPathLog;
    static String classPathInfo;

       static Boolean tiaoShi = false;
//     static   Boolean tiaoShi = true;

      static  Long count = 0L;
      static  int error = 1;//连续3次访问api失败，报警

    @RequestMapping(value = "/genDanOld", method = RequestMethod.GET)
    @ResponseBody
    void genDan(@RequestParam("data") String data){

        try {
            T5.sendMe("测试");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("跟单:" + getCurrentTime());
        System.out.println("我们持有：" + lsChiYou.toString());
        System.out.println("老师持有：" + data);
        method(stringToArray(data));

    }



    @RequestMapping(value = "/jianKongOld", method = RequestMethod.GET)
    @ResponseBody
    void jianKong(@RequestParam("data") String data) throws InterruptedException {

        jiaoYan(stringToArray(data));
        writeLog();
    }




    public void method(JSONArray jsonArray)  {
        // 变量
        Boolean duo;
        Boolean mai;
        String symbolNew = "";
        BigDecimal geShu = new BigDecimal(0);
        try {
            //执行
            for (Object o : jsonArray) {

                JSONObject ls = (JSONObject) o;
                symbolNew = ls.getString("symbol");
                //老师持仓的个数
                BigDecimal positionAmtNew = ls.getBigDecimal("positionAmt");
                if (positionAmtNew.compareTo(ling) < 0) {
                    duo = false;
                } else {
                    duo = true;
                }
                //如果之前做过，那么看看是否由多转空
                if (lsDuoKong.keySet().contains(symbolNew)) {
                    if (!lsDuoKong.get(symbolNew).equals(duo)) {
                        //做了相反的操作，首先清空
                        Map<String, String> duoKong = getDuoKong(false, lsDuoKong.get(symbolNew));
                        execute(BianController.listPersonInfo, threadPoolExecutor, symbolNew, lsChiYou.get(symbolNew), duoKong.get("buy"), duoKong.get("positionSide"), true);
                        lsChiYou.remove(symbolNew);
                        lsDuoKong.remove(symbolNew);
                    }
                }

                if (!lsChiYou.containsKey(symbolNew)) {
                    //如果持仓里没有，证明是新建的仓，需要跟，而且肯定是买
                    mai = true;
                    geShu = positionAmtNew.abs();
                } else {
                    //查看此时的新持仓和老持仓是否相同，如果不同，证明有操作，需要跟
                    BigDecimal positionAmtOld = lsChiYou.get(symbolNew);
                    BigDecimal biLi = positionAmtOld.subtract(positionAmtNew).abs().divide(positionAmtNew.abs(), 4, BigDecimal.ROUND_HALF_UP);
                    if (biLi.abs().compareTo(new BigDecimal("0.02")) > 0) {
                        BigDecimal bOld = positionAmtOld.abs();
                        BigDecimal bNew = positionAmtNew.abs();
                        if (bOld.compareTo(bNew) > 0) {
                            //新的少了，证明有卖
                            mai = false;
                            geShu = bOld.subtract(bNew);
                        } else {
                            mai = true;
                            geShu = bNew.subtract(bOld);
                        }
                    } else {
                        //相等时不操作
                        continue;
                    }
                }

                //如果代码到了这里，证明老师有操作，记录下老师的最新持仓，然后跟
                lsChiYou.put(symbolNew, ls.getBigDecimal("positionAmt"));
                lsDuoKong.put(symbolNew, duo);
                Map<String, String> duoKong = getDuoKong(mai, duo);
                if (!tiaoShi) {
                    execute(BianController.listPersonInfo, threadPoolExecutor, symbolNew, geShu, duoKong.get("buy"), duoKong.get("positionSide"), false);
                }
            }

            // 如果老师清仓了，而我们没有清时，我们要清仓
            if (jsonArray.size() != lsChiYou.size()) {
                List<String> lsSymbol = new ArrayList<>();
                for (Object o : jsonArray) {
                    JSONObject ls = (JSONObject) o;
                    lsSymbol.add(ls.getString("symbol"));
                }
                Iterator<Map.Entry<String, BigDecimal>> iterator = lsChiYou.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, BigDecimal> old = iterator.next();
                    if (!lsSymbol.contains(old.getKey())) {
                        if (old.getValue().compareTo(ling) != 0) {
                            mai = false;
                            symbolNew = old.getKey();
                            // 持仓的个数
                            BigDecimal positionAmtNew = old.getValue();
                            if (positionAmtNew.compareTo(ling) < 0) {
                                duo = false;
                            } else {
                                duo = true;
                            }
                            Map<String, String> duoKong = getDuoKong(mai, duo);
                            geShu = positionAmtNew.abs();
                            if (!tiaoShi) {
                                execute(BianController.listPersonInfo, threadPoolExecutor, symbolNew, geShu, duoKong.get("buy"), duoKong.get("positionSide"), true);
                            }
                        }
                        //移除之前的多还是空
                        lsDuoKong.remove(old.getKey());
                        iterator.remove();
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            try {
                fileWriter.write(e.getMessage());
            }catch (Exception e1){
                e1.printStackTrace();
                System.out.println("写日志错误");
            }
        }

    }

    public void jiaoYan(JSONArray jsonArrayLs) throws InterruptedException {
        System.out.println(getCurrentTime() + "正在监控");
        for (JSONObject personInfo : listPersonInfo) {
            if (!"tianShuChengXiaoHao".equals(personInfo.getString(PrivateConfig.name))) {
                continue;
            }
            SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient));
            AccountInformation accountInformation;
            try {
                accountInformation = syncRequestClient.getAccountInformation();
            } catch (Exception e) {
                e.printStackTrace();
                error++;
                if (error > 3) {
                    String msg = personInfo.getString(PrivateConfig.alias) + "的api访问失败";
                    System.out.println(msg);
                    System.out.println(e);
                    T5.sendMe(msg + e);
                    Thread.sleep(100);
                    error = 1;
                    return;
                }
                continue;
            }
            error = 1;
            List<Position> positionList = accountInformation.getPositions();

            if (jsonArrayLs.size() == 0) {
                Boolean hasProblem = false;
                //老师没有持仓，我们有持仓时报错
                for (Position position : positionList) {
                    if (position.getPositionAmt().abs().compareTo(ling) > 0) {
                        String msg = personInfo.getString(PrivateConfig.alias) + "，老师没有" + position.getSymbol() + "，报警，手动平仓";
                        System.out.println(msg);
                        T5.sendMe(msg);
                        Thread.sleep(100);
                        hasProblem = true;
                    }
                }
                if (!hasProblem) {
                    System.out.println(personInfo.getString(PrivateConfig.alias) + "和老师都没有持仓");
    //                                T5.sendMe("[呲牙][坏笑]" + "");
                }
            } else {
                BigDecimal beiShu = new BigDecimal(personInfo.getString(PrivateConfig.beiShu));
                List<String> symbolListLs = new ArrayList<>();
                for (Object o : jsonArrayLs) {
                    JSONObject ls = (JSONObject) o;
                    String symbol = ls.getString("symbol");
                    symbolListLs.add(symbol);
                    //老师的数量乘以倍数就是我们应该有的数量
                    BigDecimal yingGaiYou = ls.getBigDecimal("positionAmt").multiply(beiShu).abs();
                    Boolean hasProblem = true;
                    for (Position position : positionList) {
                        BigDecimal you = position.getPositionAmt().abs();
                        if (symbol.equals(position.getSymbol()) && you.abs().compareTo(ling) > 0) {
                            if (yingGaiYou.subtract(you).abs().divide(you.abs(), 5, BigDecimal.ROUND_HALF_UP).compareTo(ling05) > 0) {
                                String msg = personInfo.getString(PrivateConfig.alias) + "，" + position.getSymbol() + "和老师个数不同" + "，应该有" + yingGaiYou + "个，现在有" + you + "个";
                                System.out.println(msg);
                                T5.sendMe(msg);
                                Thread.sleep(100);
                                hasProblem = false;
                                break;
                            } else {
                                //和老师相同就退出
                                hasProblem = false;
                                System.out.println(personInfo.getString(PrivateConfig.alias) + "的" + symbol + "和老师相同，没有问题");
                                break;
                            }
                        }
                    }
                    if (hasProblem) {
                        String msg = personInfo.getString(PrivateConfig.alias) + "应该有" + yingGaiYou + "个" + symbol + "，现在没有。";
                        System.out.println(msg);
                        T5.sendMe(msg);
                        Thread.sleep(100);
                    }
                }
                // 如果和老师都有，并且symbol不同时
                for (Position position : positionList) {
                    BigDecimal you = position.getPositionAmt().abs();
                    if (you.abs().compareTo(ling) > 0) {
                        if (!symbolListLs.contains(position.getSymbol())) {
                            String msg = personInfo.getString(PrivateConfig.alias) + "，老师没有" + position.getSymbol() + "，报警，手动平仓";
                            System.out.println(msg);
                            T5.sendMe(msg);
                            Thread.sleep(100);
                        }
                    }
                }

            }
        }
        if (error != 1) {
            System.out.println("有错误了");
            jiaoYan(jsonArrayLs);
        }

    }

    public  void execute(List<JSONObject> listPersonInfo, ThreadPoolExecutor threadPoolExecutor, String symbol, BigDecimal geShu, String buy, String positionSide, Boolean qingCang) {

        if(tiaoShi){
            return;
        }
        for (JSONObject personInfo : listPersonInfo) {
            /*if("huYaLong".equals(personInfo.getString(PrivateConfig.name))){
                if(!"BTCUSDT/ETHUSDT".contains(symbol)){
                    continue;
                }
            }*/
            personInfo.put("symbol", symbol);
            personInfo.put("geShu", new BigDecimal(geShu.stripTrailingZeros().toPlainString()));
            personInfo.put("buy", buy);
            personInfo.put("positionSide", positionSide);
            personInfo.put("qingCang", qingCang);
            System.out.println("执行：" + personInfo.getString(PrivateConfig.alias));
            System.out.println("线程使用个数：" + threadPoolExecutor.getActiveCount());
            MulTradeOrder mulTradeOrder = new MulTradeOrder(personInfo);
            threadPoolExecutor.submit(mulTradeOrder);

        }
    }

    public String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis())); // 时间戳转换日期
    }

    public  String getCurrentData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(System.currentTimeMillis())); // 时间戳转换日期
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

    public JSONArray stringToArray(String data){
        if (StringUtils.isEmpty(data)) {
            data = "";
        }
        String[] aa = data.split(",");
        JSONArray jsonArrayLS = new JSONArray();
        for (int i = 0; i < (aa.length) / 2; i++) {
            int j = i * 2;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("symbol", aa[j].split("USDT")[0] + "USDT");
            jsonObject.put("positionAmt", aa[j + 1]);
            jsonArrayLS.add(jsonObject);
        }
        return jsonArrayLS;
    }
    /**
     * 更新日志文件，当log文件不更新时，证明跟单有问题了
     */
    public void writeLog() {
        try {
            fileWriter.write("1");
            fileWriter.flush();
//            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("刷新日志文件报错");
            try {
                File file = new File(classPathLog);
                if (!file.exists()) {
                    file.createNewFile();
                }
                fileWriter = new FileWriter(file, true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void main(String[] agrs) throws InterruptedException {

        T5.sendMe("测试。");

//        BianController bianController = new BianController();
//        String data = "CHRUSDT(全仓),-32911.00000,CHRUSDT（半仓）,-32911.00000,";
//        String data = "CHRUSDT(全仓),-32911.00000,CHRUSDT（半仓）,-32911.00000,";
//        bianController.genDan(data);
    }



}


class MulTradeOrder implements Callable {
    static Boolean tiaoShi = false;//按照造的参数执行

    JSONObject personInfo;

    MulTradeOrder (JSONObject jsonObject){
        this.personInfo = jsonObject;
    }

    @Override
    public Object call() throws Exception {
        try {
            if (tiaoShi) {
                return 0;
            }
            JSONObject error = new JSONObject();
            BigDecimal geShu = personInfo.getBigDecimal("geShu").abs();
            String symbol = personInfo.getString("symbol");
            String buy = personInfo.getString("buy");
            String positionSide = personInfo.getString("positionSide");
            if (StringUtils.isEmpty(symbol) || StringUtils.isEmpty(buy) || StringUtils.isEmpty(positionSide)) {
                System.out.println("symbol：" + symbol + "，buy：" + buy + "，positionSide：" + positionSide);
                return 0;
            }
            SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient));

            if (personInfo.getBoolean("qingCang")) {
                System.out.println(getCurrentTime() + "-------------------平仓------------------");
                AccountInformation accountInformation = syncRequestClient.getAccountInformation();
                List<Position> positionList = accountInformation.getPositions();
                for (Position position : positionList) {
                    geShu = position.getPositionAmt().abs();
                    if (symbol.equals(position.getSymbol()) && geShu.compareTo(new BigDecimal("0")) > 0 && positionSide.equals(position.getPositionSide())) {
                        Order myOrderQingCang = syncRequestClient.postOrder(
                                symbol,
                                OrderSide.valueOf(buy),//买还是卖
                                PositionSide.valueOf(positionSide),//做多还是做空 long short both
                                OrderType.valueOf("MARKET"),// 订单类型，limit：限价单；MARKET：市价单（想要成功买卖，使用这个）
                                null,//TimeInForce.valueOf("GTC"),//成交为止，一直有效，不用管
                                geShu.toString(),//跟单数量，需要大于5（跟单个数一定是大于0的，通过AccountInformation查询到的，做空的个数小于0，做多的大于0）
                                null,//跟单单价，总价需要大于5（市价时，可以不填）
                                null,//order.getReduceOnly().toString(),
                                null,//order.getClientOrderId(),
                                null,//order.getStopPrice().toString(),
                                null,//WorkingType.valueOf(order.getWorkingType()),
                                NewOrderRespType.RESULT);
                        System.out.println(myOrderQingCang.toString());
                        break;
                    }
                }
                return 0;
            }


            if (personInfo.getString(PrivateConfig.beiShu) != null) {
                BigDecimal beiShu = new BigDecimal(personInfo.getString(PrivateConfig.beiShu));
                geShu = geShu.multiply(beiShu);
            } else {
                geShu = geShu.movePointLeft(2);
            }
            String getOrigQty = geShu.setScale(getXSM(symbol), BigDecimal.ROUND_DOWN).toString();

            System.out.println(getCurrentTime() + "，老师购买个数：" + geShu + "；" + personInfo.getString(PrivateConfig.name) + "购买个数为：" + getOrigQty);
            if (!tiaoShi) {
                try {
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
                } catch (Exception e) {
                    e.printStackTrace();
                    fileWriter.write(e.getMessage());
                }

            }
            error.put("code", "0");
            return error;
        }catch (Exception e){
            e.printStackTrace();
            fileWriter.write(e.getMessage());
        }
        return 0;
    }

    public String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis())); // 时间戳转换日期
    }



}

