package com.example.bian;

import com.ChongQi.ChongQi;
import com.ChongQi.QiDongJava;
import com.ChongQi.QiDongJavaOK;
import com.ChongQi.QiDongLog;
import com.alibaba.fastjson.JSONObject;
import com.example.OK.QingCangOk;
import com.example.OK.genDansOk;
import com.example.bian.coin.genBiCoin;
import com.example.bian.coins.genBiCoins;
import com.example.bian.genDan.*;
import com.example.bian.client.bushu.PrivateConfig;
import java.io.IOException;
import java.util.List;

import com.example.bian.genDan.analysis.Analysis;
import com.example.bian.genDan.genDans.genDans;
import com.example.bian.ok.genOkByPosition;
import com.example.bian.xin.*;
import com.example.bian.xin1.GenTian5;
import com.ling.Dit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class BianApplication {
    public static void main(String[] args)
            throws InterruptedException, IOException {



        if(args.length==0){
            args = new String[3];
            args[0] = "E:/code/biance";
            args[1] = "GenDanZhengQian";
            args[2] = "a10000000";
        }

        PrivateConfig.init((args[0]));
        if(PrivateConfig.daiLi.equals("1")) {
            PrivateConfig.printLog("开代理");
            System.setProperty("https.proxySet", "true");
            System.setProperty("https.proxyHost", "127.0.0.1");
            System.setProperty("https.proxyPort", "10819");
        }


        /*args = new String[5];
        args[0] = "E://code//biance-main";
        args[1] = "qingCang";
        args[2] = "USDCUSDT";
        args[3] = "LONG";
        args[4] = "--server.port=10187";*/


        for(String s : args){
            System.out.println(s);
        }

        if ("chongQi".equals(args[1])) {
            System.out.println("重启服务器");
            ChongQi genDan = new ChongQi();
            genDan.method(args[0]);
            System.out.println("重启服务器完成");
            return;
        }
        if ("qiDongJava".equals(args[1])) {
            System.out.println("启动java");
            QiDongJava genDan = new QiDongJava();
            genDan.method(args[0]);
            System.out.println("启动java完成");
            return;
        }

        if ("qiDongJavaOK".equals(args[1])) {
            System.out.println("启动java ok");
            QiDongJavaOK genDan = new QiDongJavaOK();
            genDan.method(args[0]);
            System.out.println("启动java完成");
            return;
        }

        if ("qiDongLog".equals(args[1])) {
            System.out.println("启动log");
            QiDongLog genDan = new QiDongLog();
            genDan.method(args[0]);
            System.out.println("启动log完成");
            return;
        }

        if ("GenDanZhengQian".equals(args[1])){
            if(PrivateConfig.daiLi.equals("1")) {
                System.out.println("开代理");
                System.setProperty("https.proxySet", "true");
                System.setProperty("https.proxyHost", "127.0.0.1");
                System.setProperty("https.proxyPort", PrivateConfig.port);
            }

            PrivateConfig.before(args[0], "0-"+args[1]);

            System.out.println("开始挣钱");
            if(PrivateConfig.ok_isOk.equals("1")){
                com.example.bian.ok.GenDanZhengQian genDan = new com.example.bian.ok.GenDanZhengQian();
                if(args.length>2){
                    genDan.method(args[2]);
                }else {
                    genDan.method("");
                }
            }else {
                GenDanZhengQian genDan = new GenDanZhengQian();
                if(args.length>2){
                    genDan.method(args[2]);
                }else {
                    genDan.method("");
                }
            }

            System.out.println("开始挣钱完成");
            return;
        }

        if ("dit".equals(args[1])) {
            System.out.println("dit开始");
            Dit dit = new Dit();
            dit.method(args[0]);
            System.out.println("dit结束");
        } else if(args[1].contains("musk")) {
            PrivateConfig.before(args[0], "0-" + args[1]);
        }else {
            PrivateConfig.before(args[0], "0-"+args[1]);
//        PrivateConfig.printLog(PrivateConfig.fileWriter, "启动");
            PrivateConfig.xsw(true);
        }





        SpringApplicationBuilder builder = new SpringApplicationBuilder(new Class[]{BianApplication.class});
        builder.headless(false).run(args);
        List<JSONObject> personInfoList;
        if(PrivateConfig.genDan_isGenDan.equals("1")){
            personInfoList = PrivateConfig.genDan_personInfoList;
        }else if(PrivateConfig.genDans_isGenDans.equals("1")){
            personInfoList = PrivateConfig.genDans_personInfoList;
        }else if(PrivateConfig.analysis_isAnalysis.equals("1")){
            personInfoList = PrivateConfig.genDan_personInfoList;
        }else if(PrivateConfig.biCoin_isBiCoin.equals("1")){
            personInfoList = PrivateConfig.biCoin_personInfoList;
        }else if(PrivateConfig.biCoins_isBiCoins.equals("1")){
            personInfoList = PrivateConfig.biCoins_personInfoList;
        }else if(PrivateConfig.ok_isOk.equals("1")){
            personInfoList = PrivateConfig.ok_personInfoList;
        }else {
            personInfoList = PrivateConfig.personInfoList;
        }
        if ("chaKan".equals(args[1])) {
            System.out.println("查看账号余额");
            if(PrivateConfig.daiDanOk_isDaiDanOk.equals("1")){
                com.example.OK.ChaKan postOrder = new com.example.OK.ChaKan();
                postOrder.method(PrivateConfig.daiDanOk_personInfoList);
            }else {
                ChaKan chaKan = new ChaKan();
                chaKan.method(personInfoList);
            }

            System.out.println("查看完成");
        }
        if ("sheZhi".equals(args[1])) {
            System.out.println("设置杠杆");
            ChangeInitialLeverage changeInitialLeverage = new ChangeInitialLeverage();
            changeInitialLeverage.method(personInfoList);
            System.out.println("设置完成");
        }
        if ("kaiDuo".equals(args[1])) {
            KaiDuo3 kaiDuo3 = new KaiDuo3();
            kaiDuo3.method1(personInfoList);
            System.out.println("多空测试完成");
        }
        if ("genTian".equals(args[1])) {
            System.out.println("跟田开始啦");
//            PrivateConfig.printLog(PrivateConfig.fileWriter, "跟田开始啦");
            GenTian5 genTian5 = new GenTian5();
            genTian5.method(args);
            QingCang3 qingCang = new QingCang3();
            qingCang.qingCang(PrivateConfig.personInfoList, null, null);
            System.out.println("跟田结束啦");
        }

        //查看是否能关闭
        if ("guanBi".equals(args[1])) {
            System.out.println("启动查看是否能够关闭");
            GuanBi3 guanBi3 = new GuanBi3();
            guanBi3.method(args, personInfoList);
            System.out.println("启动查看是否能够关闭");
        }

        if ("qingCang".equals(args[1])) {
            System.out.println("清仓");
            if(PrivateConfig.daiDanOk_isDaiDanOk.equals("1")){
                QingCangOk qingCang = new QingCangOk();
                qingCang.method(args, PrivateConfig.daiDanOk_personInfoList);
            }else {
                QingCang3 qingCang3 = new QingCang3();
                qingCang3.method(args, personInfoList);
            }
            System.out.println("清仓完成");
        }

        if ("transfer".equals(args[1])) {
            System.out.println("查看转移");
            Transfer transfer = new Transfer();
            transfer.method(personInfoList);

            System.out.println("查看转移完成");
        }

        if ("bianGenDan".equals(args[1])) {
            System.out.println("币安跟单");
            if (args.length > 3) {
                PrivateConfig.genDan_token = args[2];
            }

            if(PrivateConfig.genDans_isGenDans.equals("1")){
                genDans genDans = new genDans();
                genDans.method(args);
            }else if(PrivateConfig.analysis_isAnalysis.equals("1")){
                Analysis analysis = new Analysis();
                analysis.method(args[0]);
            }else if(PrivateConfig.daiDanOk_isDaiDanOk.equals("1")){
                genDansOk genDansOk = new genDansOk();
                genDansOk.method(args);
            }else {
                genDan genDan = new genDan();
                genDan.method(null);
            }

            //异常结束，就是报错了
            QingCang3 qingCang = new QingCang3();
            qingCang.qingCang(personInfoList, null, null);
            System.out.println("币安跟单完成");
        }


        if ("genBiCoin".equals(args[1])) {
            System.out.println("BiCoin");
            genBiCoin genBiCoin = new genBiCoin();
            genBiCoin.method(null);
            //异常结束，就是报错了
            QingCang3 qingCang = new QingCang3();
            qingCang.qingCang(PrivateConfig.biCoin_personInfoList, null, null);
            System.out.println("BiCoin完成");
        }

        if ("genBiCoins".equals(args[1])) {
            System.out.println("BiCoins");
            genBiCoins genBiCoins = new genBiCoins();
            genBiCoins.method(null);
            //异常结束，就是报错了
            QingCang3 qingCang = new QingCang3();
            qingCang.qingCang(PrivateConfig.biCoins_personInfoList, null, null);
            System.out.println("BiCoins完成");
        }

        if (args[1].contains("qingCangBiCoins")) {
            System.out.println("清仓BiCoins");
            QingCang3 qingCang3 = new QingCang3();
            String[] symNames = args[1].split("_");
            if(symNames.length>1){//清仓指定老师的
                qingCang3.method(args, PrivateConfig.biCoins_gendanMap.get(symNames[1]));
            }else {
                qingCang3.method(args, PrivateConfig.biCoins_personInfoList);
            }

            System.out.println("清仓完成");
        }

        if ("genOk".equals(args[1])) {
            System.out.println("genOk");
            genOkByPosition genBiCoins = new genOkByPosition();
            genBiCoins.method(args);
            //异常结束，就是报错了
            QingCang3 qingCang = new QingCang3();
            qingCang.qingCang(PrivateConfig.ok_personInfoList, null, null);
            System.out.println("genOk完成");
        }



        if ("bianGenDanNew".equals(args[1])) {
            System.out.println("只获取订单");
            HuoQuDingDan huoQuDingDan = new HuoQuDingDan();
            huoQuDingDan.method();
            System.out.println("币安跟单完成");
        }





        if ("postMan".equals(args[1])) {
            System.out.println("币安跟单测试");
            Postman postman = new Postman();
            postman.ceShi();
            System.out.println("币安跟单测试完成");
        }












        //远程监控其他服务器
        if ("jianKongWangYe".equals(args[1])) {
            System.out.println("启动监控网页");
//            PrivateConfig.printLog(PrivateConfig.fileWriter, "远程监控开始啦");
            JianKongWangYe jianKongWangYe = new JianKongWangYe();
            jianKongWangYe.method(args);
            System.out.println("监控网页成功");
        }





        if ("genDan".equals(args[1])) {
            System.out.println("跟单开始啦");
            GenDan6 genDan = new GenDan6();
            genDan.method(args);
            System.out.println("跟单结束啦");
        }
        if ("jianKongCMD".equals(args[1])) {
            System.out.println("监控log开始啦");
//            PrivateConfig.printLog(PrivateConfig.fileWriter, "监控log开始啦");
            JianKongCMD jianKongCMD = new JianKongCMD();
            jianKongCMD.method(args);
            System.out.println("监控log结束啦");
        }

        if ("xiuFu".equals(args[1])) {
            System.out.println("修复");
            XiuFu xiuFu = new XiuFu();
            try {
                xiuFu.xiuFu();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("修复完成");
        }

        if ("yiJianQiDong".equals(args[1])) {
            System.out.println("启动网页");
            PrivateConfig.wangYe = true;
            Chrome chrome = new Chrome();
            chrome.open();
            System.out.println("启动网页成功");
        }



        if ("wangYe".equals(args[1])) {
            PrivateConfig.wangYe = true;
        }

        if ("ceShi".equals(args[1])) {
            try {
                Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
                //Runtime.getRuntime().exec("taskkill /F /IM iexplorer.exe");
                //Runtime.getRuntime().exec("taskkill /F /IM 360se.exe");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("启动成功");
    }

    public static void main1(String[] args)
            throws InterruptedException {
        SpringApplication.run(BianApplication.class, args);
    }
}
