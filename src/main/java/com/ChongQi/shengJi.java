package com.ChongQi;

import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.bushu.PrivateConfig;
import com.google.gson.JsonObject;
import com.jcraft.jsch.*;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class shengJi {

    public static void main(String[] args) throws InterruptedException {
        shengJi chongQi = new shengJi();
        chongQi.method("E://huyl//0bat");

    }


    public void method(String directory) throws InterruptedException {
        List<String> paths = new ArrayList<>();
        File1.DirectoryScanner(directory, paths);
        for(String path : paths){
            System.out.println(path);
        }
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(paths.size(), paths.size(), 10,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.DiscardPolicy());
        JSONObject token = PrivateConfig.readJsonFile(directory + "/token.json");
        for(String path : paths){
            Callable callable1 = new Callable() {
                @Override
                public String call() throws Exception {
                    JSONObject config1 = PrivateConfig.readJsonFile(path);
                    String computer = config1.getString("computer");
                    String ip = config1.getString("ip");

                    try{
                        zhiXing(ip, computer, directory, path, token);
                    }catch (Exception e){
                        e.printStackTrace();
                    }catch (Throwable throwable){
                        throwable.printStackTrace();
                    }
                    return "1";
                }
            };
            threadPoolExecutor.submit(callable1);
            Thread.sleep(50);

        }


    }
    public  void zhiXing(String host, String computer, String directory, String path, JSONObject token) throws Exception {
        String user = "root";
        int port = 22;
        String password = "4mWkCV88cKBJzPy";

        // ========== 1. 定义多条需要执行的命令 ==========
        String[] cmdList = new String[]{
                "source /etc/profile; pkill -9 -f java ",
                "source /etc/profile; \\cp -rf /root/huyl/bian-0.0.1-tongYong.jar /root/huyl/bushu/ "
        };

        // ========== 2. 循环逐条执行，每条执行完等待1秒 ==========
        for (String command : cmdList) {
//            System.out.println("==================== 开始执行命令：" + command + " ====================");
            execSingleCmd(user, host, port, password, command, computer);

            // 每条命令执行完成后，间隔1秒再执行下一条
            Thread.sleep(2000);
        }
        System.out.println(computer + ":jar包已升级");
        Thread.sleep(3000);
        QiDongJava chongQi = new QiDongJava();
        chongQi.mo(directory, path, token);
    }

    /**
     * 封装：单独执行一条SSH命令，执行完成自动关闭通道和会话
     */
    public static void execSingleCmd(String user, String host, int port, String password, String command, String computer) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(user, host, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand(command);
        channel.setInputStream(null);
        channel.setErrStream(System.err);

        InputStream in = channel.getInputStream();
        channel.connect();

        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) break;
                String msg = new String(tmp, 0, i);
                System.out.print(msg);
            }
            if (channel.isClosed()) {
//                System.out.println(computer + " : 当前命令 exit-status: " + channel.getExitStatus());
                break;
            }
            Thread.sleep(500);
        }

        // 释放资源
        channel.disconnect();
        session.disconnect();
    }
}
