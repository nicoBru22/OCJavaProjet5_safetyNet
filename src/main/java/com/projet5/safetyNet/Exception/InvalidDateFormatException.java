package com.projet5.safetyNet.Exception;

/**
 * Exception levée lorsqu'un format de date invalide est détecté.
 */
public class InvalidDateFormatException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de l'exception avec un message personnalisé.
     *
     * @param message le message décrivant l'erreur
     */
    public InvalidDateFormatException(String message) {
        super(message);
    }
}

