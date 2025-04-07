/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80040 (8.0.40)
 Source Host           : localhost:3306
 Source Schema         : sss_mysql

 Target Server Type    : MySQL
 Target Server Version : 80040 (8.0.40)
 File Encoding         : 65001

 Date: 30/03/2025 19:55:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', 'admin123', '管理员', 'http://localhost:8081/files/download/1739697108661-壁纸；流星；日出.png', 'ADMIN', '12345678900', '666@qq.com');

-- ----------------------------
-- Table structure for college
-- ----------------------------
DROP TABLE IF EXISTS `college`;
CREATE TABLE `college`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学院名称',
  `dean` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '院长',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学院表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of college
-- ----------------------------

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '课程名称',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '课程编码',
  `credit` decimal(3, 1) NULL DEFAULT NULL COMMENT '学分',
  `hour` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '课时',
  `college` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开课学院',
  `teacher_id` int NULL DEFAULT NULL COMMENT '授课教师ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_teacher_course`(`teacher_id` ASC) USING BTREE,
  CONSTRAINT `fk_teacher_course` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (1, '数据结构', 'CS101', 2.5, '24节', '计算机学院', 2);
INSERT INTO `course` VALUES (2, '高等数学', 'MATH101', 4.0, '32节', '数学学院', 2);
INSERT INTO `course` VALUES (3, '待定', '未知', 2.5, '16节', '待定', 16);
INSERT INTO `course` VALUES (14, '测试', 'dd', 1.0, '6', 'dd', 16);
INSERT INTO `course` VALUES (15, '马克思主义现代化', 'TC19977', 1.5, '16', '基础学院', 16);

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sender_username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '发送者学号/工号',
  `sender_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发送者姓名',
  `sender_role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '发送者角色(student/teacher)',
  `receiver_username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '接收者学号/工号',
  `receiver_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '接收者姓名',
  `receiver_role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '接收者角色',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息内容',
  `timestamp` datetime NOT NULL COMMENT '发送时间',
  `is_read` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已读',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父消息ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES (28, 'admin', '管理员', 'ADMIN', 's2000000001', '哈哈', 'STUDENT', 'aa', '2025-03-30 01:57:18', 1, NULL);
INSERT INTO `message` VALUES (29, 's2000000001', '哈哈', 'STUDENT', 'admin', '管理员', 'ADMIN', 'hhhhh--aaaaaaa', '2025-03-30 02:12:43', 1, NULL);
INSERT INTO `message` VALUES (30, 'admin', '管理员', 'ADMIN', 's2000000001', '哈哈', 'STUDENT', 'aaaaaaaaaa----hhhh', '2025-03-30 02:13:18', 1, NULL);
INSERT INTO `message` VALUES (31, 'admin', '管理员', 'ADMIN', 't1001', '张三1', 'TEACHER', 'aaaaa-sss', '2025-03-30 02:13:36', 1, NULL);
INSERT INTO `message` VALUES (32, 't1001', '张三1', 'TEACHER', 's2000000001', '哈哈', 'STUDENT', 'ssssss-hhhhhhh', '2025-03-30 02:13:54', 1, NULL);
INSERT INTO `message` VALUES (33, 't1001', '张三1', 'TEACHER', 'admin', '管理员', 'ADMIN', 'ssss--aaa', '2025-03-30 02:35:44', 1, NULL);
INSERT INTO `message` VALUES (34, 'admin', '管理员', 'ADMIN', 't1001', '张三1', 'TEACHER', '安安心心', '2025-03-30 03:03:00', 1, NULL);
INSERT INTO `message` VALUES (35, 't1001', '张三1', 'TEACHER', 'admin', '管理员', 'ADMIN', '11111', '2025-03-30 03:03:10', 1, NULL);
INSERT INTO `message` VALUES (36, 't1001', '张三1', 'TEACHER', 'admin', '管理员', 'ADMIN', '🤖🤖🤖', '2025-03-30 03:03:14', 1, NULL);
INSERT INTO `message` VALUES (37, 'admin', '管理员', 'ADMIN', 't1001', '张三1', 'TEACHER', '啊啊啊啊啊啊啊啊啊啊啊啊😸', '2025-03-30 03:03:37', 1, NULL);
INSERT INTO `message` VALUES (38, 'admin', '管理员', 'ADMIN', 't1001', '张三1', 'TEACHER', '顶顶顶顶顶顶顶顶', '2025-03-30 03:03:40', 1, NULL);
INSERT INTO `message` VALUES (39, 't1001', '张三1', 'TEACHER', 'admin', '管理员', 'ADMIN', 'hhhhhhhhhhhhhhhhh', '2025-03-30 03:03:57', 1, NULL);
INSERT INTO `message` VALUES (40, 't1001', '张三1', 'TEACHER', 'admin', '管理员', 'ADMIN', 'gggggggggg', '2025-03-30 03:03:59', 1, NULL);
INSERT INTO `message` VALUES (41, 'admin', '管理员', 'ADMIN', 't1001', '张三1', 'TEACHER', '啊啊啊啊啊啊啊啊啊啊', '2025-03-30 03:04:05', 1, NULL);
INSERT INTO `message` VALUES (42, 'admin', '管理员', 'ADMIN', 't1001', '张三1', 'TEACHER', '😱', '2025-03-30 03:04:21', 1, NULL);
INSERT INTO `message` VALUES (43, 's1001', '王五', 'STUDENT', 'admin', '管理员', 'ADMIN', 'aaaaaaaa', '2025-03-30 03:10:20', 1, NULL);
INSERT INTO `message` VALUES (44, 'admin', '管理员', 'ADMIN', 's1001', '王五', 'STUDENT', '灌灌灌灌灌灌灌灌', '2025-03-30 03:10:33', 1, NULL);
INSERT INTO `message` VALUES (45, 't1001', '张三1', 'TEACHER', 'admin', '管理员', 'ADMIN', '啊', '2025-03-30 15:48:25', 1, NULL);
INSERT INTO `message` VALUES (46, 't1001', '张三1', 'TEACHER', 'admin', '管理员', 'ADMIN', '☠️☠️', '2025-03-30 15:48:28', 1, NULL);
INSERT INTO `message` VALUES (47, 't1001', '张三1', 'TEACHER', 'admin', '管理员', 'ADMIN', '奥奥奥', '2025-03-30 15:53:02', 0, NULL);

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公告标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '公告内容',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (8, '系统维护', '维护时间：2025年2月17日23点00分--2025年2月20日08点00分', '2025-02-16 17:09:31');
INSERT INTO `notice` VALUES (10, '开学通知', '新学期将于2024年9月1日正式开学，请同学们按时返校。', '2024-08-20 10:00:00');
INSERT INTO `notice` VALUES (11, '考试安排', '期末考试将于2024年12月20日开始，具体安排请查看学校官网。', '2024-12-01 14:30:00');

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, 'manage_user', '管理用户');
INSERT INTO `permission` VALUES (2, 'manage_course', '管理课程');
INSERT INTO `permission` VALUES (3, 'view_score', '查看成绩');
INSERT INTO `permission` VALUES (4, 'system:home', '访问系统首页');
INSERT INTO `permission` VALUES (5, 'user:manage', '用户管理');
INSERT INTO `permission` VALUES (6, 'course:manage', '课程管理');
INSERT INTO `permission` VALUES (7, 'score:input', '成绩录入');
INSERT INTO `permission` VALUES (8, 'score:view', '成绩查看');
INSERT INTO `permission` VALUES (9, 'score:analyze', '成绩分析');
INSERT INTO `permission` VALUES (10, 'notice:manage', '公告管理');
INSERT INTO `permission` VALUES (11, 'message:reply', '消息回复');

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `asker_id` int NOT NULL COMMENT '提问者ID',
  `answer_id` int NULL DEFAULT NULL COMMENT '回答者ID',
  `status` enum('OPEN','CLOSED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'OPEN',
  `college` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `course_id` int NULL DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '问题表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question
-- ----------------------------

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 'ADMIN', '系统管理员');
INSERT INTO `role` VALUES (2, 'TEACHER', '教师');
INSERT INTO `role` VALUES (3, 'STUDENT', '学生');

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` int NULL DEFAULT NULL COMMENT '角色ID',
  `permission_id` int NULL DEFAULT NULL COMMENT '权限ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_role_permission_role`(`role_id` ASC) USING BTREE,
  INDEX `fk_role_permission_permission`(`permission_id` ASC) USING BTREE,
  CONSTRAINT `fk_role_permission_permission` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_role_permission_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色权限关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES (1, 1, 1);
