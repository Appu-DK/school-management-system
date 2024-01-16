package com.school.sba.controller;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.entity.User;
import com.school.sba.requestdto.UserRequest;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.service.UserService;
import com.school.sba.utility.ResponseStructure;

import jakarta.validation.Valid;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	@PostMapping("/users/register")
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(@RequestBody @Valid UserRequest userRequest){

		return userService.saveUser(userRequest);

	}
	
	@GetMapping("users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> findUser(@PathVariable int userId){
		return userService.findUser(userId);
	}
	@DeleteMapping("users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> deleteUser(@PathVariable int userId){
		
		return userService.deleteUser(userId);
		
	}

}
