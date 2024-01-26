package com.school.sba.service;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.ClassHourRequest;
import com.school.sba.responsedto.ClassHourResponse;
import com.school.sba.utility.ResponseStructure;

public interface ClassHourService {

	ResponseEntity<ResponseStructure<String>> createClassHour(int programId
			);

}
