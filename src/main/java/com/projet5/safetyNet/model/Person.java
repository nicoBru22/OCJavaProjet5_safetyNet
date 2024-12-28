package com.projet5.safetyNet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Classe `Person` représentant le modèle d'une personne.
 * 
 * Cette classe utilise :
 * <ul>
 * <li>L'annotation Lombok {@link Data} pour générer automatiquement les
 * getters, setters, toString, equals, et hashCode.</li>
 * <li>L'annotation {@link AllArgsConstructor} pour créer un constructeur
 * exigeant toutes les propriétés.</li>
 * <li>Les annotations Jackson {@link JsonProperty} pour mapper les noms des
 * champs avec une structure JSON.</li>
 * </ul>
 * 
 * Les propriétés de la classe sont :
 * <ul>
 * <li>firstName : le prénom sous forme de String</li>
 * <li>lastName : le nom sous forme de String</li>
 * <li>address : l'adresse sous forme de String</li>
 * <li>city : la ville sous forme de String</li>
 * <li>zip : le code postal sous forme de String (exemple : "22630")</li>
 * <li>phone : le numéro de téléphone sous forme de String</li>
 * <li>email : l'adresse email sous forme de String</li>
 * </ul>
 * 
 * La forme String est utilisée pour zip et phone car ils ne doivent pas être
 * interprêté comme des nombres pouvant être calculés. Pour construire un objet
 * `Person`, toutes les propriétés doivent être fournies.
 */
@Data
@AllArgsConstructor
public class Person {
	/**
	 * Le prénom de la personne.
	 */
	@JsonProperty("firstName")
	private String firstName;

	/**
	 * Le nom de la personne.
	 */
	@JsonProperty("lastName")
	private String lastName;

	/**
	 * L'adresse où vit la personne.
	 */
	@JsonProperty("address")
	private String address;

	/**
	 * La ville où vit la personne.
	 */
	@JsonProperty("city")
	private String city;

	/**
	 * Le code postal de la ville où vit la personne (format String, exemple :
	 * "22630").
	 */
	@JsonProperty("zip")
	private String zip;

	/**
	 * Le numéro de téléphone de la personne (format String).
	 */
	@JsonProperty("phone")
	private String phone;

	/**
	 * L'adresse email de la personne.
	 */
	@JsonProperty("email")
	private String email;
}
