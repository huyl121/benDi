package com.example.bian.coin;

import com.alibaba.fastjson.JSONObject;
import com.example.bian.ChangeInitialLeverage;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import com.example.bian.genDan.Transfer;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * 根据老师现有的持仓，自己分析他是加仓还是减仓
 */
public class JianKongCoin {

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


        args = new String[2];
        System.out.println("开始啦");
        args[0] = "E://code//biance";
        args[1] = "jianKong";
        PrivateConfig.before(args[0], args[1]);
        PrivateConfig.getJGXsw();
        PrivateConfig.xsw(true);


        JianKongCoin jianKong4 = new JianKongCoin();
        jianKong4.method(PrivateConfig.genDan_personInfoList);

    }



    public void method(List<JSONObject> personInfoList) throws InterruptedException {
        try {
            PrivateConfig.printLog("BiCoin开启监控：" + PrivateConfig.getCurrentTime());
            //休息一下，等待主线程完成后，再监控
            Thread.sleep(30 * 1000);

            SyncRequestClient syncRequestClient = (SyncRequestClient) personInfoList.get(0).get(PrivateConfig.syncRequestClient);
            int g = 0;
            int h = 0;
            int a = 0;
            boolean api = true;
            while (true) {
                try {
                    Thread.sleep(60 * 1000);
                    PrivateConfig.xsw(false);

                    //设置杠杆
                    g++;
                    if (g > 60*48) {
                        g = 0;
                        System.out.println("gang gan");
                        /*ChangeInitialLeverage changeInitialLeverage = new ChangeInitialLeverage();
                        changeInitialLeverage.method(personInfoList);*/

                        //查看客户的转入转出
                        Transfer transfer = new Transfer();
                        transfer.method1(personInfoList);

                    }

                    //检查api，只有在需要开通VPN的地方采用
                    if(PrivateConfig.daiLi.equals("1")){
                        if(!api){
                            try {
                                PrivateConfig.printLog(syncRequestClient.getAccountInformation().getTotalMarginBalance() + "");
                                api = true;
                                a = 0;
                            } catch (Exception e) {
                                api = false;
                                e.printStackTrace();
                                a++;
                                if(a > 2){
                                    a= 0;
                                    T5.searchAll("连续5次，说明出大问题了，抓紧联系龙，api不通了");
                                }
                            }
                        }else {
                            h++;
                            if (h > 30) {
                                h = 0;
                                System.out.println("check api ");
                                try {
                                    PrivateConfig.printLog(syncRequestClient.getAccountInformation().getTotalMarginBalance() + "");
                                    api = true;
                                } catch (Exception e) {
                                    api = false;
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    Thread.sleep(1000 * 6);
                    e.printStackTrace();
                } catch (Throwable t) {
                    t.printStackTrace();
                }

            }
        } catch (Exception e) {
            System.out.println("监控程序启动出错了");
            e.printStackTrace();
        }

    }

}

