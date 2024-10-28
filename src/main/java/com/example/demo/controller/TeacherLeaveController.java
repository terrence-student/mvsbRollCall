package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.DAO.LeaveRepository;
import com.example.demo.model.Homework;
import com.example.demo.model.Leave;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/teacherleaves")
public class TeacherLeaveController {

    @Autowired
    private LeaveRepository leaveRepository;

    // 處理無效的請假狀態
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body("請假無法新增：" + e.getMessage());
    }

    // 1. 查詢所有請假記錄
    @GetMapping("/all")
    public List<Leave> getAllLeaves() {
        return leaveRepository.findAll();
    }

    // 2. 根據 leaveid 查詢單條請假記錄
    @GetMapping("/{leaveid}")
    public ResponseEntity<Leave> getLeaveById(@PathVariable int leaveid) {
        Optional<Leave> leave = leaveRepository.findById(leaveid);
        return leave.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3. 根據 studentId 查詢多條請假記錄
    @GetMapping("/student/{studentid}")
    public List<Leave> getLeavesByStudentId(@PathVariable String studentid) {
        return leaveRepository.findByStudentid(studentid);
    }

   
    // 5. 統計某個 studentId 的請假記錄數量
    @GetMapping("/count/{studentid}")
    public long countLeavesByStudentId(@PathVariable String studentid) {
        return leaveRepository.countByStudentid(studentid);
    }

   

    
    @GetMapping("/time-range")
    public ResponseEntity<List<Leave>> getLeavesByTimeRange(
        @RequestParam("starttime") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime starttime, 
        @RequestParam("endtime") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime endtime) {
    	System.out.println("starttime:"+starttime);
    	System.out.println("endtime:"+endtime);
        List<Leave> leaveList = leaveRepository.findByStartTimeBetween(starttime, endtime);
        
        return ResponseEntity.ok(leaveList);
    }
    
    

 // 8. 更新已有的請假記錄
    @PutMapping("/update/{leaveid}")
    public ResponseEntity<Leave> updateLeave(@PathVariable int leaveid, @RequestParam String checkstatus) {
        Optional<Leave> optionalLeave = leaveRepository.findById(leaveid);
        if (optionalLeave.isPresent()) {
            Leave leave = optionalLeave.get();
            leave.setCheckstatus(checkstatus); // 更新狀態為從請求中取得的值

            // 如果需要更新其他字段，可以在此處添加相應的邏輯

            Leave updatedLeave = leaveRepository.save(leave);
            return ResponseEntity.ok(updatedLeave);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // 9. 刪除請假記錄
    @DeleteMapping("/delete/{leaveid}")
    public ResponseEntity<Void> deleteLeave(@PathVariable int leaveid) {
        Optional<Leave> optionalLeave = leaveRepository.findById(leaveid);
        if (optionalLeave.isPresent()) {
            leaveRepository.deleteById(leaveid);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // 根據審核狀態獲取請假記錄
    @GetMapping("/checkstatus/{checkstatus}")
    public List<Leave> getLeavesByCheckstatus(@PathVariable(required = false) String checkstatus) {
        if ("null".equals(checkstatus)) {
            return leaveRepository.findByCheckstatusIsNull(); // 查詢 checkstatus 為 null 的記錄
        }
        return leaveRepository.findByCheckstatus(checkstatus); // 查詢特定狀態的請假記錄
    }
    
    
    
}
