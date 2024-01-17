package com.school.sba.requestdto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SchoolRequest {
	
	private String schoolName;
	private long contactNo;
	private String emailId;
	private String address;

}
