package com.school.sba.serviceimpl;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.Schedule;
import com.school.sba.entity.School;
import com.school.sba.exception.ScheduleAlreadyExistingException;
import com.school.sba.exception.ScheduleNotFoundException;
import com.school.sba.exception.SchoolAlreadyExistingException;
import com.school.sba.exception.SchoolNotFoundByIdException;
import com.school.sba.exception.SchoolNotFoundException;
import com.school.sba.repository.ScheduleRepo;
import com.school.sba.repository.SchoolRepo;
import com.school.sba.requestdto.ScheduleRequest;
import com.school.sba.responsedto.ScheduleResponse;
import com.school.sba.service.ScheduleService;
import com.school.sba.utility.ResponseStructure;

@Service
public class ScheduleServiceImpl implements ScheduleService{

	@Autowired
	private ScheduleRepo scheduleRepo;

	@Autowired
	private SchoolRepo schoolRepo;

	@Autowired
	private ResponseStructure<ScheduleResponse> scheduleStructure;


	private Schedule  mapScheduleRequestToSchedule(ScheduleRequest scheduleRequest) {
		return Schedule.builder()
				.opensAt(scheduleRequest.getOpensAt())
				.closeAt(scheduleRequest.getCloseAt())
				.classHoursPerDay(scheduleRequest.getClassHoursPerDay())
				.classHourLengthInMinutes(Duration.ofMinutes(scheduleRequest.getBreakLengthInMinutes()))
				.breakTime(scheduleRequest.getBreakTime())
				.breakLengthInMinutes(Duration.ofMinutes(scheduleRequest.getBreakLengthInMinutes()))
				.lunchTime(scheduleRequest.getLunchTime())
				.lunchLengthInMinutes(Duration.ofMinutes(scheduleRequest.getBreakLengthInMinutes()))
				.build();

	}

	private ScheduleResponse mapScheduleToScheduleResponse(Schedule schedule) {
		return ScheduleResponse.builder()
				.scheduleId(schedule.getScheduleId())
				.opensAt(schedule.getOpensAt())
				.closeAt(schedule.getCloseAt())
				.classHourLengthInMinutes((int)
						(Duration.ofMinutes(schedule.getClassHourLengthInMinutes().toMinutes())
								.toMinutes()))
				.breakLengthInMinutes((int)(Duration.ofMinutes(schedule.getBreakLengthInMinutes().toMinutes()).toMinutes()))
				.breakTime(schedule.getBreakTime())
				.lunchLengthInMinutes((int)Duration.ofMinutes(schedule.getLunchLengthInMinutes().toMinutes()).toMinutes())
				.lunchTime(schedule.getLunchTime())
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> saveSchedule(int schoolId,
			ScheduleRequest scheduleRequest) {
		return   schoolRepo.findById(schoolId).map(school->{
			if(school.getSchedule()==null) {
				Schedule schedule = mapScheduleRequestToSchedule(scheduleRequest);
				schedule=scheduleRepo.save(schedule);
				school.setSchedule(schedule);
				schoolRepo.save(school);
				scheduleStructure.setStatus(HttpStatus.CREATED.value());
				scheduleStructure.setMessage("schedule created succesfully");
				scheduleStructure.setData(mapScheduleToScheduleResponse(schedule));

				return new ResponseEntity<ResponseStructure<ScheduleResponse>>(scheduleStructure,HttpStatus.CREATED);
			}else {
				throw new ScheduleAlreadyExistingException("schedule is already existing");
			}
		}).orElseThrow(()->new SchoolNotFoundException("school is  not found"));


	}

	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> findSchedule(int schoolId) {

		School school=schoolRepo.findById(schoolId)
				.orElseThrow(()->new SchoolNotFoundByIdException("school not found"));

		return scheduleRepo.findById(school.getSchedule().getScheduleId())
				.map(schedule->{
					scheduleStructure.setStatus(HttpStatus.FOUND.value());
					scheduleStructure.setMessage("schedule found");
					scheduleStructure.setData(mapScheduleToScheduleResponse(schedule));

					return new ResponseEntity<ResponseStructure<ScheduleResponse>>(scheduleStructure,HttpStatus.FOUND);
				})
				.orElseThrow(()->new ScheduleNotFoundException("schedule not found"));
	}


	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> updateSchedule(int scheduleId,
			ScheduleRequest scheduleRequest) {

		return scheduleRepo.findById(scheduleId)
				.map(schedule->{ schedule=scheduleRepo.save(mapScheduleRequestToSchedule(scheduleRequest));
				scheduleStructure.setMessage("schedule updated successfully");
				scheduleStructure.setStatus(HttpStatus.OK.value());;
				scheduleStructure.setData(mapScheduleToScheduleResponse(schedule));

				return new ResponseEntity<ResponseStructure<ScheduleResponse>>(scheduleStructure,HttpStatus.OK);


				})
				.orElseThrow(()-> new ScheduleNotFoundException("schedule not found"));

	}



}
