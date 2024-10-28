package com.example.demo.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Leave;


import java.time.LocalDateTime;
import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Integer> {

    // 根據 studentId 查找多條請假記錄
    List<Leave> findByStudentid(String studentid);

    Leave findByLeaveid(Integer leaveid); // 將參數類型改為 Integer，返回單個 Leave

    // 統計某個 studentId 的請假記錄數量
    @Query("SELECT COUNT(l) FROM Leave l WHERE l.studentid = :studentid")
    long countByStudentid(@Param("studentid") String studentid);

    // 根據時間範圍查詢請假記錄（使用 startTime 和 endTime 字段）
    List<Leave> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    
    List<Leave> findByCheckstatus(String checkstatus);
    List<Leave> findByCheckstatusIsNull();
}
