package com.projet5.safetyNet.Exception;

public class InvalidDateFormatException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public InvalidDateFormatException(String message) {
		super(message);
	}
}
