package com.example.demo.controller;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DAO.HomeworkRepository;
import com.example.demo.DAO.TeacherRepository;
import com.example.demo.model.Homework;
import com.example.demo.model.Teacher;

@RestController
@RequestMapping("/homework")
public class HomeworkController {

    @Autowired
    private HomeworkRepository homeworkRepository;
    
    @Autowired
    private TeacherRepository teacherAccount;  // 正確注入 TeacherRepository

    // 根據 username 查詢單一老師資料
    @GetMapping(value = "/teachers/getSingleTeacherData")
    public List<Teacher> getSingleTeacherData(@RequestParam String username) {
        List<Teacher> teacher = teacherAccount.findByUsername(username);
        Teacher teacher2 = teacher.get(0); // 獲取第一個老師的資料
        return teacher;
    }

    // 查詢所有作業
    @GetMapping("/all")
    public ResponseEntity<List<Homework>> getAllHomework() {
        List<Homework> homeworkList = homeworkRepository.findAll();
        Homework h=homeworkList.get(0);
        System.out.println(h.getClassname());
        return ResponseEntity.ok(homeworkList); // 返回 200 OK 和作業列表
    }

    // 新增作業
    @PostMapping("/add")
    public ResponseEntity<?> addHomework(@RequestBody Homework homework) {
        try {
            if (homework.getTitle() == null || homework.getTitle().isEmpty()) {
                return ResponseEntity.badRequest().body("標題不能為空"); // 返回 400 錯誤
            }

            homework.setCreated_at(new java.util.Date());
           
            System.out.println(homework.getClassname());
            Homework savedHomework = homeworkRepository.save(homework);
            return ResponseEntity.status(201).body(savedHomework);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("伺服器內部錯誤: " + e.getMessage());
        }
    }
    
 // 修改作業
    @PutMapping("/{id}")
    public ResponseEntity<?> updateHomework(@PathVariable Integer id, @RequestBody Homework homework) {
        Optional<Homework> existingHomework = homeworkRepository.findById(id);
        
        if (!existingHomework.isPresent()) {
            return ResponseEntity.notFound().build(); // 返回 404 錯誤
        }
        
        Homework updatedHomework = existingHomework.get();
        updatedHomework.setTitle(homework.getTitle());
        updatedHomework.setHomeworkcontent(homework.getHomeworkcontent());
        updatedHomework.setDue_date(homework.getDue_date()); // 確保有正確的字段名
        updatedHomework.setClassname(homework.getClassname());
        homeworkRepository.save(updatedHomework); // 保存更新的作業
        return ResponseEntity.ok(updatedHomework); // 返回更新後的作業資料
    }
    
 // 刪除作業
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHomework(@PathVariable Integer id) {
        Optional<Homework> existingHomework = homeworkRepository.findById(id);
        
        if (!existingHomework.isPresent()) {
            return ResponseEntity.notFound().build(); // 返回 404 錯誤
        }

        homeworkRepository.deleteById(id); // 刪除作業
        return ResponseEntity.ok("作業已刪除"); // 返回成功消息
    }
    
    // 根據關鍵字查詢作業標題
    @GetMapping("/search")
    public ResponseEntity<List<Homework>> searchHomeworkByTitle(@RequestParam String keyword) {
        List<Homework> homeworkList = homeworkRepository.findByTitleContaining(keyword);
        return ResponseEntity.ok(homeworkList);
    }
    @GetMapping("/filterByDate")
    public ResponseEntity<List<Homework>> filterHomeworkByDate(
        @RequestParam("starttime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date starttime, 
        @RequestParam("endtime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endtime) {
    	System.out.println("starttime:"+starttime);
    	System.out.println("endtime:"+endtime);
        List<Homework> homeworkList = homeworkRepository.findByCreatedAtBetween(starttime, endtime);
        
        return ResponseEntity.ok(homeworkList);
    }
    
    
 
    
    
    
    
    
    
}

