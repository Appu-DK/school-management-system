package com.school.sba.serviceimpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.loader.ast.spi.Loadable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.ClassHour;
import com.school.sba.entity.Schedule;
import com.school.sba.entity.Subject;
import com.school.sba.entity.User;
import com.school.sba.enums.ClassStatus;
import com.school.sba.enums.UserRole;
import com.school.sba.exception.AcademicProgramNotFoundException;
import com.school.sba.exception.ClassHourAlreadyExistingException;
import com.school.sba.exception.ClassHourNotFoundByIdException;
import com.school.sba.exception.ClassIsAlreadyOccupied;
import com.school.sba.exception.ScheduleNotFoundException;
import com.school.sba.exception.SubjectAndUserNotAssignToClassHourException;
import com.school.sba.exception.SubjectNotFoundException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepo;
import com.school.sba.repository.ClassHourRepo;
import com.school.sba.repository.SubjectRepo;
import com.school.sba.repository.UserRepo;
import com.school.sba.requestdto.ClassHourRequest;
import com.school.sba.responsedto.ClassHourResponse;
import com.school.sba.service.ClassHourService;
import com.school.sba.utility.ResponseEntityProxy;
import com.school.sba.utility.ResponseStructure;

@Service
public class ClassHourServiceImpl implements ClassHourService {

	@Autowired
	private ClassHourRepo classHourRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private AcademicProgramRepo academicProgramRepo;

	@Autowired
	private ResponseStructure<String> structure;

	@Autowired
	private SubjectRepo subjectRepo;

	@Autowired
	private ResponseStructure<List<ClassHourResponse>> listStructure;


