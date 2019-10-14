package com.xinshang.control.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 获取经纬度
 *
 * @author jueyue 返回格式：Map<String,Object> map map.put("status",
 * reader.nextString());//状态 map.put("result", list);//查询结果
 * list<map<String,String>> 密钥:f247cdb592eb43ebac6ccd27f796e2d2
 */
@Slf4j
public class SiteUtils {

    /**
     * @param addr 查询的地址
     * @return
     * @throws IOException
     */
    public static Object[] getCoordinate(String addr) throws IOException {
        String lng = "0";// 经度
        String lat = "0";// 纬度
        String address = null;
        if (addr == null || addr == "") {
            return new Object[]{"0", "0"};
        }
        try {
            address = java.net.URLEncoder.encode(addr, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            log.error(e1.getMessage());
        }
        String key = "f247cdb592eb43ebac6ccd27f796e2d2";
        String url = String.format("http://api.map.baidu.com/geocoder?address=%s&output=json&key=%s", address, key);
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
                int count = 1;
                while ((data = br.readLine()) != null) {
                    if (count == 5) {
                        if (data.indexOf(":") < 0 || data.indexOf(",") < 0) {
                            System.out.println(data.indexOf(":") + "::::::::" + data.indexOf(","));
                            return new Object[]{"0", "0"};
                        }
                        lng = (String) data.subSequence(data.indexOf(":") + 1, data.indexOf(","));// 经度
                        count++;
                    } else if (count == 6) {
                        if (data.indexOf(":") < 0) {
                            System.out.println(data.indexOf(":") + "::::::::");
                            return new Object[]{"0", "0"};
                        }
                        lat = data.substring(data.indexOf(":") + 1);// 纬度
                        count++;
                    } else {
                        count++;
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (insr != null) {
                insr.close();
            }
            if (br != null) {
                br.close();
            }
        }
        return new Object[]{lng, lat};
    }

    /**
     * Created by yuliang on 2015/3/20.
     */

    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 通过经纬度获取距离(单位：米)
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(
                Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;
        return s;
    }
}