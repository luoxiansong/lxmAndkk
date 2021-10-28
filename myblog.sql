/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50734
Source Host           : 127.0.0.1:3306
Source Database       : myblog

Target Server Type    : MYSQL
Target Server Version : 50734
File Encoding         : 65001

Date: 2021-10-28 15:49:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `URL_NAME` varchar(255) DEFAULT NULL COMMENT '接口名称',
  `URL` varchar(255) DEFAULT NULL COMMENT '接口地址',
  `IP` varchar(255) DEFAULT NULL COMMENT '访问人IP',
  `USER_NAME` varchar(266) DEFAULT NULL COMMENT '访问用户姓名',
  `ADD_DATE` datetime DEFAULT NULL COMMENT '访问时间',
  `STATUS` varchar(2) DEFAULT '1' COMMENT '0 成功 1失败',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7837 DEFAULT CHARSET=utf8 COMMENT='系统访问日志';

-- ----------------------------
-- Records of log
-- ----------------------------
INSERT INTO `log` VALUES ('7833', 'com.cqkk.controller.SingerController', 'http://localhost:8888/singer/getSingerInfoByPageHelper', '0:0:0:0:0:0:0:1', 'admin', '2021-06-17 22:20:36', '0');
INSERT INTO `log` VALUES ('7834', 'com.cqkk.controller.SingerController', 'http://localhost:8888/singer/getSingerInfoByPageHelper', '0:0:0:0:0:0:0:1', 'admin', '2021-06-17 22:23:30', '0');
INSERT INTO `log` VALUES ('7835', 'com.cqkk.controller.SingerController', 'http://localhost:8888/singer/getSingerInfoByPageHelper', '0:0:0:0:0:0:0:1', 'admin', '2021-06-17 22:24:44', '0');
INSERT INTO `log` VALUES ('7836', 'com.cqkk.controller.SingerController', 'http://localhost:8888/singer/getSingerInfoByPageHelper', '0:0:0:0:0:0:0:1', 'admin', '2021-06-17 22:25:43', '0');

-- ----------------------------
-- Table structure for sing
-- ----------------------------
DROP TABLE IF EXISTS `sing`;
CREATE TABLE `sing` (
  `SING_ID` int(20) NOT NULL AUTO_INCREMENT COMMENT '歌曲ID',
  `SING_NM` varchar(200) DEFAULT NULL COMMENT '歌曲名称',
  `SINGER_ID` int(20) DEFAULT NULL COMMENT '歌手ID',
  `ISSUE_DATE` varchar(8) DEFAULT NULL COMMENT '发行年月',
  `SING_DSC` varchar(200) DEFAULT NULL COMMENT '歌曲描述',
  `SING_PATH` varchar(200) DEFAULT NULL COMMENT '歌曲存放路径',
  `SING_IMG_PATH` varchar(200) DEFAULT NULL COMMENT '歌曲图片存放路径',
  PRIMARY KEY (`SING_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sing
-- ----------------------------
INSERT INTO `sing` VALUES ('1', '可爱女人', '1', '20001107', '周杰伦的可爱女人', null, null);
INSERT INTO `sing` VALUES ('2', '简单爱', '1', '20010101', '周杰伦的简单爱', null, null);

-- ----------------------------
-- Table structure for singer
-- ----------------------------
DROP TABLE IF EXISTS `singer`;
CREATE TABLE `singer` (
  `SINGER_ID` int(20) NOT NULL AUTO_INCREMENT COMMENT '歌曲ID',
  `SINGER_NM` varchar(200) DEFAULT NULL COMMENT '歌曲名称',
  `SINGER_SEX` varchar(20) DEFAULT NULL COMMENT '歌手ID',
  `AGE` int(100) DEFAULT NULL COMMENT '发行年月',
  `SINGER_PATH` varchar(200) DEFAULT NULL COMMENT '歌曲描述',
  PRIMARY KEY (`SINGER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of singer
-- ----------------------------
INSERT INTO `singer` VALUES ('1', '周杰伦', '0', '42', '');
INSERT INTO `singer` VALUES ('2', '周华健', '0', '61', null);
INSERT INTO `singer` VALUES ('35', '周杰伦0', '0', '42', '0');
INSERT INTO `singer` VALUES ('41', '周杰伦6', '0', '48', '6');
INSERT INTO `singer` VALUES ('42', '周杰伦7', '0', '49', '7');
INSERT INTO `singer` VALUES ('43', '周杰伦8', '0', '50', '8');
INSERT INTO `singer` VALUES ('44', '周杰伦9', '0', '51', '9');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
