package com.example.demo.model;


import jakarta.persistence.*;


@Entity
@Table(name="student")
public class Student  {
 @Id
 
 @Column(name="studentid")
 private String studentid;

 @Column(name="classname")
 private String class_;

 private String email;

 private int grade;

 private String major;

 private String name;

 private String password;

 private String username;

 public Student() {
 }
 
 public Student(String studentid, String class_, String email, int grade, String major, String name, String password,
   String username) {
  super();
  this.studentid = studentid;
  this.class_ = class_;
  this.email = email;
  this.grade = grade;
  this.major = major;
  this.name = name;
  this.password = password;
  this.username = username;
 }

 public String getStudentid() {
  return this.studentid;
 }

 public void setStudentid(String studentid) {
  this.studentid = studentid;
 }

 public String getClass_() {
  return this.class_;
 }

 public void setClass_(String class_) {
  this.class_ = class_;
 }

 public String getEmail() {
  return this.email;
 }

 public void setEmail(String email) {
  this.email = email;
 }

 public int getGrade() {
  return this.grade;
 }

 public void setGrade(int grade) {
  this.grade = grade;
 }

 public String getMajor() {
  return this.major;
 }

 public void setMajor(String major) {
  this.major = major;
 }

 public String getName() {
  return this.name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public String getPassword() {
  return this.password;
 }

 public void setPassword(String password) {
  this.password = password;
 }

 public String getUsername() {
  return this.username;
 }

 public void setUsername(String username) {
  this.username = username;
 }

 


}