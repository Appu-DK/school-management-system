package com.school.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.requestdto.AcademicProgramResponse;
import com.school.sba.requestdto.SubjectRequest;
import com.school.sba.responsedto.AcademicProgramRequest;
import com.school.sba.service.AcademicProgramService;
import com.school.sba.utility.ResponseStructure;

@RestController
public class AcademicController {
	
	@Autowired
	private AcademicProgramService academicProgramService;
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/schools/{schoolId}/academicprograms")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> createProgram(@PathVariable int schoolId,
			@RequestBody AcademicProgramRequest academicProgramRequest){
		return academicProgramService.createProgram(schoolId,academicProgramRequest);
	}
	
	@GetMapping("/schools/{schoolId}/academicprogram")
	public ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>> findAllAcademicProgram(@PathVariable int schoolId ){
		return academicProgramService.findAllAcademicProgram(schoolId);
	}
	
	@PutMapping("/academicprograms/{programId}")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> updateSubject(@PathVariable int programId,
			@RequestBody SubjectRequest subjectRequest){
		return academicProgramService.updateSubject(programId, subjectRequest);
	}
	
	

}
