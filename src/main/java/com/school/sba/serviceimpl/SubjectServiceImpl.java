package com.school.sba.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.Subject;
import com.school.sba.repository.AcademicProgramRepo;
import com.school.sba.repository.SubjectRepo;
import com.school.sba.requestdto.AcademicProgramResponse;
import com.school.sba.requestdto.SubjectRequest;
import com.school.sba.responsedto.SubjectResponse;
import com.school.sba.service.SubjectService;
import com.school.sba.utility.ResponseStructure;
import com.school.sba.exception.*;


@Service
public class SubjectServiceImpl implements SubjectService {

	@Autowired
	private SubjectRepo subjectRepo;

	@Autowired
	private AcademicProgramRepo academicProgramRepo;

	@Autowired
	private ResponseStructure<AcademicProgramResponse> structure;

	@Autowired
	private AcademicProgramImpl academicProgramImpl;

	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addSubject(int programId, SubjectRequest subjectRequest){

		return academicProgramRepo.findById(programId)
				.map(academicProgram->{
					List<Subject>listSubject=new ArrayList<>();

					subjectRequest.getSubjectNames().forEach(name->
					{
						Subject fetchedSubject=subjectRepo.findBysubjectName(name.toLowerCase()).map(subject->{
							listSubject.add(subject);
							return subject;
						}).orElseGet( () -> 
						{
							Subject subject = new Subject();
							subject.setSubjectName(name.toLowerCase());
							subjectRepo.save(subject);
							listSubject.add(subject);
							return subject;
						});
					});

					academicProgram.setListOfSubjects(listSubject);
					academicProgramRepo.save(academicProgram);

					structure.setStatus(HttpStatus.CREATED.value());
					structure.setMessage("subject added successfully");
					structure.setData(academicProgramImpl.mapAcademicToAcademicResponse(academicProgram));

					return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure,HttpStatus.CREATED);
				})

				.orElseThrow( ()-> new  AcademicProgramNotFoundException("academic not found"));

	}

	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> updateSubject(int programId,
			SubjectRequest subjectRequest) {
		// TODO Auto-generated method stub
		return null;
	}

}
