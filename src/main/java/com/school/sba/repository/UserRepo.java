package com.school.sba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.sba.entity.User;
import com.school.sba.enums.UserRole;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

	public boolean existsByUserRole(UserRole userRole) ;

}
