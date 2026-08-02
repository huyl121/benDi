package com.example.bian;

import com.alibaba.fastjson.JSON;
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
import okhttp3.*;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import static com.example.bian.client.bushu.PrivateConfig.getXSM;

/**
 * 根据老师现有的持仓，自己分析他是加仓还是减仓
 */
public class GenDan6 {

    String token = "";
    static Integer loginCount = 0;
    public static Boolean gameOver = false;

    BigDecimal ling = new BigDecimal(0);
    Long i = 1L;
    // 变量
    JSONArray jsonArray;


    String name = "";
    static Map<String, BigDecimal> lsChiYou = new HashMap<>();
    static Map<String, Boolean> lsDuoKong = new HashMap<>();




    public static void main(String[] args) throws IOException, InterruptedException {

        args = new String[1];
        System.out.println("开始啦");
        args[0] = "E://baidutongbu//baidutongbu//tongbu//bian//bian";
        PrivateConfig.before(args[0] + "//info.json", args[1]);

        // 准备工作
        // 对https也开启代理
        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10819");


        GenDan6 genDan6 = new GenDan6();

        genDan6.method(args);

    }

    public void method(String[] args) {
        try {

            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Response response = login(client);
            Request lsRequest = getLsRequest(response);

            List<JSONObject> listPersonInfo = PrivateConfig.personInfoList;


            for (JSONObject personInfo : listPersonInfo) {
                name += personInfo.getString(PrivateConfig.alias) + "，" + personInfo.getString(PrivateConfig.beiShu) + "，" ;
                if ("tianShuChengXiaoHao".equals(personInfo.getString(PrivateConfig.name))) {
                    SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient));
                    AccountInformation accountInformation = syncRequestClient.getAccountInformation();
                    List<Position> positionList = accountInformation.getPositions();
                    BigDecimal beiShu = new BigDecimal(personInfo.getString(PrivateConfig.beiShu));
                    for (Position position : positionList) {
                        if (position.getPositionAmt().abs().compareTo(new BigDecimal("0")) > 0) {
                            lsChiYou.put(position.getSymbol(), position.getPositionAmt().divide(beiShu, 4, BigDecimal.ROUND_HALF_UP));
                            BigDecimal positionAmtOld = position.getPositionAmt();
                            Boolean duo = false;
                            if (positionAmtOld.compareTo(new BigDecimal("0")) < 0) {
                                duo = false;
                            } else {
                                duo = true;
                            }
                            lsDuoKong.put(position.getSymbol(), duo);
                        }
                    }
                }
            }



