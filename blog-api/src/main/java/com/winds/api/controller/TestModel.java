package com.winds.api.controller;

import com.winds.common.constant.Result;
import com.winds.common.constant.ResultCode;

public class TestModel {
    public static void main(String args[]){
        Result rs = new Result();
        rs.setCode(ResultCode.SUCCESS.code());
        System.out.println(rs.getCode());
        System.out.println("1q123");
    }
}
