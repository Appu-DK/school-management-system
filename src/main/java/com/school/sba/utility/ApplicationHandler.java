package com.school.sba.utility;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.school.sba.exception.AcademicProgramNotFoundException;
import com.school.sba.exception.AdminNotFoundException;
import com.school.sba.exception.ExistingAdminException;
import com.school.sba.exception.ScheduleAlreadyExistingException;
import com.school.sba.exception.ScheduleNotFoundException;
import com.school.sba.exception.SchoolAlreadyExistingException;
import com.school.sba.exception.SchoolNotFoundException;
import com.school.sba.exception.SubjectNotFoundException;
import com.school.sba.exception.TeacherNotFoundException;
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

	@ExceptionHandler
	public ResponseEntity<Object> alreadyScheduleExisted(ScheduleAlreadyExistingException ex){
		return structure(HttpStatus.BAD_REQUEST, ex.getMessage(),"schedule is already existing for this school");
	}

	@ExceptionHandler
	public ResponseEntity<Object> schoolNotFound(SchoolNotFoundException ex){
		return structure(HttpStatus.NOT_FOUND, ex.getMessage(), "school is not found in database so cannot schedule to school");
	}
	
	@ExceptionHandler
	public ResponseEntity<Object> subjectNotFound(SubjectNotFoundException ex){
		return structure(HttpStatus.NOT_FOUND, ex.getMessage(), "subject not found exists in database");
	}
	@ExceptionHandler
	public ResponseEntity<Object> teacherNotFound(TeacherNotFoundException ex){
		return structure(HttpStatus.NOT_FOUND, ex.getMessage(), "teacher is not found not erxits in database");
	}
	@ExceptionHandler
	public ResponseEntity<Object> schedueleNotFound(ScheduleNotFoundException ex){
		return structure(HttpStatus.NOT_FOUND, ex.getMessage(), "schedule is not found in database");
	}
	@ExceptionHandler
	public ResponseEntity<Object> academicProgramNotFound(AcademicProgramNotFoundException ex){
		return structure(HttpStatus.NOT_FOUND, ex.getMessage(), "academic program is not found");
	}

}
