package com.projet5.safetyNet.Exception;

/**
 * Exception levée lorsqu'une personne existe déjà et qu'une duplication est détectée.
 */
public class PersonExistingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de l'exception avec un message personnalisé.
     *
     * @param message le message décrivant l'erreur
     */
    public PersonExistingException(String message) {
        super(message);
    }
}