            while (true) {
                if(gameOver) {
                    T5.sendMe("密码变更后，跟单停止了");
                    return;
                }
                try {
                    try {
                        if(lsChiYou.isEmpty()) {
                            //如果手里没有单子了 每天凌晨到8点之间，不调用。如果有单子会把当前的单子做完
                            if ("1".equals(PrivateConfig.time)) {
                                Long currentTime = System.currentTimeMillis();
                                if (!(currentTime > getTodayStartTime(PrivateConfig.startTime) && currentTime < getTodayStartTime(PrivateConfig.endTime))) {
                                    System.out.println("到时间了，结束");
                                    T5.sendMe("到时间了，跟单结束");
                                    Thread.sleep(60 * 1000);
                                    return;
                                }
                            }
                        }

                        Thread.sleep(1500);

                        Call callLs = client.newCall(lsRequest);
                        Response responseLs = callLs.execute();
                        String result = responseLs.body().string();
                        JSONObject jsonLs = JSON.parseObject(result);
                        jsonArray = ((JSONArray) jsonLs.get("data"));

                        zhiXing(jsonArray);

                        //监控，只是辅助，尽量不影响跟单
                        if (i % 25 == 0) {
                            JianKong6 jianKong = new JianKong6(jsonArray, PrivateConfig.personInfoList);
                            PrivateConfig.threadPoolExecutor.submit(jianKong);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        response = login(client);
                        lsRequest = getLsRequest(response);
                        PrivateConfig.printLog(PrivateConfig.fileWriter, e);
                    }

                    i++;
                    if(i % 40 == 0) {
                        System.out.println(getCurrentTime() + name + "正在跟单");
                        System.out.println("持有：" + lsChiYou.toString());
                        System.out.println("线程池里正在运行的线程个数（任务结束时处于待机状态的线程不在此里）" + PrivateConfig.threadPoolExecutor.getActiveCount());
                    }
                    /*if (i % 40 == 0) {
                        try {
                            fileWriter.write("1");
                            fileWriter.flush();
                            fileWriter.close();
                        } catch (Exception e) {
                            fileLog = new File(logPath);
                            if (!fileLog.exists()) {
                                fileLog.createNewFile();
                            }
                            System.out.println("刷新日志文件报错");
                        } finally {
                            fileWriter = new OutputStreamWriter(new FileOutputStream(fileLog, true),"gbk"); //gbk UTF-8
                        }
                    }*/

                    if (i > 60*60*5) {
                        //每隔一段时间重新登陆一下
                        i = 1L;
                        response = login(client);
                        lsRequest = getLsRequest(response);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    PrivateConfig.printLog(PrivateConfig.fileWriter, e);
                }catch (Throwable t){
                    t.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            PrivateConfig.printLog(PrivateConfig.fileWriter, e);
        }
    }

    public void zhiXing(JSONArray jsonArray) {
        try {
            Boolean duo;
            Boolean mai;
            String symbolNew = "";
            BigDecimal geShu = new BigDecimal(0);
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
                if (lsDuoKong.containsKey(symbolNew)) {
                    if (!lsDuoKong.get(symbolNew).equals(duo)) {
                        //做了相反的操作，首先清空
                        Map<String, String> duoKong = getDuoKong(false, lsDuoKong.get(symbolNew));
                        execute(PrivateConfig.personInfoList, PrivateConfig.threadPoolExecutor, symbolNew, lsChiYou.get(symbolNew), duoKong.get("buy"), duoKong.get("positionSide"), true);
                        lsChiYou.remove(symbolNew);
                        lsDuoKong.remove(symbolNew);
                        //如果老师从多转空，给1秒中的时间卖了再买，怕请求频繁买入不成功
                        Thread.sleep(1000);
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
                execute(PrivateConfig.personInfoList, PrivateConfig.threadPoolExecutor, symbolNew, geShu, duoKong.get("buy"), duoKong.get("positionSide"), false);
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
                            execute(PrivateConfig.personInfoList, PrivateConfig.threadPoolExecutor, symbolNew, geShu, duoKong.get("buy"), duoKong.get("positionSide"), true);
                        }
                        //移除之前的多还是空
                        lsDuoKong.remove(old.getKey());
                        iterator.remove();
                    }
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }catch (Throwable t){
            t.printStackTrace();
        }
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

    public static long getTodayStartTime(String  hour) {
        //设置时区
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour.split(":")[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(hour.split(":")[1]));
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }


    public  void execute(List<JSONObject> listPersonInfo, ThreadPoolExecutor threadPoolExecutor, String symbol, BigDecimal geShu, String buy, String positionSide, Boolean qingCang) {

        for (JSONObject personInfo : listPersonInfo) {
            /*if("huYaLong".equals(personInfo.getString(PrivateConfig.alias))){
                if(!"BTCUSDT/ETHUSDT".contains(symbol)){
                    continue;
                }
            }*/
            personInfo.put("symbol", symbol);
            personInfo.put("geShu", new BigDecimal(geShu.stripTrailingZeros().toPlainString()));
            personInfo.put("buy", buy);
            personInfo.put("positionSide", positionSide);
            personInfo.put("qingCang", qingCang);
//            System.out.println("执行：" + personInfo.getString(PrivateConfig.alias));
//            System.out.println("线程使用个数：" + threadPoolExecutor.getActiveCount());
            MulTradeOrder6 mulTradeOrder6 = new MulTradeOrder6(personInfo, PrivateConfig.fileWriter);
            threadPoolExecutor.submit(mulTradeOrder6);
            try {
                //两个账号之间执行的间隔
                /*if(qingCang){
                    //因为清仓又查询了一次，而且查询的数据量较大，再下单，间隔短了容易超时
                    Thread.sleep(500);
                }else {
                    Thread.sleep(50);
                }*/
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

    public  Response login(OkHttpClient client) throws IOException {

        String urlLogin = "https://349assistant.club/api/login";
        FormBody formBody = new FormBody.Builder()
                .add("username", "bjpy567")
                .add("password", "654123")
//                .add("username", "dale121")
//                .add("password", "hulong1226")
//                .add("username", "349学习群飞翔的企鹅")
//                .add("password", "tian4419")
                .build();

        Request request = new Request.Builder()
                .url(urlLogin)
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        Response response = call.execute();
        JSONObject json = JSON.parseObject(response.body().string());
        if(json.getString("token") == null){
            loginCount++;
            if(loginCount > 4){
                gameOver = true;
            }
        }
        token = json.getString("token");
        System.out.println(json);
        return response;
    }

    public Request getLsRequest(Response response) {

        //获取老师的现状，可以根据保证金和数量判断咱们的跟单软件是否错误
        String getLsUrl = "https://349assistant.club/api/getPosition?realFirmId=" + PrivateConfig.id349;
        FormBody formBodyLs = new FormBody.Builder()
                .add("realFirmId", PrivateConfig.id349)
                .build();

        /*
        :authority: 349assistant.club
:method: GET
:path: /api/getPosition?realFirmId=24
:scheme: https
user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36
         */
        Request requestLs = new Request.Builder()
                .addHeader("authority", "349assistant.club")
                .addHeader("method", "GET")
                .addHeader("path", "/api/getPosition?realFirmId=" + PrivateConfig.id349)
                .addHeader("scheme", "https")
                .addHeader("accept", "application/json, text/plain, */*")
//                .addHeader("accept-encoding", "gzip, deflate, br")
                .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8")
                .addHeader("cache-control", "no-cache")
//                .addHeader("content-length", "24")
                .addHeader("origin", "https://349assistant.com")
//                .addHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("pragma", "no-cache")
                .addHeader("referer", "https://349assistant.com/")
                .addHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"90\", \"Google Chrome\";v=\"90\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("sec-fetch-site", "cross-site")
                .addHeader("token", token)
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36")
                .url(getLsUrl)
                .get()
                .build();
        return requestLs;
    }

}


class MulTradeOrder6 implements Callable {

    JSONObject personInfo;
    Writer fileWriter;

    MulTradeOrder6 (JSONObject jsonObject, Writer fileWriter){
        this.personInfo = jsonObject;
        this.fileWriter = fileWriter;
    }

    @Override
    public Object call() throws Exception {
        try {
            JSONObject error = new JSONObject();
            BigDecimal geShu = personInfo.getBigDecimal("geShu").abs();
            String symbol = personInfo.getString("symbol");
            String buy = personInfo.getString("buy");
            String positionSide = personInfo.getString("positionSide");
            if (StringUtils.isEmpty(symbol) || StringUtils.isEmpty(buy) || StringUtils.isEmpty(positionSide)) {
                System.out.println("symbol：" + symbol + "，buy：" + buy + "，positionSide：" + positionSide);
                T5.sendMe("有问题了" + "symbol：" + symbol + "，buy：" + buy + "，positionSide：" + positionSide);
                return 0;
            }
            SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient));

            /*if (personInfo.getBoolean("qingCang")) {
                System.out.println(getCurrentTime() + "---------平仓-----");
                AccountInformation accountInformation = syncRequestClient.getAccountInformation();
                List<Position> positionList = accountInformation.getPositions();
                for (Position position : positionList) {
                    geShu = position.getPositionAmt().abs();
                    if (symbol.equals(position.getSymbol()) && geShu.compareTo(new BigDecimal("0")) > 0 && positionSide.equals(position.getPositionSide())) {
                        postOrder(syncRequestClient, symbol, buy, positionSide, geShu.toString());
                        break;
                    }
                }
                return 0;
            }*/


            BigDecimal beiShu = new BigDecimal(personInfo.getString(PrivateConfig.beiShu));
            geShu = geShu.multiply(beiShu);
            String getOrigQty = geShu.setScale(getXSM(symbol), BigDecimal.ROUND_DOWN).toString();
//            System.out.println(getCurrentTime() + "，老师购买个数：" + geShu + "；" + personInfo.getString(PrivateConfig.alias) + "购买个数为：" + getOrigQty);

            postOrder(syncRequestClient, symbol, buy, positionSide, getOrigQty);
            error.put("code", "0");
            if (personInfo.getBoolean("qingCang")) {
                System.out.println(getCurrentTime() + "---------平仓-----");
                if ("huYaLong.tianShuChengXiaoHao.huShuoLong".contains(personInfo.getString(PrivateConfig.name))) {
                    T5.sendMe("平仓了，看看处理是否正确");
                }
            }
            return error;
        } catch (Exception e) {
            e.printStackTrace();
            PrivateConfig.printLog(fileWriter, e);
        }
        return 0;
    }

    public void postOrder(SyncRequestClient syncRequestClient, String symbol, String buy, String positionSide, String getOrigQty) {
        for (int i = 0; i < 2; i++) {
            try {
                /*if ("huYaLong.tianShuChengXiaoHao.huShuoLong".contains(personInfo.getString(PrivateConfig.name))) {
                    PrivateConfig.printLog(fileWriter, "开始下单");
                }*/
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
                /*if ("huYaLong.tianShuChengXiaoHao.huShuoLong".contains(personInfo.getString(PrivateConfig.name))) {
                    PrivateConfig.printLog(fileWriter, "下单结束");
                }*/
                if ("huYaLong.tianShuChengXiaoHao.huShuoLong".contains(personInfo.getString(PrivateConfig.name))) {
                    if(isMai(myOrder)){
                        System.out.println(personInfo.getString(PrivateConfig.alias)  + "买：" + myOrder.getSymbol() + "：" + getOrigQty);
                    }else {
                        System.out.println(personInfo.getString(PrivateConfig.alias)  + "卖：" + myOrder.getSymbol() + "：" + getOrigQty);
                    }
                }
//                System.out.println(myOrder.toString());
                return;
            } catch (Exception e) {
                try {
                    e.printStackTrace();
                    PrivateConfig.printLog(fileWriter, e);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

    public Boolean isMai(Order order){
        if(("SELL".equals(order.getSide()) && "LONG".equals(order.getPositionSide())) || ("BUY".equals(order.getSide()) && "SHORT".equals(order.getPositionSide())) ){
            return false;
        }
        return true;
    }

    public String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis())); // 时间戳转换日期
    }



}

class JianKong6 implements Callable {


    BigDecimal ling = new BigDecimal("0");
    BigDecimal ling02 = new BigDecimal("0.2");
    Long count = 1L;
    int error = 1;//记录登录超时的次数，连续超过3次，报警

    JSONArray jsonArrayLs;
    List<JSONObject> listPersonInfo;

    public JianKong6(){

    }
    public JianKong6(JSONArray jsonArrayLs, List<JSONObject> listPersonInfo){
        this.jsonArrayLs = jsonArrayLs;
        this.listPersonInfo = listPersonInfo;
    }

    @Override
    public Object call() throws Exception {
        jiaoYan(jsonArrayLs, listPersonInfo);
        return null;
    }

    public String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis())); // 时间戳转换日期
    }

    public void jiaoYan(JSONArray jsonArrayLs, List<JSONObject> listPersonInfo) throws InterruptedException {
        System.out.println(getCurrentTime() + "正在监控");
        //执行完后，歇1秒再监控，保证监控时，前面的执行都已结束
        Thread.sleep(100);
        for (JSONObject personInfo : listPersonInfo) {
            if(!"huYaLong.tianShuChengXiaoHao.huShuoLong".contains(personInfo.getString(PrivateConfig.name))){
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
                    String msg = personInfo.getString(PrivateConfig.alias) + "的api访问失败，有问题！如果连续三次报这个问题，抓紧联系胡亚龙解决";
                    System.out.println(msg);
                    System.out.println(e);
                    T5.searchAll(msg + e);
                    Thread.sleep(2000);
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
                    BigDecimal geShu = position.getPositionAmt().abs();
                    if (geShu.compareTo(ling) > 0) {
                        // 先自动平仓解决
                        QingCang qingCang = new QingCang();
                        qingCang.qingCang(listPersonInfo, position.getSymbol());

                        String msg = personInfo.getString(PrivateConfig.alias) + "，老师没有" + position.getSymbol() + "。有问题！如果连续三次报这个问题，请联系他手动平仓";

                        System.out.println(msg);
                        T5.searchAll(msg);
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

                    if (PrivateConfig.getXSM(symbol) <= 0.1) {
                        //小数位为0
                        ling02 = new BigDecimal("0.4");
                        if (beiShu.compareTo(PrivateConfig.ling035) < 0) {
                            System.out.println(personInfo.getString(PrivateConfig.alias) + "的" + symbol + "和老师相同，没有问题");
                            //只有倍数大时才校验个数是否正确
                            continue;
                        }
                    } else {
                        if (beiShu.compareTo(PrivateConfig.ling035) < 0) {
                            ling02 = new BigDecimal("0.4");
                        } else {
                            ling02 = new BigDecimal("0.2");
                        }
                    }


                    //老师的数量乘以倍数就是我们应该有的数量
                    BigDecimal yingGaiYou = ls.getBigDecimal("positionAmt").multiply(beiShu).abs();
                    Boolean hasProblem = true;
                    for (Position position : positionList) {
                        BigDecimal you = position.getPositionAmt().abs();
                        if (symbol.equals(position.getSymbol()) && you.abs().compareTo(ling) > 0) {
                            if (yingGaiYou.subtract(you).abs().divide(you.abs(), 5, BigDecimal.ROUND_HALF_UP).compareTo(ling02) > 0) {

                                if (PrivateConfig.getXSM(symbol) <= 0.1 && yingGaiYou.multiply(beiShu).compareTo(new BigDecimal("5")) < 0) {
                                    //小数位为0，且应该有的个数小于5时，不校验
                                    hasProblem = false;
                                    System.out.println(personInfo.getString(PrivateConfig.alias) + "的" + symbol + "和老师相同，没有问题");
                                    continue;
                                }

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
                        Thread.sleep(10);
                        //先自动买

                    }
                }
                // 如果和老师都有，并且symbol不同时
                for (Position position : positionList) {
                    BigDecimal geShu = position.getPositionAmt().abs();
                    if (geShu.abs().compareTo(ling) > 0) {
                        if (!symbolListLs.contains(position.getSymbol())) {
                            // 先自动平仓解决
                            QingCang qingCang = new QingCang();
                            qingCang.qingCang(listPersonInfo, position.getSymbol());

                            String msg = personInfo.getString(PrivateConfig.alias) + "，老师没有" + position.getSymbol() + "。有问题！如果连续三次报这个问题，请联系他手动平仓";
                            System.out.println(msg);
                            T5.searchAll(msg);
                        }
                    }
                }

            }
            // 间隔20毫秒
            Thread.sleep(20);
        }
        if(error != 1){
            System.out.println("有错误了");
            jiaoYan(jsonArrayLs, listPersonInfo);
        }

    }

    public void postOrder(SyncRequestClient syncRequestClient, String symbol, String buy, String positionSide, String getOrigQty) {
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
//        System.out.println(myOrder.toString());
    }
}
