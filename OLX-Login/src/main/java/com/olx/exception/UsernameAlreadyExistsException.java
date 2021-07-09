package com.olx.exception;

public class UsernameAlreadyExistsException extends RuntimeException {

	private String message = "";
	public UsernameAlreadyExistsException() {}
	public UsernameAlreadyExistsException(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "Username Already Exists " + message;
	}
	
}
