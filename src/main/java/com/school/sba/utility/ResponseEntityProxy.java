package com.school.sba.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityProxy {
	
	public static<T> ResponseEntity<ResponseStructure<T>> setResponseStructure(HttpStatus status,String message,T data){
		
		ResponseStructure<T> structure=new ResponseStructure<>();
		structure.setData(data);
		structure.setMessage(message);
		structure.setStatus(status.value());
		return new ResponseEntity<ResponseStructure<T>>(structure,status);
	}

}
