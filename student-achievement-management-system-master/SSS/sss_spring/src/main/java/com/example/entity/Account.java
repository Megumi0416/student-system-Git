package com.example.entity;

public class Account {

    private Integer id;
    private String username;    //用户名
    private String password;    //密码
    private String role;    //用户角色
    private String newPassword; //新密码
    private String token;   //认证令牌

    // Getter 和 Setter 方法
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Teacher toTeacher() {
        Teacher teacher = new Teacher();
        teacher.setUsername(this.username);
        teacher.setPassword(this.password);
        teacher.setRole(this.role);
        return teacher;
    }

    public Student toStudent() {
        Student student = new Student();
        student.setUsername(this.username);
        student.setPassword(this.password);
        student.setRole(this.role);
        return student;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
