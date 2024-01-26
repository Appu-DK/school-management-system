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

	private LocalTime classBeginsAt;
	private LocalTime classEndsAt;
	private int classRoomNumber;
	private ClassStatus classStatus;

}
