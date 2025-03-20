package com.projet5.safetyNet.Exception;

/**
 * Exception levée lorsqu'une caserne de pompiers existe déjà et qu'une duplication est détectée.
 */
public class FirestationExistingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de l'exception avec un message personnalisé.
     *
     * @param message le message décrivant l'erreur
     */
    public FirestationExistingException(String message) {
        super(message);
    }
}

