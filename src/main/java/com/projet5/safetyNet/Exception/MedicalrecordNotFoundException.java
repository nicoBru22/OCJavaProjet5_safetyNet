package com.projet5.safetyNet.Exception;

public class MedicalrecordNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public MedicalrecordNotFoundException(String message) {
		super(message);
	}

}