INSERT INTO `role_permission` VALUES (2, 1, 2);
INSERT INTO `role_permission` VALUES (3, 2, 3);
INSERT INTO `role_permission` VALUES (4, 3, 3);
INSERT INTO `role_permission` VALUES (5, 1, 1);
INSERT INTO `role_permission` VALUES (6, 1, 2);
INSERT INTO `role_permission` VALUES (7, 1, 3);
INSERT INTO `role_permission` VALUES (8, 1, 4);
INSERT INTO `role_permission` VALUES (9, 1, 5);
INSERT INTO `role_permission` VALUES (10, 1, 6);
INSERT INTO `role_permission` VALUES (11, 1, 7);
INSERT INTO `role_permission` VALUES (12, 1, 8);
INSERT INTO `role_permission` VALUES (13, 2, 1);
INSERT INTO `role_permission` VALUES (14, 2, 4);
INSERT INTO `role_permission` VALUES (15, 2, 6);
INSERT INTO `role_permission` VALUES (16, 2, 8);

-- ----------------------------
-- Table structure for score
-- ----------------------------
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` int NULL DEFAULT NULL COMMENT '学生ID',
  `course_id` int NULL DEFAULT NULL COMMENT '课程ID',
  `score` decimal(5, 2) NULL DEFAULT NULL COMMENT '成绩',
  `term` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学期',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_student_score`(`student_id` ASC) USING BTREE,
  INDEX `fk_course_score`(`course_id` ASC) USING BTREE,
  CONSTRAINT `fk_course_score` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_student_score` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '成绩表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of score
-- ----------------------------
INSERT INTO `score` VALUES (1, 1, 1, NULL, '2022-2025学年第一学期');
INSERT INTO `score` VALUES (2, 2, 2, 90.99, '2022-2023学年第一学期');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `sex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `birth` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '生日',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `grade` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '年级',
  `college` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学院',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 214 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学生信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (1, '20191001', '123', '小白白1', '13788997788', 'xbb@xm.com', '男', '2000-10-01', 'http://localhost:8081/files/download/1740560718581-敦煌风美女4k壁纸_彼岸图网.jpg', '2021', '人工智能', 'STUDENT');
