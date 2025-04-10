-- 创建考试表
CREATE TABLE IF NOT EXISTS `exam` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '考试名称',
  `course_id` int(11) NOT NULL COMMENT '课程ID',
  `exam_type` varchar(20) NOT NULL COMMENT '考试类型(midterm/final/makeup/other)',
  `exam_format` varchar(20) NOT NULL COMMENT '考试形式(closed/open/computer)',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `location` varchar(100) DEFAULT NULL COMMENT '考试地点',
  `capacity` int(11) DEFAULT NULL COMMENT '考试容量',
  `total_score` int(11) DEFAULT NULL COMMENT '总分',
  `notes` text COMMENT '注意事项',
  `duration` int(11) DEFAULT NULL COMMENT '时长(分钟)',
  `status` varchar(20) DEFAULT 'PENDING' COMMENT '状态(PENDING/ACTIVE/FINISHED/CANCELLED)',
  `description` text COMMENT '描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建人ID',
  PRIMARY KEY (`id`),
  KEY `idx_course_id` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考试表';

-- 创建考试学生关联表
CREATE TABLE IF NOT EXISTS `exam_student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `exam_id` int(11) NOT NULL COMMENT '考试ID',
  `student_id` int(11) NOT NULL COMMENT '学生ID',
  `seat_no` int(11) DEFAULT NULL COMMENT '座位号',
  `status` varchar(20) DEFAULT 'NOT_STARTED' COMMENT '状态(NOT_STARTED/ABSENT/PRESENT/FINISHED)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_exam_student` (`exam_id`,`student_id`),
  KEY `idx_exam_id` (`exam_id`),
  KEY `idx_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考试学生关联表';

-- 创建考试监考教师关联表
CREATE TABLE IF NOT EXISTS `exam_invigilator` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `exam_id` int(11) NOT NULL COMMENT '考试ID',
  `teacher_id` int(11) NOT NULL COMMENT '教师ID',
  `is_main` tinyint(1) DEFAULT '0' COMMENT '是否主监考',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_exam_teacher` (`exam_id`,`teacher_id`),
  KEY `idx_exam_id` (`exam_id`),
  KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考试监考教师关联表';

-- 插入测试数据（可选）
INSERT INTO `exam` (`name`, `course_id`, `exam_type`, `exam_format`, `start_time`, `end_time`, `location`, `capacity`, `total_score`, `notes`)
VALUES
('高等数学期中考试', 1, 'midterm', 'closed', DATE_ADD(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 1 DAY + INTERVAL 2 HOUR), '教学楼101', 100, 100, '请携带学生证和2B铅笔');

-- 插入学生考试关联数据（可选）
INSERT INTO `exam_student` (`exam_id`, `student_id`, `seat_no`, `status`)
VALUES
(1, 1, 10, 'NOT_STARTED');

-- 插入监考教师数据（可选）
INSERT INTO `exam_invigilator` (`exam_id`, `teacher_id`, `is_main`)
VALUES
(1, 1, 1); 