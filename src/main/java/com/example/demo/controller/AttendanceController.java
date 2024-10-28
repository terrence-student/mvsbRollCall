package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.DAO.StudentRepository;
import com.example.demo.model.Attendance;
import com.example.demo.model.Student;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.controller.*;


import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin() 
public class AttendanceController {
	private static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);

    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private StudentRepository studentAccount;

    // 保留的現有方法：獲取所有點名記錄
    @GetMapping("/all")
    public List<Attendance> getAllAttendance() {
        return attendanceService.getAllAttendance();
    }
    //根據username查詢單一學生資料
    @GetMapping(value="/getSingleStudentData")
    public List<Student>getStudentData(@RequestParam String username){ 
    	List<Student> student=studentAccount.findByUsername(username);
		return student;
    }
    

    // 保留的現有方法：學生報到
    @PostMapping("/report")
    public ResponseEntity<?> reportAttendance(@RequestBody Attendance attendance) {
        if (attendance.getStudentId() == null) {
            return ResponseEntity.badRequest().body("學生ID不能為空");
        }

        try {
            Attendance savedAttendance = attendanceService.saveAttendance(attendance);
            return ResponseEntity.ok(savedAttendance);
        } catch (Exception e) {
            logger.error("Error saving attendance: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("報到失敗");
        }
    }
    // 擴充功能：開始點名
    @PostMapping("/start")
    public ResponseEntity<Map<String, String>> startAttendance(@RequestBody Map<String, String> request) {
        String teacherIp = request.get("teacherIp");
        logger.info("老師 IP: {} 點名已啟動", teacherIp);

        attendanceService.startAttendance(teacherIp);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "點名已啟動");
        response.put("teacherIp", teacherIp);
        return ResponseEntity.ok(response);
    }

    // 擴充功能：結束點名
    @PostMapping("/end")
    public ResponseEntity<String> endAttendance() {
        logger.info("結束點名");
        attendanceService.endAttendance();
        return ResponseEntity.ok("點名已結束");
    }

    // 擴充功能：重設點名狀態
    @PostMapping("/reset")
    public ResponseEntity<String> resetAttendance() {
        logger.info("重設點名狀態");
        attendanceService.resetAttendance();
        return ResponseEntity.ok("點名狀態已重置");
    }

    // 擴充功能：查詢已點名學生列表
    @GetMapping("/attendance-list")
    public ResponseEntity<List<Attendance>> getAttendanceList() {
        logger.info("查詢已點名學生記錄");
        List<Attendance> attendanceList = attendanceService.getAllAttendance();
        if (attendanceList.isEmpty()) {
            logger.warn("沒有已點名的學生記錄");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(attendanceList);
    }
     // 清空已點名學生資料
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearAttendance() {
        attendanceService.clearAllAttendances(); // 這裡是服務層的方法，負責刪除所有記錄
        return ResponseEntity.ok("所有點名記錄已清空");
    }

    // 刪除點名資料的 API
    @DeleteMapping("/delete/{attendanceid}")
    public ResponseEntity<String> deleteAttendance(@PathVariable int attendanceid) {
        boolean isDeleted = attendanceService.deleteAttendanceById(attendanceid);

        if (isDeleted) {
            return ResponseEntity.ok("點名資料刪除成功");
        } else {
            return ResponseEntity.status(404).body("找不到該點名資料");
        }
    }
}
