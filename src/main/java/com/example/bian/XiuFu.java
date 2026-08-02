package com.example.bian;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.model.enums.NewOrderRespType;
import com.example.bian.client.model.enums.OrderSide;
import com.example.bian.client.model.enums.OrderType;
import com.example.bian.client.model.enums.PositionSide;
import com.example.bian.client.model.trade.AccountInformation;
import com.example.bian.client.model.trade.Order;
import com.example.bian.client.model.trade.Position;
import okhttp3.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static com.example.bian.client.bushu.PrivateConfig.getXSM;
import static com.example.bian.client.bushu.PrivateConfig.getXsw;

/**
 * Created by adimn on 2021/8/12.
 */
public class XiuFu {



    public static void main(String[] args) throws IOException {
        System.out.println("开始");
        System.setProperty("https.proxySet", "true");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyPort", "10819");
        if (args.length == 0) {
			args = new String[1];
			args[0] = "E://baidutongbu//baidutongbu//tongbu//bian//bian";
		}
        PrivateConfig.getListNew(args[0] + "//info.json");
        XiuFu xiuFu = new XiuFu();
        xiuFu.xiuFu();
        System.out.println("结束");
    }

    BigDecimal ling = new BigDecimal(0);
    String token = "";

    public void xiuFu() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Response response = login(client);
        Request lsRequest = getLsRequest(response);
        Call callLs = client.newCall(lsRequest);
        Response responseLs = callLs.execute();
        String result = responseLs.body().string();
        JSONObject jsonLs = JSON.parseObject(result);
        JSONArray jsonArray = ((JSONArray) jsonLs.get("data"));
         List<JSONObject> listPersonInfo = PrivateConfig.personInfoList;

        Map<String, BigDecimal> lsChiYou = new HashMap<>();
        Map<String, Boolean> lsDuoKong = new HashMap<>();

        for (Object o : jsonArray) {
            JSONObject ls = (JSONObject) o;
            BigDecimal geShuLs = ls.getBigDecimal("positionAmt");
            String symbol = ls.getString("symbol");
            if (geShuLs.compareTo(ling) < 0) {
                lsDuoKong.put(symbol, false);
                lsChiYou.put(symbol+"SHORT", geShuLs);
            } else {
                lsDuoKong.put(symbol, true);
                lsChiYou.put(symbol+"LONG", geShuLs);
            }
        }

