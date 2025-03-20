package com.projet5.safetyNet.Exception;

/**
 * Exception levée lorsqu'un dossier médical existe déjà et qu'une duplication est détectée.
 */
public class MedicalRecordExistException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de l'exception avec un message personnalisé.
     *
     * @param message le message décrivant l'erreur
     */
    public MedicalRecordExistException(String message) {
        super(message);
    }
}

