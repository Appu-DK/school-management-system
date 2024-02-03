package com.school.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.requestdto.ClassHourRequest;
import com.school.sba.responsedto.ClassHourResponse;
import com.school.sba.service.ClassHourService;
import com.school.sba.utility.ResponseStructure;

@RestController
public class ClassHourController {

	@Autowired
	private ClassHourService classHourService;

	@PostMapping("/academic-program/{programId}/class-hours")
	public ResponseEntity<ResponseStructure<String>> createClassHour(@PathVariable int programId){

		return classHourService.createClassHour(programId);
	}

	@PutMapping("/class-hours")
	public ResponseEntity<ResponseStructure<List<ClassHourResponse>>> assigningPeriods(@RequestBody List<ClassHourRequest> classHour){
		return classHourService.assignPeriods(classHour);
	}

	@PutMapping("/academic-program/{programId}/class-hours")
	public ResponseEntity<ResponseStructure<String>> classHourForNextWeek(@PathVariable int programId){
		return classHourService.classHourForNextWeek(programId);
	}




}
