package com.example.bian;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.bushu.PrivateConfig;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by adimn on 2021/10/16.老师收益
 */
public class  LaoShiShouYi{

    public static void main(String[] args){

        String s = "E://baidutongbu//baidutongbu//tongbu//bian//bian//laoshi.json";
        JSONObject config = PrivateConfig.readJsonFile(s).getJSONObject("data");
        JSONArray list = config.getJSONArray("list");
        Map<String, BigDecimal> map = new HashMap<>();
        BigDecimal beishu = new BigDecimal("0.02");
        for(int i=0; i<list.size(); i++){
            JSONObject jsonObject = (JSONObject)list.get(i);
            String updatedTime = jsonObject.getString("updatedTime").split(" ")[0];
            if(map.get(updatedTime) == null){
                map.put(updatedTime, jsonObject.getBigDecimal("realizedPnl"));
            }else {
                map.put(updatedTime, map.get(updatedTime).add(jsonObject.getBigDecimal("realizedPnl")));
            }
        }
        for(Map.Entry<String, BigDecimal> entry : map.entrySet()){
            System.out.println(entry.getKey() + ":" + entry.getValue().multiply(beishu));
        }
    }

}
