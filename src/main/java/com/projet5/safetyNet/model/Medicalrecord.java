package com.projet5.safetyNet.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Medicalrecord {
	
	@JsonProperty("firstName")
	private String firstName;
	
	@JsonProperty("lastName")
	private String lastName;
	
	@JsonProperty("birthdate")
	private String birthdate;
	
	@JsonProperty("medications")
	private List<String> medications;
	
	@JsonProperty("allergies")
	private List<String> allergies;
}
