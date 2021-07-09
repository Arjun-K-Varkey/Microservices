package com.olx.exception;

public class InvalidCategoryIdException extends RuntimeException {

	private String message;
	public InvalidCategoryIdException() {
		this.message = "";
	}
	public InvalidCategoryIdException(String message) {
		this.message = message;
	}
	public String toString() {
		return "Invalid category id: " + this.message;
	}
}
