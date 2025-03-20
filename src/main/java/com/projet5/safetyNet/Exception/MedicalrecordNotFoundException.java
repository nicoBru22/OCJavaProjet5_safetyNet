package com.projet5.safetyNet.Exception;

/**
 * Exception levée lorsqu'un dossier médical n'est pas trouvé.
 */
public class MedicalrecordNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de l'exception avec un message personnalisé.
     *
     * @param message le message décrivant l'erreur
     */
    public MedicalrecordNotFoundException(String message) {
        super(message);
    }
}

