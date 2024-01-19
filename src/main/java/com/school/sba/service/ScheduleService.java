package com.school.sba.service;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.ScheduleRequest;
import com.school.sba.responsedto.ScheduleResponse;
import com.school.sba.utility.ResponseStructure;

public interface ScheduleService {

	ResponseEntity<ResponseStructure<ScheduleResponse>> saveSchedule(int schoolId, ScheduleRequest scheduleRequest);

	ResponseEntity<ResponseStructure<ScheduleResponse>> findSchedule(int scheduleId);

	ResponseEntity<ResponseStructure<ScheduleResponse>> updateSchedule(int scheduleId, ScheduleRequest scheduleRequest);

	

}
