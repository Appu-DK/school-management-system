package com.school.sba.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.ClassHour;
import com.school.sba.entity.School;
import com.school.sba.entity.User;
import com.school.sba.enums.UserRole;
import com.school.sba.exception.AdminNotFoundException;
import com.school.sba.exception.SchoolAlreadyExistingException;
import com.school.sba.exception.SchoolNotFoundByIdException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepo;
import com.school.sba.repository.ClassHourRepo;
import com.school.sba.repository.SchoolRepo;
import com.school.sba.repository.UserRepo;
import com.school.sba.requestdto.SchoolRequest;
import com.school.sba.responsedto.SchoolResponse;
import com.school.sba.service.SchoolService;
import com.school.sba.utility.ResponseEntityProxy;
import com.school.sba.utility.ResponseStructure;

@Service
public class SchoolServiceImpl implements SchoolService{

	@Autowired
	private SchoolRepo schoolRepo;

	@Autowired
	private ResponseStructure<SchoolResponse> structure;

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ClassHourRepo classHourRepo;

	@Autowired
	private AcademicProgramRepo academicProgramRepo;

	private School mapSchoolRequestToSchool(SchoolRequest schoolRequest) {

		return School.builder()
				.schoolName(schoolRequest.getSchoolName())
				.address(schoolRequest.getAddress())
				.emailId(schoolRequest.getEmailId())
				.contactNo(schoolRequest.getContactNo())
				.build();
	}
	private SchoolResponse mapSchoolToSchoolResponse(School school) {
		return SchoolResponse.builder()
				.contactNo(school.getContactNo())
				.emailId(school.getEmailId())
				.address(school.getAddress())
				.schoolName(school.getSchoolName())
				.schoolId(school.getSchoolId())
				.build();
	}


	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> saveSchool( SchoolRequest schoolRequest) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepo.findByUserName(userName).map(u->{
			if(u.getUserRole().equals(UserRole.ADMIN)) {
				if(u.getSchool()==null) {
					School school = mapSchoolRequestToSchool(schoolRequest);
					userRepo.findAll().forEach(user->{
						user.setSchool(school);
						schoolRepo.save(school);
					});
					userRepo.save(u);
					structure.setMessage("school saved succesfully");
					structure.setStatus(HttpStatus.CREATED.value());
					structure.setData(mapSchoolToSchoolResponse(school));
					return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.CREATED);
				}
				else {
					throw new SchoolAlreadyExistingException("school already existing");

				}

			}
			else {
				throw new AdminNotFoundException("admin is only create school");
			}
		}).orElseThrow(()->new UserNotFoundByIdException("user not  found"));





		//		if(user.getUserRole().equals(UserRole.ADMIN)) {
		//			School exis= mapSchoolRequestToSchool(schoolRequest);
		//			if(schoolRepo.existsBySchoolId(sch.getSchoolId())==false) {
		//				School school = schoolRepo.save(mapSchoolRequestToSchool(schoolRequest));
		//				structure.setStatus(HttpStatus.CREATED.value());
		//				structure.setMessage("school created successfully");
		//				structure.setData(mapSchoolToSchoolResponse(school));
		//				return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.CREATED);
		//			}
		//			else {
		//				throw new SchoolAlreadyExistingException("school already existing");
		//			}
		//		}
		//		else {
		//			throw new AdminNotFoundException("admin is not found");
		//
		//		}

	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> updateSchool(int schoolId, SchoolRequest schoolRequest) {
		School existSchool = schoolRepo.findById(schoolId).map(u->{
			School school = mapSchoolRequestToSchool(schoolRequest);
			school.setSchoolId(schoolId);
			return schoolRepo.save(school);
		})
				.orElseThrow(()->new SchoolNotFoundByIdException("school is not updated becz school is not exist"));
		structure.setMessage("school updated successfully");
		structure.setStatus(HttpStatus.OK.value());
		structure.setData(mapSchoolToSchoolResponse(existSchool));

		return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.OK);
	}
	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> findSchool(int schoolId) {
		School school = schoolRepo.findById(schoolId)
				.orElseThrow(()-> new SchoolNotFoundByIdException("school is not found"));

		structure.setMessage("school data found in database");
		structure.setStatus(HttpStatus.FOUND.value());
		structure.setData(mapSchoolToSchoolResponse(school));

		return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.FOUND);
	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> deleteSchool(int schoolId) {

		School school = schoolRepo.findById(schoolId)
				.orElseThrow(()->new SchoolNotFoundByIdException("school cannot be deleted because school is not exist"));

		school.setDeleted(true);
		schoolRepo.save(school);

		return new ResponseEntityProxy().setResponseStructure(HttpStatus.OK ,"school is soft deleted", mapSchoolToSchoolResponse(school));
	}

	public void hardDeleteSchool(int schoolId) {
		schoolRepo.findByIsDeleted(true).forEach(school->{
			List<AcademicProgram> academics = school.getListOfAcademicPrograms();
			for(AcademicProgram academic:academics) {
				classHourRepo.deleteAll(academic.getListOfClassHours());
				academicProgramRepo.delete(academic);
			}
			
			 List<User> users = userRepo.findBySchool(school);
			 for(User user:users) {
				 if(!user.equals(UserRole.ADMIN)) {
					 userRepo.delete(user);
				 }
			 }		
			schoolRepo.delete(school);
		});
		
	}

}
