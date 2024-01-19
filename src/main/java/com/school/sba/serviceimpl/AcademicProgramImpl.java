package com.school.sba.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.Subject;
import com.school.sba.exception.SchoolNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepo;
import com.school.sba.repository.SchoolRepo;
import com.school.sba.repository.SubjectRepo;
import com.school.sba.requestdto.AcademicProgramResponse;
import com.school.sba.requestdto.SubjectRequest;
import com.school.sba.responsedto.AcademicProgramRequest;
import com.school.sba.service.AcademicProgramService;
import com.school.sba.utility.ResponseStructure;

@Service
public class AcademicProgramImpl implements AcademicProgramService{

	@Autowired
	private AcademicProgramRepo academicProgramRepo;
	
	@Autowired
	private SubjectRepo subjectRep;
	
	@Autowired
	private SchoolRepo schoolRepo;
	
	@Autowired
	private ResponseStructure<AcademicProgramResponse> structure;
	
	@Autowired
	private ResponseStructure<List<AcademicProgramResponse>> listStructure;

	
	public AcademicProgramResponse mapAcademicToAcademicResponse(AcademicProgram academicProgram) {
		
		List<String> subjects=new ArrayList();
		
		List<Subject> listOfSubjects = academicProgram.getListOfSubjects();
		if(listOfSubjects!=null) {
			listOfSubjects.forEach(sub -> {
				subjects.add(sub.getSubjectName());
			});
		}
		
		return AcademicProgramResponse.builder()
				.programId(academicProgram.getProgramId())
				.programName(academicProgram.getProgramName())
				.programType(academicProgram.getProgramType())
				.programBeginsAt(academicProgram.getProgramBeginsAt())
				.programEndsAt(academicProgram.getProgramEndsAt())
				.listOfSubjects(subjects)
				.build();
		
	}
	
	private AcademicProgram mapAcademicRequestToAcademic(AcademicProgramRequest academicProgramRquest) {
		return AcademicProgram.builder()
				.programBeginsAt(academicProgramRquest.getProgramBeginsAt())
				.programEndsAt(academicProgramRquest.getProgramEndsAt())
				.programName(academicProgramRquest.getProgramName())
				.programType(academicProgramRquest.getProgramType())
				.build();
				
	}
	
	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> createProgram(int schoolId,
			AcademicProgramRequest academicProgramRequest) {
		
		return schoolRepo.findById(schoolId).map(school->
				{
					AcademicProgram academicPrograms = academicProgramRepo.save(mapAcademicRequestToAcademic(academicProgramRequest));
				   school.getListOfAcademicPrograms().add(academicPrograms);
				   school=schoolRepo.save(school);
				   academicPrograms.setSchool(school);	
				   academicPrograms=academicProgramRepo.save(academicPrograms);
				   
				   structure.setMessage("academic program created succesfully");
				   structure.setStatus(HttpStatus.CREATED.value());
				   structure.setData(mapAcademicToAcademicResponse(academicPrograms));
				   
				   return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure,HttpStatus.CREATED);
				}
				)
				.orElseThrow(()->new SchoolNotFoundByIdException("school not found"));
				
				
	}

	@Override
	public ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>> findAllAcademicProgram(int schoolId) {
		return schoolRepo.findById(schoolId).map(School->{
			List<AcademicProgram> listAcademicPrograms = academicProgramRepo.findAll();
			List<AcademicProgramResponse> listPrograms = listAcademicPrograms.stream()
			.map(this::mapAcademicToAcademicResponse)
			.collect(Collectors.toList());
			
			if(listAcademicPrograms.isEmpty()) {
				listStructure.setStatus(HttpStatus.NO_CONTENT.value());
				listStructure.setMessage("no programs has been found");
				listStructure.setData( listPrograms);
				
				return new ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>>(listStructure, HttpStatus.NO_CONTENT);
			}
			else {
				listStructure.setStatus(HttpStatus.FOUND.value());
				listStructure.setMessage("found list of academic programs");
				listStructure.setData(listPrograms);

				return new ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>>(listStructure, HttpStatus.FOUND);
			}
			
		}).orElseThrow(()-> new SchoolNotFoundByIdException("school not found"));
		
	}

	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> updateSubject(int programId,
			SubjectRequest subjectRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
