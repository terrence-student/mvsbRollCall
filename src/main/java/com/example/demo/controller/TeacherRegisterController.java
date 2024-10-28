package com.example.demo.controller;

import com.example.demo.DAO.TeacherRegisterRepository;
import com.example.demo.model.TeacherRegister;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/teachers")
public class TeacherRegisterController {

    @Autowired
    private TeacherRegisterRepository teacherRegisterRepository;

    // 創建新的教師
    @PostMapping("/add")
    public ResponseEntity<String> createTeacher(@RequestBody TeacherRegister teacher) {
        // 檢查是否已存在相同的用戶名或電子郵件
        if (teacherRegisterRepository.existsByUsername(teacher.getUsername())) {
            return new ResponseEntity<>("用戶名已存在", HttpStatus.CONFLICT);
        }
        if (teacherRegisterRepository.existsByEmail(teacher.getEmail())) {
            return new ResponseEntity<>("電子郵件已存在", HttpStatus.CONFLICT);
        }
        teacher.setStatus("-1");
        TeacherRegister savedTeacher = teacherRegisterRepository.save(teacher);
        return new ResponseEntity<>("註冊成功", HttpStatus.CREATED);
    }

    // 獲取所有教師
    @GetMapping("/get")
    public ResponseEntity<List<TeacherRegister>> getAllTeachers() {
        List<TeacherRegister> teachers = teacherRegisterRepository.findAll();
        return new ResponseEntity<>(teachers, HttpStatus.OK);
    }

    // 獲取指定教師
    @GetMapping("/{id}")
    public ResponseEntity<TeacherRegister> getTeacherById(@PathVariable Long id) {
        Optional<TeacherRegister> teacher = teacherRegisterRepository.findById(id);
        return teacher.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 更新教師資料
    @PutMapping("/{id}")
    public ResponseEntity<TeacherRegister> updateTeacher(@PathVariable Long id, @RequestBody TeacherRegister teacherDetails) {
        Optional<TeacherRegister> optionalTeacher = teacherRegisterRepository.findById(id);
        if (optionalTeacher.isPresent()) {
            TeacherRegister teacher = optionalTeacher.get();
            teacher.setName(teacherDetails.getName());
            teacher.setUsername(teacherDetails.getUsername());
            teacher.setPassword(teacherDetails.getPassword());
            teacher.setEmail(teacherDetails.getEmail());
            TeacherRegister updatedTeacher = teacherRegisterRepository.save(teacher);
            return ResponseEntity.ok(updatedTeacher);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 刪除教師
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        if (teacherRegisterRepository.existsById(id)) {
            teacherRegisterRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
