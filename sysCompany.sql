/*
Navicat MySQL Data Transfer

Source Server         : adt-app
Source Server Version : 50717
Source Host           : 192.168.131.168:3306
Source Database       : sysCompany

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2020-12-28 11:28:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `code` varchar(64) DEFAULT NULL COMMENT '菜单权限编码',
  `name` varchar(300) DEFAULT NULL COMMENT '菜单权限名称',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(如：sys:user:add)',
  `url` varchar(100) DEFAULT NULL COMMENT '访问地址URL',
  `method` varchar(10) DEFAULT NULL COMMENT '资源请求类型',
  `pid` varchar(64) DEFAULT NULL COMMENT '父级菜单权限名称',
  `order_num` int(11) DEFAULT '0' COMMENT '排序',
  `type` tinyint(4) DEFAULT NULL COMMENT '菜单权限类型(1:目录;2:菜单;3:按钮)',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态1:正常 0：禁用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(4) DEFAULT '1' COMMENT '是否删除(1未删除；0已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('13a6f9f7-edb1-4e00-b95e-4e8cc00e650a', null, '查询用户信息', 'sys:user:detail', '/user/getuser', 'GET', null, '0', null, '1', '2020-12-28 11:22:55', '2020-12-28 11:23:30', '1');
INSERT INTO `sys_permission` VALUES ('13a6f9f7-edb1-4e00-b95e-4e8cc00e650f', '', '部门管理', '', '/index/depts', 'GET', '2495c5da-d43c-4671-a67a-36565f5b61b9', '98', '2', '1', '2020-02-18 19:39:35', '2020-02-25 13:35:53', '1');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '角色名称',
  `description` varchar(300) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '1' COMMENT '状态(1:正常0:弃用)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(4) DEFAULT '1' COMMENT '是否删除(1未删除；0已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('0fee4b64-9d48-4cf1-b9a0-bfeaa723e7a0', 'admin', '所有权限', '1', '2020-02-27 12:36:47', '2020-02-27 15:52:01', '0');
INSERT INTO `sys_role` VALUES ('0fee4b64-9d48-4cf1-b9a0-bfeaa723e7a1', 'test', '测试权限', '1', null, null, '1');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `role_id` varchar(64) DEFAULT NULL COMMENT '角色id',
  `permission_id` varchar(64) DEFAULT NULL COMMENT '菜单权限id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('00c959f3-0493-438f-840a-adb4a5fd56a1', '0fee4b64-9d48-4cf1-b9a0-bfeaa723e7a0', '13a6f9f7-edb1-4e00-b95e-4e8cc00e650a', '2020-03-05 19:37:51');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(64) NOT NULL COMMENT '用户id',
  `username` varchar(255) NOT NULL COMMENT '账户名称',
  `salt` varchar(255) DEFAULT NULL COMMENT '加密盐值',
  `password` varchar(255) DEFAULT NULL COMMENT '用户密码密文',
  `phone` varchar(255) DEFAULT NULL COMMENT '手机号码',
  `dept_id` varchar(255) DEFAULT NULL COMMENT '部门id',
  `real_name` varchar(255) DEFAULT NULL COMMENT '真实姓名',
  `nick_name` varchar(255) DEFAULT NULL COMMENT '昵称',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `status` int(11) DEFAULT '1' COMMENT '账户状态1正常2锁定',
  `sex` int(11) DEFAULT NULL COMMENT '性别1男2女',
  `deleted` int(11) DEFAULT '1' COMMENT '是否删除1未删除0已删除',
  `create_id` varchar(255) DEFAULT NULL COMMENT '创建人',
  `update_id` varchar(255) DEFAULT NULL COMMENT '更新人',
  `create_where` int(11) DEFAULT NULL COMMENT '创建来源1web2Android3iOS',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('27739ac4-5e56-4a00-99f0-37006a5a5941', 'dev', '1b1b2968794247579385', '789dfe0b30d20a70e6d705e3dc396deb', '12636546980', '86a242b9-65d4-4ff5-b70c-54fc4add1f1c', null, null, null, '1', null, '1', null, '9a26f5f1-cbd2-473d-82db-1d6dcf4598f8', null, '2020-02-27 22:43:16', '2020-03-10 10:40:30');
INSERT INTO `sys_user` VALUES ('39903d11-9340-4a5b-aee3-9ea1d1dbc8f2', 'fengfanli', '81ac673918da4e328866', '2c7dc03c6dbef02e2b6dbcd79ae0ec82', '17862970812', '22d93869-b95e-4c9a-ba0c-afc79cff9713', null, null, null, '1', null, '1', null, null, null, '2020-03-10 10:31:55', null);
INSERT INTO `sys_user` VALUES ('8a938151-53e6-4182-925a-684f3be840e8', 'feng', '9ef7440dc3bd48e7a6c6', '53d903c19dc3c7f092fe8f88e2a56e93', '17862970812', '22d93869-b95e-4c9a-ba0c-afc79cff9713', null, null, null, '1', null, '1', null, null, null, '2020-03-06 21:51:42', null);
INSERT INTO `sys_user` VALUES ('9a26f5f1-cbd2-473d-82db-1d6dcf4598f4', 'dev123', '324ce32d86224b00a02b', 'ac7e435db19997a46e3b390e69cb148b', '13666666666', '09e56250-ca9f-424f-96fb-99cd38c63a54', null, null, 'yingxue@163.com', '1', '1', '0', null, '7e872c3f-9958-4996-87be-c56c663dde23', '3', '2019-09-22 19:38:05', '2020-02-24 17:45:50');
INSERT INTO `sys_user` VALUES ('9a26f5f1-cbd2-473d-82db-1d6dcf4598f8', 'admin', '324ce32d86224b00a02b', 'ac7e435db19997a46e3b390e69cb148b', '13888888888', '06d45b3d-2134-4b36-a9df-d43d2e35041f', '冯凡利', null, 'yingxue@163.com', '1', '1', '1', null, '9a26f5f1-cbd2-473d-82db-1d6dcf4598f8', '3', '2019-09-22 19:38:05', '2020-03-10 10:40:58');
INSERT INTO `sys_user` VALUES ('9a96f5f1-cbd2-473d-82db-1d6dcf4598f8', 'fffff', null, '123456', '123456', null, '123456', '123456', '123456', '1', '1', '1', null, null, null, null, null);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `user_id` varchar(64) DEFAULT NULL COMMENT '用户id',
  `role_id` varchar(64) DEFAULT NULL COMMENT '角色id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('03f30f09-73b8-49af-9c4f-ac2b8ded697a', '8a938151-53e6-4182-925a-684f3be840e8', '0fee4b64-9d48-4cf1-b9a0-bfeaa723e7a0', '2020-03-06 21:52:51');
