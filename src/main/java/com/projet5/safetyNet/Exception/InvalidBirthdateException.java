package com.projet5.safetyNet.Exception;

/**
 * Exception levée lorsqu'une date de naissance invalide est détectée.
 */
public class InvalidBirthdateException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de l'exception avec un message personnalisé.
     *
     * @param message le message décrivant l'erreur
     */
    public InvalidBirthdateException(String message) {
        super(message);
    }
}

