package com.example.demo.controller;


import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.DAO.BoardRepository;

import com.example.demo.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/studentboard")
@CrossOrigin()
public class StudentBoardController {

    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    private BoardRepository boardRepository;

    // 顯示所有記錄
    @GetMapping("/all")
    public ResponseEntity<List<Board>> getAllBoards() {
        logger.info("查詢所有記錄");
        List<Board> boards = boardRepository.findAll();
        if (boards.isEmpty()) {
            logger.warn("沒有記錄");
            return ResponseEntity.notFound().build();
        }
        logger.info("找到 {} 條記錄", boards.size());
        return ResponseEntity.ok(boards);
    }

    // 根據教師 ID 查詢 Board 數據
    @GetMapping("/teacher/{teacherid}")
    public ResponseEntity<List<Board>> getBoardsByTeacherId(@PathVariable Long teacherid) {
        logger.info("查詢教師 ID 為 {} 的記錄", teacherid);
        List<Board> boardList = boardRepository.findByTeacherid(teacherid);
        if (boardList.isEmpty()) {
            logger.warn("教師 ID 為 {} 的記錄不存在", teacherid);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(boardList);
    }

    // 根據標題查詢 Board 數據
    @GetMapping("/title/{title}")
    public ResponseEntity<List<Board>> getBoardsByTitle(@PathVariable String title) {
        logger.info("查詢標題為 {} 的記錄", title);
        List<Board> boards = boardRepository.findByTitle(title);
        if (boards.isEmpty()) {
            logger.warn("標題為 {} 的記錄不存在", title);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(boards);
    }

    // 根據 teacherid 統計記錄數量
    @GetMapping("/count/{teacherid}")
    public ResponseEntity<Long> countBoardByTeacherid(@PathVariable Long teacherid) {
        logger.info("統計 teacherid 為 {} 的記錄數量", teacherid);
        Long count = boardRepository.countByTeacherid(teacherid);
        return ResponseEntity.ok(count);
    }

    // 根據時間範圍查詢 Board 數據
    @GetMapping("/timerange")
    public ResponseEntity<List<Board>> getBoardsByTimeRange(
            @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        logger.info("查詢時間範圍 {} 到 {} 的記錄", startTime, endTime);
        List<Board> boards = boardRepository.findByTimeBetween(startTime, endTime);
        if (boards.isEmpty()) {
            logger.warn("時間範圍 {} 到 {} 的記錄不存在", startTime, endTime);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(boards);
    }

   
    @GetMapping("/title/announcement")
    public ResponseEntity<List<Board>> getBoardsByTitleContaining() {
        String keyword = "公佈欄"; // 要查詢的字樣
        logger.info("查詢標題中包含 {} 的記錄", keyword);
        List<Board> boards = boardRepository.findByTitleContaining(keyword);
        if (boards.isEmpty()) {
            logger.warn("沒有標題中包含 {} 的記錄", keyword);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(boards);
    }

}


