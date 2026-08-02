package com.example.bian;

import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import okhttp3.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JianKongWangYe {



    public static void main(String[] args)
            throws Exception {

        args = new String[2];
        System.out.println("开始啦");
        args[0] = "E://baidutongbu//baidutongbu//tongbu//bian//bian";
        args[1] = "jianKongWangYe";
        PrivateConfig.before(args[0], args[1]);

        JianKongWangYe jianKongWangYe = new JianKongWangYe();
        jianKongWangYe.method(args);
    }

    static int errorCount = 0;
    static Integer success = 200;
    public void method(String[] args) {
        for (; ; ) {
            try {
                if (!success.equals(http())) {
                    errorCount++;
                    Thread.sleep(1000 * 60);
                    if (errorCount > 2) {
                        errorCount = 0;
//                        System.out.println("远程监控失败，超过2次，查看下");
                        T5.sendMe("远程监控失败，超过2次，查看下");
                    }
                    continue;
                }
//                System.out.println("正在监控远程服务器" + getCurrentTime());
                PrivateConfig.printLog(PrivateConfig.fileWriter,  "远程监控");
                errorCount = 0;
                Thread.sleep(1000 * 60 * 3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    static OkHttpClient client = new OkHttpClient().newBuilder().build();

    private Integer http() {
        try {
            String urlLogin = "http://" + PrivateConfig.ip + "/jianKongWangYe?data=";
            Request request = new Request.Builder()
                    .url(urlLogin)
                    .get()
                    .build();

            Call call = client.newCall(request);
            Response response = call.execute();

            return response.code();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis()));
    }
}
