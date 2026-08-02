package com.example.bian.genDan;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.binance.connector.client.SpotClient;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import org.apache.commons.collections4.CollectionUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Transfer {

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10819");


        args = new String[2];
        System.out.println("开始啦");
        args[0] = "E://code//biance";
        args[1] = "transfer";
        PrivateConfig.before(args[0], args[1]);
        PrivateConfig.getJGXsw();
        PrivateConfig.xsw(true);


        Transfer transfer = new Transfer();
        transfer.method(PrivateConfig.personInfoList);
//        transfer.method1(PrivateConfig.personInfoList);

    }

    public  void method1(List<JSONObject> personInfoList) throws InterruptedException {

        for (JSONObject personInfo : personInfoList) {

            /*
             * UMFUTURE_MAIN U本位合约钱包转向现货钱包
             * UMFUTURE_FUNDING U本位合约钱包转向资金钱包
             * UMFUTURE_MARGIN U本位合约钱包转向杠杆全仓钱包
             * UMFUTURE_OPTION U本位合约钱包转向期权钱包
             *
             * */
            if("qiuXuYan".equals(personInfo.getString(PrivateConfig.name))
                    ||"huYaLong".equals(personInfo.getString(PrivateConfig.name))
                    ||"huYaZhen".equals(personInfo.getString(PrivateConfig.name))
                    ||"huDongMin".equals(personInfo.getString(PrivateConfig.name))
                    ||"huPengPeng".equals(personInfo.getString(PrivateConfig.name))
                    ||"huMengLong".equals(personInfo.getString(PrivateConfig.name))
                    ||"laoEr".equals(personInfo.getString(PrivateConfig.name))
                    ||"laoSan".equals(personInfo.getString(PrivateConfig.name))

            ){
                continue;
            }
            print1(personInfo, PrivateConfig.UMFUTURE_MAIN);
            print1(personInfo, PrivateConfig.UMFUTURE_FUNDING);
            print1(personInfo, PrivateConfig.UMFUTURE_MARGIN);
            print1(personInfo, PrivateConfig.UMFUTURE_OPTION);

        }
        System.out.println("查看客户的转入转出");
    }

    public void print1(JSONObject personInfo, String msg) throws InterruptedException {
        try {
            SpotClient client = ((SpotClient) personInfo.get(PrivateConfig.spotClient));
            String alias = personInfo.getString(PrivateConfig.alias);
            Map<String, Object> parameters = new LinkedHashMap<>();
            parameters.put("type", msg);
            parameters.remove("signature");
            parameters.put("timestamp", System.currentTimeMillis() - 1000);
            String result = client.createWallet().queryUniversalTransfer(parameters);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("rows");
            if (CollectionUtils.isNotEmpty(jsonArray)) {
                for (Object o : jsonArray) {
                    JSONObject jsonObject1 = (JSONObject) o;
                    Long timestamp = jsonObject1.getLong("timestamp");
                    String amount = jsonObject1.getString("amount");
                    String s = "_" + getData(timestamp) + "_" + amount;

                    if (PrivateConfig.UMFUTURE_MAIN.equals(msg)) {
                        if(!timestamp.equals(personInfo.getLong(PrivateConfig.UMFUTURE_MAIN))){
                            T5.searchAll(alias + "看到这个问题，抓紧联系胡亚龙。U本位合约钱包转向现货钱包" + s + "_" + personInfo.getString(PrivateConfig.alias));
                        }
                    }

                    if (PrivateConfig.UMFUTURE_FUNDING.equals(msg)) {
                        if(!timestamp.equals(personInfo.getLong(PrivateConfig.UMFUTURE_FUNDING))){
                            T5.searchAll(alias + "看到这个问题，抓紧联系胡亚龙。U本位合约钱包转向资金钱包" + s + "_" + personInfo.getString(PrivateConfig.alias));
                        }
                    }

                    if (PrivateConfig.UMFUTURE_MARGIN.equals(msg)) {
                        if(!timestamp.equals(personInfo.getLong(PrivateConfig.UMFUTURE_MARGIN))){
                            T5.searchAll(alias + "看到这个问题，抓紧联系胡亚龙。U本位合约钱包转向杠杆全仓钱包" + s + "_" + personInfo.getString(PrivateConfig.alias));
                        }
                    }

                    if (PrivateConfig.UMFUTURE_OPTION.equals(msg)) {
                        if(!timestamp.equals(personInfo.getLong(PrivateConfig.UMFUTURE_OPTION))){
                            T5.searchAll(alias + "看到这个问题，抓紧联系胡亚龙。U本位合约钱包转向期权钱包" + s + "_" + personInfo.getString(PrivateConfig.alias));
                        }
                    }
                    //只查最新的一条
                    break;
                }
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        Thread.sleep(3000);

    }

    public  void method(List<JSONObject> personInfoList) {

        Map<String, Object> parameters = new LinkedHashMap<>();
        for (JSONObject personInfo : personInfoList) {

            System.out.println(personInfo.getString(PrivateConfig.alias));
            PrivateConfig.printLog1(PrivateConfig.fileWriter, personInfo.getString(PrivateConfig.alias));

            SpotClient client = ((SpotClient) personInfo.get(PrivateConfig.spotClient));
            parameters.put("startTime", System.currentTimeMillis() - (long)170*86400000);

            /*
             * UMFUTURE_MAIN U本位合约钱包转向现货钱包
             * UMFUTURE_FUNDING U本位合约钱包转向资金钱包
             * UMFUTURE_MARGIN U本位合约钱包转向杠杆全仓钱包
             * UMFUTURE_OPTION U本位合约钱包转向期权钱包
             *
             * */

            parameters.put("type", "UMFUTURE_MAIN");
            print(client, parameters, "U本位合约钱包转向现货钱包UMFUTURE_MAIN");

            parameters.put("type", "UMFUTURE_FUNDING");
            print(client, parameters, "U本位合约钱包转向资金钱包UMFUTURE_FUNDING");

            parameters.put("type", "UMFUTURE_MARGIN");
            print(client, parameters, "U本位合约钱包转向杠杆全仓钱包UMFUTURE_MARGIN");

            parameters.put("type", "UMFUTURE_OPTION");
            print(client, parameters, "U本位合约钱包转向期权钱包UMFUTURE_OPTION");

            System.out.println("---------11111111111111-----------");
            PrivateConfig.printLog1(PrivateConfig.fileWriter, "---------11111111111111-----------");

            /*
             * MAIN_UMFUTURE 现货钱包转向U本位合约钱包
             * MARGIN_UMFUTURE 杠杆全仓钱包转向U本位合约钱包
             * FUNDING_UMFUTURE 资金钱包转向U本位合约钱包
             * OPTION_UMFUTURE 期权钱包转向U本位合约钱包
             * */
            parameters.put("type", "MAIN_UMFUTURE");
            print(client, parameters, "现货钱包转向U本位合约钱包MAIN_UMFUTURE");

            parameters.put("type", "MARGIN_UMFUTURE");
            print(client, parameters, "杠杆全仓钱包转向U本位合约钱包MARGIN_UMFUTURE");

            parameters.put("type", "FUNDING_UMFUTURE");
            print(client, parameters, "资金钱包转向U本位合约钱包FUNDING_UMFUTURE");

            parameters.put("type", "OPTION_UMFUTURE");
            print(client, parameters, "期权钱包转向U本位合约钱包OPTION_UMFUTURE");

        }

        System.out.println();
    }

    public void print(SpotClient client, Map<String, Object> parameters, String msg){
        try {
            parameters.remove("signature");
            parameters.put("timestamp", System.currentTimeMillis() - 1000);
            String result = client.createWallet().queryUniversalTransfer(parameters);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("rows");
            if (CollectionUtils.isNotEmpty(jsonArray)) {
                for (Object o : jsonArray) {
                    JSONObject jsonObject1 = (JSONObject) o;
                    Long timestamp = jsonObject1.getLong("timestamp");
                    String amount = jsonObject1.getString("amount");
                    String s = msg + "_" + getData(timestamp) + "_" + amount + "_" + timestamp;
                    System.out.println(s);
                    PrivateConfig.printLog1(PrivateConfig.fileWriter, s);
                }
            }
            System.out.println("--------------------");
            PrivateConfig.printLog1(PrivateConfig.fileWriter, "--------------------");
            Thread.sleep(1000);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public String getData(Long time){
        //当前时间毫秒的时间戳转换为日期
        Date millisecondDate= new Date(time);
        //格式化时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String millisecondStrings = formatter.format(millisecondDate);
        return millisecondStrings;
    }
}