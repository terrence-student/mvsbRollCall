package com.example.demo.DAO;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Student;



public interface StudentRepository  extends JpaRepository<Student, String>{
	
	List<Student> findAll();	
	List<Student> findByUsernameAndPassword(String username,String password);
	List<Student> findByUsername(String username);
	
	
	List<Student> findByNameIgnoreCase(String name);
    List<Student> findByMajorIgnoreCase(String major);
    List<Student> findByGrade(int grade);
    boolean existsByUsernameAndStudentid(String username, String studentid);
    

}
