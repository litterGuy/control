package com.xinshang.control.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class IpUtils {

    /**
     * 判断ip是否合法
     *
     * @param str
     * @return
     */
    public static boolean isIp(String str) {
        Pattern pattern = Pattern.compile("((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 根据ip获取地址
     *
     * @param ip
     * @return
     */
    public static String getAddress(String ip) {
        String site = null;// 地点
        String address = null;
        if (ip == null || ip == "") {
            return site;
        }
        try {
            address = java.net.URLEncoder.encode(ip, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String url = String.format("http://api.map.baidu.com/location/ip?ak=AEae92ac30aa25f0a53f4d72960af92a&ip=%s", address);
        URL myURL = null;
        URLConnection httpsConn = null;
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
        }
        InputStreamReader insr = null;
        BufferedReader br = null;
        try {
            httpsConn = (URLConnection) myURL.openConnection();// 不使用代理
            if (httpsConn != null) {
                insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");
                br = new BufferedReader(insr);
                String data = null;
                if ((data = br.readLine()) != null) {
                    JSONObject jo = JSON.parseObject(data);
                    if (jo.getIntValue("status") != 0) {
                        site = "";
                    } else {
                        site = jo.get("address").toString();
                    }
                }
            }
            if (insr != null) {
                insr.close();
            }
            if (br != null) {
                br.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return site;
    }

    public static boolean isCh(String site) {
        if (StringUtils.isNotEmpty(site)) {
            String substring = site.split("\\|")[0];
            if ("CN".equals(substring)) {
                return true;
            }
        }
        return false;
    }

}
