package com.school.sba.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.requestdto.SchoolRequest;
import com.school.sba.responsedto.SchoolResponse;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.utility.ResponseStructure;

import jakarta.validation.Valid;


public interface SchoolService {

	ResponseEntity<ResponseStructure<SchoolResponse>> saveSchool(int userId, @Valid SchoolRequest schoolRequest);

}
