package com.school.sba.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.School;
import com.school.sba.enums.UserRole;
import com.school.sba.exception.AdminNotFoundException;
import com.school.sba.exception.SchoolAlreadyExistingException;
import com.school.sba.exception.SchoolNotFoundByIdException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.SchoolRepo;
import com.school.sba.repository.UserRepo;
import com.school.sba.requestdto.SchoolRequest;
import com.school.sba.responsedto.SchoolResponse;
import com.school.sba.service.SchoolService;
import com.school.sba.utility.ResponseStructure;

@Service
public class SchoolServiceImpl implements SchoolService{

	@Autowired
	private SchoolRepo schoolRepo;

	@Autowired
	private ResponseStructure<SchoolResponse> structure;

	@Autowired
	private UserRepo userRepo;

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
	public ResponseEntity<ResponseStructure<SchoolResponse>> saveSchool(int userId, SchoolRequest schoolRequest) {

		return userRepo.findById(userId).map(u->{
			if(u.getUserRole().equals(UserRole.ADMIN)) {
				if(u.getSchool()==null) {
					School school = mapSchoolRequestToSchool(schoolRequest);
					school=schoolRepo.save(school);
					u.setSchool(school);
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
	public ResponseEntity<ResponseStructure<SchoolResponse>> deleteSchool(int schoolId) {
		
		School school = schoolRepo.findById(schoolId)
		.orElseThrow(()->new SchoolNotFoundByIdException("school cannot be deleted because school is not exist"));
		schoolRepo.deleteById(schoolId);
		structure.setMessage("school deleted");
		structure.setStatus(HttpStatus.OK.value());
		structure.setData(mapSchoolToSchoolResponse(school));
		
		return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.OK);
		
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



}
