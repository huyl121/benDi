package com.example.bian;

import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.PrintStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class JianKongCMD {



    public static void main(String[] args)
            throws Exception {
        args = new String[1];
        System.out.println("开始啦");
        args[0] = "E://baidutongbu//baidutongbu//tongbu//bian//bian";

        PrivateConfig.before(args[0], args[1]);

        JianKongCMD jianKongCMD = new JianKongCMD();
        jianKongCMD.method(args);
    }

    static int errorCount = 0;
    public void method(String[] args)
            throws InterruptedException {
        for (; ; ) {
            try {
                if (System.currentTimeMillis() - PrivateConfig.fileLog.lastModified() > 60000L) {
                    errorCount++;
                    Chrome chrome = new Chrome();
                    chrome.open();
                    Thread.sleep(5000L);
//                    break;
                    if(errorCount > 2){
                        errorCount = 0;
                        System.out.println("log文件没有更新，抓紧联系胡亚龙，刷新跟单网页");
                        T5.searchAll("log文件没有更新，抓紧联系胡亚龙，刷新跟单网页");
                    }
                    continue;
                }
                errorCount = 0;
//                System.out.println(PrivateConfig.fileLog.getPath());
                System.out.println(System.currentTimeMillis() - PrivateConfig.fileLog.lastModified());
//                System.out.println(PrivateConfig.getCurrentTime() + "监控log");
                Thread.sleep(34000L);
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }



    public static void restart()
            throws InterruptedException {
        Thread.sleep(180000L);
    }
}
