package com.example.demo.DAO;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
	Optional<Attendance> findByStudentId(String studentId);
}
