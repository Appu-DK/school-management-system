package com.school.sba.entity;

import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Schedule {
	
	@Id
	private  int scheduleId;
	private LocalTime opensAt;
	private LocalTime closeAt;
	private int classHoursPerDay;
	private LocalTime classHourLength;
	private LocalTime breakTime;
	private LocalTime breakLength;
	private LocalTime lunchTime;
	private LocalTime lunchLength;
	

}
