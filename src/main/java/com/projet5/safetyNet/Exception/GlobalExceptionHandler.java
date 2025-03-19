package com.projet5.safetyNet.Exception;

import java.time.format.DateTimeParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception e) {
        logger.error("Une erreur interne est survenue : {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Une erreur interne est survenue.");
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleGlobalRuntimeException(RuntimeException e) {
        logger.error("Une erreur interne est survenue : {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Une erreur interne est survenue.");
    }
    
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException e) {
        logger.error("Format de date invalide : {}", e.getParsedString(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Format de date invalide.");
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("Requête invalide : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<String> handleInvalidRequestException(InvalidRequestException e) {
        logger.warn("Requête invalide : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    
    @ExceptionHandler(InvalidDateFormatException.class)
    public ResponseEntity<String> handleInvalidDateFormatException(InvalidDateFormatException e) {
    	logger.warn("Format de date invalide : {}", e.getMessage());
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    
// Gestion des exceptions Person
    
    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<String> handlePersonNotFoundException(PersonNotFoundException e) {
        logger.warn("Personne non trouvée : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    
    @ExceptionHandler(PersonExistingException.class)
    public ResponseEntity<String> handlePersonExistingException(PersonExistingException e) {
        logger.warn("Personne déjà existante : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    
// Gestion des exceptions Firestation
    
    @ExceptionHandler(FirestationExistingException.class)
    public ResponseEntity<String> handleFirestationExistingException(FirestationExistingException e) {
        logger.warn("Caserne déjà existante : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    
    @ExceptionHandler(FirestationNotFoundException.class)
    public ResponseEntity<String> handleFirestationNotFoundException(FirestationNotFoundException e) {
        logger.warn("Caserne non trouvée : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    
// Gestion des exceptions Medicalrecord
    
    @ExceptionHandler(MedicalrecordNotFoundException.class)
    public ResponseEntity<String> handleMedicalrecordNotFoundException(MedicalrecordNotFoundException e) {
    	logger.warn("Medicalrecord non trouvée : {}", e.getMessage());
    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    
    @ExceptionHandler(MedicalRecordExistException.class)
    public ResponseEntity<String> handleMedicalRecordExistException(MedicalRecordExistException e) {
    	return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    
    
    
    
    
}
