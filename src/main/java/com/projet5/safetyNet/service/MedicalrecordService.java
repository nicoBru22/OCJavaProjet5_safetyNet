package com.projet5.safetyNet.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet5.safetyNet.model.DataModel;
import com.projet5.safetyNet.model.Medicalrecord;

@Service
public class MedicalrecordService {

	private List<Medicalrecord> medicalrecords;
	String cheminFichier = "src/main/resources/data.json";

	public MedicalrecordService() throws IOException {

		String contenuFichier = Files.readString(Paths.get(cheminFichier));
		ObjectMapper objectMapper = new ObjectMapper();
		DataModel dataModel = objectMapper.readValue(contenuFichier, DataModel.class);

		this.medicalrecords = dataModel.getMedicalrecords();
	}

	public List<Medicalrecord> getAllMedicalRecords() {
		return medicalrecords;
	}

	public void addMedicalRecord(Medicalrecord newMedicalRecord)
			throws JsonMappingException, JsonProcessingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		DataModel dataModel = objectMapper.readValue(Files.readString(Paths.get(cheminFichier)), DataModel.class);

		dataModel.getMedicalrecords().add(newMedicalRecord);
		String updatedContenuFichier = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataModel);
		Files.write(Paths.get(cheminFichier), updatedContenuFichier.getBytes());
	}

	public void deleteMedicarecord(Medicalrecord deteledMedicalRecord)
			throws JsonMappingException, JsonProcessingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		DataModel dataModel = objectMapper.readValue(Files.readString(Paths.get(cheminFichier)), DataModel.class);

		if (dataModel.getMedicalrecords()
				.removeIf(medicalrecord -> medicalrecord.getFirstName().equals(deteledMedicalRecord.getFirstName())
						&& medicalrecord.getLastName().equals(deteledMedicalRecord.getLastName())
						&& medicalrecord.getBirthdate().equals(deteledMedicalRecord.getBirthdate()))) {

			String updatedContenuFichier = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataModel);
			Files.write(Paths.get(cheminFichier), updatedContenuFichier.getBytes());
		} else {
			System.out.println("Medicalrecord is not found.");
		}
	}

}
