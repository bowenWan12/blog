package com.winds.bm.dto;

import java.util.List;
import java.util.Map;

public class Order {
    private String[] date;
    private List<Map<String,String>> data;

    public String[] getDate() {
        return date;
    }

    public void setDate(String[] date) {
        this.date = date;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }
}
