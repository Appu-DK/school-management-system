package com.school.sba.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class School {
	
	@Id
	private int schoolId;
	private String schoolName;
	private long contactNo;
	private String emailId;
	private String address;
	
	
	

}
