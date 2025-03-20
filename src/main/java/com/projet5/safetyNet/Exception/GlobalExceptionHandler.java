package com.projet5.safetyNet.Exception;

import java.time.format.DateTimeParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Gestionnaire global des exceptions dans l'application.
 * 
 * Cette classe capte les différentes exceptions pouvant survenir dans l'application, les enregistre dans les logs et renvoie une réponse appropriée au client, 
 * avec un code de statut HTTP correspondant à l'exception.
 * Elle est utilisée dans le cadre de l'annotation {@link ControllerAdvice} de Spring.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    /**
     * Gère les exceptions générales (toutes les exceptions non capturées par d'autres handlers).
     *
     * @param e l'exception qui a été levée
     * @return une réponse HTTP avec le code de statut 500 (erreur interne du serveur) et un message d'erreur générique
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception e) {
        logger.error("Une erreur interne est survenue : {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur interne est survenue.");
    }

    /**
     * Gère les exceptions RuntimeException.
     *
     * @param e l'exception RuntimeException qui a été levée
     * @return une réponse HTTP avec le code de statut 500 (erreur interne du serveur) et un message d'erreur générique
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleGlobalRuntimeException(RuntimeException e) {
        logger.error("Une erreur interne est survenue : {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur interne est survenue.");
    }

    /**
     * Gère les exceptions liées à un format de date invalide.
     *
     * @param e l'exception DateTimeParseException qui a été levée
     * @return une réponse HTTP avec le code de statut 400 (mauvaise requête) et un message indiquant le format de date invalide
     */
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException e) {
        logger.error("Format de date invalide : {}", e.getParsedString(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Format de date invalide.");
    }

    /**
     * Gère les exceptions liées à des arguments invalides dans les requêtes.
     *
     * @param e l'exception IllegalArgumentException qui a été levée
     * @return une réponse HTTP avec le code de statut 400 (mauvaise requête) et un message d'erreur détaillant l'argument invalide
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error("Requête invalide : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * Gère les exceptions spécifiquement liées à des requêtes invalides.
     *
     * @param e l'exception InvalidRequestException qui a été levée
     * @return une réponse HTTP avec le code de statut 400 (mauvaise requête) et un message d'erreur détaillant la requête invalide
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<String> handleInvalidRequestException(InvalidRequestException e) {
        logger.error("Requête invalide : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * Gère les exceptions liées à un format de date invalide spécifique.
     *
     * @param e l'exception InvalidDateFormatException qui a été levée
     * @return une réponse HTTP avec le code de statut 400 (mauvaise requête) et un message détaillant l'erreur de format
     */
    @ExceptionHandler(InvalidDateFormatException.class)
    public ResponseEntity<String> handleInvalidDateFormatException(InvalidDateFormatException e) {
        logger.error("Format de date invalide : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    // Gestion des exceptions liées aux Personnes

    /**
     * Gère les exceptions liées à l'absence d'une personne dans la base de données.
     *
     * @param e l'exception PersonNotFoundException qui a été levée
     * @return une réponse HTTP avec le code de statut 404 (non trouvé) et un message d'erreur détaillant l'absence de la personne
     */
    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<String> handlePersonNotFoundException(PersonNotFoundException e) {
        logger.error("Personne non trouvée : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * Gère les exceptions lorsque la personne existe déjà dans la base de données.
     *
     * @param e l'exception PersonExistingException qui a été levée
     * @return une réponse HTTP avec le code de statut 409 (conflit) et un message d'erreur indiquant que la personne existe déjà
     */
    @ExceptionHandler(PersonExistingException.class)
    public ResponseEntity<String> handlePersonExistingException(PersonExistingException e) {
        logger.error("Personne déjà existante : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    // Gestion des exceptions liées aux Casernes

    /**
     * Gère les exceptions lorsque la caserne existe déjà dans la base de données.
     *
     * @param e l'exception FirestationExistingException qui a été levée
     * @return une réponse HTTP avec le code de statut 409 (conflit) et un message d'erreur indiquant que la caserne existe déjà
     */
    @ExceptionHandler(FirestationExistingException.class)
    public ResponseEntity<String> handleFirestationExistingException(FirestationExistingException e) {
        logger.error("Caserne déjà existante : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    /**
     * Gère les exceptions lorsque la caserne n'est pas trouvée dans la base de données.
     *
     * @param e l'exception FirestationNotFoundException qui a été levée
     * @return une réponse HTTP avec le code de statut 404 (non trouvé) et un message d'erreur indiquant l'absence de la caserne
     */
    @ExceptionHandler(FirestationNotFoundException.class)
    public ResponseEntity<String> handleFirestationNotFoundException(FirestationNotFoundException e) {
        logger.error("Caserne non trouvée : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    // Gestion des exceptions liées aux Dossiers Médicaux

    /**
     * Gère les exceptions liées à l'absence d'un dossier médical dans la base de données.
     *
     * @param e l'exception MedicalrecordNotFoundException qui a été levée
     * @return une réponse HTTP avec le code de statut 404 (non trouvé) et un message d'erreur détaillant l'absence du dossier médical
     */
    @ExceptionHandler(MedicalrecordNotFoundException.class)
    public ResponseEntity<String> handleMedicalrecordNotFoundException(MedicalrecordNotFoundException e) {
        logger.error("Medicalrecord non trouvée : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * Gère les exceptions lorsqu'un dossier médical existe déjà dans la base de données.
     *
     * @param e l'exception MedicalRecordExistException qui a été levée
     * @return une réponse HTTP avec le code de statut 409 (conflit) et un message d'erreur indiquant que le dossier médical existe déjà
     */
    @ExceptionHandler(MedicalRecordExistException.class)
    public ResponseEntity<String> handleMedicalRecordExistException(MedicalRecordExistException e) {
        logger.error("Un dossier médical existe déjà : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

}