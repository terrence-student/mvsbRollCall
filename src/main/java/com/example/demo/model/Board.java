package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "board")
public class Board implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int boardid;

	private String content;

	@Column(name = "teacherid", columnDefinition = "INT")
    private Long teacherid;

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	private Date time;

	private String title;

	public Board() {
	}

	public int getBoardid() {
		return this.boardid;
	}

	public void setBoardid(int boardid) {
		this.boardid = boardid;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getTeacherid() {
		return this.teacherid;
	}

	public void setTeacherid(Long teacherid) {
		this.teacherid = teacherid;
	}

	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
