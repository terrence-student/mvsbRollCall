package com.example.demo.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


@Entity
@Table(name="homework_student")
public class HomeworkforStudent  {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	
	@Column(name = "homework_accountid")
	private int homeworkAccountid;
	private int homeworkid;
	private String teachername;
	private String classname;
	private String title;
	private String homeworkcontent;
	
	@Column(name = "submitted_at")  // 假設資料庫欄位名稱是 submittedAt
	@Temporal(TemporalType.TIMESTAMP)
	private Date submittedAt;
	
	private String studentid;
	private String name;
	private int grade;
	private String homework;
	private int score;
	public HomeworkforStudent(){}
	public HomeworkforStudent(int homeworkAccountid, int homeworkid, String teachername, String classname, String title,
			String homeworkcontent, Date submittedAt, String studentid, String name, int grade, String homework,
			int score) {
		super();
		this.homeworkAccountid = homeworkAccountid;
		this.homeworkid = homeworkid;
		this.teachername = teachername;
		this.classname = classname;
		this.title = title;
		this.homeworkcontent = homeworkcontent;
		this.submittedAt = submittedAt;
		this.studentid = studentid;
		this.name = name;
		this.grade = grade;
		this.homework = homework;
		this.score = score;
	}
	public int getHomeworkAccountid() {
		return homeworkAccountid;
	}
	public void setHomeworkAccountid(int homeworkAccountid) {
		this.homeworkAccountid = homeworkAccountid;
	}
	public int getHomeworkid() {
		return homeworkid;
	}
	public void setHomeworkid(int homeworkid) {
		this.homeworkid = homeworkid;
	}
	public String getTeachername() {
		return teachername;
	}
	public void setTeachername(String teachername) {
		this.teachername = teachername;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getHomeworkcontent() {
		return homeworkcontent;
	}
	public void setHomeworkcontent(String homeworkcontent) {
		this.homeworkcontent = homeworkcontent;
	}
	public Date getSubmittedAt() {
		return submittedAt;
	}
	public void setSubmittedAt(Date submittedAt) {
		this.submittedAt = submittedAt;
	}
	public String getStudentid() {
		return studentid;
	}
	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public String getHomework() {
		return homework;
	}
	public void setHomework(String homework) {
		this.homework = homework;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}

}