package com.school.sba.requestdto;

import java.time.Duration;
import java.time.LocalTime;

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
public class ScheduleRequest {
	
	private LocalTime opensAt;
	private LocalTime closeAt;
	private int classHoursPerDay;
	private int classHourLengthInMinutes;
	private LocalTime breakTime;
	private int  breakLengthInMinutes;
	private LocalTime lunchTime;
	private int lunchLengthInMinutes;
	

}
