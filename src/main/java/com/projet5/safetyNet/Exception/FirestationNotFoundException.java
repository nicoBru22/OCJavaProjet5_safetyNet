package com.projet5.safetyNet.Exception;

public class FirestationNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public FirestationNotFoundException (String message) {
		super(message);
	}

}
