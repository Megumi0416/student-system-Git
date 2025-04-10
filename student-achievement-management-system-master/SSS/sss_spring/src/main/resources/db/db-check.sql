-- 检查课程表是否存在，如果不存在则创建
CREATE TABLE IF NOT EXISTS course (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '课程名称',
    code VARCHAR(50) COMMENT '课程代码',
    college_id INT COMMENT '学院ID',
    teacher_id INT COMMENT '任课教师ID',
    credit DECIMAL(5,2) COMMENT '学分',
    hour VARCHAR(50) COMMENT '课时',
    college VARCHAR(100) COMMENT '开课学院',
    teachcollege VARCHAR(100) COMMENT '授课教师学院',
    teacher_name VARCHAR(50) COMMENT '教师姓名'
);

-- 检查教师表是否存在，如果不存在则创建
CREATE TABLE IF NOT EXISTS teacher (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL COMMENT '教师工号',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    avatar VARCHAR(255) COMMENT '头像',
    role VARCHAR(20) COMMENT '角色',
    phone VARCHAR(20) COMMENT '电话',
    email VARCHAR(100) COMMENT '邮箱',
    title VARCHAR(50) COMMENT '职称',
    college_id INT COMMENT '学院ID',
    college VARCHAR(100) COMMENT '学院'
);

-- 检查考试表是否存在，如果不存在则创建
CREATE TABLE IF NOT EXISTS exam (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '考试名称',
    course_id INT COMMENT '课程ID',
    exam_type VARCHAR(20) COMMENT '考试类型：midterm/final/makeup/other',
    exam_format VARCHAR(20) COMMENT '考试形式：closed/open/computer',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    location VARCHAR(100) COMMENT '考试地点',
    capacity INT COMMENT '考试容量',
    total_score INT COMMENT '总分值',
    notes TEXT COMMENT '考试说明',
    create_time DATETIME COMMENT '创建时间',
    creator_id INT COMMENT '创建者ID'
);

-- 检查考试学生关联表是否存在，如果不存在则创建
CREATE TABLE IF NOT EXISTS exam_student (
    id INT PRIMARY KEY AUTO_INCREMENT,
    exam_id INT NOT NULL COMMENT '考试ID',
    student_id INT NOT NULL COMMENT '学生ID',
    seat_no INT COMMENT '座位号',
    status VARCHAR(20) DEFAULT 'NOT_STARTED' COMMENT '状态：NOT_STARTED/ABSENT/PRESENT/FINISHED'
);

-- 检查考试监考教师表是否存在，如果不存在则创建
CREATE TABLE IF NOT EXISTS exam_invigilator (
    id INT PRIMARY KEY AUTO_INCREMENT,
    exam_id INT NOT NULL COMMENT '考试ID',
    teacher_id INT NOT NULL COMMENT '教师ID',
    is_main BOOLEAN DEFAULT false COMMENT '是否为主监考'
);

-- 添加一些测试数据（如果表为空）
INSERT INTO course (name, code, teacher_id, credit, hour, college, teachcollege, teacher_name)
SELECT '高等数学', 'MATH101', 1, 4, '32', '数学学院', '数学学院', '张教授'
WHERE NOT EXISTS (SELECT 1 FROM course WHERE name = '高等数学');

INSERT INTO course (name, code, teacher_id, credit, hour, college, teachcollege, teacher_name)
SELECT '数据结构', 'CS201', 2, 3, '32', '计算机学院', '计算机学院', '李教授'
WHERE NOT EXISTS (SELECT 1 FROM course WHERE name = '数据结构');

INSERT INTO course (name, code, teacher_id, credit, hour, college, teachcollege, teacher_name)
SELECT '计算机网络', 'CS301', 3, 3, '32', '计算机学院', '计算机学院', '王副教授'
WHERE NOT EXISTS (SELECT 1 FROM course WHERE name = '计算机网络');

