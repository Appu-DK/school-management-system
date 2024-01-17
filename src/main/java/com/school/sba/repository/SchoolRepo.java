package com.school.sba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.sba.entity.School;
import com.school.sba.requestdto.SchoolRequest;
import com.school.sba.responsedto.SchoolResponse;

@Repository
public interface SchoolRepo extends JpaRepository<School, Integer> {

	public boolean existsBySchoolId(int schoolId);
}
