package com.school.sba.serviceimpl;

import java.io.Console;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.ClassHour;
import com.school.sba.entity.School;
import com.school.sba.entity.Subject;
import com.school.sba.entity.User;
import com.school.sba.enums.UserRole;
import com.school.sba.exception.AcademicProgramNotFoundException;
import com.school.sba.exception.AdminCannotBeAssignedToAcademicProgram;
import com.school.sba.exception.AdminNotFoundException;
import com.school.sba.exception.ExistingAdminException;
import com.school.sba.exception.InvalidUserRoleAdminException;
import com.school.sba.exception.InvalidUserRoleException;
import com.school.sba.exception.SubjectNotFoundException;
import com.school.sba.exception.TeacherNotFoundException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepo;
import com.school.sba.repository.ClassHourRepo;
import com.school.sba.repository.SubjectRepo;
import com.school.sba.repository.UserRepo;
import com.school.sba.requestdto.UserRequest;
import com.school.sba.responsedto.AcademicProgramRequest;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.service.UserService;
import com.school.sba.utility.ResponseEntityProxy;
import com.school.sba.utility.ResponseStructure;

import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@org.springframework.stereotype.Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo repo;

	@Autowired
	private ClassHourRepo classHourRepo;
	@Autowired
	private SubjectRepo subjectRepo;

	@Autowired
	ResponseStructure<UserResponse> structrure;

	@Autowired
	private AcademicProgramRepo academicProgramRepo;

	@Autowired
	private PasswordEncoder encoder;

	private User mapUserRequestToUser(UserRequest userRequest) {
		return	User.builder()
				.userName(userRequest.getUserName())
				.password(encoder.encode(userRequest.getPassword()))
				.firstName(userRequest.getFirstName())
				.lastName(userRequest.getLastName())
				.email(userRequest.getEmail())
				.contactNo(userRequest.getContactNo())
				.userRole(userRequest.getUserRole())
				.build();
	}
	public UserResponse mapUserToUserResponse(User user) {
		return UserResponse.builder()
				.userId(user.getUserId())
				.userName(user.getUserName())
				.email(user.getEmail())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.contactNo(user.getContactNo())
				.userRole(user.getUserRole())

				.build();
	}
	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> registerAdmin(UserRequest userRequest)
	{
		if(userRequest.getUserRole().equals(UserRole.ADMIN)) {
			if(repo.existsByUserRole(userRequest.getUserRole())==false) {

				User user = repo.save(mapUserRequestToUser(userRequest));
				user.isDeleted();
				structrure.setStatus(HttpStatus.CREATED.value());
				structrure.setMessage(" user saved succesfully");
				structrure.setData(mapUserToUserResponse(user));
				return new ResponseEntity<ResponseStructure<UserResponse>>(structrure,HttpStatus.CREATED); 
			}


			else {
				throw new ExistingAdminException("admin already  register");
			}}
		else {
			throw new AdminNotFoundException("admin is not found");
		}

	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> addOtherUser(UserRequest userRequest) {

		//		List<User> users = repo.findByIsDeletedIsTrue();
		//		
		//		for(User user:users) {
		//			System.out.println(user);
		//		}
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		if(userRequest.getUserRole().equals(UserRole.ADMIN)) {
			throw new ExistingAdminException("admin is already present");
		}
		else {

			return repo.findByUserName(userName).map(admin->{
				School school = admin.getSchool();
				User user=mapUserRequestToUser(userRequest);
				user.setSchool(school);
				repo.save(user);
				structrure.setStatus(HttpStatus.CREATED.value());
				structrure.setMessage(" user saved succesfully");
				structrure.setData(mapUserToUserResponse(user));
				return new ResponseEntity<ResponseStructure<UserResponse>>(structrure,HttpStatus.CREATED); 
			}).orElseThrow(()-> new AdminNotFoundException("admin is not found"));


		}



	}
	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(UserRequest userRequest) {

		if(userRequest.getUserRole().equals(UserRole.ADMIN)) {
			if(repo.existsByUserRole(userRequest.getUserRole())==false) {

				User user = repo.save(mapUserRequestToUser(userRequest));
				structrure.setStatus(HttpStatus.CREATED.value());
				structrure.setMessage(" user saved succesfully");
				structrure.setData(mapUserToUserResponse(user));
				return new ResponseEntity<ResponseStructure<UserResponse>>(structrure,HttpStatus.CREATED); 

			}
			else {
				throw new ExistingAdminException("admin is already existing");

			}
		}
		else {

			User user = repo.save(mapUserRequestToUser(userRequest));
			structrure.setStatus(HttpStatus.CREATED.value());
			structrure.setMessage(" user saved succesfully");
			structrure.setData(mapUserToUserResponse(user));
			return new ResponseEntity<ResponseStructure<UserResponse>>(structrure,HttpStatus.CREATED); 
		}



	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> deleteUser(int userId) {

		User user = repo.findById(userId).orElseThrow(()-> new UserNotFoundByIdException("user not found"));
		user.setDeleted(true);
		repo.save(user);

		structrure.setStatus(HttpStatus.OK.value());
		structrure.setMessage("user is soft deleted");
		structrure.setData(mapUserToUserResponse(user));


		return new ResponseEntity<ResponseStructure<UserResponse>>(structrure,HttpStatus.OK);

	}
	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> findUser(int userId) {
		User user = repo.findById(userId).orElseThrow(()-> new UserNotFoundByIdException("user not found"));

		structrure.setStatus(HttpStatus.FOUND.value());
		structrure.setMessage("user found successfully");
		structrure.setData(mapUserToUserResponse(user));

		return new ResponseEntity<ResponseStructure<UserResponse>>(structrure,HttpStatus.FOUND);
	}


	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> addUserToAcademic(int programId,
			int userId) 
	{
		return repo.findById(userId)
				.map(user -> {
					if(user.getUserRole().equals(UserRole.ADMIN)) {
						throw new AdminCannotBeAssignedToAcademicProgram("admin cannot be assigned");
					}
					else {
						return academicProgramRepo.findById(programId)
								.map(academicProgram -> {
									if(user.getUserRole().equals(UserRole.TEACHER))
									{

										if(academicProgram.getListOfSubjects().contains(user.getSubject()))
										{
											academicProgram.getListOfUsers().add(user);
											repo.save(user);
											academicProgramRepo.save(academicProgram);

											structrure.setData(mapUserToUserResponse(user));
											structrure.setMessage("added user to academic");
											structrure.setStatus(HttpStatus.OK.value());

											return new ResponseEntity<ResponseStructure<UserResponse>>(structrure, HttpStatus.OK);
										}

									}
									else if	(user.getUserRole().equals(UserRole.STUDENT)) {
										academicProgram.getListOfUsers().add(user);
										repo.save(user);
										academicProgramRepo.save(academicProgram);

										structrure.setData(mapUserToUserResponse(user));
										structrure.setMessage("added user to academic");
										structrure.setStatus(HttpStatus.OK.value());

										return new ResponseEntity<ResponseStructure<UserResponse>>(structrure,HttpStatus.OK);	
									}
									else {
										throw new TeacherNotFoundException("teacher is not found");}
									return null;


								})
								.orElseThrow(() -> new AcademicProgramNotFoundException("academic program not found"));
					}
				})
				.orElseThrow(() -> new UserNotFoundByIdException("user not found"));

	}



	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> addSubjectToUser(int subjectId, int userId) {

		User user = repo.findById(userId).orElseThrow(()-> new UserNotFoundByIdException("user not found"));

		Subject subject = subjectRepo.findById(subjectId).orElseThrow(()->new SubjectNotFoundException("subject not found"));

		if(user.getUserRole().equals(UserRole.TEACHER)) {

			user.setSubject(subject);
			repo.save(user);
			structrure.setMessage("subject added to user successfully");
			structrure.setStatus(HttpStatus.OK.value());
			structrure.setData(mapUserToUserResponse(user));

			return new ResponseEntity<ResponseStructure<UserResponse>>(structrure,HttpStatus.OK);
		}
		else {
			throw new TeacherNotFoundException("teacher  not found");
		}
	}
	@Override
	public ResponseEntity<ResponseStructure<List<UserResponse>>> getUsers(int programId, String userRole) {


		return academicProgramRepo.findById(programId)
				.map(academicProgram->{
					UserRole user = UserRole.valueOf(userRole.toUpperCase());

					if(EnumSet.allOf(UserRole.class).contains(user)) {
						if(user.equals(UserRole.ADMIN)) {
							throw new InvalidUserRoleAdminException("admin cannot fetched");
						}


						List<UserResponse> listOfUsers =repo.findByUserRoleAndListofAcademicPrograms( user,academicProgram).stream().map(this::mapUserToUserResponse).collect(Collectors.toList());
						if(listOfUsers.isEmpty()) {
							return ResponseEntityProxy.setResponseStructure(HttpStatus.NOT_FOUND, "user is not found", listOfUsers);
						}
						else {
							return ResponseEntityProxy.setResponseStructure(HttpStatus.FOUND,"get all user with specified user role",listOfUsers);
						}

					}
					else {
						throw new InvalidUserRoleException("invalid user rolee");
					}
				})
				.orElseThrow(()->new AcademicProgramNotFoundException("academic program is not found"));
	}


	public void hardDeleteUser(int userId) {
		User user = repo.findById(userId).orElseThrow(()-> new UserNotFoundByIdException("user is not found"));

		List<ClassHour> classHours = classHourRepo.findByUser(user);
		for(ClassHour classHour:classHours) {
			classHour.setUser(null);
			classHourRepo.save(classHour);
		}
		List<AcademicProgram> academicPrograms = user.getListofAcademicPrograms();
		for(AcademicProgram academic :academicPrograms) {
			academic.setListOfUsers(null);
			academicProgramRepo.save(academic);
		}


		repo.delete(user);

	}




}
