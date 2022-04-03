/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : map-data-inspection

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2022-04-03 21:19:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for history
-- ----------------------------
DROP TABLE IF EXISTS `history`;
CREATE TABLE `history` (
  `username` varchar(255) NOT NULL,
  `mapname` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `discription` varchar(255) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of history
-- ----------------------------
INSERT INTO `history` VALUES ('lht', '01-map', 'DirectPosition2D[113.65187713310581, 63.467088198092185]', 'wrong discri', '22test', '1');
INSERT INTO `history` VALUES ('lht', '02-map', 'DirectPosition2D[113.65187713310581, 63.467088198092185]', 'wrong point', '233333', '2');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('guanzian', '123456', '1');
INSERT INTO `user` VALUES ('lhy', '1010', '2');
INSERT INTO `user` VALUES ('lwz', '0827', '3');
INSERT INTO `user` VALUES ('hyg', '0231', '4');
INSERT INTO `user` VALUES ('罗皓天', 'jaylht', '5');
