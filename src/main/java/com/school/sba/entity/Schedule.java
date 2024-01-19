package com.school.sba.entity;

import java.time.Duration;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
public class Schedule {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private  int scheduleId;
	private LocalTime opensAt;
	private LocalTime closeAt;
	private int classHoursPerDay;
	private Duration classHourLengthInMinutes;
	private LocalTime breakTime;
	private Duration breakLengthInMinutes;
	private LocalTime lunchTime;
	private Duration lunchLengthInMinutes;
	

}
