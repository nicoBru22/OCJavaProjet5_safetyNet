package com.projet5.safetyNet.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DataModel {
	
	@JsonProperty("persons")
    private List<Person> personsList; 
    
    @JsonProperty("firestations")
    private List<Firestation> fireStations;
    
    @JsonProperty("medicalrecords")
    private List<Medicalrecord> medicalrecords;

}