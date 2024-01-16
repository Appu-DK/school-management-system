package com.school.sba.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.User;
import com.school.sba.requestdto.UserRequest;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.serviceimpl.UserServiceImpl;
import com.school.sba.utility.ResponseStructure;

import jakarta.validation.Valid;


public interface UserService  {

	ResponseEntity<ResponseStructure<UserResponse>> saveUser(@Valid UserRequest userRequest);

	ResponseEntity<ResponseStructure<UserResponse>> deleteUser(int userId);

	ResponseEntity<ResponseStructure<UserResponse>> findUser(int userId);

}
