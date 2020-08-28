package com.winds.bm.dto;

public class User {
    /**
     * 时间
     */
    private String date;
    /**
     * 新增用户
     */
    private String newly;
    /**
     * 活跃用户
     */
    private String active;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNewly() {
        return newly;
    }

    public void setNewly(String newly) {
        this.newly = newly;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
