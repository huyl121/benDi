package com.example.bian.genDan.analysis;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Set;
import java.util.TreeSet;

public class MyOrder {

    int funShu = 0;//因为每次都重新查一次份数，所以这个字段没用了
    String positionSide;
    Set<String> otherPortIdSet;

    public MyOrder(){

    }

    public int getFunShu() {
        return funShu;
    }

    public void setFunShu(int funShu) {
        this.funShu += funShu;
    }

    public String getPositionSide() {
        return positionSide;
    }

    public void setPositionSide(String positionSide) {
        this.positionSide = positionSide.toUpperCase();
    }

    public Set<String> getOtherPortIdSet() {
        return otherPortIdSet;
    }

    public void setOtherPortIdSet(String otherPortId) {
        if(CollectionUtils.isEmpty(otherPortIdSet)){
            otherPortIdSet = new TreeSet<>();
        }
        this.otherPortIdSet.add(otherPortId);
    }
}
