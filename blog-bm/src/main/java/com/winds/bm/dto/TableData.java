package com.winds.bm.dto;

import java.math.BigDecimal;

public class TableData {
    private String name;
    private BigDecimal todayBuy;
    private BigDecimal monthBuy;
    private BigDecimal totalBuy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTodayBuy() {
        return todayBuy;
    }

    public void setTodayBuy(BigDecimal todayBuy) {
        this.todayBuy = todayBuy;
    }

    public BigDecimal getMonthBuy() {
        return monthBuy;
    }

    public void setMonthBuy(BigDecimal monthBuy) {
        this.monthBuy = monthBuy;
    }

    public BigDecimal getTotalBuy() {
        return totalBuy;
    }

    public void setTotalBuy(BigDecimal totalBuy) {
        this.totalBuy = totalBuy;
    }
}
