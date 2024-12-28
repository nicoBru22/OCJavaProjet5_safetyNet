package com.projet5.safetyNet.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Classe `DataModel` représentant le modèle de données principal de l'application.
 * 
 * Cette classe utilise :
 * <ul>
 * <li>L'annotation Lombok {@link Data} pour générer automatiquement les getters, setters,
 *     toString, equals, et hashCode.</li>
 * <li>Les annotations Jackson {@link JsonProperty} pour mapper les noms des champs avec 
 *     une structure JSON.</li>
 * </ul>
 * 
 * Les propriétés de la classe sont :
 * <ul>
 * <li>personsList : une liste des personnes ({@link Person}) contenues dans les données (format List).</li>
 * <li>fireStations : une liste des casernes de pompiers ({@link Firestation}) contenues dans les données (format List).</li>
 * <li>medicalrecords : une liste des dossiers médicaux ({@link Medicalrecord}) contenus dans les données (format List).</li>
 * </ul>
 * 
 * Cette classe centralise les principales données manipulées dans l'application.
 */
@Data
public class DataModel {

    /**
     * Liste des personnes présentes dans les données.
     */
    @JsonProperty("persons")
    private List<Person> personsList; 
    
    /**
     * Liste des casernes de pompiers présentes dans les données.
     */
    @JsonProperty("firestations")
    private List<Firestation> fireStations;
    
    /**
     * Liste des dossiers médicaux présents dans les données.
     */
    @JsonProperty("medicalrecords")
    private List<Medicalrecord> medicalrecords;

}