INSERT INTO teacher (username, password, name, avatar, role, phone, email, title, college_id, college)
SELECT 'teacher1', 'password', '张教授', 'avatar_link_1', '教授', '1234567890', 'zhang@example.com', '教授', 1, '数学学院'
WHERE NOT EXISTS (SELECT 1 FROM teacher WHERE username = 'teacher1');

INSERT INTO teacher (username, password, name, avatar, role, phone, email, title, college_id, college)
SELECT 'teacher2', 'password', '李教授', 'avatar_link_2', '副教授', '1234567890', 'li@example.com', '副教授', 2, '计算机学院'
WHERE NOT EXISTS (SELECT 1 FROM teacher WHERE username = 'teacher2');

INSERT INTO teacher (username, password, name, avatar, role, phone, email, title, college_id, college)
SELECT 'teacher3', 'password', '王副教授', 'avatar_link_3', '副教授', '1234567890', 'wang@example.com', '副教授', 3, '计算机学院'
WHERE NOT EXISTS (SELECT 1 FROM teacher WHERE username = 'teacher3');

-- 检查是否有测试数据，如果没有则添加一些示例数据
INSERT INTO exam (name, course_id, exam_type, exam_format, start_time, end_time, location, total_score, notes)
SELECT '高等数学期中考试', 1, 'midterm', 'closed', DATE_ADD(NOW(), INTERVAL 2 DAY), DATE_ADD(NOW(), INTERVAL 2 DAY) + INTERVAL 2 HOUR, '教学楼101', 100, '请携带学生证、2B铅笔和橡皮。禁止携带手机等电子设备。'
WHERE NOT EXISTS (SELECT 1 FROM exam WHERE name = '高等数学期中考试');

INSERT INTO exam (name, course_id, exam_type, exam_format, start_time, end_time, location, total_score, notes)
SELECT '数据结构期中考试', 2, 'midterm', 'open', DATE_ADD(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 1 DAY) + INTERVAL 3 HOUR, '教学楼203', 100, '允许携带课本和笔记，但不允许携带电子设备。'
WHERE NOT EXISTS (SELECT 1 FROM exam WHERE name = '数据结构期中考试');

INSERT INTO exam (name, course_id, exam_type, exam_format, start_time, end_time, location, total_score, notes)
SELECT '计算机网络期末考试', 3, 'final', 'closed', DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY) + INTERVAL 2 HOUR, '教学楼305', 100, '闭卷考试，禁止携带任何资料和电子设备。'
WHERE NOT EXISTS (SELECT 1 FROM exam WHERE name = '计算机网络期末考试');

-- 添加学生安排数据
INSERT INTO exam_student (exam_id, student_id, seat_no, status)
SELECT 1, 1, 10, 'NOT_STARTED'
WHERE NOT EXISTS (SELECT 1 FROM exam_student WHERE exam_id = 1 AND student_id = 1);

INSERT INTO exam_student (exam_id, student_id, seat_no, status)
SELECT 2, 1, 25, 'NOT_STARTED'
WHERE NOT EXISTS (SELECT 1 FROM exam_student WHERE exam_id = 2 AND student_id = 1);

INSERT INTO exam_student (exam_id, student_id, seat_no, status)
SELECT 3, 1, 42, 'FINISHED'
WHERE NOT EXISTS (SELECT 1 FROM exam_student WHERE exam_id = 3 AND student_id = 1);

-- 添加监考教师安排数据
INSERT INTO exam_invigilator (exam_id, teacher_id, is_main)
SELECT 1, 1, true
WHERE NOT EXISTS (SELECT 1 FROM exam_invigilator WHERE exam_id = 1 AND teacher_id = 1);

INSERT INTO exam_invigilator (exam_id, teacher_id, is_main)
SELECT 2, 2, true
WHERE NOT EXISTS (SELECT 1 FROM exam_invigilator WHERE exam_id = 2 AND teacher_id = 2);

INSERT INTO exam_invigilator (exam_id, teacher_id, is_main)
SELECT 3, 3, true
WHERE NOT EXISTS (SELECT 1 FROM exam_invigilator WHERE exam_id = 3 AND teacher_id = 3); 