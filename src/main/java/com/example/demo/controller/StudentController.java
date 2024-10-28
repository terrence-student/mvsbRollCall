package com.example.demo.controller;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;


import com.example.demo.DAO.StudentRepository;
import com.example.demo.model.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
public class StudentController {
	 @Autowired
	    private StudentRepository studentAccount;	
	 
	 @Autowired
	    private JavaMailSender mailSender; // 用於發送郵件的服務

	    private String generatedVerificationCode; // 儲存生成的驗證碼
	    
	    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
	    
	    @GetMapping("/students/exists")
	    public ResponseEntity<Boolean> existsByUsernameAndStudentid(@RequestParam String username, @RequestParam String studentid) {
	        boolean exists = studentAccount.existsByUsernameAndStudentid(username, studentid);
	        return ResponseEntity.ok(exists);
	    }
	
	 @RequestMapping(value = "/students/login", method = RequestMethod.POST)
	 public ResponseEntity<String> login(@RequestBody Student loginRequest) {
	     String username = loginRequest.getUsername();
	     String password = loginRequest.getPassword();
	     // 查找用戶名和密碼是否匹配
	        List<Student> students = studentAccount.findByUsernameAndPassword(username, password);
	        
	        // 檢查是否存在此用戶
	        if (!students.isEmpty()){ 
	        	Student foundStudent = students.get(0); 
            
            // 驗證密碼是否正確
            if (foundStudent.getPassword().equals(password)) {
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
	// 更新學生資料
	 @PutMapping(value = "/students/update")
	 public ResponseEntity<String> updateStudent(@RequestBody Map<String, String> studentData) {
	     String username = studentData.get("username");
	     String name = studentData.get("name");
	     String password = studentData.get("password");
	     String email = studentData.get("email");

	     // 查找該 username 的學生資料
	     List<Student> students = studentAccount.findByUsername(username);
	     if (students.isEmpty()) {
	         return new ResponseEntity<>("學生不存在", HttpStatus.NOT_FOUND);
	     }

	     Student student = students.get(0);
	     student.setName(name);
	     student.setPassword(password);
	     student.setEmail(email);

	     // 更新學生資料到資料庫
	     studentAccount.save(student);

	     return new ResponseEntity<>("更新成功！", HttpStatus.OK);
	 }
	 
	 
	 
	// 根據 username 查詢單一學生資料
		 @GetMapping(value = "/getSingleStudentData")
		 public ResponseEntity<Student> getSingleStudentData(@RequestParam String username) {
		     List<Student> students = studentAccount.findByUsername(username);
		     
		     if (students.isEmpty()) {
		         return ResponseEntity.notFound().build(); // 如果沒有找到學生，返回 404
		     }
		     
		     return ResponseEntity.ok(students.get(0)); // 返回第一個學生資料
		 }
	 
	 

	 @PostMapping("/student/forgot/get-email")
	 public ResponseEntity<String> getEmailByUsername(@RequestBody Map<String, String> requestBody) {
	     String username = requestBody.get("username");
	     System.out.println("Requested username: " + username);
	     
	     // 查詢用戶是否存在
	     List<Student> students = studentAccount.findByUsername(username);
	     
	     // 檢查用戶是否存在
	     if (students.isEmpty()) {
	         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	     }

	     // 若找到用戶則取得其 email
	     Student student = students.get(0);
	     String email = student.getEmail();
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

	    @PostMapping("/student/forgot/verify-code")
	    public ResponseEntity<String> verifyCode(@RequestBody String requestBody) {
	        // 假設 requestBody 是一個 JSON 字串，格式為 {"username": "yourUsername", "verificationCode": "yourCode"}
	        try {
	            // 使用 JSON 解析庫（例如 Jackson）將 requestBody 轉換為對應的資料
	            ObjectMapper objectMapper = new ObjectMapper();
	            Map<String, String> requestMap = objectMapper.readValue(requestBody, new TypeReference<Map<String, String>>() {});

	            String username = requestMap.get("username");
	            String verificationCode = requestMap.get("verificationCode");

	            // 檢查用戶是否存在
	            List<Student> students = studentAccount.findByUsername(username);
	            if (students.isEmpty()) {
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
	    @PostMapping(value = "/student/forgot/change-password")
	    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> requestBody) {
	        String username = requestBody.get("username");
	        String newPassword = requestBody.get("newPassword");

	        List<Student> students = studentAccount.findByUsername(username);

	        if (students.isEmpty()) {
	            return ResponseEntity.notFound().build(); // 如果沒有找到學生，返回 404
	        }

	        Student student = students.get(0);
	        // 更新密碼
	        System.out.println(newPassword);
	        student.setPassword(newPassword); // 假設你有一個方法可以設置新密碼
	        studentAccount.save(student); // 保存學生資料

	        return ResponseEntity.ok("Password updated successfully.");
	    }
	
	    
	    // 根據學號查詢學生資料
	    @GetMapping("/id/{studentid}")
	    public ResponseEntity<Student> getStudentById(@PathVariable String studentid) {
	        logger.info("查詢學號為 {} 的學生資料", studentid);

	        Optional<Student> student = studentAccount.findById(studentid);
	        return student.map(ResponseEntity::ok)
	                      .orElseGet(() -> {
	                          logger.warn("學號為 {} 的學生記錄不存在", studentid);
	                          return ResponseEntity.notFound().build();
	                      });
	    }
	    

	    

	    // 根據姓名查詢學生資料
	    @GetMapping("/name/{name}")
	    public ResponseEntity<List<Student>> getStudentsByName(@PathVariable String name) {
	        logger.info("查詢姓名為 {} 的學生資料", name);
	        List<Student> students = studentAccount.findByNameIgnoreCase(name);
	        if (students.isEmpty()) {
	            logger.warn("姓名為 {} 的學生記錄不存在", name);
	            return ResponseEntity.notFound().build();
	        }
	        return ResponseEntity.ok(students);
	    }

	    // 根據專業查詢學生
	    @GetMapping("/major/{majorname}")
	    public ResponseEntity<List<Student>> getStudentsByMajor(@PathVariable String majorname) {
	        logger.info("查詢專業為 {} 的學生記錄", majorname);

	        List<Student> students =studentAccount.findByMajorIgnoreCase(majorname);
	        if (students.isEmpty()) {
	            logger.warn("專業為 {} 的學生記錄不存在", majorname);
	            return ResponseEntity.notFound().build();
	        }
	        logger.info("找到 {} 名學生記錄", students.size());
	        return ResponseEntity.ok(students);
	    }

	    // 根據年級查詢學生
	    @GetMapping("/grade/{grade}")
	    public ResponseEntity<List<Student>> getStudentsByGrade(@PathVariable int grade) {
	        logger.info("查詢年級為 {} 的學生記錄", grade);

	        List<Student> students = studentAccount.findByGrade(grade);
	        if (students.isEmpty()) {
	            logger.warn("年級為 {} 的學生記錄不存在", grade);
	            return ResponseEntity.notFound().build();
	        }
	        logger.info("找到 {} 名學生記錄", students.size());
	        return ResponseEntity.ok(students);
	    }

	    // 新增學生資料
	    @PostMapping("/add")
	    public ResponseEntity<?> addStudent(@RequestBody Student student) {
	        logger.info("添加學生記錄：{}", student);

	        try {
	            // 保存學生記錄
	            Student newStudent = studentAccount.save(student);
	            logger.info("成功添加學生記錄：{}", newStudent);
	            return ResponseEntity.status(HttpStatus.CREATED).body(newStudent);
	        } catch (Exception e) {
	            // 捕獲並處理異常
	            logger.error("添加學生時發生錯誤: {}", e.getMessage(), e);
	            // 返回錯誤訊息給前端
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("添加學生失敗，請檢查輸入數據或稍後再試。");
	        }
	    }

	    // 刪除學生資料
	    @DeleteMapping("/id/{studentid}")
	    public ResponseEntity<Void> deleteStudent(@PathVariable String studentid) {
	        logger.info("刪除學號為 {} 的學生記錄", studentid);

	        Optional<Student> student = studentAccount.findById(studentid);
	        if (student.isPresent()) {
	            logger.info("找到學號為 {} 的學生記錄", studentid);
	            studentAccount.delete(student.get());
	            logger.info("成功刪除學號為 {} 的學生記錄", studentid);
	            return ResponseEntity.ok().build();
	        } else {
	            logger.warn("學號為 {} 的學生記錄不存在，無法刪除", studentid);
	            return ResponseEntity.notFound().build();
	        }
	    }

	    // 顯示所有學生資料
	    @GetMapping("/all")
	    public ResponseEntity<List<Student>> getAllStudents() {
	        logger.info("查詢所有學生");
	        List<Student> students = studentAccount.findAll();
	        if (students.isEmpty()) {
	            logger.warn("沒有學生");
	            return ResponseEntity.notFound().build();
	        }
	        logger.info("找到 {} 名學生", students.size());
	        return ResponseEntity.ok(students);
	    }
    
    
}