	private List<ClassHourResponse> mapClassHourToClassHourResponse(List<ClassHour>listOfClassHours){

		List<ClassHourResponse>listOfClassHourResponses=new ArrayList<>();
		listOfClassHours.forEach(classHour->{
			ClassHourResponse response=new ClassHourResponse();
			response.setClassBeginsAt(classHour.getClassBeginsAt());
			response.setClassEndsAt(classHour.getClassEndsAt());
			response.setClassHourId(classHour.getClassHourId());
			response.setClassRoomNumber(classHour.getClassRoomNumber());
			response.setClassStatus(classHour.getClassStatus());
			listOfClassHourResponses.add(response);
		});
		return listOfClassHourResponses;
	}

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
			System.out.println(academicProgram.getListOfClassHours());
			if(schedule!=null)
			{
				if(academicProgram.getListOfClassHours().isEmpty())
				{
					LocalDate nextSaturday=null;

					if(LocalDate.now().equals(DayOfWeek.MONDAY)==false) {
						nextSaturday=LocalDate.now().plusWeeks(1).with(DayOfWeek.SATURDAY);
					}
					else {
						nextSaturday=LocalDate.now().with(DayOfWeek.SATURDAY);

					}
					addClassHour(academicProgram, schedule, nextSaturday);
				}
				else 
				{

					LocalDateTime start=academicProgram.getListOfClassHours().getFirst().getClassBeginsAt();
					LocalDateTime end=academicProgram.getListOfClassHours().getLast().getClassEndsAt();

					if((LocalDate.now().isAfter(start.toLocalDate())||(LocalDate.now().isEqual(start.toLocalDate()))
							&&
							(LocalDate.now().isBefore(end.toLocalDate()))||(LocalDate.now().isEqual(end.toLocalDate()))==false
							&&(LocalDate.now().minusDays(1).isAfter(academicProgram.getProgramBeginsAt()))&&(LocalDate.now().isBefore(academicProgram.getProgramEndsAt()))))
					{

						LocalDate	nextSaturday=LocalDate.now().with(DayOfWeek.SATURDAY);
						addClassHour(academicProgram, schedule, nextSaturday);
					}
					else 
						throw new ClassHourAlreadyExistingException("class hour is already generated for this ");

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

	public void addClassHour(AcademicProgram academicProgram,Schedule schedule,LocalDate nextSaturday) {


		int classHourPerDay=schedule.getClassHoursPerDay();
		int classHourLength=(int)schedule.getClassHourLengthInMinutes().toMinutes();

		LocalDate currentDate=LocalDate.now();
		LocalDateTime currentTime=LocalDateTime.now().with(schedule.getOpensAt());

		LocalTime breakStartTime=schedule.getBreakTime();
		LocalTime breakEndTime=breakStartTime.plusMinutes(schedule.getBreakLengthInMinutes().toMinutes());
		LocalTime lunchTimeStart=schedule.getLunchTime();
		LocalTime lunchEndTime=lunchTimeStart.plusMinutes(schedule.getLunchLengthInMinutes().toMinutes());


		while(currentDate.isBefore(nextSaturday.plusDays(1))){
			if(currentDate.getDayOfWeek()!=DayOfWeek.SUNDAY) {
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
							classHour.setClassStatus(ClassStatus.NOT_SCHEDULED);

							currentTime=endsAt;
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
				}// for ends

				currentTime=currentTime.plusDays(1).with(schedule.getOpensAt());
			}// if condn

			currentDate=currentDate.plusDays(1);
		}// while ends


	}

	@Override
	public ResponseEntity<ResponseStructure<List<ClassHourResponse>>> assignPeriods(List<ClassHourRequest> classHourRequest) {

		List<ClassHour> listOfClassHour=new ArrayList<>();
		for(ClassHourRequest classHours:classHourRequest) {

			User user=userRepo.findById(classHours.getUserId()).orElseThrow(()-> new UserNotFoundByIdException("user is not found"));
			Subject subject = subjectRepo.findById(classHours.getSubjectId()).orElseThrow(()-> new SubjectNotFoundException("subject is not found"));

			ClassHour classHour = classHourRepo.findById(classHours.getClassHourId()).orElseThrow(()-> new ClassHourNotFoundByIdException("class hour is not found"));

			if(user.getSubject().equals(subject)&&user.getUserRole().equals(UserRole.TEACHER)&&user.getListofAcademicPrograms().contains(classHour.getAcademicPrograms()))
			{



				LocalDateTime currentTime = LocalDateTime.now();
				if(classHourRepo.existsByClassBeginsAtAndClassRoomNumber(classHour.getClassBeginsAt(), classHour.getClassRoomNumber()))
				{

					if(currentTime.isAfter(classHour.getClassBeginsAt())&&currentTime.isBefore(classHour.getClassEndsAt())) {

						classHour.setUser(user);
						classHour.setSubject(subject);
						classHour.setClassRoomNumber(classHours.getClassHourId());
						classHour.setClassStatus(ClassStatus.ONGOING);
						classHourRepo.save(classHour);
						listOfClassHour.add(classHour);
					}
					else if(currentTime.isAfter(classHour.getClassEndsAt())) {
						classHour.setUser(user);
						classHour.setSubject(subject);
						classHour.setClassRoomNumber(classHours.getClassHourId());
						classHour.setClassStatus(ClassStatus.FINISHED);
						classHourRepo.save(classHour);
						listOfClassHour.add(classHour);
					}
					else {
						classHour.setUser(user);
						classHour.setSubject(subject);
						classHour.setClassRoomNumber(classHours.getClassHourId());
						classHour.setClassStatus(ClassStatus.UPCOMING);
						classHourRepo.save(classHour);
						listOfClassHour.add(classHour);
					}
				}
				else
					throw new ClassIsAlreadyOccupied("class hour is already occupied by other");
			}	
			else  
				throw new SubjectAndUserNotAssignToClassHourException("user and subject is not assign to classHour");

		}
		classHourRepo.saveAll(listOfClassHour);

		listStructure.setData(mapClassHourToClassHourResponse(listOfClassHour));
		listStructure.setMessage("class hours updated");
		listStructure.setStatus(HttpStatus.FOUND.value());

		return new ResponseEntity<ResponseStructure<List<ClassHourResponse>>>(listStructure,HttpStatus.FOUND);

	}

	public void deleteClassHour(List<ClassHour> classHour) {

		classHourRepo.deleteAll();
	}

	@Override
	public ResponseEntity<ResponseStructure<String>> classHourForNextWeek(int programId) {
		AcademicProgram acadedmic = academicProgramRepo.findById(programId).orElseThrow(()->new AcademicProgramNotFoundException("academic not found"));
		LocalDateTime previousEnd = acadedmic.getListOfClassHours().getLast().getClassEndsAt();
		System.out.println(previousEnd);
		
		LocalDateTime previousBegin = previousEnd.minusDays(6);
		System.err.println(previousBegin);
		;

		List<ClassHour> classHourWeek=new ArrayList<>();


		classHourRepo.findByClassEndsAtBetween(previousBegin, previousEnd).forEach(classHour->{
			classHourWeek.add(ClassHour.builder().classBeginsAt(classHour.getClassBeginsAt().plusWeeks(1))
					.classEndsAt(classHour.getClassEndsAt().plusWeeks(1))
					.academicPrograms(classHour.getAcademicPrograms())
					.classStatus(classHour.getClassStatus())
					.classRoomNumber(classHour.getClassRoomNumber())
					.subject(classHour.getSubject())
					.user(classHour.getUser())
					.build());

		});

		classHourRepo.saveAll(classHourWeek);
		return ResponseEntityProxy.setResponseStructure(HttpStatus.OK, "generated for next week", "next week class hourss generated");

	}



}
