package com.school.sba.requestdto;

import com.school.sba.enums.UserRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
	
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private long contactNo;
	private String email;
	private UserRole userRole;

}
