package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.DAO.StudentRepository;
import com.example.demo.model.Student;

@RestController
@RequestMapping("/api/studentlist")
@CrossOrigin()
public class ManageStudentController {
    private static final Logger logger = LoggerFactory.getLogger(ManageStudentController.class);

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/id/{studentid}")
    public ResponseEntity<Student> getStudentById(@PathVariable String studentid) {
        logger.info("查詢學號為 {} 的學生資料", studentid);
        Optional<Student> student = studentRepository.findById(studentid);
        return student.map(ResponseEntity::ok)
                      .orElseGet(() -> {
                          logger.warn("學號為 {} 的學生記錄不存在", studentid);
                          return ResponseEntity.notFound().build();
                      });
    }

    @PostMapping("/add")
    public ResponseEntity<?> addStudent(@RequestBody Student student) {
        logger.info("添加學生記錄：{}", student);
        try {
            Student newStudent = studentRepository.save(student);
            logger.info("成功添加學生記錄：{}", newStudent);
            return ResponseEntity.status(HttpStatus.CREATED).body(newStudent);
        } catch (Exception e) {
            logger.error("添加學生時發生錯誤: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("添加學生失敗，請檢查輸入數據或稍後再試。");
        }
    }

    @DeleteMapping("/id/{studentid}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String studentid) {
        logger.info("刪除學號為 {} 的學生記錄", studentid);
        Optional<Student> student = studentRepository.findById(studentid);
        if (student.isPresent()) {
            studentRepository.delete(student.get());
            logger.info("成功刪除學號為 {} 的學生記錄", studentid);
            return ResponseEntity.ok().build();
        } else {
            logger.warn("學號為 {} 的學生記錄不存在，無法刪除", studentid);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAllStudents() {
        logger.info("查詢所有學生");
        List<Student> students = studentRepository.findAll();
        if (students.isEmpty()) {
            logger.warn("沒有學生");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
    }

    @PutMapping("/id/{studentid}")
    public ResponseEntity<?> updateStudent(@PathVariable String studentid, @RequestBody Student student) {
        logger.info("查詢學號為 '{}' 的學生資料", studentid.trim());
        Optional<Student> existingStudent = studentRepository.findById(studentid);
        if (existingStudent.isPresent()) {
            Student updatedStudent = existingStudent.get();
            updatedStudent.setName(student.getName());
            updatedStudent.setUsername(student.getUsername());
            updatedStudent.setEmail(student.getEmail());
            updatedStudent.setGrade(student.getGrade());
            updatedStudent.setMajor(student.getMajor());
            updatedStudent.setPassword(student.getPassword());
            updatedStudent.setClass_(student.getClass_());
            
            try {
                studentRepository.save(updatedStudent);
                logger.info("成功修改學號為 {} 的學生記錄", studentid);
                return ResponseEntity.ok(updatedStudent);
            } catch (Exception e) {
                logger.error("修改學生時發生錯誤: {}", e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("修改學生失敗，請檢查輸入數據或稍後再試。");
            }
        } else {
            logger.warn("學號為 {} 的學生記錄不存在，無法修改", studentid);
            return ResponseEntity.notFound().build();
        }
    }
}