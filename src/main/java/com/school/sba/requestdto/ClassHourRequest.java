package com.school.sba.requestdto;

import java.time.LocalTime;

import com.school.sba.enums.ClassStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClassHourRequest {

	private int classHourId;
	private int subjectId;
	private int userId;
	private int roomNo;

}
