package com.xinshang.control.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Data
@Entity
public class Operation {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    private String accountId;
    private String operateTime;
    private String userAgent;
    private String refer;
    private String mac;
    private String operateSource;
    private String appVersion;
    private String deviceType;
    private String ip;
    private String site;
    private String userNameType;//未使用字段
    private String registerTime;
    private String loginType;
    private String registerIp;
    private String loginResult;//未使用字段
    private String hashedPassword;
    private String failReason;//未使用字段
    private String imsi;
    private String imei;
    private String wifiMac;
    private String operationType;
    private String operationNum;
    private String onePrice;
    private String score;
    private String dollar;
    private String isCh;
    private String identity;//用户进行了身份验证，只进行交易风控
}
