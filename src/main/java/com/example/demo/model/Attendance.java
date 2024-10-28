package com.example.demo.model;





import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "attendance")

public class Attendance {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer attendanceid;

	    @Column(name = "studentid", nullable = false, length = 255)
	    private String studentId;

	    @Column(name = "name", nullable = false, length = 255)
	    private String name;

	    @Enumerated(EnumType.STRING)
	    @Column(name = "status", nullable = false)
	    private AttendanceStatus status;

	    public enum AttendanceStatus {
	        出席, 缺席, 遲到,準時
	    }

		public Integer getAttendanceid() {
			return attendanceid;
		}

		public void setAttendanceid(Integer attendanceid) {
			this.attendanceid = attendanceid;
		}

		public String getStudentId() {
			return studentId;
		}

		public void setStudentId(String studentId) {
			this.studentId = studentId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public AttendanceStatus getStatus() {
			return status;
		}

		public void setStatus(AttendanceStatus status) {
			this.status = status;
		}
        

}
