package com.projet5.safetyNet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Classe `Firestation` représentant le modèle d'une caserne de pompiers.
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
 * <li>address : l'adresse de la caserne (format String).</li>
 * <li>station : le numéro ou identifiant de la caserne (format String).</li>
 * </ul>
 * 
 * Pour construire un objet `Firestation`, toutes les propriétés doivent être renseignées.
 */
@Data
@AllArgsConstructor
public class Firestation {

    /**
     * L'adresse de la caserne de pompiers.
     */
    @JsonProperty("address")
    private String address;
    
    /**
     * Le numéro ou identifiant de la caserne de pompiers.
     */
    @JsonProperty("station")
    private String station;
}

