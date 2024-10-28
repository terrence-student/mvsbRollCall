package com.example.demo.DAO;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.*;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

	List<Teacher> findAll();

	List<Teacher> findByUsernameAndPassword(String username, String password);

	List<Teacher> findByUsername(String username);

	List<Teacher> findByTeacherId(Integer teacherId);

	@Query("SELECT t.status FROM Teacher t WHERE t.username = :username AND t.password = :password")
	String findStatusByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

	List<Teacher> findByNameIgnoreCase(String name);
}
