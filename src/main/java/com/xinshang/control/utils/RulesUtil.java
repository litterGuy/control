package com.xinshang.control.utils;

import com.xinshang.control.interceptor.JudgeInterceptor;
import com.xinshang.control.model.Operation;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RulesUtil {

    public static void addScore(Operation operation) {
        if (StringUtils.isEmpty(operation.getScore()) || "0".equals(operation.getScore())) {
            operation.setScore("10");
        } else {
            Integer score = Integer.parseInt(operation.getScore()) + 10;
            operation.setScore(score.toString());
        }
    }

    public static void addScore(Operation operation, int sc) {
        if (StringUtils.isEmpty(operation.getScore()) || "0".equals(operation.getScore())) {
            operation.setScore(sc + "");
        } else {
            Integer score = Integer.parseInt(operation.getScore()) + sc;
            operation.setScore(score.toString());
        }
    }

    /**
     * 是否为下半夜操作
     *
     * @param nowTime
     * @return
     */
    public static boolean isNight(String nowTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String strDate = sdf.format(new Date(Long.parseLong(nowTime))); // 2016-12-16 11:53:54
        // 截取当前时间时分秒 转成整型
        int tempDate = Integer.parseInt(strDate.substring(11, 13) + strDate.substring(14, 16) + strDate.substring(17, 19));
        // 截取开始时间时分秒 转成整型
        int tempDateBegin = Integer.parseInt("050000");
        // 截取结束时间时分秒 转成整型
        int tempDateEnd = Integer.parseInt("230000");

        if ((tempDate >= tempDateBegin && tempDate <= tempDateEnd)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 验证refer是否不一致
     *
     * @param refer
     * @return
     */
    public static boolean validRefer(String refer) {
        if (StringUtils.isEmpty(refer)) {
            return false;
        }
        String orignRefer = JudgeInterceptor.base.getRefer();
        if (refer.contains(orignRefer)) {
            return false;
        }
        return true;
    }
}
