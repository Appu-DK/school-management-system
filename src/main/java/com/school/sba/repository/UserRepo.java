package com.school.sba.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.School;
import com.school.sba.entity.User;
import com.school.sba.enums.UserRole;


public interface UserRepo extends JpaRepository<User,Integer> {

	public boolean existsByUserRole(UserRole userRole) ;

	public boolean existsByUserRole(int userId) ;

	public Optional<User> findByUserName(String userName);

	public List<User> findByUserRoleAndListofAcademicPrograms(UserRole userRole,AcademicProgram academicProgram);


	public List<User> findByIsDeletedIsTrue();
	
	public List<User>  findBySchool(School school);

}
