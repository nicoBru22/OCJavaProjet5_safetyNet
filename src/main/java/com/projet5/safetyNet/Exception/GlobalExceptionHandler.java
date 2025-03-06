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
        logger.error("Une erreur interne est survenue : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Une erreur interne est survenue : " + e.getMessage());
    }
    
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Format de date invalide.");
    }
    
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<String> handleInvalidRequestException(InvalidRequestException e) {
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    
  //La gestion des exceptions provenant de Person
    
    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<String> handlePersonNotFoundException(PersonNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    
    @ExceptionHandler(PersonDeletionException.class)
    public ResponseEntity<String> handlePersonDeletionException(PersonDeletionException e) {
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    
    @ExceptionHandler(PersonAdditionException.class)
    public ResponseEntity<String> handlePersonAdditionException(PersonAdditionException e) {
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    
    @ExceptionHandler(PersonExistingException.class)
    public ResponseEntity<String> handlePersonExistingException(PersonExistingException e) {
    	return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    
//La gestion des exceptions provenant de Firestation
    
    @ExceptionHandler(FirestationDeletedException.class)
    public ResponseEntity<String> handleFirestationDeletedException(FirestationDeletedException e) {
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    
    @ExceptionHandler(FirestationExistingException.class)
    public ResponseEntity<String> handleFirestationExistingException(FirestationExistingException e) {
    	return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    
    @ExceptionHandler(FirestationAdditionException.class)
    public ResponseEntity<String> handleFirestationAdditionException(FirestationAdditionException e) {
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    
    @ExceptionHandler(FirestationNotFoundException.class)
    public ResponseEntity<String> handleFirestationNotFoundException(FirestationNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

}
