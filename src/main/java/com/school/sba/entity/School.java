package com.school.sba.entity;

import java.util.List;

import org.springframework.aot.generate.GeneratedTypeReference;
import org.springframework.stereotype.Component;

import com.school.sba.responsedto.SchoolResponse.SchoolResponseBuilder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor
public class School {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int schoolId;
	private String schoolName;
	private long contactNo;
	private String emailId;
	private String address;
	private boolean isDeleted;

	@OneToOne(cascade = CascadeType.REMOVE)
	private Schedule schedule;

	@OneToMany(mappedBy = "school")
	private List<AcademicProgram> listOfAcademicPrograms;



}
