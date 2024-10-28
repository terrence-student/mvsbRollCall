package com.example.demo.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.DAO.HomeworkRepository;
import com.example.demo.DAO.HomeworkforStudentRepository;
import com.example.demo.DAO.StudentRepository;
import com.example.demo.DAO.TeacherRepository;
import com.example.demo.model.Homework;
import com.example.demo.model.HomeworkforStudent;
import com.example.demo.model.Student;
import com.example.demo.model.Teacher;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


@RestController
@RequestMapping("/homework/student")
public class  HomeworkforStudentController {

    @Autowired
    private HomeworkRepository homeworkRepository;
    
    @Autowired
    private TeacherRepository teacherAccount;  // 正確注入 TeacherRepository
    
    @Autowired
    private HomeworkforStudentRepository  homeworkforStudentRepository;
    
    @Autowired
    private StudentRepository studentAccount;
    
    @Autowired
    private Environment env;  // 注入 Environment 物件，用來讀取 application.properties 中的配置

    // 預設的上傳目錄
    private static final String DEFAULT_UPLOAD_DIR = "/uploads";

    // 檢查學生是否已提交過作業
    @GetMapping("/checkSubmission")
    public ResponseEntity<Boolean> checkSubmission(@RequestParam String studentId, @RequestParam Integer homeworkId) {
        boolean hasSubmitted = homeworkforStudentRepository.existsByStudentidAndHomeworkid(studentId, homeworkId);
        return ResponseEntity.ok(hasSubmitted); // 返回提交狀態
    }
    
    
    // 根據 homeworkId 獲取作業提交的學生列表
    @GetMapping("/submissions") // 確保此行與 URL 匹配
    public ResponseEntity<List<HomeworkforStudent>> getSubmissionsByHomeworkId(@RequestParam Integer homeworkId) {
        List<HomeworkforStudent> submissions = homeworkforStudentRepository.findByHomeworkid(homeworkId);
        
        if (submissions.isEmpty()) {
            return ResponseEntity.noContent().build(); // 沒有找到任何提交
        }
        
        return ResponseEntity.ok(submissions); // 返回提交的學生列表
    }
        
 // 根據 teacherid 查詢單一老師資料
    @GetMapping(value = "/getSingleTeacherData")
    public String getSingleTeacherData(@RequestParam Integer teacherid) {
        List<Teacher> teacher = teacherAccount.findByTeacherId(teacherid);
        Teacher teacher2=teacher.get(0);
        return teacher2.getName();
    }

 // 查詢所有學生作業
    @GetMapping("/all")
    public ResponseEntity<List<HomeworkforStudent>> getAllStudentHomework() {
        // 從 homeworkforStudentRepository 中查詢所有學生作業
        List<HomeworkforStudent> homeworkList = homeworkforStudentRepository.findAll();

        // 如果列表為空，可以檢查並給出相應的回應
        if (homeworkList.isEmpty()) {
            return ResponseEntity.noContent().build(); // 返回 204 No Content
        }

        // 返回 200 OK 和學生作業列表
        return ResponseEntity.ok(homeworkList);
    }

    // 下載作業檔案
    @GetMapping("/download/{studentId}/{homeworkId}")
    public ResponseEntity<?> downloadHomeworkFile(@PathVariable String studentId, @PathVariable Integer homeworkId) {
        Optional<HomeworkforStudent> homeworkOptional = homeworkforStudentRepository.findByStudentidAndHomeworkid(studentId, homeworkId);
        System.out.println(homeworkOptional);
        if (!homeworkOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("作業不存在");
        }

        HomeworkforStudent homework = homeworkOptional.get();
        String homeworkFileName = homework.getHomework();

        // 使用 application.properties 中的上傳目錄
        String uploadDir = env.getProperty("upload.path", "./uploads");
        File file = new File(uploadDir + "/" + homeworkFileName);

        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("檔案不存在");
        }

        try {
            FileSystemResource fileResource = new FileSystemResource(file);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(fileResource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("檔案下載失敗: " + e.getMessage());
        }
    }

    
    @PostMapping("/fetchsave")
    public ResponseEntity<?> handleFileUpload(
            @RequestParam("homeworkid") Integer homeworkId,
            @RequestParam("teachername") String teacherName,
            @RequestParam("classname") String className,
            @RequestParam("title") String title,
            @RequestParam("homeworkcontent") String homeworkContent,
            @RequestParam("studentid") String studentId,
            @RequestParam("name") String studentName,
            @RequestParam("grade") Integer studentGrade,
            @RequestParam("myfileUpload") MultipartFile file,
            @RequestParam("fileName") String fileName) {  // 保留前端傳遞的檔案名稱

        try {
            // 使用 application.properties 中的上傳目錄
            String uploadDir = env.getProperty("upload.path", "./uploads");
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 使用前端傳遞的 fileName 作為檔案名稱
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            HomeworkforStudent homework = new HomeworkforStudent();
            homework.setHomeworkid(homeworkId);
            homework.setTeachername(teacherName);
            homework.setClassname(className);
            homework.setTitle(title);
            homework.setHomeworkcontent(homeworkContent);
            homework.setStudentid(studentId);
            homework.setName(studentName);
            homework.setGrade(studentGrade);
            homework.setHomework(fileName); // 儲存檔案名稱
            homework.setSubmittedAt(new Date());
            homework.setScore(-1);

            homeworkforStudentRepository.save(homework);

            return ResponseEntity.ok("檔案上傳成功！");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("檔案上傳失敗: " + e.getMessage());
        }
    }
    @PutMapping("/grade/{studentId}/{homeworkId}")
    public ResponseEntity<?> updateStudentScore(@PathVariable String studentId, @PathVariable Integer homeworkId, @RequestBody Map<String, Object> payload) {
        Integer newScore = (Integer) payload.get("score");
        Optional<HomeworkforStudent> homeworkOptional = homeworkforStudentRepository.findByStudentidAndHomeworkid(studentId, homeworkId);

        if (!homeworkOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("作業不存在");
        }

        HomeworkforStudent homework = homeworkOptional.get();
        homework.setScore(newScore); // 假設有一個 setScore 方法來更新分數
        homeworkforStudentRepository.save(homework); // 保存到資料庫

        return ResponseEntity.ok("分數更新成功！");
    }
    
    @GetMapping("/getStudentScore")
    public ResponseEntity<?> getStudentScore(@RequestParam String studentId, @RequestParam Integer homeworkId) {
        Optional<HomeworkforStudent> homeworkOptional = homeworkforStudentRepository.findByStudentidAndHomeworkid(studentId, homeworkId);

        if (!homeworkOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("作業不存在");
        }

        HomeworkforStudent homework = homeworkOptional.get();
        return ResponseEntity.ok(Collections.singletonMap("score", homework.getScore()));
    }
    
    
}
    

