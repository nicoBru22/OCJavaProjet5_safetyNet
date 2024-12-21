package com.projet5.safetyNet.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.AllArgsConstructor;

/**
 * Représente un dossier médical d'une personne.
 * Contient des informations telles que le prénom, le nom, la date de naissance,
 * les médicaments et les allergies d'une personne.
 */
@Data
@AllArgsConstructor
public class Medicalrecord {
    
    /**
     * Prénom de la personne.
     */
    @JsonProperty("firstName")
    private String firstName;
    
    /**
     * Nom de la personne.
     */
    @JsonProperty("lastName")
    private String lastName;
    
    /**
     * Date de naissance de la personne.
     * Le format attendu pour la désérialisation est "yyyy/MM/dd".
     */
    @JsonProperty("birthdate")
    private String birthdate;
    
    /**
     * Liste des médicaments de la personne.
     */
    @JsonProperty("medications")
    private List<String> medications;
    
    /**
     * Liste des allergies de la personne.
     */
    @JsonProperty("allergies")
    private List<String> allergies;
}
