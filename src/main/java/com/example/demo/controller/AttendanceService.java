package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DAO.AttendanceRepository;
import com.example.demo.model.Attendance;

@Service
public class AttendanceService {
    
    @Autowired
    private AttendanceRepository attendanceRepository;
    
    private String teacherIp;
    private long startTime;

    // 獲取所有點名記錄
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    // 根據ID獲取點名記錄
    public Optional<Attendance> getAttendanceById(Integer id) {
        return attendanceRepository.findById(id);
    }

    // 處理學生報到
    public Attendance processAttendance(Attendance attendance, String studentIp) {
        if (teacherIp == null || startTime == 0) {
            throw new IllegalStateException("老師尚未開啟點名，無法報到。");
        }

        long currentTime = System.currentTimeMillis();
        long elapsedMinutes = (currentTime - startTime) / 60000;

        // 根據時間和IP來設置狀態
        if (!studentIp.equals(teacherIp)) {
            throw new IllegalArgumentException("學生IP與老師IP不匹配，報到無效。");
        }

        if (elapsedMinutes <= 3) {
            attendance.setStatus(Attendance.AttendanceStatus.出席);
        } else if (elapsedMinutes <= 5) {
            attendance.setStatus(Attendance.AttendanceStatus.遲到);
        } else {
            attendance.setStatus(Attendance.AttendanceStatus.缺席);
        }

        return attendanceRepository.save(attendance);
    }

    // 開始點名
    public void startAttendance(String ip) {
        this.teacherIp = ip;
        this.startTime = System.currentTimeMillis();
    }

    // 結束點名
    public void endAttendance() {
        this.teacherIp = null;
        this.startTime = 0;
    }

    // 重設點名狀態
    public void resetAttendance() {
        this.teacherIp = null;
        this.startTime = 0;
    }

    // 獲取老師的IP
    public String getTeacherIp() {
        return this.teacherIp;
    }
    
    // 清空所有點名記錄
    public Attendance saveAttendance(Attendance attendance) {
        
        return attendanceRepository.save(attendance);
    }
    public void clearAllAttendances() {
        attendanceRepository.deleteAll();
    }
    
    // 刪除點名資料
    public boolean deleteAttendanceById(int attendanceid) {
        if (attendanceRepository.existsById(attendanceid)) {
            attendanceRepository.deleteById(attendanceid);
            return true;
        }
        return false; // 如果找不到該 ID，返回 false
    }
}

  
