package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.DAO.LeaveRepository;
import com.example.demo.model.Leave;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

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

    @PostMapping("/addleave")
    public ResponseEntity<Leave> createLeave(@RequestBody Leave leave) {
        // 儲存假單
        Leave savedLeave = leaveRepository.save(leave);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLeave);
    }

    // 根據時間範圍獲取請假記錄
    @GetMapping("/time-range")
    public List<Leave> getLeavesByTimeRange(@RequestParam String start, @RequestParam String end) {
        LocalDateTime startTime = LocalDateTime.parse(start);
        LocalDateTime endTime = LocalDateTime.parse(end);
        return leaveRepository.findByStartTimeBetween(startTime, endTime);
    }

    // 7. 更新已有的請假記錄
    @PutMapping("/update/{leaveid}")
    public ResponseEntity<Leave> updateLeave(@PathVariable int leaveid, @RequestBody Leave leaveDetails) {
        Optional<Leave> optionalLeave = leaveRepository.findById(leaveid);
        if (optionalLeave.isPresent()) {
            Leave leave = optionalLeave.get();
            leave.setName(leaveDetails.getName());
            leave.setStudentid(leaveDetails.getStudentid());
            leave.setStatus(leaveDetails.getStatus()); // 更新狀態
            leave.setStartTime(leaveDetails.getStartTime()); // 更新開始時間
            leave.setEndTime(leaveDetails.getEndTime()); // 更新結束時間
            leave.setCheckstatus(null);
            Leave updatedLeave = leaveRepository.save(leave);
            return ResponseEntity.ok(updatedLeave);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

 // 8. 刪除請假記錄
    @DeleteMapping("/delete/{leaveid}")
    public ResponseEntity<Void> deleteLeave(@PathVariable int leaveid) {
        Optional<Leave> optionalLeave = leaveRepository.findById(leaveid);
        if (optionalLeave.isPresent()) {
            leaveRepository.deleteById(leaveid);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
