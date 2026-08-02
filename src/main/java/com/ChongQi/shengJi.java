package com.ChongQi;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.InputStream;

public class shengJi {
    public static void main(String[] args) throws Exception {
        String user = "root";
        String host = "你的IP";
        int port = 22;
        String password = "4mWkCV88cKBJzPy";

        // ========== 1. 定义多条需要执行的命令 ==========
        String[] cmdList = new String[]{
                "source /etc/profile; echo 第一条命令执行成功",
                "source /etc/profile; mkdir -p /root/huyl/test",
                "source /etc/profile; nohup /root/huyl/jdk1.8.0_141/bin/java -jar /root/huyl/bushu/bian-0.0.1-tongYong.jar /root/huyl/bushu bianGenDan "
                        + "这里替换你的token" + " --server.port=10184 > /root/huyl/bushu/nohup.out 2>&1 &",
                "source /etc/profile; echo 程序启动完成，查看日志"
        };

        // ========== 2. 循环逐条执行，每条执行完等待1秒 ==========
        for (String command : cmdList) {
            System.out.println("==================== 开始执行命令：" + command + " ====================");
            execSingleCmd(user, host, port, password, command);

            // 每条命令执行完成后，间隔1秒再执行下一条
            Thread.sleep(1000);
        }
        System.out.println("所有命令全部执行完毕");
    }

    /**
     * 封装：单独执行一条SSH命令，执行完成自动关闭通道和会话
     */
    public static void execSingleCmd(String user, String host, int port, String password, String command) throws Exception {
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
                System.out.println("当前命令 exit-status: " + channel.getExitStatus());
                break;
            }
            Thread.sleep(500);
        }

        // 释放资源
        channel.disconnect();
        session.disconnect();
    }
}
