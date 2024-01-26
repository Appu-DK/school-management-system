package com.school.sba.serviceimpl;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.ClassHour;
import com.school.sba.entity.Schedule;
import com.school.sba.enums.ClassStatus;
import com.school.sba.exception.AcademicProgramNotFoundException;
import com.school.sba.repository.AcademicProgramRepo;
import com.school.sba.repository.ClassHourRepo;
import com.school.sba.responsedto.ClassHourResponse;
import com.school.sba.service.ClassHourService;
import com.school.sba.utility.ResponseStructure;

@Service
public class ClassHourServiceImpl implements ClassHourService {

	@Autowired
	private ClassHourRepo classHourRepo;

	@Autowired
	private AcademicProgramRepo academicProgramRepo;

	@Autowired
	private ResponseStructure<String> structure;

	private boolean isBreakTime(LocalDateTime currentTime, Schedule schedule) {
		LocalTime breakTimeStart = schedule.getBreakTime();
		LocalTime breakTimeEnd = breakTimeStart.plusMinutes(schedule.getBreakLengthInMinutes().toMinutes());

		return (currentTime.toLocalTime().isAfter(breakTimeStart) && currentTime.toLocalTime().isBefore(breakTimeEnd));
	}

	private boolean isLunchTime(LocalDateTime currentTime, Schedule schedule) {
		LocalTime lunchStartTime = schedule.getLunchTime();
		LocalTime lunchEndTime = lunchStartTime.plusMinutes(schedule.getLunchLengthInMinutes().toMinutes());

		return (currentTime.toLocalTime().isAfter(lunchStartTime) && currentTime.toLocalTime().isAfter(lunchEndTime));
	}

	@Override
	public ResponseEntity<ResponseStructure<String>> createClassHour(int programId) {

		return academicProgramRepo.findById(programId).map(academicProgram->{
			Schedule schedule = academicProgram.getSchool().getSchedule();
			if(schedule!=null) {

				int classHourPerDay=schedule.getClassHoursPerDay();
				int classHourLength=(int)schedule.getClassHourLengthInMinutes().toMinutes();

				LocalDateTime currentTime=LocalDateTime.now().with(schedule.getOpensAt());

				LocalTime breakStartTime=schedule.getBreakTime();
				LocalTime breakEndTime=breakStartTime.plusMinutes(schedule.getBreakLengthInMinutes().toMinutes());
				LocalTime lunchTimeStart=schedule.getLunchTime();
				LocalTime lunchEndTime=lunchTimeStart.plusMinutes(schedule.getLunchLengthInMinutes().toMinutes());


				for(int day=1;day<=6;day++) {
					
					for(int hour=0;hour<classHourPerDay+2;hour++) {
						
						ClassHour classHour=new ClassHour();

						if(!currentTime.toLocalTime().equals(lunchTimeStart)&&!isLunchTime(currentTime, schedule))
						{
							if(!currentTime.toLocalTime().equals(breakStartTime)&&!isBreakTime(currentTime, schedule))
							{
								LocalDateTime beginsAt=currentTime;
								LocalDateTime endsAt=beginsAt.plusMinutes(classHourLength);
								
								classHour.setClassBeginsAt(beginsAt);
								classHour.setClassEndsAt(endsAt);
								classHour.setClassStatus(ClassStatus.LUNCH_TIME);
								
								beginsAt=endsAt;
							}
							else {
								classHour.setClassBeginsAt(currentTime);
								classHour.setClassEndsAt(LocalDateTime.now().with(breakEndTime));
								classHour.setClassStatus(ClassStatus.BREAK_TIME);
								currentTime = currentTime.plusMinutes(schedule.getBreakLengthInMinutes().toMinutes());
							}
						}
						else {

							classHour.setClassBeginsAt(currentTime);
							classHour.setClassEndsAt(LocalDateTime.now().with(lunchEndTime));
							classHour.setClassStatus(ClassStatus.LUNCH_TIME);
							currentTime = currentTime.plusMinutes(schedule.getLunchLengthInMinutes().toMinutes());
						}
						classHour.setAcademicPrograms(academicProgram);
						classHourRepo.save(classHour);
					}

					currentTime=currentTime.plusDays(1).with(schedule.getOpensAt());
				}
				}
			else {
				throw new RuntimeException("school does  not have any schedule ");
			}
			structure.setMessage("class hour generated");
			structure.setStatus(HttpStatus.CREATED.value());
			structure.setData("class hour is genereated for the academicprogram");
			
			return	new ResponseEntity<ResponseStructure<String>>(structure, HttpStatus.CREATED);
					
		

		}).orElseThrow(()->new AcademicProgramNotFoundException("academic program is not found"));

		}
}
