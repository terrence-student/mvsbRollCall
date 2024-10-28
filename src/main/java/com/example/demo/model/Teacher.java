package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "teacher")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacherid", columnDefinition = "INT")
    private Long teacherId;

    @Column(name = "name") // 對應資料庫的欄位名稱
    private String name;

    @Column(name = "username") // 對應資料庫的欄位名稱
    private String username;

    @Column(name = "password") // 對應資料庫的欄位名稱
    private String password;

    @Column(name = "email") // 對應資料庫的欄位名稱
    private String email;

    @Column(name = "status") // 新增的欄位
    private String status;

    public Teacher() {
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() { // 新增的 getter 方法
        return status;
    }

    public void setStatus(String status) { // 新增的 setter 方法
        this.status = status;
    }

    // 更新 toString 方法
    @Override
    public String toString() {
        return "Teacher{" +
                "teacherId=" + teacherId +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}