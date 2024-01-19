package com.school.sba.serviceimpl;

import org.hibernate.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.User;
import com.school.sba.enums.UserRole;
import com.school.sba.exception.AcademicProgramNotFoundException;
import com.school.sba.exception.ExistingAdminException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepo;
import com.school.sba.repository.UserRepo;
import com.school.sba.requestdto.UserRequest;
import com.school.sba.responsedto.AcademicProgramRequest;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.service.UserService;
import com.school.sba.utility.ResponseStructure;

import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;
import jakarta.validation.Valid;

@org.springframework.stereotype.Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo repo;

	@Autowired
	ResponseStructure<UserResponse> structrure;

	@Autowired
	private AcademicProgramRepo academicProgramRepo;

	private User mapUserRequestToUser(UserRequest userRequest) {
		return	User.builder()
				.userName(userRequest.getUserName())
				.password(userRequest.getPassword())
				.firstName(userRequest.getFirstName())
				.lastName(userRequest.getLastName())
				.email(userRequest.getEmail())
				.contactNo(userRequest.getContactNo())
				.userRole(userRequest.getUserRole())
				.build();
	}
	private UserResponse mapUserToUserResponse(User user) {
		return UserResponse.builder()
				.userId(user.getUserId())
				.userName(user.getUserName())
				.email(user.getEmail())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.contactNo(user.getContactNo())
				.userRole(user.getUserRole())

				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(UserRequest userRequest) {

		if(userRequest.getUserRole().equals(UserRole.ADMIN)) {
			if(repo.existsByUserRole(userRequest.getUserRole())==false) {

				User user = repo.save(mapUserRequestToUser(userRequest));
				structrure.setStatus(HttpStatus.CREATED.value());
				structrure.setMessage(" user saved succesfully");
				structrure.setData(mapUserToUserResponse(user));
				return new ResponseEntity<ResponseStructure<UserResponse>>(structrure,HttpStatus.CREATED); 

			}
			else {
				throw new ExistingAdminException("admin is already existing");

			}
		}
		else {
			User user = repo.save(mapUserRequestToUser(userRequest));
			structrure.setStatus(HttpStatus.CREATED.value());
			structrure.setMessage(" user saved succesfully");
			structrure.setData(mapUserToUserResponse(user));
			return new ResponseEntity<ResponseStructure<UserResponse>>(structrure,HttpStatus.CREATED); 
		}


	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> deleteUser(int userId) {

		User user = repo.findById(userId).orElseThrow(()-> new UserNotFoundByIdException("user not found"));
		user.setDeleted(true);
		repo.save(user);

		structrure.setStatus(HttpStatus.OK.value());
		structrure.setMessage("user is soft deleted");
		structrure.setData(mapUserToUserResponse(user));


		return new ResponseEntity<ResponseStructure<UserResponse>>(structrure,HttpStatus.OK);

	}
	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> findUser(int userId) {
		User user = repo.findById(userId).orElseThrow(()-> new UserNotFoundByIdException("user not found"));

		structrure.setStatus(HttpStatus.FOUND.value());
		structrure.setMessage("user found successfully");
		structrure.setData(mapUserToUserResponse(user));

		return new ResponseEntity<ResponseStructure<UserResponse>>(structrure,HttpStatus.FOUND);
	}
	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> addUserToAcademic(int programId,
			int userId) 
	{
		User user = repo.findById(userId).orElseThrow(()->new UserNotFoundByIdException("userNot found  by"));
		AcademicProgram acadmicProgram = academicProgramRepo.findById(programId).orElseThrow(()-> new AcademicProgramNotFoundException("acdemic program not found "));

		acadmicProgram.getListOfUsers().add(user);
		academicProgramRepo.save(acadmicProgram);
		repo.save(user);
		structrure.setStatus(HttpStatus.OK.value());
		structrure.setMessage("user added to academic");
		structrure.setData(mapUserToUserResponse(user));

		return new ResponseEntity<ResponseStructure<UserResponse>>(structrure,HttpStatus.OK);


	}


}