INSERT INTO `student` VALUES (2, '20191002', '123', '没头脑', '12345678900', NULL, NULL, NULL, NULL, '2020', '信息技术', 'STUDENT');
INSERT INTO `student` VALUES (13, 's1001', '123456', '王五', '13800138003', 'wangwu@example.com', '男', '2000-01-01', 'http://localhost:8081/files/download/1740624417880-玫瑰乐园.png', '2022', '信息技术', 'STUDENT');
INSERT INTO `student` VALUES (16, 's2000000001', '123', '哈哈', '15915184843', '965042387@qq.com', '男', '2000-02-01', 'http://localhost:8081/files/download/1740819959595-晴天1.png', '2020', '计算机学院', 'STUDENT');
INSERT INTO `student` VALUES (65, 's2734056596', 'c6AFwuUe', '王华', '18728713890', 'minjin@example.org', '男', '2004-07-21', 'default_avatar.png', '2023', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (66, 's2641033121', '2Jz0shZF', '魏晶', '15695336290', 'xueqiang@example.com', '男', '1999-05-08', 'default_avatar.png', '2023', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (67, 's2984256539', '9lPhHZrs', '吴畅', '18231109622', 'yongzhong@example.com', '女', '2001-03-13', 'default_avatar.png', '2020', '商学院', 'STUDENTS');
INSERT INTO `student` VALUES (68, 's2296817358', 'esw9URZa', '孙桂英', '18871805845', 'xiulan07@example.org', '男', '1998-02-07', 'default_avatar.png', '2024', '商学院', 'STUDENTS');
INSERT INTO `student` VALUES (69, 's2126738184', 'Oyols2GZ', '李凯', '18171503513', 'nameng@example.org', '男', '1998-06-16', 'default_avatar.png', '2021', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (70, 's2939272607', 'x1Mk1FPa', '罗芳', '18674843738', 'qdong@example.org', '男', '2002-11-22', 'default_avatar.png', '2020', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (71, 's2904012008', '5QrF1CIr', '蔡磊', '18153781349', 'luping@example.com', '女', '2003-04-27', 'default_avatar.png', '2020', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (72, 's2798665363', '3EcxKhF7', '鞠磊', '18142196004', 'yeyang@example.org', '男', '2004-01-28', 'default_avatar.png', '2022', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (73, 's2349740462', 'GEG75xro', '许荣', '15295377760', 'yuanming@example.org', '男', '2003-04-09', 'default_avatar.png', '2019', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (74, 's2978495256', 'FeyutZNt', '丁峰', '14708195575', 'juanqian@example.net', '女', '1999-03-11', 'default_avatar.png', '2023', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (75, 's2017969612', 'nyvoJ52c', '杨秀荣', '15593368582', 'bma@example.com', '男', '2000-07-07', 'default_avatar.png', '2024', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (76, 's2868828375', 'ANsm64WR', '张欢', '15172686813', 'gang78@example.org', '女', '2002-10-24', 'default_avatar.png', '2022', '法学院', 'STUDENTS');
INSERT INTO `student` VALUES (77, 's2123574749', 'RvngpmeU', '杨玉兰', '15798890851', 'btao@example.net', '女', '2000-02-03', 'default_avatar.png', '2019', '法学院', 'STUDENTS');
INSERT INTO `student` VALUES (78, 's2504406596', 'wuqlhmVm', '汪桂兰', '15676193887', 'kshen@example.org', '女', '1998-11-12', 'default_avatar.png', '2021', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (79, 's2195380951', 'm2Pqzqoa', '杨帅', '13629030929', 'qiangkang@example.org', '女', '2002-11-12', 'default_avatar.png', '2019', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (80, 's2088898435', 'URCHG1qB', '熊婷婷', '13747693217', 'xiangjuan@example.com', '男', '2005-06-23', 'default_avatar.png', '2022', '商学院', 'STUDENTS');
INSERT INTO `student` VALUES (81, 's2359853636', 'nTvKZ4BR', '林亮', '13146862711', 'daiwei@example.org', '女', '2005-12-25', 'default_avatar.png', '2023', '商学院', 'STUDENTS');
INSERT INTO `student` VALUES (82, 's2179946892', '0eMMSdJN', '张桂芳', '14574998147', 'qiang65@example.com', '女', '2000-04-25', 'default_avatar.png', '2022', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (83, 's2905018779', '4fGjgFjI', '吕坤', '18828632809', 'jingmo@example.net', '男', '2001-12-28', 'default_avatar.png', '2024', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (84, 's2841843515', '5dJdRjQB', '黄彬', '15994715572', 'weiyao@example.net', '男', '1998-04-19', 'default_avatar.png', '2024', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (85, 's2570401990', '5pK843bp', '周军', '18010872310', 'xiashao@example.com', '女', '2002-06-22', 'default_avatar.png', '2023', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (86, 's2652458231', 'g6WtljTN', '陈桂兰', '13512795371', 'zxia@example.net', '男', '2003-09-05', 'default_avatar.png', '2021', '法学院', 'STUDENTS');
INSERT INTO `student` VALUES (87, 's2765251747', 'sTiJQDWU', '张楠', '18194036732', 'xmo@example.com', '男', '2005-09-25', 'default_avatar.png', '2022', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (88, 's2302146755', 'oRlV1REA', '燕桂兰', '18875306754', 'chenjun@example.org', '女', '1998-04-07', 'default_avatar.png', '2020', '法学院', 'STUDENTS');
INSERT INTO `student` VALUES (89, 's2372998108', 'lPyMl2AC', '李玉', '15610276410', 'bkong@example.org', '男', '2002-02-18', 'default_avatar.png', '2020', '商学院', 'STUDENTS');
INSERT INTO `student` VALUES (90, 's2191836208', 'TU2t3FLu', '麦平', '15666669958', 'longmin@example.org', '女', '2003-06-16', 'default_avatar.png', '2020', '医学院', 'STUDENTS');
INSERT INTO `student` VALUES (91, 's2738266796', 'BihU2NgN', '詹桂芝', '13952065608', 'uxiong@example.com', '女', '2000-11-10', 'default_avatar.png', '2022', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (92, 's2612980589', '0Bhzd6OD', '龙秀珍', '13302784970', 'minliao@example.org', '女', '1999-03-22', 'default_avatar.png', '2024', '医学院', 'STUDENTS');
INSERT INTO `student` VALUES (93, 's2040189390', 'y6BsNNGW', '陈志强', '18894110673', 'guiyingduan@example.com', '男', '1999-05-28', 'default_avatar.png', '2022', '法学院', 'STUDENTS');
INSERT INTO `student` VALUES (94, 's2176553973', '1jzkV55v', '程丽', '14501597037', 'nacheng@example.com', '男', '2003-08-21', 'default_avatar.png', '2024', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (95, 's2617695689', 'yMlskvru', '韩建华', '13470107539', 'xluo@example.org', '女', '2000-11-26', 'default_avatar.png', '2020', '医学院', 'STUDENTS');
INSERT INTO `student` VALUES (96, 's2421498229', '6hZcElKW', '严倩', '18514573573', 'fengxiulan@example.org', '男', '2003-04-13', 'default_avatar.png', '2023', '商学院', 'STUDENTS');
INSERT INTO `student` VALUES (97, 's2916315456', 'w47QyiJu', '郭斌', '15823219901', 'chaoxia@example.org', '男', '2000-12-01', 'default_avatar.png', '2022', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (98, 's2615702019', 'HYqXJBAl', '杨帆', '15750678388', 'tanwei@example.net', '男', '2003-11-05', 'default_avatar.png', '2020', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (99, 's2598172228', 'CDA6uP7W', '卿玉英', '13385536986', 'zhaowei@example.org', '男', '2005-12-06', 'default_avatar.png', '2020', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (100, 's2218673522', 'zaDFq2dL', '李秀华', '15592087942', 'guiying71@example.com', '男', '2003-01-11', 'default_avatar.png', '2022', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (101, 's2430484186', '21tLSKuy', '赵伟', '14768700834', 'duqiang@example.com', '女', '2005-08-06', 'default_avatar.png', '2022', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (102, 's2198802156', 'luJ0aXBa', '王波', '13244992934', 'min04@example.net', '女', '2000-05-19', 'default_avatar.png', '2020', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (103, 's2699276256', 'Ezmxqswo', '黄莉', '15278446444', 'caiqiang@example.com', '女', '2001-07-03', 'default_avatar.png', '2019', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (104, 's2995971388', 'nql6AwLu', '刘淑兰', '13730917444', 'sxiao@example.com', '女', '1998-11-23', 'default_avatar.png', '2022', '商学院', 'STUDENTS');
INSERT INTO `student` VALUES (105, 's2093354827', '68Q9ZfJg', '宋斌', '18685405851', 'songjun@example.net', '女', '2003-05-22', 'default_avatar.png', '2021', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (106, 's2160017459', 'wWOReqCF', '李玉兰', '13031297775', 'nazhao@example.net', '男', '1999-01-10', 'default_avatar.png', '2022', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (107, 's2269147429', 'zhe3Q4yS', '耿婷', '13278706597', 'xiuyingyao@example.com', '女', '2001-07-04', 'default_avatar.png', '2023', '法学院', 'STUDENTS');
INSERT INTO `student` VALUES (108, 's2723694114', 'HD0jd2sj', '周玉珍', '13372708676', 'lishi@example.org', '男', '2002-02-13', 'default_avatar.png', '2022', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (109, 's2390513862', 'G6hEBP5u', '郭娜', '15317068691', 'shu@example.org', '男', '1999-09-03', 'default_avatar.png', '2021', '法学院', 'STUDENTS');
INSERT INTO `student` VALUES (110, 's2901741535', 'V0IxxE6X', '吴丽娟', '15173726476', 'qiang64@example.net', '女', '2000-01-05', 'default_avatar.png', '2023', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (111, 's2326431270', 'EKidzhMv', '王平', '18146740251', 'nhan@example.org', '女', '1999-08-23', 'default_avatar.png', '2021', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (112, 's2100241512', 'XFWBa9on', '郝帆', '13766685690', 'yongliu@example.net', '女', '2001-01-12', 'default_avatar.png', '2019', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (113, 's2292827375', 'vYLGp3OJ', '余刚', '13698460151', 'luoyan@example.net', '男', '1999-10-03', 'default_avatar.png', '2024', '法学院', 'STUDENTS');
INSERT INTO `student` VALUES (114, 's2550815664', 'pdbLc9Gj', '曾涛', '13284133979', 'xdai@example.org', '女', '2004-10-20', 'default_avatar.png', '2023', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (115, 's2999667511', 'Sc0P3VKt', '郭冬梅', '13831767299', 'na80@example.net', '女', '1998-05-25', 'default_avatar.png', '2019', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (116, 's2464075540', 'IcLFF5SR', '王艳', '13749321988', 'jing36@example.org', '男', '1999-06-03', 'default_avatar.png', '2021', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (117, 's2716457213', 'Q0vpra8p', '朱杨', '13196919549', 'guiying88@example.org', '男', '2003-12-26', 'default_avatar.png', '2021', '医学院', 'STUDENTS');
INSERT INTO `student` VALUES (118, 's2696610717', 'SQkHt77C', '王颖', '15216020254', 'jlei@example.org', '女', '2000-11-25', 'default_avatar.png', '2022', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (119, 's2315791094', 'CwuPNiK2', '谭磊', '15779622899', 'li03@example.org', '女', '2003-06-21', 'default_avatar.png', '2021', '商学院', 'STUDENTS');
INSERT INTO `student` VALUES (120, 's2454787177', 'hBVBs33p', '蔡颖', '14555662858', 'caiyang@example.net', '女', '1998-12-10', 'default_avatar.png', '2019', '商学院', 'STUDENTS');
INSERT INTO `student` VALUES (121, 's2589894486', 'gE1fOw2l', '高桂香', '18532414341', 'qxu@example.com', '女', '2004-12-01', 'default_avatar.png', '2022', '商学院', 'STUDENTS');
INSERT INTO `student` VALUES (122, 's2310301296', 'MgZJsbbS', '袁秀英', '18633732062', 'guiying29@example.net', '女', '2001-02-25', 'default_avatar.png', '2022', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (123, 's2367094761', 'FNlwfg93', '许晶', '15917543431', 'fangsun@example.com', '男', '1998-08-01', 'default_avatar.png', '2021', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (124, 's2989313864', 'IgWxL9uW', '谈玉梅', '15111577682', 'ehou@example.com', '女', '1999-06-28', 'default_avatar.png', '2020', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (125, 's2086398010', 'lORmzh6f', '张英', '13868128477', 'ishi@example.org', '男', '2004-03-27', 'default_avatar.png', '2020', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (126, 's2983945265', 'IC78N0Gs', '李玲', '15510502709', 'tao29@example.com', '男', '1998-11-09', 'default_avatar.png', '2022', '医学院', 'STUDENTS');
INSERT INTO `student` VALUES (127, 's2148702655', 'RJj8xXsR', '李斌', '15786829266', 'linjun@example.org', '男', '1998-04-14', 'default_avatar.png', '2023', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (128, 's2064266398', '5fR3RyGr', '马芳', '13316641724', 'czhou@example.net', '男', '2003-12-06', 'default_avatar.png', '2022', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (129, 's2298623436', 'kQxMu0xJ', '梁桂香', '18979169505', 'jing45@example.org', '女', '2000-07-16', 'default_avatar.png', '2024', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (130, 's2525231644', 'ZBpfjGeP', '徐玉', '18941187151', 'xiulan23@example.net', '男', '2001-05-22', 'default_avatar.png', '2021', '医学院', 'STUDENTS');
INSERT INTO `student` VALUES (131, 's2131471932', '8hUXs8Gj', '郑琴', '18540310360', 'juan72@example.org', '男', '2000-03-12', 'default_avatar.png', '2020', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (132, 's2997056992', 'DjjmMclX', '崔雪梅', '13236142210', 'yzhong@example.net', '女', '2000-12-12', 'default_avatar.png', '2023', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (133, 's2502697121', 'DfbExDDM', '罗健', '14524190463', 'shengang@example.com', '男', '1999-12-23', 'default_avatar.png', '2024', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (134, 's2966989137', 'gbugljJY', '吴淑华', '14793251426', 'xma@example.org', '男', '2003-05-02', 'default_avatar.png', '2019', '医学院', 'STUDENTS');
INSERT INTO `student` VALUES (135, 's2357400419', '4kfYO5ev', '尹芳', '13817136485', 'xiuying91@example.net', '男', '2002-11-04', 'default_avatar.png', '2024', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (136, 's2007607824', 'BZNo8yzI', '钟玲', '15548218915', 'xiuying73@example.org', '男', '2004-05-04', 'default_avatar.png', '2020', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (137, 's2613755734', 'IsZJjpD4', '瞿宁', '18865292152', 'xiuying04@example.com', '女', '2005-12-05', 'default_avatar.png', '2022', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (138, 's2127037354', 'bBN7vqLq', '虞娟', '18187523990', 'qkang@example.net', '男', '2001-05-22', 'default_avatar.png', '2023', '医学院', 'STUDENTS');
INSERT INTO `student` VALUES (139, 's2903835113', 'eJvKzGx5', '韦平', '18032879504', 'weiguiying@example.org', '女', '2005-11-26', 'default_avatar.png', '2024', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (140, 's2771812387', 'W4CII0UK', '赵娟', '18843852551', 'guiying57@example.com', '女', '1998-06-16', 'default_avatar.png', '2021', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (141, 's2323298953', 'wW444wK2', '张萍', '15242216034', 'li80@example.org', '男', '2004-11-20', 'default_avatar.png', '2019', '商学院', 'STUDENTS');
INSERT INTO `student` VALUES (142, 's2401202504', 'H29xyILq', '李丹', '15584833997', 'zhuqiang@example.net', '女', '2002-03-02', 'default_avatar.png', '2019', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (143, 's2555061820', '8LnjfMvr', '刘博', '13983944224', 'guiyingwei@example.com', '男', '2001-04-27', 'default_avatar.png', '2019', '商学院', 'STUDENTS');
INSERT INTO `student` VALUES (144, 's2998366648', 'sV2cvYRv', '鞠雪', '13360969550', 'xia43@example.com', '男', '1998-08-21', 'default_avatar.png', '2023', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (145, 's2193640554', 'I2vsG7gP', '李明', '15820463850', 'bxu@example.net', '女', '2001-07-06', 'default_avatar.png', '2021', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (146, 's2933382037', 'iLz8pBsJ', '鲁帆', '13174447864', 'yanna@example.com', '男', '2002-05-07', 'default_avatar.png', '2019', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (147, 's2681535833', 'l71bDXyt', '冷丹', '13598484739', 'qsun@example.net', '女', '2004-08-20', 'default_avatar.png', '2024', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (148, 's2949337216', 'OiAXHxGF', '纪玉兰', '13202062202', 'kangfang@example.com', '女', '1999-10-03', 'default_avatar.png', '2020', '医学院', 'STUDENTS');
INSERT INTO `student` VALUES (149, 's2202559356', 'YIhDa4sB', '萧旭', '13376663097', 'yangcao@example.com', '男', '2004-03-04', 'default_avatar.png', '2024', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (150, 's2101692689', 'bWxNxuaY', '黄淑华', '13938983867', 'ming14@example.com', '男', '2003-09-23', 'default_avatar.png', '2020', '法学院', 'STUDENTS');
INSERT INTO `student` VALUES (151, 's2661883713', 'm8NBskgk', '陈凯', '13148610319', 'wenxiuying@example.org', '男', '2004-04-21', 'default_avatar.png', '2024', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (152, 's2194545616', '5LTCyur2', '李芳', '14722475640', 'lei00@example.com', '女', '2002-05-11', 'default_avatar.png', '2024', '法学院', 'STUDENTS');
INSERT INTO `student` VALUES (153, 's2598646842', 'lsJW7RL7', '马桂英', '15812911734', 'yaojun@example.net', '男', '2004-11-14', 'default_avatar.png', '2021', '商学院', 'STUDENTS');
INSERT INTO `student` VALUES (154, 's2571913102', 'LvWmM87u', '阮鹏', '13358878836', 'linlei@example.com', '女', '2000-04-27', 'default_avatar.png', '2022', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (155, 's2474163339', 'kWRBrcKE', '何琴', '13673451446', 'leixie@example.com', '男', '1999-03-15', 'default_avatar.png', '2020', '商学院', 'STUDENTS');
INSERT INTO `student` VALUES (156, 's2038982713', 'UPtiKs63', '孙燕', '14782391447', 'xiaoxia@example.org', '女', '2002-09-14', 'default_avatar.png', '2019', '商学院', 'STUDENTS');
INSERT INTO `student` VALUES (157, 's2500567210', '4NOsppEN', '吴桂香', '15157192076', 'moyang@example.org', '女', '2002-02-04', 'default_avatar.png', '2023', '商学院', 'STUDENTS');
INSERT INTO `student` VALUES (158, 's2490887125', 'lniafNdV', '张健', '18822705991', 'jingxiong@example.net', '男', '2003-05-25', 'default_avatar.png', '2020', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (159, 's2974804177', 'rBxFeaj4', '马冬梅', '15157810389', 'qiang46@example.net', '男', '2001-08-03', 'default_avatar.png', '2024', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (160, 's2609626565', 't4jl4MSg', '何秀云', '18220599907', 'nsun@example.net', '女', '1998-07-05', 'default_avatar.png', '2021', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (161, 's2619429599', 'N2NgMhC0', '黄玲', '13120030558', 'yong86@example.net', '女', '1999-04-26', 'default_avatar.png', '2022', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (162, 's2171857712', 'Kn2De9pU', '唐玉', '15023183466', 'tangqiang@example.org', '男', '2002-03-05', 'default_avatar.png', '2020', '商学院', 'STUDENTS');
INSERT INTO `student` VALUES (163, 's2593565190', 'fKGz9w7Y', '徐丽娟', '15741328132', 'wei58@example.net', '男', '2005-05-23', 'default_avatar.png', '2022', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (164, 's2879610928', 'PvELbLfO', '甘凤兰', '18211741590', 'jie06@example.com', '男', '2003-02-15', 'default_avatar.png', '2020', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (165, 's2064352157', 'Db6Q90KD', '雷欣', '18088758625', 'caijun@example.net', '男', '2005-03-22', 'default_avatar.png', '2020', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (166, 's2747821216', '7tJu3ObN', '陈杨', '13845352695', 'leima@example.com', '女', '2002-01-03', 'default_avatar.png', '2024', '医学院', 'STUDENTS');
INSERT INTO `student` VALUES (167, 's2841024464', 'iQHqropR', '宋瑜', '14547703710', 'xiuying36@example.org', '男', '2003-04-10', 'default_avatar.png', '2023', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (168, 's2986349440', '6XvARt3Q', '张萍', '18975037219', 'jun07@example.org', '男', '2005-12-09', 'default_avatar.png', '2022', '法学院', 'STUDENTS');
INSERT INTO `student` VALUES (169, 's2241695586', 'KwYB3bPw', '邓林', '15688980865', 'zbai@example.com', '男', '1998-07-20', 'default_avatar.png', '2022', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (170, 's2188742730', 'MFQg9Nlg', '程晶', '13497540883', 'nwang@example.org', '男', '2000-08-10', 'default_avatar.png', '2024', '法学院', 'STUDENTS');
INSERT INTO `student` VALUES (171, 's2632794597', 'KBWEtI2t', '金俊', '18533321794', 'weiping@example.com', '男', '1998-10-24', 'default_avatar.png', '2024', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (172, 's2990237433', 'a5makVQz', '莫超', '13347921942', 'xiuying75@example.net', '男', '2005-04-05', 'default_avatar.png', '2022', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (173, 's2778340016', 'QmySLX1g', '陈娟', '13829076229', 'yong29@example.net', '女', '1999-01-06', 'default_avatar.png', '2023', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (174, 's2639258236', 'NcAMI8wq', '陈博', '15057183084', 'fanqiang@example.org', '男', '2005-11-22', 'default_avatar.png', '2024', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (175, 's2190608832', 'jKw6nruW', '吴淑英', '15729823666', 'fang39@example.com', '女', '1999-02-20', 'default_avatar.png', '2019', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (176, 's2210237453', 'W5GdfCF9', '叶鑫', '13390923145', 'caoguiying@example.org', '男', '2005-04-10', 'default_avatar.png', '2019', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (177, 's2266753619', '2NLTDYQ1', '翁斌', '13498292966', 'qiangdai@example.net', '男', '2005-03-04', 'default_avatar.png', '2020', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (178, 's2714774143', '2PT7k42F', '李建华', '15153940314', 'jingzeng@example.net', '女', '2000-11-20', 'default_avatar.png', '2023', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (179, 's2304654217', 'Xi9Xzn2X', '石杨', '15049374972', 'ushi@example.com', '女', '2003-11-18', 'default_avatar.png', '2020', '医学院', 'STUDENTS');
INSERT INTO `student` VALUES (180, 's2107375578', 'zmrN6xZa', '欧阳健', '13370230811', 'guiying15@example.com', '男', '1998-07-05', 'default_avatar.png', '2022', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (181, 's2279391834', 'rDpiJM5N', '李海燕', '13533114786', 'nafang@example.org', '女', '2003-06-26', 'default_avatar.png', '2022', '法学院', 'STUDENTS');
INSERT INTO `student` VALUES (182, 's2792015003', 'd7GzWiZv', '刘敏', '18295275617', 'li08@example.net', '男', '2001-07-27', 'default_avatar.png', '2023', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (183, 's2460204464', 'y5kJdQYd', '杨建平', '13946673867', 'xia38@example.com', '男', '2001-01-21', 'default_avatar.png', '2022', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (184, 's2827682829', 'aV130E3n', '刘瑞', '13281060153', 'rzhang@example.com', '女', '2005-09-14', 'default_avatar.png', '2019', '医学院', 'STUDENTS');
INSERT INTO `student` VALUES (185, 's2399322502', 'RsGxNB6p', '陈浩', '15935177611', 'pingxiang@example.net', '男', '2002-06-08', 'default_avatar.png', '2021', '医学院', 'STUDENTS');
INSERT INTO `student` VALUES (186, 's2254690490', 'dc3tJbUa', '赵敏', '13518636967', 'yanchang@example.net', '男', '2002-11-19', 'default_avatar.png', '2023', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (187, 's2791778571', 'ZB5EDltW', '刘涛', '13872203101', 'fhan@example.net', '女', '2002-05-12', 'default_avatar.png', '2024', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (188, 's2820881547', 'CJlPKRlH', '李颖', '13972043286', 'yong89@example.com', '女', '2000-06-16', 'default_avatar.png', '2022', '法学院', 'STUDENTS');
INSERT INTO `student` VALUES (189, 's2399302388', 'DFGnpKcr', '陈琴', '13618357604', 'vwang@example.net', '男', '2005-12-16', 'default_avatar.png', '2019', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (190, 's2654595947', 'xZ6VBexY', '刘军', '13857292222', 'majun@example.org', '女', '2001-12-25', 'default_avatar.png', '2023', '法学院', 'STUDENTS');
INSERT INTO `student` VALUES (191, 's2601956380', '9z074Kpu', '张林', '13974790991', 'yong82@example.net', '女', '2003-10-17', 'default_avatar.png', '2020', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (192, 's2645538605', 'CDQhJPYH', '李娟', '15040466158', 'eqiao@example.net', '男', '2005-05-03', 'default_avatar.png', '2023', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (193, 's2901167021', 'XvqbBwvx', '姚秀云', '13366384473', 'laimin@example.net', '男', '2004-05-13', 'default_avatar.png', '2023', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (194, 's2660174499', 'xRBA0qDM', '黄凯', '13166382327', 'lifang@example.org', '女', '2003-01-09', 'default_avatar.png', '2021', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (195, 's2471787846', 'iSpH6Rh8', '蒋帆', '15912944376', 'longmin@example.com', '女', '2002-07-06', 'default_avatar.png', '2023', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (196, 's2128934528', 'bDZ7q6VI', '林霞', '18041480027', 'xialuo@example.org', '男', '2001-09-06', 'default_avatar.png', '2021', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (197, 's2636950847', 'neVtxWF6', '张磊', '13060632277', 'whe@example.org', '男', '2000-10-04', 'default_avatar.png', '2019', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (198, 's2558482406', '3RGgtoRA', '任晶', '15180385646', 'jiechang@example.org', '男', '2005-07-20', 'default_avatar.png', '2024', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (199, 's2369817218', '23TmU5Pp', '吕林', '13852825962', 'tao17@example.net', '男', '2001-02-23', 'default_avatar.png', '2019', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (200, 's2693231272', '8xCfuKWm', '杨桂芳', '15675093315', 'xiuying71@example.org', '女', '2003-03-05', 'default_avatar.png', '2024', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (201, 's2734466976', 'Qbq0uJ1c', '王玉珍', '18087238782', 'fangtao@example.org', '男', '2000-06-05', 'default_avatar.png', '2021', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (202, 's2454971687', 'eChO7dd1', '金丹', '18145321465', 'yang09@example.net', '男', '2003-01-26', 'default_avatar.png', '2019', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (203, 's2628324817', 'fyJ41NVG', '陆芳', '18739394348', 'fanghe@example.net', '男', '1998-08-07', 'default_avatar.png', '2024', '文学院', 'STUDENTS');
INSERT INTO `student` VALUES (204, 's2590068658', 'cfgPvnHD', '罗玲', '13211142568', 'yan42@example.com', '女', '2004-04-09', 'default_avatar.png', '2024', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (205, 's2031917089', '9Z4KUHAT', '邹玉华', '15654544182', 'jun94@example.net', '男', '2004-06-11', 'default_avatar.png', '2020', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (206, 's2291188233', '3zkYKMvG', '尹文', '13179994860', 'hshao@example.net', '女', '2001-10-01', 'default_avatar.png', '2019', '商学院', 'STUDENTS');
INSERT INTO `student` VALUES (207, 's2389977209', 'N9eIjOaP', '邓玉珍', '13343201556', 'yan68@example.com', '男', '1999-05-11', 'default_avatar.png', '2023', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (208, 's2670276524', 'XPak3YRR', '杜岩', '14580069858', 'li27@example.net', '女', '2000-07-09', 'default_avatar.png', '2023', '计算机科学学院', 'STUDENTS');
INSERT INTO `student` VALUES (209, 's2561805555', 'E8HU9Jth', '任楠', '15945793562', 'xiajun@example.com', '男', '2002-12-28', 'default_avatar.png', '2023', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (210, 's2009410478', 'ywnASdJQ', '关丽丽', '18198016283', 'guiying07@example.org', '女', '2004-07-02', 'default_avatar.png', '2022', '工程学院', 'STUDENTS');
INSERT INTO `student` VALUES (211, 's2058686953', 'wbtMnEyM', '梁艳', '13074098813', 'fengmin@example.com', '男', '2005-01-27', 'default_avatar.png', '2023', '商学院', 'STUDENTS');
INSERT INTO `student` VALUES (212, 's2299852823', '1sKKybiG', '胡秀珍', '13380246115', 'dengping@example.net', '女', '2000-06-27', 'default_avatar.png', '2024', '艺术学院', 'STUDENTS');
INSERT INTO `student` VALUES (213, 's2647300443', 'ugCN0dkH', '林海燕', '13142065597', 'chaoxiao@example.com', '男', '2000-11-14', 'default_avatar.png', '2021', '文学院', 'STUDENTS');

-- ----------------------------
-- Table structure for student_course
-- ----------------------------
DROP TABLE IF EXISTS `student_course`;
CREATE TABLE `student_course`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` int NULL DEFAULT NULL COMMENT '学生ID',
  `course_id` int NULL DEFAULT NULL COMMENT '课程ID',
  `score` decimal(5, 2) NULL DEFAULT NULL COMMENT '成绩',
  `term` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学期',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_student_course`(`student_id` ASC) USING BTREE,
  INDEX `fk_course_student`(`course_id` ASC) USING BTREE,
  CONSTRAINT `fk_course_student` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_student_course` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '选课表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student_course
-- ----------------------------
INSERT INTO `student_course` VALUES (15, 16, 3, 80.00, '2024春季学期');
INSERT INTO `student_course` VALUES (16, 13, 1, 98.00, '2024春季学期');
INSERT INTO `student_course` VALUES (18, 13, 3, 98.00, '2025秋季学期');
INSERT INTO `student_course` VALUES (21, 16, 1, 80.00, '2024春季学期');
INSERT INTO `student_course` VALUES (22, 16, 14, 80.00, '2024春季学期');
INSERT INTO `student_course` VALUES (25, 16, 15, 80.00, '2024春季学期');
INSERT INTO `student_course` VALUES (28, 16, 2, NULL, NULL);

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '工号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职称',
  `college` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学院',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '教师表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES (1, 't1001', '123456', '张三', 'http://localhost:8081/files/download/1740623327985-马尾2.jpg', 'TEACHER', '13800138000', 'zhangsan@example.com', '教授', '计算机学院');
INSERT INTO `teacher` VALUES (2, 't1002', '123456', '李四', 'avatar2.jpg', 'TEACHER', '13800138002', 'lisi@example.com', '副教授', '数学学院');
INSERT INTO `teacher` VALUES (16, 'ggg', '123456', '待定', NULL, 'TEACHER', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for exam
-- ----------------------------
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '考试名称',
  `course_id` int NOT NULL COMMENT '考试科目ID',
  `exam_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '考试类型(期中/期末/补考)',
  `exam_format` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '考试形式(闭卷/开卷/机考)',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '考试地点',
  `capacity` int NULL DEFAULT 60 COMMENT '考试容量',
  `total_score` int NULL DEFAULT 100 COMMENT '考试总分',
  `notes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '考试注意事项',
  `creator_id` int NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_course_exam`(`course_id` ASC) USING BTREE,
  CONSTRAINT `fk_course_exam` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '考试表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exam
-- ----------------------------

-- ----------------------------
-- Table structure for exam_student
-- ----------------------------
DROP TABLE IF EXISTS `exam_student`;
CREATE TABLE `exam_student`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `exam_id` int NOT NULL COMMENT '考试ID',
  `student_id` int NOT NULL COMMENT '学生ID',
  `seat_no` int NULL DEFAULT NULL COMMENT '座位号',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'NOT_STARTED' COMMENT '状态(NOT_STARTED/ABSENT/PRESENT/FINISHED)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_exam_student_exam`(`exam_id` ASC) USING BTREE,
  INDEX `fk_exam_student_student`(`student_id` ASC) USING BTREE,
  CONSTRAINT `fk_exam_student_exam` FOREIGN KEY (`exam_id`) REFERENCES `exam` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_exam_student_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '考试学生关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exam_student
-- ----------------------------

-- ----------------------------
-- Table structure for exam_invigilator
-- ----------------------------
DROP TABLE IF EXISTS `exam_invigilator`;
CREATE TABLE `exam_invigilator`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `exam_id` int NOT NULL COMMENT '考试ID',
  `teacher_id` int NOT NULL COMMENT '教师ID',
  `is_main` tinyint(1) NULL DEFAULT 0 COMMENT '是否主监考',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_exam_invigilator_exam`(`exam_id` ASC) USING BTREE,
  INDEX `fk_exam_invigilator_teacher`(`teacher_id` ASC) USING BTREE,
  CONSTRAINT `fk_exam_invigilator_exam` FOREIGN KEY (`exam_id`) REFERENCES `exam` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_exam_invigilator_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '考试监考教师关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exam_invigilator
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
