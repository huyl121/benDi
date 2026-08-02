package com.ChongQi;

import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.xin.JianKong4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ChongQi {

    public static void main(String[] args) throws InterruptedException {
       ChongQi chongQi = new ChongQi();
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

        for(String path : paths){
            Callable callable1 = new Callable() {
                @Override
                public String call() throws Exception {
                    JSONObject config = PrivateConfig.readJsonFile(path);
                    String ip = config.getString("ip");
                    return ChongQiFuWu.resultDevice(ip, config.getString("computer"));
                }
            };
            threadPoolExecutor.submit(callable1);
            Thread.sleep(50);

        }
    }

}
