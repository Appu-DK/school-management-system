package com.school.sba.entity;

import java.time.LocalTime;
import java.util.List;

import org.hibernate.validator.constraints.CodePointLength;
import org.springframework.stereotype.Component;

import com.school.sba.enums.ProgramType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
//@Component
public class AcademicProgram {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int programId;
	private ProgramType programType;
	private String programName;
	private LocalTime programBeginsAt;
	private LocalTime programEndsAt;

	@ManyToOne
	private School school;

	@ManyToMany
	private List<Subject> listOfSubjects;
	
	@ManyToMany
	private List<User>listOfUsers;

}
