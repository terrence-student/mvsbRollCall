package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "`leave`") // 确保表名与数据库一致
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int leaveid;  // 主键

    private String name; 
   
    private String studentid; // 學號

    
    private String status;  

    @Column(name = "start_time") // 映射数据库中的請假開始時間
    private LocalDateTime startTime; // 請假開始時間
    
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime; // 請假結束時間
    
    private String checkstatus;
    
    public Leave() {
    }

	public Leave(int leaveid, String name, String studentid, String status, LocalDateTime startTime,
			LocalDateTime endTime, String checkstatus) {
		super();
		this.leaveid = leaveid;
		this.name = name;
		this.studentid = studentid;
		this.status = status;
		this.startTime = startTime;
		this.endTime = endTime;
		this.checkstatus = checkstatus;
	}

	public int getLeaveid() {
		return leaveid;
	}

	public void setLeaveid(int leaveid) {
		this.leaveid = leaveid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStudentid() {
		return studentid;
	}

	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public String getCheckstatus() {
		return checkstatus;
	}

	public void setCheckstatus(String checkstatus) {
		this.checkstatus = checkstatus;
	}
    
}
    