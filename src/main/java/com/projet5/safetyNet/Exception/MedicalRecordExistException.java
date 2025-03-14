package com.projet5.safetyNet.Exception;

public class MedicalRecordExistException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public MedicalRecordExistException(String message) {
		super(message);
	}

}
