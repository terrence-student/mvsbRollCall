package com.example.demo.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.TeacherRegister;



public interface TeacherRegisterRepository extends JpaRepository<TeacherRegister, Long> {

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);
}
