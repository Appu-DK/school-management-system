package com.school.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<ResponseStructure<UserResponse>> registerAdmin(@RequestBody UserRequest userRequest )
	{
		return userService.registerAdmin(userRequest);
	}

	@PostMapping("/users")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<ResponseStructure<UserResponse>> addOtherUser(@RequestBody UserRequest userRequest){
		return userService.addOtherUser(userRequest);
	}


	@PostMapping("/users/register/save")
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(@RequestBody @Valid UserRequest userRequest){

		return userService.saveUser(userRequest);

	}

	@GetMapping("users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> findUser(@PathVariable int userId){
		return userService.findUser(userId);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> deleteUser(@PathVariable int userId){

		return userService.deleteUser(userId);

	}
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/academic-programs/{programId}/users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> addUserToAcademic(@PathVariable int programId,@PathVariable int userId)
	{
		return userService.addUserToAcademic(programId,userId);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/subjects/{subjectId}/users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> addSubjectToUser(@PathVariable int subjectId,@PathVariable int userId){

		return userService.addSubjectToUser(subjectId,userId);
	}
	
	@GetMapping("/academic-programs/{programId}/user-roles/{userRole}/users")
	public ResponseEntity<ResponseStructure<List<UserResponse>>> getUsers(@PathVariable int programId,@PathVariable String userRole){
		
		return userService.getUsers(programId,userRole);
	}





}
