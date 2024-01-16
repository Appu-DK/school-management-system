package com.school.sba.utility;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.school.sba.exception.ExistingAdminException;

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

}
