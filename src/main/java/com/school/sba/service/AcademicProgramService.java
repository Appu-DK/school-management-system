package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.AcademicProgramResponse;
import com.school.sba.requestdto.SubjectRequest;
import com.school.sba.responsedto.AcademicProgramRequest;
import com.school.sba.utility.ResponseStructure;

public interface AcademicProgramService {

	ResponseEntity<ResponseStructure<AcademicProgramResponse>> createProgram(int schoolId,
			AcademicProgramRequest academicProgramRequest);

	ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>> findAllAcademicProgram(int schoolId);

	ResponseEntity<ResponseStructure<AcademicProgramResponse>> updateSubject(int programId,
			SubjectRequest subjectRequest);

	

}
