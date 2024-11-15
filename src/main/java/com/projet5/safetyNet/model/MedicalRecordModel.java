package com.projet5.safetyNet.model;

import java.util.List;

import lombok.Data;

@Data
public class MedicalRecordModel {
	private String firstName;
	private String lastName;
	private String birthdate;
	private List<String> medications;
	private List<String> allergies;
}
