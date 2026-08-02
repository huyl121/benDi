package com.example.bian;

import com.example.bian.client.bushu.PrivateConfig;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Created by adimn on 2021/10/9.
 */
public class Chrome {

    static String[] urls = {"https://coolco.vip/",
            "https://coolco.vip/auth/login?redirect=/user"};


    public static void main(String[] args) throws InterruptedException {
        Chrome chrome = new Chrome();
        chrome.open();
    }

    public void open() throws InterruptedException {
        Robot robot = getRobot();
        closeBrowse();
        Thread.sleep(5000);
        openBrowse(urls);
        Thread.sleep(5000);
        //登录前确保自动填入用户名和密码
        loginChrome(robot);
        Thread.sleep(2000);
        String[] urls1 = {PrivateConfig.idCode};
        openBrowse(urls1);
        /*robot.keyPress(KeyEvent.VK_F12);
        Thread.sleep(20);
        robot.keyRelease(KeyEvent.VK_F12);*/
        Thread.sleep(1000);
        /*robot.keyPress(KeyEvent.VK_F5);
        Thread.sleep(20);
        robot.keyRelease(KeyEvent.VK_F5);*/
    }

    private void loginChrome(Robot robot) throws InterruptedException {

        for(int i=0; i<12; i++){
            robot.keyPress(KeyEvent.VK_TAB);
            Thread.sleep(300);
        }
        robot.keyRelease(KeyEvent.VK_TAB);
        Thread.sleep(20);
        robot.keyPress(KeyEvent.VK_ENTER);
        Thread.sleep(20);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    private static Robot getRobot(){
        // 创建Robot对象
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return robot;
    }



    /**
	* @Description: 打开计算机默认的浏览器访问指定的url页面
	* @date: 2017年7月28日 下午2:30:24
	* @修改备注:
	*/
	private  void openBrowse(String[] urls) throws InterruptedException {
        for (String url : urls) {
            if (java.awt.Desktop.isDesktopSupported()) {
                try {
                    //创建一个URI实例,注意不是URL
                    java.net.URI uri = java.net.URI.create(url);
                    //获取当前系统桌面扩展
                    java.awt.Desktop dp = java.awt.Desktop.getDesktop();
                    //判断系统桌面是否支持要执行的功能
                    if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                        //获取系统默认浏览器打开链接
                        dp.browse(uri);
                    }
                } catch (java.lang.NullPointerException e) {
                    //此为uri为空时抛出异常
                    e.printStackTrace();
                } catch (java.io.IOException e) {
                    //此为无法获取系统默认浏览器
                    e.printStackTrace();
                }
            }
            Thread.sleep(3000);
        }

    }


    /**
	* @Description: 关闭浏览器（关闭指定的浏览器，在此处是强行关闭浏览器，强行杀死进程）
     * https://blog.csdn.net/QQ578473688/article/details/77749932
	*/
	private void closeBrowse(){
		try {
			Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
            Runtime.getRuntime().exec("taskkill /F /IM chromeCore.exe");
//			Runtime.getRuntime().exec("taskkill /F /IM iexplorer.exe");
//	        Runtime.getRuntime().exec("taskkill /F /IM 360se.exe");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}



}
