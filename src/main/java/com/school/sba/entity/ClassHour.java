package com.school.sba.entity;

import java.time.LocalDateTime;

import com.school.sba.enums.ClassStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ClassHour {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int classHourId;

	private LocalDateTime classBeginsAt;
	private LocalDateTime classEndsAt;
	private int classRoomNumber;

	@Enumerated(EnumType.STRING)
	private ClassStatus classStatus;

	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	@ManyToOne
	@JoinColumn(name = "subjectId")
	private Subject subject;

	@ManyToOne
	@JoinColumn(name = "academicProgramId")
	private AcademicProgram academicPrograms;

}