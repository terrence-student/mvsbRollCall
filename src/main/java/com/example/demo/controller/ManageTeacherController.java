package com.example.demo.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.DAO.TeacherRepository;
import com.example.demo.model.Teacher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/teacherlist")
@CrossOrigin()
public class ManageTeacherController {

    private static final Logger logger = LoggerFactory.getLogger(ManageTeacherController.class);

    @Autowired
    private TeacherRepository teacherRepository;

    // 查詢所有老師資料
    @GetMapping("/all")
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        logger.info("查詢所有老師資料");
        List<Teacher> teachers = teacherRepository.findAll();
        if (teachers.isEmpty()) {
            logger.warn("沒有老師資料");
            return ResponseEntity.notFound().build();
        }
        logger.info("找到 {} 名老師", teachers.size());
        return ResponseEntity.ok(teachers);
    }

    // 根據ID查詢老師資料
    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {
        logger.info("查詢ID為 {} 的老師資料", id);

        Optional<Teacher> teacher = teacherRepository.findById(id);
        if (teacher.isPresent()) {
            logger.info("找到ID為 {} 的老師資料", id);
            return new ResponseEntity<>(teacher.get(), HttpStatus.OK);
        } else {
            logger.warn("ID為 {} 的老師記錄不存在", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 根據姓名查詢老師資料
    @GetMapping("/name/{name}")
    public ResponseEntity<List<Teacher>> getTeachersByName(@PathVariable String name) {
        logger.info("查詢姓名為 {} 的老師資料", name);
        List<Teacher> teachers = teacherRepository.findByNameIgnoreCase(name);
        if (teachers.isEmpty()) {
            logger.warn("姓名為 {} 的老師記錄不存在", name);
            return ResponseEntity.notFound().build();
        }
        logger.info("找到 {} 名姓名為 {} 的老師", teachers.size(), name);
        return ResponseEntity.ok(teachers);
    }

    // 新增老師資料
    @PostMapping("/add")
    public ResponseEntity<?> createTeacher(@RequestBody Teacher teacher) {
        logger.info("添加老師記錄：{}", teacher);

        try {
            Teacher newTeacher = teacherRepository.save(teacher);
            logger.info("成功添加老師記錄：{}", newTeacher);
            return ResponseEntity.status(HttpStatus.CREATED).body(newTeacher);
        } catch (Exception e) {
            logger.error("添加老師時發生錯誤: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("添加老師失敗，請檢查輸入數據或稍後再試。");
        }
    }

    // 修改老師資料
    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
        logger.info("修改ID為 {} 的老師資料", id);

        Optional<Teacher> existingTeacher = teacherRepository.findById(id);
        if (existingTeacher.isPresent()) {
            Teacher updatedTeacher = existingTeacher.get();
            updatedTeacher.setName(teacher.getName());
            updatedTeacher.setUsername(teacher.getUsername());
            updatedTeacher.setPassword(teacher.getPassword());
            updatedTeacher.setEmail(teacher.getEmail());
            teacherRepository.save(updatedTeacher);
            logger.info("成功更新ID為 {} 的老師資料", id);
            return new ResponseEntity<>(updatedTeacher, HttpStatus.OK);
        } else {
            logger.warn("ID為 {} 的老師記錄不存在，無法更新", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 刪除老師資料
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        logger.info("刪除ID為 {} 的老師記錄", id);

        Optional<Teacher> teacher = teacherRepository.findById(id);
        if (teacher.isPresent()) {
            teacherRepository.delete(teacher.get());
            logger.info("成功刪除ID為 {} 的老師記錄", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            logger.warn("ID為 {} 的老師記錄不存在，無法刪除", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}