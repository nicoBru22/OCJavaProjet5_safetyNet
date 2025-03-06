package com.projet5.safetyNet.Exception;

public class PersonExistingException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public PersonExistingException(String message) {
		super(message);
	}

}
