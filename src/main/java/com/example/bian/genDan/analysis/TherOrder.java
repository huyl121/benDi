package com.example.bian.genDan.analysis;

import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.model.enums.PositionSide;
import com.example.bian.client.model.trade.Order;
import com.example.bian.client.model.trade.Position;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class TherOrder {

    private int count = 0;
    private int minCount = 0;
    //方向相反，且个数相同时，方向为空
    private String positionSide = "";
    private Set<String> portIdSet = new TreeSet<>();

    public TherOrder(Map<String, Order> orderMap ){
        if(orderMap.isEmpty()){
            return;
        }
        count = orderMap.size();

        int shortCount = 0;
        int longCount = 0;
        Set<String> longPortIdSet = new TreeSet<>();
        Set<String> shortPortIdSet = new TreeSet<>();
        for(Map.Entry<String, Order> trade : orderMap.entrySet()) {
            if(PositionSide.LONG.toString().equals(trade.getValue().getPositionSide().toUpperCase())){
                longCount++;
                longPortIdSet.add(trade.getKey());
            }else if(PositionSide.SHORT.toString().equals(trade.getValue().getPositionSide().toUpperCase())){
                shortCount++;
                shortPortIdSet.add(trade.getKey());
            }
        }

        if(longCount >= shortCount){
            minCount = shortCount;
            positionSide = PositionSide.LONG.toString();
            portIdSet = longPortIdSet;
        }else if(longCount < shortCount){
            minCount = longCount;
            positionSide = PositionSide.SHORT.toString();
            portIdSet = shortPortIdSet;
        }

    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getMinCount() {
        return minCount;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }

    public String getPositionSide() {
        return positionSide;
    }

    public void setPositionSide(String positionSide) {
        this.positionSide = positionSide.toUpperCase();
    }

    public Set<String> getPortIdSet() {
        return portIdSet;
    }

    public void setPortIdSet(Set<String> portIdSet) {
        this.portIdSet = portIdSet;
    }
}
