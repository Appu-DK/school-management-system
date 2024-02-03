package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.ClassHourRequest;
import com.school.sba.responsedto.ClassHourResponse;
import com.school.sba.utility.ResponseStructure;

public interface ClassHourService {

	ResponseEntity<ResponseStructure<String>> createClassHour(int programId
			);

	ResponseEntity<ResponseStructure<List<ClassHourResponse>>> assignPeriods(List<ClassHourRequest> classHour);

	ResponseEntity<ResponseStructure<String>> classHourForNextWeek(int programId);

}
