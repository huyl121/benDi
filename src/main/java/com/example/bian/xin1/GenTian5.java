package com.example.bian.xin1;

import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.RequestOptions;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import com.example.bian.client.exception.BinanceApiException;
import com.example.bian.client.model.enums.*;
import com.example.bian.client.model.trade.Order;
import com.example.bian.xin.JianKong4;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import static com.example.bian.client.bushu.PrivateConfig.getXSM;

public class GenTian5 {


    public String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis())); // 时间戳转换日期
    }

    public static Integer tianBoth = 1;

    public static void main(String[] args) {

        // 准备工作
        // 对https也开启代理
        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10819");

        args = new String[2];
        System.out.println("开始啦");
        args[0] = "E://code//biance";
        args[1] = "0-genTian";
        PrivateConfig.before(args[0], args[1]);
        PrivateConfig.getJGXsw();
        PrivateConfig.xsw(true);





        GenTian5 genTian5 = new GenTian5();
        genTian5.method(args);

        /*MulGetAllOrders mulGetAllOrders = new MulGetAllOrders(null, null, null, null);
        Boolean b = mulGetAllOrders.chaDa("0", "0", "0.003");
        System.out.println();*/
    }

    public void method(String[] args) {
        try {

            ThreadPoolExecutor threadPoolExecutor =
                    new ThreadPoolExecutor(PrivateConfig.personInfoList.size() + 5,
                            PrivateConfig.personInfoList.size() + 10,
                            10,
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(),
                            Executors.defaultThreadFactory(),
                            new ThreadPoolExecutor.DiscardPolicy());

            //启动监控线程
            Callable callable1 = new Callable() {
                @Override
                public String call() throws Exception {
                    JianKong4 jianKong4 = new JianKong4();
                    jianKong4.method(args, threadPoolExecutor, PrivateConfig.personInfoList);
                    return "";
                }
            };
            threadPoolExecutor.submit(callable1);

            /*Callable callable2 = new Callable() {
                @Override
                public String call() throws Exception {
                    JianKongLog jianKong4 = new JianKongLog();
                    jianKong4.method(PrivateConfig.personInfoList);
                    return "";
                }
            };
            threadPoolExecutor.submit(callable2);*/

            SyncRequestClient syncRequestClientTiansc = ((SyncRequestClient) PrivateConfig.standard.get(PrivateConfig.syncRequestClient));

            Set<String> orderIdSet = new HashSet<>();
            Set<Long> orderIdGuaSet = new HashSet<>();

            //设置一下倍数
            PrivateConfig.setBeiShu(threadPoolExecutor);

            //存储的是田的orderID，我们跟单人的所有order，会根据田的orderID，取消我们所有的order
            HashMap<Long, Order[]> orderIdGuaMap = new HashMap<>();
            int o = 0;
            int j = 0;
            int k=0;
            double lastTime0 = System.currentTimeMillis();
            int diaoYongCount = 0;
            PrivateConfig.printLogJianKong();
            while (true) {
                try {

                    /*o++;
                    if(o > 20){
                        o = 0;
                        System.out.println(getCurrentTime() + "正在跟单，正在工作的线程数：" + threadPoolExecutor.getActiveCount());
                    }*/

                    diaoYongCount++;
                    if(diaoYongCount > 10 * 60){
                        diaoYongCount = 0;
                        PrivateConfig.printLogJianKong();
                    }
                    // 查询田总的订单频率
                    Thread.sleep(Long.parseLong(PrivateConfig.shiJian));
                    Long currentTime = System.currentTimeMillis();

                    if (!orderIdGuaSet.isEmpty()) {
                        //使用多线程处理
                        gua(threadPoolExecutor, syncRequestClientTiansc, orderIdGuaMap);
                    }
                    if(orderIdGuaMap.isEmpty() && !orderIdGuaSet.isEmpty()){
                        orderIdGuaSet.clear();
                        /*System.out.println("需要清空orderIdGuaSet");
                        for(Long orderId : orderIdGuaSet){
                            System.out.println("orderId");
                        }*/
                    }

                    List<Order> list = PrivateConfig.getOrders(syncRequestClientTiansc, threadPoolExecutor);

                    Long t2 = System.currentTimeMillis();
                    if((t2-currentTime) > 1000 * 60){
                        T5.searchAll("连续5次，有问题，1请求时间为：" + (t2-currentTime)/1000);
                    }
                    List<Order> listYou = new ArrayList<>();
                    for (Order order : list) {
                        if (Math.abs(currentTime - order.getUpdateTime()) > 10000L) {//订单大于10秒时，不跟
                            /*System.out.println("当前时间为：" + currentTime);
                            System.out.println("订单时间为：" + order.getUpdateTime());*/
                            continue;
                        }
                        listYou.add(order);
                    }
                    //#2 单向变双向
                    if (listYou.size() > 0) {
                        for(Order order : listYou){
                            order.setPositionSide(PrivateConfig.getOrderPositionSide(order));
                        }
                    }

                    if (listYou.size() > 0) {

                        Order order = listYou.get(0);
                        if(PositionSide.BOTH.toString().equals(order.getPositionSide())){
                            tianBoth = 1;
                        }else {
                            tianBoth = 0;
                        }

                        /*System.out.println(getCurrentTime() + "有新单子了");
                        System.out.println(order);*/
                        //因为下单后撤销是同一个订单，只是状态不同，所以只能这样区分是否是已经操作过的订单
                        if (orderIdSet.contains(order.getOrderId() + order.getStatus())) {
                            continue;
                        } else {
                            if (PrivateConfig.isGua(order)) {
                                orderIdSet.add(order.getOrderId() + "NEW");
                                orderIdGuaSet.add(order.getOrderId());

                                if (!orderIdGuaMap.containsKey(order.getOrderId())) {
                                    Order[] orders = new Order[PrivateConfig.personInfoList.size() + 1];
                                    orders[PrivateConfig.personInfoList.size()] = order;
                                    orderIdGuaMap.put(order.getOrderId(), orders);
                                }

                            } else if ("NEW,PARTIALLY_FILLED,FILLED".contains(order.getStatus())) {
                                if (orderIdGuaSet.contains(order.getOrderId())) {
                                    //如果有过挂单，挂单成交了，如果这里获取到，不要再执行
                                    continue;
                                }
                                //市价
                                orderIdSet.add(order.getOrderId() + "NEW");
                                orderIdSet.add(order.getOrderId() + "PARTIALLY_FILLED");
                                orderIdSet.add(order.getOrderId() + "FILLED");
                            } else {
                                orderIdSet.add(order.getOrderId() + "CANCELED");
                                orderIdSet.add(order.getOrderId() + "PENDING_CANCEL");
                                orderIdSet.add(order.getOrderId() + "REJECTED");
                                orderIdSet.add(order.getOrderId() + "EXPIRED");
                            }
                        }

                        /*if(PrivateConfig.isLinux.equals(1)){
                            PrivateConfig.printLog(PrivateConfig.fileWriter, order.toString());
                        }*/

                        //循环提交任务
                        for (int i = 0; i < PrivateConfig.personInfoList.size(); i++) {
                            try {
                                MulGetAllOrders mulGetAllOrders = new MulGetAllOrders(PrivateConfig.personInfoList.get(i), listYou, orderIdGuaMap, i);
                                threadPoolExecutor.submit(mulGetAllOrders);//启动一般的线程
                            } catch (Exception e) {
//                                System.out.println(e);
                                T5.searchAll("连续3次，有问题，关闭软件，重新启动2"+ e.getMessage()  + PrivateConfig.personInfoList.get(i));
                            }

                            //两个账号之间执行的间隔
                            if (PrivateConfig.isGua(order)) {
                                //如果是挂单，为了保证顺序，时间间隔长一点
                                Thread.sleep(100);
                            }else {
                                Thread.sleep(50);
                            }


                        }
//                        System.out.println("下单了：" + getCurrentTime());
                        //有新单子就监控一次
                        JianKong4.needCheck = true;
                        JianKong4.checkCount = 0;
                        Thread.sleep(2000);//前面有跟单，歇2秒再跟
                    }

                    j++;

                    if (j > 60 * 30) {
                        j = 0;
                        PrivateConfig.xsw(false);
                        System.out.println(getCurrentTime() + "正在跟单，正在工作的线程数：" + threadPoolExecutor.getActiveCount());
//                        PrivateConfig.printLog(PrivateConfig.fileWriter, "gen dan：" + threadPoolExecutor.getActiveCount());
                    }


                    /*k++;
                    if (k > 60 * 200) {
                        k = 0;
                        double lastTime1 = System.currentTimeMillis();
                        double xiaoShi = (lastTime1 - lastTime0) / 1000 / 60 / 60;
                        BigDecimal two11 = new BigDecimal(xiaoShi);
                        T5.searchAll("：用时：" + two11.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue() + "小时，超过3小时有问题，正在跟单" + PrivateConfig.getCurrentTime());
                        lastTime0 = lastTime1;

                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
//                    PrivateConfig.printLog(PrivateConfig.fileWriter, e);
                    T5.searchAll("连续3次，有问题，关闭软件，重新启动1"+ e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("有问题了2" + e.toString());
        }
    }

    //使用多线程处理
    public void gua(ThreadPoolExecutor threadPoolExecutor, SyncRequestClient syncRequestClientTiansc, HashMap<Long, Order[]> orderIdGuaMap) throws InterruptedException {
        Iterator iterator = orderIdGuaMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Order[]> entry = (Map.Entry<Long, Order[]>) iterator.next();
            Order orderTian = entry.getValue()[PrivateConfig.personInfoList.size()];

            Order orderTianNew = PrivateConfig.getOrder(orderTian, syncRequestClientTiansc, threadPoolExecutor);

            if ("NEW".contains(orderTianNew.getStatus())) {
                //不处理
            } else {
                if ("PARTIALLY_FILLED,FILLED".contains(orderTianNew.getStatus())) {
                    // 田的成交了，查看我们的是否成交，如果没有成交，那么进行处理
                    //循环提交任务
                    for (int i = 0; i < PrivateConfig.personInfoList.size(); i++) {
                        try {
                            GuaOrder guaOrder = new GuaOrder(PrivateConfig.personInfoList.get(i), entry.getValue()[i], threadPoolExecutor);
                            threadPoolExecutor.submit(guaOrder);//启动一般的线程
                        } catch (Exception e) {
//                            System.out.println(e);
                            T5.searchAll("连续3次，有问题。关闭软件，重新启动3"+ e.getMessage() + PrivateConfig.personInfoList.get(i));
                        }
                    }
                } else {
                    //取消订单
                    for (int i = 0; i < PrivateConfig.personInfoList.size(); i++) {
                        try {
                            PrivateConfig.cancleMap(PrivateConfig.personInfoList.get(i), orderTian);
                            Thread.sleep(50);
                        } catch (Exception e) {
//                            System.out.println(e);
                            T5.searchAll("连续3次，有问题。关闭软件，重新启动4"+ e.getMessage() + PrivateConfig.personInfoList.get(i));
                        }
                    }

                }
                iterator.remove();
            }
        }
    }



}

class GuaOrder implements Callable {

    JSONObject personInfo;
    Order order;
    ThreadPoolExecutor threadPoolExecutor;
    public GuaOrder(JSONObject personInfo, Order order, ThreadPoolExecutor threadPoolExecutor){
        this.personInfo = personInfo;
        this.order = order;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public Object call() throws Exception {
        SyncRequestClient syncRequestClient = (SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient);

        Order orderWe = PrivateConfig.getOrder(order, syncRequestClient, threadPoolExecutor);

        if ("NEW".equals(orderWe.getStatus()) || "PARTIALLY_FILLED".equals(orderWe.getStatus())) {
//            T5.searchAll("田的挂单成交了，" + personInfo.getString(PrivateConfig.alias) + "的挂单没有成交，会自动取消订单，然后按照市价卖，可以调整下卖的小数，如果联系3次出现就有问题！联系胡");
           // 取消
            Order myCancleOrder = syncRequestClient.cancelOrder(orderWe.getSymbol(), orderWe.getOrderId(), orderWe.getClientOrderId());
            if(myCancleOrder == null){
                T5.sendMe("撤销失败，查看代码逻辑");
            }
            // 市价卖
            Order myOrder = postOrder(syncRequestClient, order, order.getOrigQty().toString());
        }else {
             //不处理
        }
        return null;
    }

    /**市价
     *
     * @param syncRequestClient
     * @param order
     * @param getOrigQty
     */
    public Order postOrder(SyncRequestClient syncRequestClient, Order order, String getOrigQty) throws InterruptedException {
//        System.out.println(getCurrentTime() + "买卖一单");
        for (int i = 0; i < 2; i++) {
            try {
//                PrivateConfig.printLog(PrivateConfig.fileWriter, "开始下单");
                String reduceonly  = null;
                if(PositionSide.BOTH.toString().equals(order.getPositionSide())){
                    reduceonly = order.getReduceOnly().toString();
                }
                Order myOrder = syncRequestClient.postOrder(
                        order.getSymbol(),
                        OrderSide.valueOf(order.getSide()),//买还是卖
                        PositionSide.valueOf(order.getPositionSide()),//做多还是做空 long short both
                        OrderType.valueOf("MARKET"),// 订单类型，limit：限价单；MARKET：市价单（想要成功买卖，使用这个）
                        null,//TimeInForce.valueOf("GTC"),//成交为止，一直有效，不用管
                        getOrigQty,//跟单数量，需要大于5
                        null,//跟单单价，总价需要大于5（市价时，可以不填）
                        reduceonly, //order.getReduceOnly().toString(),
                        null,//order.getClientOrderId(),
                        null,//order.getStopPrice().toString(),
                        null,//WorkingType.valueOf(order.getWorkingType()),
                        NewOrderRespType.RESULT);
//                PrivateConfig.printLog(PrivateConfig.fileWriter, "下单结束");
                /*if (PrivateConfig.daYin.contains(personInfo.getString(PrivateConfig.name))) {
                    if(PrivateConfig.isMai3(myOrder)){
                        System.out.println("买：" + myOrder.getSymbol() + "：" + getOrigQty);
                    }else {
                        System.out.println("卖：" + myOrder.getSymbol() + "：" + getOrigQty);
                    }
                }*/
                return myOrder;
            } catch (Exception e) {
                try {
                    e.printStackTrace();
//                    PrivateConfig.printLog(PrivateConfig.fileWriter, e);
                    T5.searchAll("连续3次，有问题，关闭软件，重新启动4"+ e.getMessage() + personInfo.getString(PrivateConfig.name));
                } catch (Exception e1) {
                    e1.printStackTrace();
                    T5.searchAll("连续3次，有问题，关闭软件，重新启动5"+ e1.getMessage() + personInfo.getString(PrivateConfig.name));
                }
            }
        }
        return new Order();
    }

    
}


class MulGetAllOrders implements Callable {

    private JSONObject personInfo;
    private List<Order> list;
    private HashMap<Long, Order[]> orderIdGuaMap;
    private Integer i;

    public MulGetAllOrders(JSONObject jsonObject, List<Order> list, HashMap<Long, Order[]> orderIdGuaMap , Integer i) {
        this.personInfo = jsonObject;
        this.list = list;
        this.orderIdGuaMap = orderIdGuaMap;
        this.i = i;

    }

    @Override
    public Object call() throws Exception {

        try {
            //已创建的订单
            Map<String, Order> newMap = (Map<String, Order>) personInfo.get(PrivateConfig.newMap);
            //已撤销的订单
            Map<String, Order> cancleMap = (Map<String, Order>) personInfo.get(PrivateConfig.cancleMap);

            SyncRequestClient syncRequestClient = (SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient);

            BigDecimal beiShu = new BigDecimal(personInfo.getString(PrivateConfig.beiShu));

            for (Order order : list) {
                //跟单数量
                BigDecimal geShu = order.getOrigQty().multiply(beiShu);
                String getOrigQty = geShu.setScale(getXSM(order.getSymbol()), BigDecimal.ROUND_DOWN).toString();
                //跟单单价
                String laojia = order.getPrice().toString();
                String getPrice = order.getPrice().toString();
                int xsw = getNumberDecimalDigits(order);
                //两个小数位以上改价格，2个小数位的话就不改了
//            if (xsw > 2) {
                //因为买入都是市价，只有卖出的时候会挂单
                if (PrivateConfig.isDuo3(order)) {
                    // 做多
                    BigDecimal bigDecimal = new BigDecimal(PrivateConfig.shao);//价格低一些
                    // 币安支持的小数位是有限制的，如果小数多，会下单失败的，所以要和田的下单小数位一致
                    // 要求田下单时，有几位小数就用几位小数，例如gala的0.52.可以下单为0.51999
                    getPrice = order.getPrice().multiply(bigDecimal).setScale(xsw, BigDecimal.ROUND_DOWN).toString();
                } else {
                    //做空
                    BigDecimal bigDecimal = new BigDecimal(PrivateConfig.duo);
                    getPrice = order.getPrice().multiply(bigDecimal).setScale(xsw, BigDecimal.ROUND_DOWN).toString();
                }
//            }
                // 如果老价和新价相差大于0.003，证明新价计算不合理，取老价
                if (chaDa(laojia, getPrice, "0.003")) {
                    getPrice = laojia;
                }
                if (PrivateConfig.isGua(order)) {
                    //挂单（当挂单时，状态是new，等成交后，不会创建新的订单信息，而是改动此订单的状态为filled，更新下时间，这点需要注意，别当成新订单下了）
                    if (!newMap.containsKey(personInfo.getString(PrivateConfig.name) + order.getOrderId())) {
                        // 下单，如何做到下单成功？
                        print(personInfo, "老价：" + laojia);
                        print(personInfo, "新价：" + getPrice);

                        Order myOrder = postOrderGua(syncRequestClient, order, getOrigQty, getPrice, laojia);
                        Order[] orders = orderIdGuaMap.get(order.getOrderId());
                        orders[i] = myOrder;
                        newMap.put(personInfo.getString(PrivateConfig.name) + order.getOrderId(), myOrder);
                        print(personInfo, Thread.currentThread().getName() + "---挂单成功，没有问题！" + currentTime());
                    }

                } else if ("NEW,PARTIALLY_FILLED,FILLED".contains(order.getStatus())) {
                    //市价
                    if (!newMap.containsKey(personInfo.getString(PrivateConfig.name) + order.getOrderId())) {
                        Order myOrder = postOrder(syncRequestClient, order, getOrigQty);
                        newMap.put(personInfo.getString(PrivateConfig.name) + order.getOrderId(), myOrder);
                        print(personInfo, Thread.currentThread().getName() + "---下单成功，没有问题！" + currentTime());
                    }

                } else {
                    PrivateConfig.cancleMap(personInfo, order);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
//            PrivateConfig.printLog(PrivateConfig.fileWriter, e);
            T5.searchAll("连续3次，有问题，关闭软件，重新启动6。"+ e.getMessage() + personInfo.getString(PrivateConfig.name));
        }
        return null;
    }

    public Boolean chaDa(String a, String b, String cha) throws InterruptedException {
        try {
            BigDecimal a1 = new BigDecimal(a);
            if(a1.compareTo(new BigDecimal("0")) <= 0){
                a1 = a1.add((new BigDecimal("0.000001")));
            }
            BigDecimal b1 = new BigDecimal(b);
            BigDecimal cha1 = new BigDecimal(cha);
            if (a1.subtract(b1).abs().compareTo(new BigDecimal("0.000001")) < 0) {
                return false;
            }
            if (a1.subtract(b1).abs().divide(a1, 3, BigDecimal.ROUND_HALF_UP).compareTo(cha1) > 0) {
                return true;
            }
        } catch (Exception e) {
//            System.out.println("出错了1");
            e.printStackTrace();
//            PrivateConfig.printLog(PrivateConfig.fileWriter, e);
            T5.searchAll("连续3次，有问题，关闭软件，重新启动7。"+ e.getMessage() + personInfo.getString(PrivateConfig.name));
        }

        return false;
    }


    public void print(JSONObject personInfo, String msg){
        if(PrivateConfig.ceShi.equals("0")){
           System.out.println(msg);
       }

    }

    /**挂单
     *
     * @param syncRequestClient
     * @param order
     * @param getOrigQty
     */
    public Order postOrderGua(SyncRequestClient syncRequestClient, Order order, String getOrigQty, String getPrice, String laoJia) throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            try {
//                PrivateConfig.printLog(PrivateConfig.fileWriter, "开始下单");
                String reduceonly  = null;
                if(PositionSide.BOTH.toString().equals(order.getPositionSide())){
                    reduceonly = order.getReduceOnly().toString();
                }
                Order myOrder = syncRequestClient.postOrder(
                                order.getSymbol(),
                                OrderSide.valueOf(order.getSide()),//买还是卖
                                PositionSide.valueOf(order.getPositionSide()),//做多还是做空
                                OrderType.valueOf(order.getType()),
                                TimeInForce.valueOf(order.getTimeInForce()),
                                getOrigQty,//跟单数量，需要大于5。
                                getPrice,//跟单单价，总价需要大于5
                                reduceonly, //order.getReduceOnly().toString(),
                                order.getClientOrderId(),
                                null,//order.getStopPrice().toString(),
                                WorkingType.valueOf(order.getWorkingType()),
                                NewOrderRespType.RESULT);
//                PrivateConfig.printLog(PrivateConfig.fileWriter, "下单结束");
                /*if (PrivateConfig.daYin.contains(personInfo.getString(PrivateConfig.name))) {
                    if(PrivateConfig.isMai3(myOrder)){
                        System.out.println("买：" + myOrder.getSymbol() + "：" + getOrigQty);
                    }else {
                        System.out.println("卖：" + myOrder.getSymbol() + "：" + getOrigQty);
                    }
                }*/
                return myOrder;
            } catch (BinanceApiException e){
                try {
                    //价格的精度太高，挂单失败后，使用和田一样的价格
                    if(e.getMessage().contains("-1111")
                            || e.getMessage().contains("-4014")){
                        getPrice = laoJia;
                    }
                    e.printStackTrace();
//                    PrivateConfig.printLog(PrivateConfig.fileWriter, e);
                    T5.searchAll("连续3次，有问题，关闭软件，重新启动8"+ e.getMessage() + personInfo.getString(PrivateConfig.name));
                } catch (Exception e1) {
                    e1.printStackTrace();
                    T5.searchAll("连续3次，有问题，关闭软件，重新启动9"+ e.getMessage() + personInfo.getString(PrivateConfig.name));
                }
            }catch (Exception e) {
                try {
                    e.printStackTrace();
//                    PrivateConfig.printLog(PrivateConfig.fileWriter, e);
                    T5.searchAll("连续3次，有问题，关闭软件，重新启动10"+ e.getMessage() + personInfo.getString(PrivateConfig.name));
                } catch (Exception e1) {
                    e1.printStackTrace();
                    T5.searchAll("连续3次，有问题，关闭软件，重新启11"+ e.getMessage() + personInfo.getString(PrivateConfig.name));
                }
            }
        }
        return new Order();
    }

    /**市价
     *
     * @param syncRequestClient
     * @param order
     * @param getOrigQty
     */
    public Order postOrder(SyncRequestClient syncRequestClient, Order order, String getOrigQty) throws InterruptedException {
        /*if (PrivateConfig.daYin.contains(personInfo.getString(PrivateConfig.name))) {
            System.out.println(getCurrentTime() + "买卖一单");
        }*/
        for (int i = 0; i < 2; i++) {
            try {
//                PrivateConfig.printLog(PrivateConfig.fileWriter, "开始下单");
                String reduceonly  = null;
                if(PositionSide.BOTH.toString().equals(order.getPositionSide())){
                    reduceonly = order.getReduceOnly().toString();
                }
                Order myOrder = syncRequestClient.postOrder(
                        order.getSymbol(),
                        OrderSide.valueOf(order.getSide()),//买还是卖
                        PositionSide.valueOf(order.getPositionSide()),//做多还是做空 long short both
                        OrderType.valueOf("MARKET"),// 订单类型，limit：限价单；MARKET：市价单（想要成功买卖，使用这个）
                        null,//TimeInForce.valueOf("GTC"),//成交为止，一直有效，（市价时，可以不填）
                        getOrigQty,//跟单数量，需要大于5
                        null,//跟单单价，总价需要大于5（市价时，可以不填）
                        reduceonly, //order.getReduceOnly().toString(),
                        null,//order.getClientOrderId(),
                        null,//order.getStopPrice().toString(),
                        null,//WorkingType.valueOf(order.getWorkingType()),
                        NewOrderRespType.RESULT);
//                PrivateConfig.printLog(PrivateConfig.fileWriter, "下单结束");
                /*if (PrivateConfig.daYin.contains(personInfo.getString(PrivateConfig.name))) {
                    if(PrivateConfig.isMai3(myOrder)){
                        System.out.println("买：" + myOrder.getSymbol() + "：" + getOrigQty);
                    }else {
                        System.out.println("卖：" + myOrder.getSymbol() + "：" + getOrigQty);
                    }
                }*/
                return myOrder;
            } catch (Exception e) {
                try {
                    e.printStackTrace();
//                    PrivateConfig.printLog(PrivateConfig.fileWriter, e);
                    T5.sendMe("连续3次，有问题，关闭软件，重新启动12"+ e.getMessage() + personInfo.getString(PrivateConfig.name));
                } catch (Exception e1) {
                    e1.printStackTrace();
                    T5.sendMe("连续3次，有问题，关闭软件，重新启动13"+ e.getMessage() + personInfo.getString(PrivateConfig.name));
                }
            }
        }
        return new Order();
    }

    private  String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis())); // 时间戳转换日期
    }

    private String currentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd = sdf.format(new Date(System.currentTimeMillis())); // 时间戳转换日期
        return sd;
    }

    /**
     * 如果能够获取到币种的小数位是最好了，毕竟通过计算的可能会有问题
     * @param
     * @return
     */
    private int getNumberDecimalDigits(Order order) {
        if(PrivateConfig.getJGXsw(order.getSymbol()) != null){
            return PrivateConfig.getJGXsw(order.getSymbol());
        }
        int dcimalDigits = 0;
        String balanceStr = order.getPrice().toString();
        int indexOf = balanceStr.indexOf(".");
        if (indexOf > 0) {
            dcimalDigits = balanceStr.length() - 1 - indexOf;
        }
        return dcimalDigits;
    }

    private String removeEnd(String str){
        if(StringUtils.isEmpty(str) || "".equals(str)){
            return "";
        }
        return str.substring(0, str.length() - 1);
    }

}

