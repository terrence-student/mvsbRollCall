package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.DAO.BoardRepository;
import com.example.demo.model.Board;
import com.example.demo.model.Homework;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/boardlist")
@CrossOrigin
public class ManageBoardController {

    @Autowired
    private BoardRepository boardRepository;

    // 新增公告
    @PostMapping("/add")
    public ResponseEntity<?> createBoard(@RequestBody Board board) {
        try {
            if (board.getTitle() == null || board.getTitle().isEmpty() ||
                board.getContent() == null || board.getContent().isEmpty() ||
                board.getTeacherid() == null || board.getTeacherid() <= 0) {
                return new ResponseEntity<>("標題、內容和老師ID為必填欄位，且老師ID必須大於0。", HttpStatus.BAD_REQUEST);
            }

            if (board.getTime() == null) {
                board.setTime(new Date());
            }

            Board savedBoard = boardRepository.save(board);
            return new ResponseEntity<>(savedBoard, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("伺服器內部錯誤，請稍後再試。", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 顯示所有公告
    @GetMapping("/all")
    public ResponseEntity<?> getAllBoards() {
        try {
            List<Board> boards = boardRepository.findAll();
            if (boards.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(boards, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("伺服器內部錯誤，請稍後再試。", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 根據ID取得單一公告
    @GetMapping("/{id}")
    public ResponseEntity<?> getBoardById(@PathVariable("id") int id) {
        try {
            Optional<Board> boardData = boardRepository.findById(id);
            if (boardData.isPresent()) {
                return new ResponseEntity<>(boardData.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("找不到該公告。", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("伺服器內部錯誤，請稍後再試。", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 修改公告
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBoard(@PathVariable("id") Integer id, @RequestBody Board board) {
        try {
            Optional<Board> boardData = boardRepository.findById(id);
            if (boardData.isPresent()) {
                Board existingBoard = boardData.get();
                existingBoard.setContent(board.getContent());
                existingBoard.setTeacherid(board.getTeacherid());
                existingBoard.setTime(board.getTime());
                existingBoard.setTitle(board.getTitle());
                return new ResponseEntity<>(boardRepository.save(existingBoard), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("找不到該公告，無法更新。", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("伺服器內部錯誤，請稍後再試。", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

   

    // 刪除公告
    @DeleteMapping("/{boardid}")
    public ResponseEntity<?> deleteBoard(@PathVariable("boardid") int boardid) {
        try {
            if (!boardRepository.existsById(boardid)) {
                return new ResponseEntity<>("找不到該公告，無法刪除。", HttpStatus.NOT_FOUND);
            }
            boardRepository.deleteById(boardid);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("伺服器內部錯誤，無法刪除公告。", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
