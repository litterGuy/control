/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 80017
Source Host           : localhost:3306
Source Database       : fk

Target Server Type    : MYSQL
Target Server Version : 80017
File Encoding         : 65001

Date: 2019-10-11 10:35:35
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ipaddress
-- ----------------------------
DROP TABLE IF EXISTS `ipaddress`;
CREATE TABLE `ipaddress` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `site` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`,`ip`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for judge_base
-- ----------------------------
DROP TABLE IF EXISTS `judge_base`;
CREATE TABLE `judge_base` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '地点间隔过远距离',
  `distance` int(11) NOT NULL DEFAULT '200000',
  `oftensite` int(11) NOT NULL DEFAULT '3' COMMENT '设置常用城市，默认为去过城市的操作次数总和前五个',
  `loginfailtime` int(11) NOT NULL DEFAULT '3600' COMMENT '登陆失败间隔时间',
  `ipcnum` int(11) NOT NULL DEFAULT '3' COMMENT '单位时间ip更换次数',
  `dltime` int(11) NOT NULL DEFAULT '3600' COMMENT '大额提币统计时间',
  `loginfailnum` int(11) NOT NULL DEFAULT '5' COMMENT '登陆失败次数',
  `ipctime` int(11) NOT NULL DEFAULT '3600' COMMENT 'IP更换间隔时间',
  `ipntime` int(11) NOT NULL DEFAULT '60' COMMENT '新IP操作间隔时间',
  `oftenip` int(11) NOT NULL DEFAULT '5' COMMENT '头几条为常用ip',
  `dollerlimit` double(47,0) DEFAULT '0' COMMENT '单次提币金额限制，大额界定',
  `withdrawdollerlimit` double(47,0) NOT NULL DEFAULT '0' COMMENT '单次提现限制金额',
  `coinnumlimit` int(11) DEFAULT '0' COMMENT '一天内限制提币次数',
  `withdrawnumlimit` int(11) DEFAULT '0' COMMENT '一天内限制提现次数',
  `refer` varchar(511) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '交易所源站点refer',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for operation
-- ----------------------------
DROP TABLE IF EXISTS `operation`;
CREATE TABLE `operation` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一id',
  `account_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '用户id',
  `operate_time` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '操作时间',
  `user_agent` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'http请求所带的userAgent',
  `refer` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'http请求所带的refer，算是起始网址',
  `mac` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'mac地址',
  `operate_source` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '操作来源1、PC 2、H5 3、App',
  `app_version` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'app版本',
  `device_type` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '设备类型1、PC 2、MOBILE',
  `ip` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'ip',
  `site` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '操作地点',
  `user_name_type` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '用户名类型',
  `register_time` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '注册时间',
  `login_type` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '登录类型',
  `register_ip` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '注册ip',
  `login_result` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '登录结果',
  `hashed_password` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'hash后的密码',
  `fail_reason` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '失败原因',
  `imsi` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'imsi',
  `imei` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `wifi_mac` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'wifimac',
  `operation_type` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '操作类型',
  `operation_num` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `one_price` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `score` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '安全得分',
  `dollar` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '美金额',
  `is_ch` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '是否国内',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=297 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for sitelocation
-- ----------------------------
DROP TABLE IF EXISTS `sitelocation`;
CREATE TABLE `sitelocation` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `site` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `lng` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `lat` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`,`site`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` varchar(10) COLLATE utf8_bin NOT NULL,
  `nick_name` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `user_name` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `mobile` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `email` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `cert_no` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `age` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`,`account_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;
SET FOREIGN_KEY_CHECKS=1;
