package com.example.OK;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.OK.conn.client.APIClient;
import com.example.OK.conn.trade.TradeAPIService;
import com.example.OK.conn.trade.impl.TradeAPI;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.model.trade.AccountInformation;
import com.example.bian.client.model.trade.Order;
import com.example.bian.client.model.trade.Position;
import com.google.gson.JsonObject;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.*;

import static com.example.bian.client.bushu.PrivateConfig.*;

public class QingCangOk {

    public static void main(String[] args)
            throws IOException, InterruptedException {
        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10819");


        args = new String[5];

        System.out.println("开始啦");
        args[0] = "E://code//biance";
        args[1] = "qingCang";
        args[2] = "";
        args[3] = "";
        args[4] = "--server.port=10187";
        PrivateConfig.before(args[0], "0");


        for (Object o : genDans_genPortfolioIds) {
            JSONObject genPortfolioId = (JSONObject) o;
            genDans_genPortfolioId = genPortfolioId.getString("genPortfolioId");
            break;
        }

        QingCangOk qingCang = new QingCangOk();
        qingCang.method(args, PrivateConfig.daiDanOk_personInfoList);
    }

    public void method(String[] args, List<JSONObject> listPersonInfo) throws InterruptedException {
        if (args.length > 3) {
            if(args.length>4){
                qingCang(listPersonInfo, args[2], args[3]);
            }else {
                qingCang(listPersonInfo, args[2], null);
            }
        } else {
            qingCang(listPersonInfo, null, null);
        }

    }

    /**
     *
     * @param listPersonInfo
     * @param symbol 为null时，清仓所有，此时忽略positionSide的值
     * @param positionSide 为null时，清仓所有方向的。否则清仓指定方向
     */
    public void qingCang(List<JSONObject> listPersonInfo, String symbol, String positionSide) throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(listPersonInfo.size(), listPersonInfo.size(), 10L, TimeUnit.SECONDS, new LinkedBlockingQueue(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        if (StringUtils.isNotBlank(symbol)) {
            symbol = symbol.replace("USDT", "");
        }
        for (JSONObject personInfo : listPersonInfo) {

            try {
                TradeAPIService tradeAPIService = (TradeAPIService) personInfo.get(PrivateConfig.tradeAPIService);
                APIClient apiClient = (APIClient) personInfo.get(PrivateConfig.apiClient);
                TradeAPI tradeAPI = (TradeAPI) personInfo.get(PrivateConfig.tradeAPI);
                JSONObject result = tradeAPIService.getAccountAndPosition(apiClient, tradeAPI, "SWAP");
                if(result.getString("code").equals("0")) {
                    JSONArray data = result.getJSONArray("data");
                    JSONArray positions = data.getJSONObject(0).getJSONArray("posData");
                    for (Object o : positions) {
                        JSONObject position = (JSONObject) o;
                        BigDecimal geShu = position.getBigDecimal("pos").abs();
                        if (geShu.compareTo(PrivateConfig.ling) > 0) {
                            boolean qing = false;
                            if (StringUtils.isBlank(symbol)) {
                                qing = true;
                            } else if (position.getString("instId").split("-")[0].equals(symbol.trim())) {
                                if (StringUtils.isBlank(positionSide)) {
                                    qing = true;
                                } else if (position.getString("posSide").equals(positionSide.trim().toLowerCase())) {
                                    qing = true;
                                }
                            }
                            if (qing) {
                                Order qingOrder = new Order();
                                qingOrder.setSymbol(position.getString("instId").split("-")[0]);
                                qingOrder.setPositionSide(position.getString("posSide"));
                                xiaDan xiaDan = new xiaDan(personInfo, qingOrder, true);
                                threadPoolExecutor.submit(xiaDan);//启动一般的线程
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(50L);
        }
        while (threadPoolExecutor.getActiveCount() > 0) {
            Thread.yield();
        }
        System.out.println("清仓结束。");
        threadPoolExecutor.shutdown();
    }
}

class MulTradeOrder1  implements Callable {

    JSONObject personInfo;
    String symbol;
    String positionSide;

    /**
     * 当symbol为null时，清仓所有，此时positionSide的值可以忽略
     * @param jsonObject
     * @param symbol
     * @param positionSide
     */
    MulTradeOrder1(JSONObject jsonObject, String symbol, String positionSide) {
        this.personInfo = jsonObject;
        this.symbol = symbol;
        this.positionSide = positionSide;
    }

    @Override
    public Object call()
            throws Exception {
        try {

//            System.out.println(this.personInfo.getString("alias") + "清仓完毕。");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}


