package com.example.OK;

import com.alibaba.fastjson.JSONObject;
import com.example.OK.conn.ClosePositions;
import com.example.OK.conn.PlaceOrder;
import com.example.OK.conn.client.APIClient;
import com.example.OK.conn.trade.TradeAPIService;
import com.example.OK.conn.trade.impl.TradeAPI;
import com.example.OK.conn.trade.impl.TradeAPIServiceImpl;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import com.example.bian.client.model.trade.Order;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

import static com.example.bian.client.bushu.PrivateConfig.getXSM;

public class xiaDan implements Callable {

    private JSONObject personInfo;
    private Order order;
    private boolean qingCang;

    public xiaDan(JSONObject personInfo, Order order, Boolean qingCang){
        this.personInfo = personInfo;
        this.order = order;
        this.qingCang = qingCang;
    }

    @Override
    public Object call() throws Exception {

        try {
            BigDecimal beiShu = new BigDecimal(personInfo.getString(PrivateConfig.beiShu));
            TradeAPIService tradeAPIService = (TradeAPIService) personInfo.get(PrivateConfig.tradeAPIService);
            APIClient apiClient = (APIClient) personInfo.get(PrivateConfig.apiClient);
            TradeAPI tradeAPI = (TradeAPI) personInfo.get(PrivateConfig.tradeAPI);

            if(PrivateConfig.ceShi.equals("0")){
                if(qingCang){
                    closePosition(order.getSymbol(), order.getPositionSide(), tradeAPIService, apiClient, tradeAPI);
                }else {
                    //跟单数量
                    BigDecimal geShu = order.getOrigQty().multiply(beiShu);
                    placeOrder(order.getSymbol(), order.getSide(), order.getPositionSide(), tradeAPIService, geShu, apiClient, tradeAPI);
                    String msg = Thread.currentThread().getName() + "---下单成功，没有问题！个数为：" + geShu + "---" + PrivateConfig.getCurrentTime();
                    PrivateConfig.printLog(msg);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
//            PrivateConfig.printLog(PrivateConfig.fileWriter, e);
            T5.searchAll("连续3次，有问题，关闭软件，重新启动7。" + personInfo.getString(PrivateConfig.name) + e.getMessage());
        }
        return null;
    }


    public static void placeOrder(String symbol, String side, String positionSide, TradeAPIService tradeAPIService, BigDecimal getOrigQty, APIClient client, TradeAPI tradeAPI){

        for (int i = 0; i < 1; i++) {
            try {
                symbol = symbol.replace("USDT", "");
                if(!(symbol.equals("BTC")||symbol.equals("ETH")) ){
                    return;
                }

                PlaceOrder placeOrder =new PlaceOrder();
                placeOrder.setInstId(symbol + "-USDT-SWAP");
                placeOrder.setTdMode("cross");
                placeOrder.setSide(side.toLowerCase());//buy, sell
                placeOrder.setPosSide(positionSide.toLowerCase());//short, long
                placeOrder.setOrdType("market");
                getOrigQty = getOrigQty.multiply(PrivateConfig.getOKZhangShu.getBigDecimal(symbol)).setScale(2, BigDecimal.ROUND_DOWN);
//                getOrigQty = getOrigQty.setScale(getXSM(order.getSymbol()), BigDecimal.ROUND_DOWN);
                placeOrder.setSz(getOrigQty.toString());//张数 100张Btc=1个 10张eth=1个

                JSONObject result = tradeAPIService.placeOrder(client, tradeAPI, placeOrder);
                System.out.println("下单了：" + PrivateConfig.getCurrentTime()+"-" + symbol+"-"+side+"-"+positionSide+"-"+getOrigQty);
                return;
            } catch (Exception e) {
                try {
                    e.printStackTrace();

                    //
                    /*if(e.getMessage().contains("-2019")){
                        //保证金不够，少买点
                        count = count.multiply(new BigDecimal("0.5")).setScale(PrivateConfig.getXSM(symbol), BigDecimal.ROUND_HALF_UP);
                    } else if(e.getMessage().contains("-4164")) {

                    }*/

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void closePosition(String symbol, String positionSide, TradeAPIService tradeAPIService, APIClient client, TradeAPI tradeAPI){
        String sym = symbol.replace("USDT", "");
        for (int i = 0; i < 1; i++) {
            try {
                ClosePositions closePositions =  new ClosePositions();
                closePositions.setInstId(sym + "-USDT-SWAP");
                closePositions.setPosSide(positionSide.toLowerCase());
                closePositions.setMgnMode("cross");
                JSONObject result = tradeAPIService.closePositions(client, tradeAPI,closePositions);
                System.out.println("清仓了：" + PrivateConfig.getCurrentTime() + "-" + symbol + "-" + positionSide);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
