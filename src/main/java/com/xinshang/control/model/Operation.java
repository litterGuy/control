package com.xinshang.control.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    private String userNameType;
    private String registerTime;
    private String loginType;
    private String registerIp;
    private String loginResult;
    private String hashedPassword;
    private String failReason;
    private String imsi;
    private String imei;
    private String wifiMac;
    private String operationType;
    private String operationNum;
    private String onePrice;
    private String score;
    private String dollar;
    private String isCh;
}
