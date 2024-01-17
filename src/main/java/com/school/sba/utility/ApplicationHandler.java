package com.school.sba.utility;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.school.sba.exception.AdminNotFoundException;
import com.school.sba.exception.ExistingAdminException;
import com.school.sba.exception.SchoolAlreadyExistingException;
import com.school.sba.exception.UserNotFoundByIdException;

@RestControllerAdvice
public class ApplicationHandler {


	public ResponseEntity<Object> structure(HttpStatus status,String message,Object rootCause){
		return  new ResponseEntity<Object>(Map.of(
				"status",status.value(),
				"message",message,
				"rootCause",rootCause
				),status);
	}

	@ExceptionHandler
	public ResponseEntity<Object> adminAlreadyExist(ExistingAdminException ex){
		return structure(HttpStatus.BAD_REQUEST,ex.getMessage(),"admin is already existing in the database it doesn't have another admin" );
	}
	
	@ExceptionHandler
	public ResponseEntity<Object> userNotFoundById(UserNotFoundByIdException ex){
		return structure(HttpStatus.NOT_FOUND, ex.getMessage(), "user is not found in database");
	}
	
	@ExceptionHandler
	public ResponseEntity<Object> schoolAlreadyExist(SchoolAlreadyExistingException ex){
		return structure(HttpStatus.BAD_REQUEST, ex.getMessage(),"school already created in the datbase");
	}
	
	@ExceptionHandler
	public ResponseEntity<Object> adminNotFound(AdminNotFoundException ex){
		return structure(HttpStatus.NOT_FOUND, ex.getMessage(), "admin is only creating school but admin is not exist");
	}

}
