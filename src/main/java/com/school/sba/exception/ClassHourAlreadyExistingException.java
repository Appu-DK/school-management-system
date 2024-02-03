package com.school.sba.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter

public class ClassHourAlreadyExistingException extends RuntimeException {

	private String message;
}
