package com.jacobo.adyd.login.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class LoginException extends RuntimeException {
	
	private HttpStatus status;
	private String message;
	
	public LoginException(String message, HttpStatus status) {
		super(message);
		this.status = status;
		this.message =message;
	}
	
	

}
