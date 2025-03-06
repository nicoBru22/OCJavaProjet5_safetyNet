package com.projet5.safetyNet.Exception;

public class InvalidBirthdateException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidBirthdateException(String message) {
        super(message);
    }
}