        if(jsonArray.isEmpty()){
            //如果老师没有，那么清仓
            QingCang qingCang = new QingCang();
            qingCang.qingCang(listPersonInfo, null);
        }else {
            ThreadPoolExecutor threadPoolExecutor =
                    new ThreadPoolExecutor(listPersonInfo.size(),
                            listPersonInfo.size(),
                            10,
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(),
                            Executors.defaultThreadFactory(),
                            new ThreadPoolExecutor.AbortPolicy());
            for(int i=0; i< listPersonInfo.size(); i++){
                DanGeXiuFu danGeXiuFu = new DanGeXiuFu(listPersonInfo.get(i), lsChiYou, lsDuoKong);
                threadPoolExecutor.submit(danGeXiuFu);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public Response login(OkHttpClient client) throws IOException {
        /*if(tiaoShi){
            return null;
        }*/
        String urlLogin = "https://349assistant.club/api/login";
        FormBody formBody = new FormBody.Builder()
//                .add("username", "bjpy567")
//                .add("password", "654123")
                .add("username", "dale121")
                .add("password", "hulong1226")
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
        token = json.getString("token");
        System.out.println(json);
        return response;
    }

    public Request getLsRequest(Response response) {
        /*if(tiaoShi){
            return null;
        }*/
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

class DanGeXiuFu implements Callable {

    BigDecimal ling = new BigDecimal(0);
    BigDecimal ling01 = new BigDecimal("0.1");
    JSONObject personInfo;
    Map<String, BigDecimal> lsChiYou;
    Map<String, Boolean> lsDuoKong;

    DanGeXiuFu(JSONObject jsonObject, Map<String, BigDecimal> lsChiYou, Map<String, Boolean> lsDuoKong) {
        this.personInfo = jsonObject;
        this.lsChiYou = lsChiYou;
        this.lsDuoKong = lsDuoKong;
    }

    @Override
    public Object call() throws Exception {
        SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient));
        AccountInformation accountInformation = syncRequestClient.getAccountInformation();
        List<Position> positionList = accountInformation.getPositions();
        BigDecimal beiShu = new BigDecimal(personInfo.getString(PrivateConfig.beiShu));

        for (Position position : positionList) {
            BigDecimal xueShengGeShu = position.getPositionAmt().abs();
            String symbol = position.getSymbol();
            String symbolDuoKongXueSheng = symbol + position.getPositionSide();
            if (xueShengGeShu.compareTo(ling) > 0) {
                //学生有
                if (!lsChiYou.keySet().contains(symbolDuoKongXueSheng)) {
                    //如果学生有，老师没有，那么学生的清仓
                    try {
                        qingCang(syncRequestClient, position.getSymbol(), "SELL", "LONG", xueShengGeShu.toString());
                    } catch (Exception e) {
                        qingCang(syncRequestClient, position.getSymbol(), "BUY", "SHORT", xueShengGeShu.toString());
                    }
                } else {
                    //学生有，老师也有，查看有没有差值
                    BigDecimal youLs = lsChiYou.get(symbolDuoKongXueSheng).abs();
                    BigDecimal youXueSheng = xueShengGeShu.divide(beiShu, 5, BigDecimal.ROUND_HALF_UP);
                    if (youLs.subtract(youXueSheng).divide(youLs, 5, BigDecimal.ROUND_HALF_UP).abs().compareTo(ling01) > 0 &&
                            youXueSheng.subtract(youLs).divide(youXueSheng, 5, BigDecimal.ROUND_HALF_UP).abs().compareTo(ling01) > 0) {
                        Map<String, String> duoKong;
                        //有差值，补充
                        if (youLs.subtract(youXueSheng).compareTo(ling) > 0) {
                            //老师大于学生，买
                            duoKong = getDuoKong(true, lsDuoKong.get(symbol));
                        } else {
                            //老师小于学生，卖
                            duoKong = getDuoKong(false, lsDuoKong.get(symbol));
                        }
                        execute(syncRequestClient, position.getSymbol(), youLs.subtract(youXueSheng), duoKong.get("buy"), duoKong.get("positionSide"));
                    } else {
                        //无差值，不处理
                    }
                }
            } else {
                //学生没有
                if (lsChiYou.keySet().contains(symbolDuoKongXueSheng)) {
                    //如果学生没有，老师有，那么学生买
                    Map<String, String> duoKong = getDuoKong(true, lsDuoKong.get(symbol));
                    execute(syncRequestClient, symbol, lsChiYou.get(symbolDuoKongXueSheng), duoKong.get("buy"), duoKong.get("positionSide"));
                } else {
                    //如果学生没有，老师没有，不处理
                }

            }
        }
        return 0;
    }

    /**
     * 个数是老师的，需要缩小
     * @param syncRequestClient
     * @param symbol
     * @param geShu
     * @param buy
     * @param positionSide
     */
    public void execute(SyncRequestClient syncRequestClient, String symbol, BigDecimal geShu, String buy, String positionSide) {
        try {

            BigDecimal beiShu = new BigDecimal(personInfo.getString(PrivateConfig.beiShu));
            geShu = geShu.abs().multiply(beiShu);
            String getOrigQty = geShu.setScale(getXSM(symbol), BigDecimal.ROUND_DOWN).toString();

            postOrder(syncRequestClient, symbol, buy, positionSide, getOrigQty);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }


    /**
     * 真正购买的个数
     * @param syncRequestClient
     * @param symbol
     * @param buy
     * @param positionSide
     * @param getOrigQty
     */
    public void postOrder(SyncRequestClient syncRequestClient, String symbol, String buy, String positionSide, String getOrigQty) {
        for (int i = 0; i < 2; i++) {
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
//                System.out.println(myOrder.toString());
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void qingCang(SyncRequestClient syncRequestClient, String symbol, String buy, String positionSide, String getOrigQty) {

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
//                System.out.println(myOrder.toString());

    }

    public Map<String, String> getDuoKong(Boolean mai, Boolean duo) {
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

}
