package com.ChongQi;

import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.bushu.PrivateConfig;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class QiDongJavaOK {

    public static void main(String[] args) throws InterruptedException {
        QiDongJavaOK chongQi = new QiDongJavaOK();
        chongQi.method("E://huyl//0batok");

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
                        //替换
                    /*try (FileWriter writer = new FileWriter(path)) {
                        writer.write(config.toJSONString());
                        System.out.println( computer + "：token替换成功");
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println(computer + "：token替换失败");
                    }*/

                        String user = "root";
                        String host = ip;
                        int port = 22; // SSH通常使用22端口
                        String privateKey = "/path/to/your/private/key"; // 如果使用密码认证，则不需要此参数
                        String password = "4mWkCV88cKBJzPy"; // 如果使用密钥认证，则不需要此参数
//                    String command = "nohup java -jar /root/huyl/bushu/bian-0.0.1-tongYong.jar /root/huyl/bushu bianGenDan " + token.getString("token") + " --server.port=10184 &";
//                    String command = " source /etc/profile; nohup /root/huyl/jdk1.8.0_181/bin/java -jar /root/huyl/bushu/bian-0.0.1-tongYong.jar /root/huyl/bushu bianGenDan "
//                            + token.getString("token") + "  --server.port=10184 > /root/huyl/bushu/nohup.out 2>&1 &";
                        String command = " source /etc/profile; nohup /root/huyl/jdk1.8.0_141/bin/java -jar /root/huyl/bushu/bian-0.0.1-tongYong.jar /root/huyl/bushu genOk "
                                + token.getString("token") + " --server.port=10184 > /root/huyl/bushu/nohup.out 2>&1 &";

                        JSch jsch = new JSch();
                        Session session = jsch.getSession(user, host, port);
                        session.setPassword(password);

                        // 跳过主机密钥检查，实际应用中请确保安全性
                        session.setConfig("StrictHostKeyChecking", "no");

                        session.connect();

                        ChannelExec channel = (ChannelExec) session.openChannel("exec");
                        channel.setCommand(command);

                        channel.setInputStream(null);
                        ((ChannelExec) channel).setErrStream(System.err);

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
                                System.out.println("exit-status: " + channel.getExitStatus());
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (Exception ee) {
                            }
                        }
                        channel.disconnect();
                        session.disconnect();
                        System.out.println(computer + "：启动成功了");
                        return "1";
                    }catch (Exception e){
                        System.out.println(computer + "：启动失败了");
                        return "0";
                    }
                }
            };
            threadPoolExecutor.submit(callable1);
            Thread.sleep(50);

        }
    }

}
