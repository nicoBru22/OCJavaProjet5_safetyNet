package com.projet5.safetyNet.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.AllArgsConstructor;

/**
 * Classe `Medicalrecord` représentant le modèle d'un dossier médical.
 * 
 * Cette classe utilise :
 * <ul>
 * <li>L'annotation Lombok {@link Data} pour générer automatiquement les getters, setters,
 *     toString, equals, et hashCode.</li>
 * <li>L'annotation {@link AllArgsConstructor} pour créer un constructeur exigeant toutes 
 *     les propriétés.</li>
 * <li>Les annotations Jackson {@link JsonProperty} pour mapper les noms des champs avec 
 *     une structure JSON.</li>
 * </ul>
 * 
 * Les propriétés de la classe sont :
 * <ul>
 * <li>firstName : le prénom de la personne (format String).</li>
 * <li>lastName : le nom de la personne (format String).</li>
 * <li>birthdate : la date de naissance de la personne (format String, attendu : "yyyy/MM/dd").</li>
 * <li>medications : une liste des médicaments prescrits (posologie) sous forme de String.</li>
 * <li>allergies : une liste des allergies de la personne sous forme de String.</li>
 * </ul>
 * 
 * Pour construire un objet `Medicalrecord`, toutes les propriétés doivent être renseignées.
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
     * <p>
     * Le format attendu pour la désérialisation est "yyyy/MM/dd".
     * </p>
     */
    @JsonProperty("birthdate")
    private String birthdate;
    
    /**
     * Liste des médicaments prescrits pour la personne (posologie).
     * Chaque élément de la liste est une chaîne de caractères.
     */
    @JsonProperty("medications")
    private List<String> medications;
    
    /**
     * Liste des allergies connues de la personne.
     * Chaque élément de la liste est une chaîne de caractères représentant une substance allergène.
     */
    @JsonProperty("allergies")
    private List<String> allergies;
}

