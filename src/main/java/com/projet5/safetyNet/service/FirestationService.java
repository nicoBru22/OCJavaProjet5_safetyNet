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
import com.projet5.safetyNet.model.Firestation;

@Service
public class FirestationService {

	private List<Firestation> fireStation;
	String cheminFichier = "src/main/resources/data.json";

	public FirestationService() throws IOException {
		String contenuFichier = Files.readString(Paths.get(cheminFichier));

		ObjectMapper objectMapper = new ObjectMapper();
		DataModel dataModel = objectMapper.readValue(contenuFichier, DataModel.class);
		this.fireStation = dataModel.getFireStations();
	}

	public List<Firestation> getAllFireStation() {
		return fireStation;
	}

	public void addFirestation(Firestation newFirestation)
			throws JsonMappingException, JsonProcessingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		DataModel dataModel = objectMapper.readValue(Files.readString(Paths.get(cheminFichier)), DataModel.class);

		dataModel.getFireStations().add(newFirestation);

		String updatedContenuFichier = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataModel);
		Files.write(Paths.get(cheminFichier), updatedContenuFichier.getBytes());

	}

}
