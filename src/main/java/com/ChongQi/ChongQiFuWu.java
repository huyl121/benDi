package com.ChongQi;

import com.jcraft.jsch.*;
import kotlin.Result;


public class ChongQiFuWu {



    public static String resultDevice(String ip, String computer) {
        String host = ip;   //服务器ip
        String username = "root";  //服务器登录账号
        String password = "4mWkCV88cKBJzPy";   //服务器登录密码
        int port = 22; // 默认SSH端口

        try {
            // 创建JSch对象
            JSch jsch = new JSch();

            // 创建SSH会话
            Session session = (Session) jsch.getSession(username, host, port);
            session.setPassword(password);

            // 设置StrictHostKeyChecking属性为no，避免HostKey检查
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            // 连接到服务器
            session.connect();

            // 创建SSH通道
            Channel channel = session.openChannel("exec");

            // 设置执行的命令
            String command = "sudo /sbin/reboot"; // 请注意，这里使用了sudo来执行重启命令
//			 String command = "sudo mkdir /ceshi"; // 请注意，这里使用了sudo来创建文件夹

            ((ChannelExec) channel).setCommand(command);

            // 获取输入流和输出流
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);

            // 连接通道并等待执行完成
            channel.connect();
            channel.disconnect();

            // 断开SSH会话
            session.disconnect();


        } catch (JSchException e) {
            e.printStackTrace();
            System.out.println(computer + "：重启失败");
            return "重启失败";
        }
        System.out.println(computer + "：重启成功");
        return "重启成功";

    }


}
