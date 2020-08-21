package com.winds.common.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.xiaoleilu.hutool.http.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 获取Ip
 *
 */
public class IpUtils {
    private static Logger LOG = LoggerFactory.getLogger(IpUtils.class);

    /**
     * 获取IP地址
     * <p>
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = null, unknown = "unknown", seperator = ",";
        int maxLength = 15;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            LOG.error("IpUtils ERROR ", e);
        }

        // 使用代理，则获取第一个IP地址
        if (StringUtils.isEmpty(ip) && ip.length() > maxLength) {
            int idx = ip.indexOf(seperator);
            if (idx > 0) {
                ip = ip.substring(0, idx);
            }
        }

        return ip;
    }

    /**
     * 获取ip地址
     *
     * @return
     */
    public static String getIpAddr() {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        return getIpAddr(request);
    }

    /***
     * @param ip
     * @return
     */
    public static Map<String,String> getAddressByIP(String ip) {
        String area = "";
        String province = "";
        String city = "";
        String isp = "";
        Map finalMap = Maps.newHashMap();
        try{
            if("0:0:0:0:0:0:0:1".equals(ip)){
                ip = "0.0.0.0";
            }
            StringBuilder sb = new StringBuilder("http://whois.pconline.com.cn/ipJson.jsp?json=true&ip=");
            sb.append(ip);
            String result= HttpUtil.get(sb.toString(), "GBK");
            Map resultMap = JSON.parseObject(result,Map.class);
            String status = (String) resultMap.get("err");

            province = (String) resultMap.get("pro");
            city = (String) resultMap.get("city");
            if("noprovince".equalsIgnoreCase(status)){
                area = (String) resultMap.get("addr");
            }else{
                area = "中国";
                String addr = (String) resultMap.get("addr");
                if(StringUtils.isNotBlank(addr)){
                    isp = addr.split(" ")[1];
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        finalMap.put("area",area);
        finalMap.put("province",province);
        finalMap.put("city",city);
        finalMap.put("isp",isp);
        return finalMap;
    }

    /**
     * 获取操作系统,浏览器及浏览器版本信息
     * @param request
     * @return
     */
    public static Map<String,String> getOsAndBrowserInfo(HttpServletRequest request){
        Map<String,String> map = Maps.newHashMap();
        String  browserDetails  =   request.getHeader("User-Agent");
        String  userAgent       =   browserDetails;
        String  user            =   userAgent.toLowerCase();

        String os = "";
        String browser = "";

        //=================OS Info=======================
        if (userAgent.toLowerCase().contains("windows"))
        {
            os = "Windows";
        } else if(userAgent.toLowerCase().contains("mac"))
        {
            os = "Mac";
        } else if(userAgent.toLowerCase().contains("x11"))
        {
            os = "Unix";
        } else if(userAgent.toLowerCase().contains("android"))
        {
            os = "Android";
        } else if(userAgent.toLowerCase().contains("iphone"))
        {
            os = "IPhone";
        }else{
            os = "UnKnown, More-Info: "+userAgent;
        }
        //===============Browser===========================
        if (user.contains("edge"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Edge")).split(" ")[0]).replace("/", "-");
        } else if (user.contains("msie"))
        {
            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
        } else if (user.contains("safari") && user.contains("version"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]
                    + "-" +(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
        } else if ( user.contains("opr") || user.contains("opera"))
        {
            if(user.contains("opera")){
                browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]
                        +"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            }else if(user.contains("opr")){
                browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-"))
                        .replace("OPR", "Opera");
            }

        } else if (user.contains("chrome"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        } else if ((user.contains("mozilla/7.0")) || (user.contains("netscape6"))  ||
                (user.contains("mozilla/4.7")) || (user.contains("mozilla/4.78")) ||
                (user.contains("mozilla/4.08")) || (user.contains("mozilla/3")) )
        {
            browser = "Netscape-?";

        } else if (user.contains("firefox"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if(user.contains("rv"))
        {
            String IEVersion = (userAgent.substring(userAgent.indexOf("rv")).split(" ")[0]).replace("rv:", "-");
            browser="IE" + IEVersion.substring(0,IEVersion.length() - 1);
        } else
        {
            browser = "UnKnown, More-Info: "+userAgent;
        }
        map.put("os",os);
        map.put("browser",browser);
        return map;
    }
}
