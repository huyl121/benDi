package com.example.bian;

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
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

public class QingCang {
    static String All = "all";

    public static void main(String[] args)
            throws IOException, InterruptedException {
        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10819");

        args = new String[3];

        System.out.println("开始啦");
        args[0] = "E://baidutongbu//baidutongbu//tongbu//bian//bian/";
        PrivateConfig.before(args[0], args[1]);
        QingCang qingCang = new QingCang();
        qingCang.method(args);
    }

    public void method(String[] args) {
        List<JSONObject> listPersonInfo = PrivateConfig.personInfoList;
        if (args.length > 2) {
            qingCang(listPersonInfo, args[2]);
        } else {
            qingCang(listPersonInfo, null);
        }

    }

    public void qingCang(List<JSONObject> listPersonInfo, String symbol) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(listPersonInfo.size(), listPersonInfo.size(), 10L, TimeUnit.SECONDS, new LinkedBlockingQueue(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        for (JSONObject jsonObject : listPersonInfo) {
            MulTradeOrder1 mulTradeOrder1 = new MulTradeOrder1(jsonObject, symbol);
            threadPoolExecutor.submit(mulTradeOrder1);
            try {
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

    MulTradeOrder1(JSONObject jsonObject, String symbol) {
        this.personInfo = jsonObject;
        this.symbol = symbol;
    }

    @Override
    public Object call()
            throws Exception {
        try {
            SyncRequestClient syncRequestClient = (SyncRequestClient) this.personInfo.get("syncRequestClient");
            AccountInformation accountInformation = syncRequestClient.getAccountInformation();
            List<Position> positionList = accountInformation.getPositions();
            for (Position position : positionList) {
                if ((!StringUtils.isNotEmpty(this.symbol)) || (position.getSymbol().equals(this.symbol.trim()))) {
                    BigDecimal geShu = position.getPositionAmt().abs();
                    if (geShu.compareTo(new BigDecimal("0")) > 0) {
                        try {
                            postOrder(syncRequestClient, position.getSymbol(), "SELL", "LONG", geShu.toString());
                        } catch (Exception e) {
                            postOrder(syncRequestClient, position.getSymbol(), "BUY", "SHORT", geShu.toString());
                        }
                    }
                }
            }
            System.out.println(this.personInfo.getString("alias") + "清仓完毕。");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void postOrder(SyncRequestClient syncRequestClient, String symbol, String buy, String positionSide, String getOrigQty) {
        Order myOrder = syncRequestClient.postOrder(symbol,

                OrderSide.valueOf(buy),
                PositionSide.valueOf(positionSide),
                OrderType.valueOf("MARKET"), null, getOrigQty, null, null, null, null, null, NewOrderRespType.RESULT);


//        System.out.println(myOrder.toString());
    }
}

