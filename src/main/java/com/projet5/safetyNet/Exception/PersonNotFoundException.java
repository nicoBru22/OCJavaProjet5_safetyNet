package com.projet5.safetyNet.Exception;

/**
 * Exception levée lorsqu'une personne n'est pas trouvée.
 */
public class PersonNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de l'exception avec un message personnalisé.
     *
     * @param message le message décrivant l'erreur
     */
    public PersonNotFoundException(String message) {
        super(message);
    }
}

