package com.projet5.safetyNet.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DataModel {
	
	@JsonProperty("persons")
    private List<Person> persons; 
    
    @JsonProperty("firestations")
    private List<FireStation> fireStations;
    
    @JsonProperty("medicalrecords")
    private List<MedicalRecord> medicalrecords;
	
    public List<Person> getPersons() {
        return persons;
    }

    
    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
    
    public List<FireStation> getFireStations() {
        return fireStations;
    }

    public void setFireStation(List<FireStation> fireStations) {
        this.fireStations = fireStations;
    }
    
    public List<MedicalRecord> getMedicalrecords() {
    	return medicalrecords;
    }
    
    public void setMedicalRecord(List<MedicalRecord> medicalrecords) {
    	this.medicalrecords = medicalrecords;
    }

}