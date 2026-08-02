package com.example.bian.genDan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;

public class HuoQuDingDan {

    public static void main(String[] args) throws IOException {

        // 准备工作



        args = new String[2];
        args[0] = "E://code//biance";
        args[1] = "0-genDan";
        PrivateConfig.before(args[0], args[1]);

        // 对https也开启代理

        System.out.println("开代理");
        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10819");


        HuoQuDingDan huoQuDingDan = new HuoQuDingDan();
        huoQuDingDan.method();

    }

    public void method() throws IOException {
        int i=100;
        while (true){
            i++;
            try{
                String s = GetPositions.getPosition(PrivateConfig.genDan_genPortfolioId);
                if (StringUtils.isNotBlank(s)) {
//                System.out.println(s);
                    JSONObject jsonObject = JSON.parseObject(s);
                    if ("000000".equals(jsonObject.getString("code")) && jsonObject.getBoolean("success")) {
                        if (i>1) {
                            i=0;
                            System.out.println(jsonObject.toJSONString());
                            System.out.println(PrivateConfig.getCurrentTime());
                        }
                    } else {
                        PrivateConfig.printLog("订单失败，连续5次，有问题！1");
                        T5.searchAll("订单失败，连续5次，有问题！3");
                    }
                }

            }catch (IOException e){
                System.out.println(e.getMessage());
                try {
                    Thread.sleep(1000*10);
                } catch (InterruptedException e1) {
                    throw new RuntimeException(e1);
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
                try {
                    Thread.sleep(1000*60*10);
                } catch (InterruptedException e1) {
                    throw new RuntimeException(e1);
                }
            }
            try {
//                Thread.sleep(1000*60*10);
                Thread.sleep(1000*10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }



}
