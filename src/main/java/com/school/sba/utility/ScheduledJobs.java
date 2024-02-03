package com.school.sba.utility;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.School;
import com.school.sba.entity.User;
import com.school.sba.repository.AcademicProgramRepo;
import com.school.sba.repository.ClassHourRepo;
import com.school.sba.repository.SchoolRepo;
import com.school.sba.repository.UserRepo;
import com.school.sba.serviceimpl.AcademicProgramImpl;
import com.school.sba.serviceimpl.ClassHourServiceImpl;
import com.school.sba.serviceimpl.SchoolServiceImpl;
import com.school.sba.serviceimpl.UserServiceImpl;

import jakarta.transaction.Transactional;

@Component
public class ScheduledJobs {

	@Autowired
	private UserRepo repo;

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private AcademicProgramImpl academicService;

	@Autowired
	private ClassHourRepo classHourRepo;

	@Autowired
	private AcademicProgramRepo academicRepo;

	@Autowired
	private SchoolRepo schoolRepo;

	@Autowired
	private SchoolServiceImpl schoolService;
	
	@Autowired
	private ClassHourServiceImpl classHourServiceImpl;

//	@Scheduled(fixedDelay = 1000l)
//	public void test() {
//		System.out.println("scheduled jobs!!!!");
//	}
//
//	@Scheduled(fixedDelay = 1000l)
//	@Transactional
//	public void hardDeleteUser() {
//
//		List<User> users = repo.findByIsDeletedIsTrue();
//		for(User user:users) {
//
//
//			userService.hardDeleteUser(user.getUserId());
//		}
//	}
//	@Scheduled(fixedDelay = 1000l)
//	@Transactional
//	public void hardDeleteAcademicProgram() {
//
//		List<AcademicProgram> academicProgram = academicRepo.findByIsDeletedIsTrue();
//		for(AcademicProgram academic:academicProgram) {
//			academicService.hardDeleteAcademic(academic.getProgramId());
//		}
//	}
//
//	@Scheduled(fixedDelay=1000l)
//	@Transactional
//	public void hardDeleteSchool() {
//
//		List<School> schools = schoolRepo.findByIsDeletedIsTrue();
//		for(School school:schools) {
//			schoolService.hardDeleteSchool(school.getSchoolId());
//		}
//	}
//	
//	
//	@Scheduled(cron = "0 0 0 ? * MON")
//	void classHourWeekly() {
//		
//		AcademicProgram ap = academicRepo.findById(1).orElseThrow();
//		
//		if(ap.isAutoRepeat()) {
//			
//			
//			classHourServiceImpl.classHourForNextWeek(ap.getProgramId());
//			
//		}
//		
//		
//	}
}
