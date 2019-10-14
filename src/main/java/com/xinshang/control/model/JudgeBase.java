package com.xinshang.control.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Entity
public class JudgeBase {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    // 地点间隔过远距离
    private Integer distance;
    // 设置常用城市，默认为去过城市的操作次数总和前五个
    private Integer oftensite;
    // 登陆失败间隔时间
    private Integer loginfailtime;
    // 单位时间ip更换次数
    private Integer ipcnum;
    // 大额提币统计时间
    private Integer dltime;
    // 登陆失败次数
    private Integer loginfailnum;
    // IP更换间隔时间
    private Integer ipctime;
    // 新IP操作间隔时间
    private Integer ipntime;
    //头几条为常用ip
    private Integer oftenip;
    //单次提币金额限制，大额界定
    private Double dollerlimit;
    //单次提现限制金额
    private Double withdrawdollerlimit;
    //一天内限制提币次数
    private Integer coinnumlimit;
    //一天内限制提现次数
    private Integer withdrawnumlimit;
    //交易所源站点refer
    private String refer;
}
