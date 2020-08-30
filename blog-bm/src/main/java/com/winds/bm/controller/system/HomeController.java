package com.winds.bm.controller.system;

import com.winds.bm.dto.Order;
import com.winds.bm.dto.TableData;
import com.winds.bm.dto.User;
import com.winds.bm.dto.Video;
import com.winds.common.constant.Result;
import com.winds.common.constant.ResultCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

@RestController
public class HomeController {

    private static String [] curse = {"springboot","vue","小程序","ES6","Redis","React"};
    private static String [] weeks = {"周一","周二","周三","周四","周五","周六","周日"};
    private static String [] mon = {"201907","201908","201909","201910","201911","201912","202001", "202002"};


    @GetMapping("home/main")
    public Result home(){
        Result r = new Result();

        // 饼图
        List<Video> videoList = getVedioList();
        // 柱状图
        List<User> userList = getUserList();
        // 折线图
        Order orderData = getOrderData();

        Map<String,Object> m = new HashMap<>();

        m.put("userData", userList);
        m.put("videoData", videoList);
        m.put("orderData", orderData);
        m.put("tableData", getTableData());

        r.setData(m);
        r.setResultCode(ResultCode.SUCCESS);
        return r;
    }

    private List<Video> getVedioList() {
        List<Video> videoList = new ArrayList<>();
        for (int i = 0; i < curse.length; i++) {
            Video v = new Video();
            v.setName(curse[i]);
            v.setValue(String.valueOf((int) (Math.random()*10000)));
            videoList.add(v);
        }
         return videoList;
    }
    private List<User> getUserList() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < weeks.length; i++) {
            User u = new User();
            u.setDate(weeks[0]);
            u.setNewly(String.valueOf((int) 10*(Math.random())));
            u.setActive(String.valueOf((int) 50*(Math.random())));
            userList.add(u);
        }
        return userList;
    }
    private Order getOrderData() {
        Order o = new Order();
        o.setDate(mon);
        List<Map<String,String>> llm = new ArrayList();
        for (int i = 0; i < mon.length; i++) {
//            List<Map<String,String>> lm = new ArrayList<>();
            Map<String,String> m = new HashMap<>();
            for (int j = 0; j < curse.length; j++) {

                m.put(curse[j], String.valueOf((int) (Math.random()*10000)));
//                lm.add(m);
            }
            llm.add(m);
        }

        o.setData(llm);
        return o;
    }

    private List<TableData> getTableData(){
        List<TableData> tableDataList = new ArrayList<>();
        for (int i = 0; i < curse.length; i++) {
            TableData tb = new TableData();
            tb.setName(curse[i]);
            tb.setTodayBuy(BigDecimal.valueOf(Math.random()).movePointRight(2).setScale(2,BigDecimal.ROUND_HALF_UP));
            tb.setMonthBuy(BigDecimal.valueOf(Math.random()).movePointRight(2).multiply(new BigDecimal(30)).setScale(2,BigDecimal.ROUND_HALF_UP));
            tb.setTotalBuy(BigDecimal.valueOf(Math.random()).movePointRight(4).setScale(2,BigDecimal.ROUND_HALF_UP));
            tableDataList.add(tb);
        }
        return tableDataList;
    }
}
