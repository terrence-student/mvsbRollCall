package com.example.demo.DAO;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Board;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    // 根據 teacherId 查找多個 Board
    List<Board> findByTeacherid(Long teacherid); 

    // 根據標題查找多個 Board
    List<Board> findByTitle(String title);

    // 統計特定 boardid 的記錄數量
    Long countByTeacherid(Long teacherid); 

    // 根據時間範圍查找多個 Board
    List<Board> findByTimeBetween(Date startTime, Date endTime); 

    // 根據標題中包含特定字樣查找多個 Board（使用 BINARY）
    @Query(value = "SELECT * FROM board WHERE BINARY title LIKE CONCAT('%', :titlePart, '%')", nativeQuery = true)
    List<Board> findByTitleContaining(@Param("titlePart") String titlePart); // 給首頁用的
}
