package com.school.sba.responsedto;

import com.school.sba.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
	
	private int userId;
	private String userName;
	private String firstName;
	private String lastName;
	private long contactNo;
	private String email;
	private boolean isDeleted;
	private UserRole userRole;
	

}
