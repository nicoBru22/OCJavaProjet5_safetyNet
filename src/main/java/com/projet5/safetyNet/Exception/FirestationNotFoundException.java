package com.projet5.safetyNet.Exception;

/**
 * Exception levée lorsqu'une caserne de pompiers recherchée n'est pas trouvée.
 */
public class FirestationNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de l'exception avec un message personnalisé.
     *
     * @param message le message décrivant l'erreur
     */
    public FirestationNotFoundException(String message) {
        super(message);
    }
}

