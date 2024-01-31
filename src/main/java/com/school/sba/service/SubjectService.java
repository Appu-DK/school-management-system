package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.AcademicProgramResponse;
import com.school.sba.requestdto.SubjectRequest;
import com.school.sba.responsedto.SubjectResponse;
import com.school.sba.utility.ResponseStructure;

public interface SubjectService {

	ResponseEntity<ResponseStructure<AcademicProgramResponse>>addSubject(int programId, SubjectRequest subjectRequest);

	ResponseEntity<ResponseStructure<AcademicProgramResponse>> updateSubject(int programId,
			SubjectRequest subjectRequest);

	ResponseEntity<ResponseStructure<List<SubjectResponse>>> findAllSubjects();

	ResponseEntity<ResponseStructure<SubjectResponse>> deleteSubject(int subjectId);



}
