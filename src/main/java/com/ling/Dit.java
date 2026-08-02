package com.ling;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.bushu.PrivateConfig;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Dit {
    static Long fenZhong = 1000 * 60L;
    static Long xiaoShi = fenZhong * 60L;
    static Long tian = xiaoShi * 24;
    public static void main(String[] args) throws InterruptedException {

        Dit dit = new Dit();
        dit.method("E://code//biance");
    }

    public void method(String configPath){
        JSONObject config = PrivateConfig.readJsonFile(configPath + "//dit.json");

        JSONArray jsonArray = config.getJSONArray("data");
        Long oldTime = System.currentTimeMillis()-tian-fenZhong*6;
        //如果想立马执行，不传值，或者传个很小的值；或者赋上次执行完的时间
        String startTime = config.getString("startTime");
        if(StringUtils.isNotBlank(startTime)){
            oldTime = Long.parseLong(startTime);
        }

        while (true){
            Long nowTime = System.currentTimeMillis();
            if(nowTime-oldTime>tian+fenZhong*5){
                for(Object o : jsonArray){
                    JSONObject jsonObject = (JSONObject) o;
                    String name = jsonObject.getString("value");
                    String password = jsonObject.getString("key");
                    try {
                        String login = login(name, password);
                        JSONObject jsonObject1 = JSON.parseObject(login);
                        if(jsonObject1.getInteger("State") != 200){
                            System.out.println(name + "登录失败" + JSON.toJSONString(jsonObject));
                            continue;
                        }
                        Thread.sleep(1000 * 5);
                        sendPost("https://www.dit.top:2053/Go/Firing", jsonObject1.getString("Msg"));
                        Thread.sleep(1000 * 5);

                        String s = sendPost("https://www.dit.top:2053/Go/ClickToStart", jsonObject1.getString("Msg"));
                        JSONObject jsonObject2 = JSON.parseObject(s);
                        if(jsonObject2.getInteger("State") != 200){
                            System.out.println(name + JSON.toJSONString(jsonObject2));
                        }else {
                            System.out.println(name + "：成功");
                        }
                        Thread.sleep(1000 * 20);
                    }catch (Exception e){
                        System.out.println(name + "失败");
                    }
                }
                oldTime = System.currentTimeMillis();
                System.out.println("完成时间：" + PrivateConfig.getCurrentTime());
            }else {
                try {
                    Thread.sleep(1000*60*5);
                    System.out.println("等待执行：" + PrivateConfig.getCurrentTime());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


        }

    }


    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性

            conn.setRequestProperty("cache-control", "no-cache");
            conn.setRequestProperty("Postman-Token", "<calculated when request is sent>");
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", "<calculated when request is sent>");
            conn.setRequestProperty("Host", "<calculated when request is sent>");
            conn.setRequestProperty("User-Agent", "PostmanRuntime/7.37.3");
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("user-agent", "Mozilla/5.0 (Linux; Android 9; HD1900 Build/PQ3A.190605.05081124; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/91.0.4472.114 Mobile Safari/537.36 uni-app Html5Plus/1.0 (Immersed/24.0)");
            conn.setRequestProperty("Host", "www.dit.top:2053");

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print("uid="+ param + "&langu=0");
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String login(String name, String password) throws IOException {
        OkHttpClient client;
        client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "Email=" + name + "&Password=" + password + "&laugh=1");
        Request request = new Request.Builder()
                .url("https://www.dit.top:2053/User/BlockLogin")
                .method("POST", body)
                .addHeader("user-agent", "Mozilla/5.0 (Linux; Android 9; HD1900 Build/PQ3A.190605.05081124; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/91.0.4472.114 Mobile Safari/537.36 uni-app Html5Plus/1.0 (Immersed/24.0)")
                .addHeader("Host", "www.dit.top:2053")
                .addHeader("Cookie", "X-SUDUN-WAF-R-C=0001676904")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    public static void method() throws InterruptedException {


        /*
         * 2024-05-24 20:32:55
         *
         * */
        Map<String, String> map = new LinkedHashMap<>();
        map.put("1acf99d8c25c43b0a327133f2c04076d", "10");
        map.put("52865191cd794c38bc225243fbbb4558", "38");
        map.put("cd1597782d214160b53bbf878b2e4d8b", "suiyide0001@sina.com");
        map.put("bdad3456a2374d469b7986e2a63bb3c7", "laoer121@outlook.com");
        map.put("42aa378e114b43f19f960c1c5a7b089a", "laosan121@outlook.com");
        map.put("ce9ee9b393984381ad26961273913487", "suiyide0001@163.com");
        map.put("795722548dc948558466fb9af43ce16e", "suiyide0002@163.com");
        map.put("2c830a58f2aa480da2a1e3b2180cb261", "suiyide0003@163.com");//QAZwsx...
        map.put("a457a3c37daf4f33bbd68065bf2cfbce", "suiyide0004@163.com");
        map.put("67eb443599b54c9a84f042446f06165e", "suiyide0005@163.com");
        map.put("4953d29891d24b478ff4e4f60be15ac7", "suiyide0006@163.com");
        map.put("a66a8d8bdc4c47f2b7ba8f081943776f", "suiyide0001@outlook.com");
        map.put("08dac0db2af74558b1bc0844190b664c", "suiyide0002@outlook.com");
        map.put("a1d373c6cb684856b8bdefc6269c885e", "suiyide0003@outlook.com");
        map.put("ce5eacfdc3164ad782a1be7d9cebc655", "suiyide0004@outlook.com");
        map.put("7dc4677e07df40e89c96f97a10dacd19", "suiyide0005@outlook.com");
        map.put("de865e82deb04ef391820e4bb5eae462", "yalong0001@outlook.com");
        map.put("6afdae132b214eabb67f0c3098c9c56a", "yalong0002@outlook.com");
        map.put("b1f68d756dc24a54be9ede3c7bb10976", "yalong0003@outlook.com");
        map.put("bcc0401b0b364aa0baac2dd491e4aa16", "yalong0004@outlook.com");
        map.put("bcea0372dbc74d69ab38b16329fc908d", "yalong0005@outlook.com");//QAZwsx...
        map.put("51f19a4577814abe945fe2a517d93378", "dayan0001@outlook.com");
        map.put("f54b6ce91099443788229bcfe2f87884", "dayan0002@outlook.com");
        map.put("7e040ba689864b5cbb7a195738c966d1", "dayan0003@outlook.com");
        map.put("a9a0d18d560d43978eadcd5fc27f8e4d", "dayan0004@outlook.com");
        map.put("4474173fe05b45f2ae6f978de2126d0d", "dayan0005@outlook.com");
        /*map.put("", "");
        map.put("", "");
        map.put("", "");
        map.put("", "");*/


        for(Map.Entry<String, String> param : map.entrySet()){
            sendPost("https://www.dit.top:2053/Go/Firing", param.getKey());
            Thread.sleep(1000 * 5);
            String s = sendPost("https://www.dit.top:2053/Go/ClickToStart", param.getKey());
            JSONObject jsonObject = JSON.parseObject(s);
            if(jsonObject.getInteger("State") != 200){
                System.out.println(param.getValue() + JSON.toJSONString(jsonObject));
            }else {
                System.out.println(param.getValue() + "：成功");
            }
            Thread.sleep(1000 * 25);
        }
        System.out.println(System.currentTimeMillis());
    }

}
