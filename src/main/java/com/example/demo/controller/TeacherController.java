package com.example.demo.controller;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import com.example.demo.DAO.TeacherRepository;
import com.example.demo.model.Student;
import com.example.demo.model.Teacher;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
public class TeacherController {
	 @Autowired
	    private TeacherRepository teacherAccount;	
	
	 @Autowired
	    private JavaMailSender mailSender; // 用於發送郵件的服務

	    private String generatedVerificationCode; // 儲存生成的驗證碼
	 
	 @GetMapping(value = "/teachers/getSingleTeacherData")
	 public List<Teacher> getSingleTeacherData(@RequestParam String username) {
	     List<Teacher> teacher = teacherAccount.findByUsername(username);
	     return teacher;
	 }
	 
	 
	 
	 @GetMapping(value = "/Manager/login")
	 public String managerLogin(@RequestParam String username, @RequestParam String password) {
	     String status = teacherAccount.findStatusByUsernameAndPassword(username, password);

	     if (status == null) {
	         return "Unauthorized";  // 當帳號或密碼不正確時，返回適當的錯誤訊息
	     }
	     return status;
	 }
	 
	 
	 @RequestMapping(value = "/teachers/login", method = RequestMethod.POST)
	 public ResponseEntity<String> login(@RequestBody Teacher loginRequest) {
	     String username = loginRequest.getUsername();
	     String password = loginRequest.getPassword();
	     // 查找用戶名和密碼是否匹配
	        List<Teacher> teachers = teacherAccount.findByUsernameAndPassword(username, password);
	        
	        // 檢查是否存在此用戶
	        if (!teachers.isEmpty()){ 
	        	Teacher foundTeacher = teachers.get(0); 
            
            // 驗證密碼是否正確
            if (foundTeacher.getPassword().equals(password)) {
                // 密碼正確，返回成功響應
                return new ResponseEntity<>("Login successful", HttpStatus.OK);
            } else {
                // 密碼不匹配，返回未授權錯誤
                return new ResponseEntity<>("Invalid password", HttpStatus.UNAUTHORIZED);
            }
	        }
         else {
            // 找不到匹配的用戶
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
	 }
	// 更新教師資料
	 @PutMapping("/teachers/update")
	 public ResponseEntity<Teacher> update(@RequestBody Teacher updateRequest) {
	     // 先檢查傳入的請求資料是否有效
	     if (updateRequest.getUsername() == null) {
	         return ResponseEntity.badRequest().build(); // 返回400 Bad Request
	     }

	     // 根據 username 查詢教師資料
	     List<Teacher> teachers = teacherAccount.findByUsername(updateRequest.getUsername());
	     if (!teachers.isEmpty()) {
	         Teacher teacher = teachers.get(0); // 取得第一個教師資料

	         // 更新教師的各項屬性
	         
	         teacher.setName(updateRequest.getName());
	         teacher.setPassword(updateRequest.getPassword()); // 考慮對密碼進行加密處理
	         teacher.setEmail(updateRequest.getEmail());

	         // 儲存更新後的教師資料到資料庫
	         Teacher updatedTeacher = teacherAccount.save(teacher);
	         return ResponseEntity.ok(updatedTeacher); // 返回更新後的教師資料
	     } else {
	         return ResponseEntity.notFound().build(); // 返回404 Not Found
	     }
	 }
	 @PostMapping("/teacher/forgot/get-email")
	 public ResponseEntity<String> getEmailByUsername(@RequestBody Map<String, String> requestBody) {
	     String username = requestBody.get("username");
	     System.out.println("Requested username: " + username);
	     
	     // 查詢用戶是否存在
	     List<Teacher> teachers = teacherAccount.findByUsername(username);
	     
	     // 檢查用戶是否存在
	     if (teachers.isEmpty()) {
	         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	     }

	     // 若找到用戶則取得其 email
	     Teacher teacher = teachers.get(0);
	     String email = teacher.getEmail();
	     System.out.println("Found email: " + email); // 檢查是否成功取得 email

	     // 發送驗證碼
	     if (email != null && !email.isEmpty()) {
	         generatedVerificationCode = generateVerificationCode(6); // 生成6位數驗證碼
	         sendVerificationEmail(email, generatedVerificationCode); // 發送郵件
	         return ResponseEntity.ok("Verification code sent to " + email); // 返回成功信息
	     } else {
	         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found"); // 沒有找到 email
	     }
	 }


	    private String generateVerificationCode(int length) {
	        Random random = new Random();
	        StringBuilder code = new StringBuilder();
	        for (int i = 0; i < length; i++) {
	            code.append(random.nextInt(10)); // 生成0-9之間的隨機數字
	        }
	        return code.toString();
	    }

	    private void sendVerificationEmail(String to, String verificationCode) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(to);
	        message.setSubject("Verification Code"); // 主題
	        message.setText("Your verification code is: " + verificationCode); // 訊息內容

	        mailSender.send(message); // 發送郵件
	    }

	    @PostMapping("/teacher/forgot/verify-code")
	    public ResponseEntity<String> verifyCode(@RequestBody String requestBody) {
	        // 假設 requestBody 是一個 JSON 字串，格式為 {"username": "yourUsername", "verificationCode": "yourCode"}
	        try {
	            // 使用 JSON 解析庫（例如 Jackson）將 requestBody 轉換為對應的資料
	            ObjectMapper objectMapper = new ObjectMapper();
	            Map<String, String> requestMap = objectMapper.readValue(requestBody, new TypeReference<Map<String, String>>() {});

	            String username = requestMap.get("username");
	            String verificationCode = requestMap.get("verificationCode");

	            // 檢查用戶是否存在
	            List<Teacher> teachers = teacherAccount.findByUsername(username);
	            if (teachers.isEmpty()) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	            }

	            // 驗證碼比對
	            if (generatedVerificationCode != null && generatedVerificationCode.equals(verificationCode)) {
	                return ResponseEntity.ok("Verification code is valid");
	            } else {
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification code");
	            }
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request");
	        }
	    }
	    
	    // 根據 username 和 currentPassword 驗證當前密碼
	 // 根據 username 更新密碼
	    @PostMapping(value = "/teacher/forgot/change-password")
	    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> requestBody) {
	        String username = requestBody.get("username");
	        String newPassword = requestBody.get("newPassword");

	        List<Teacher> teachers = teacherAccount.findByUsername(username);

	        if (teachers.isEmpty()) {
	            return ResponseEntity.notFound().build(); // 如果沒有找到學生，返回 404
	        }

	        Teacher teacher =teachers.get(0);
	        // 更新密碼
	        System.out.println(newPassword);
	        teacher.setPassword(newPassword); // 假設你有一個方法可以設置新密碼
	        teacherAccount.save(teacher); // 保存學生資料

	        return ResponseEntity.ok("Password updated successfully.");
	    }

    
    
}
