package com.projet5.safetyNet.model;

import java.util.List;

import lombok.Data;

@Data
public class DonneesRegroupees {
	List<PersonModel> personModel;
	List<FireStationModel> fireStationModel;
	List<MedicalRecordModel> medicalRecordModel;

}
