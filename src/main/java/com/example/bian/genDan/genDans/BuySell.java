package com.example.bian.genDan.genDans;

import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.model.market.PriceChangeTicker;

import java.math.BigDecimal;

public class BuySell {

    private BigDecimal buy = PrivateConfig.ling;
    private BigDecimal sell = PrivateConfig.ling;
    private String positionSide;

    public BigDecimal getBuy() {
        return buy;
    }

    public void setBuy(BigDecimal b) {
        buy = buy.add(b);
    }

    public BigDecimal getSell() {
        return sell;
    }

    public void setSell(BigDecimal s) {
        sell = sell.add(s);
    }

    public String getPositionSide() {
        return positionSide;
    }

    public void setPositionSide(String positionSide) {
        this.positionSide = positionSide;
    }
}
