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
@Table(name="homework")
public class Homework  {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int homeworkid;
	
	private int teacherid;

	
	private String  title;
	
	private String classname;
	
	@Temporal(TemporalType.DATE)
	private Date due_date;//資料庫型態:DATE
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date createdAt;  // 確保屬性名稱一致
	
	private String homeworkcontent;

	
	public Homework() {}


	public Homework(int homeworkid, int teacherid, String title, String classname, Date due_date, Date created_at,
			String homeworkcontent) {
		super();
		this.homeworkid = homeworkid;
		this.teacherid = teacherid;
		this.title = title;
		this.classname = classname;
		this.due_date = due_date;
		this.createdAt = created_at;
		this.homeworkcontent = homeworkcontent;
	}


	public int getHomeworkid() {
		return homeworkid;
	}


	public void setHomeworkid(int homeworkid) {
		this.homeworkid = homeworkid;
	}


	public int getTeacherid() {
		return teacherid;
	}


	public void setTeacherid(int teacherid) {
		this.teacherid = teacherid;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getClassname() {
		return classname;
	}


	public void setClassname(String classname) {
		this.classname = classname;
	}


	public Date getDue_date() {
		return due_date;
	}


	public void setDue_date(Date due_date) {
		this.due_date = due_date;
	}


	public Date getCreated_at() {
		return createdAt;
	}


	public void setCreated_at(Date created_at) {
		this.createdAt = created_at;
	}


	public String getHomeworkcontent() {
		return homeworkcontent;
	}


	public void setHomeworkcontent(String homeworkcontent) {
		this.homeworkcontent = homeworkcontent;
	}
	
}