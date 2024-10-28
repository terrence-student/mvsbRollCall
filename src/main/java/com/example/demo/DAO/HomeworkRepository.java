package com.example.demo.DAO;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Homework;

public interface HomeworkRepository extends JpaRepository<Homework, Integer> {
	

    // 根據你的需求來修改這個方法，確保與 Homework 實體中的屬性名稱一致
    List<Homework> findByHomeworkidAndTitle(Integer homeworkid, String title);
    
    List<Homework> findByTitleContaining(String keyword);
    
 // 新增根據 created_at 篩選作業的查詢方法
    List<Homework> findByCreatedAtBetween(Date starttime, Date endtime);
    
    //homework表用key找
    List<Homework> findByHomeworkid(Integer homeworkid);
}