package com.example.bian;


import com.example.bian.client.RequestOptions;
import com.example.bian.client.SubscriptionClient;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;

public class SubscribeUserData {

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10819");

        SubscribeUserData subscribeUserData = new SubscribeUserData();
        subscribeUserData.method();


    }

    public void method() throws InterruptedException {
        RequestOptions options = new RequestOptions();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY,
                options);

        // Start user data stream
        String listenKey = syncRequestClient.startUserDataStream();
        System.out.println("listenKey: " + listenKey);

        // Keep user data stream
//        syncRequestClient.keepUserDataStream(listenKey);

        // Close user data stream
//        syncRequestClient.closeUserDataStream(listenKey);

        SubscriptionClient client = SubscriptionClient.create();


        client.subscribeUserDataEvent(listenKey, System.out::println, null);

        System.out.println("结束");

        for (int i = 0; i < 10000; i++) {

            //55分钟延长一次，默认延长1小时
            Thread.sleep(1000 * 60 * 55);
            syncRequestClient.keepUserDataStream(listenKey);
            System.out.println("延长" + i + "次");

        }


    }

}