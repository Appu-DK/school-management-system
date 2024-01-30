package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.User;
import com.school.sba.enums.UserRole;
import com.school.sba.requestdto.UserRequest;
import com.school.sba.responsedto.AcademicProgramRequest;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.serviceimpl.UserServiceImpl;
import com.school.sba.utility.ResponseStructure;

import jakarta.validation.Valid;


public interface UserService  {

	ResponseEntity<ResponseStructure<UserResponse>> saveUser( UserRequest userRequest);

	ResponseEntity<ResponseStructure<UserResponse>> deleteUser(int userId);

	ResponseEntity<ResponseStructure<UserResponse>> findUser(int userId);

	ResponseEntity<ResponseStructure<UserResponse>> addUserToAcademic(int programId,
			int userId);

	ResponseEntity<ResponseStructure<UserResponse>> addSubjectToUser(int subjectId, int userId);

	ResponseEntity<ResponseStructure<UserResponse>> registerAdmin(UserRequest userRequest);

	ResponseEntity<ResponseStructure<UserResponse>> addOtherUser(UserRequest userRequest);

	ResponseEntity<ResponseStructure<List<UserResponse>>> getUsers(int programId, String userRole);



}
