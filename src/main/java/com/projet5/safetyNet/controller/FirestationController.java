package com.projet5.safetyNet.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.projet5.safetyNet.model.Firestation;
import com.projet5.safetyNet.service.FirestationService;

@RestController
public class FirestationController {

	private final FirestationService firestationService;

	public FirestationController(FirestationService fireStationService) {
		this.firestationService = fireStationService;
	}

	@GetMapping("/firestation")
	public List<Firestation> getAllFireStation() {
		return firestationService.getAllFireStation();
	}

	@PostMapping("/firestation")
	public List<Firestation> addFirestation(@RequestBody Firestation newFirestation)
			throws JsonMappingException, JsonProcessingException, IOException {
		firestationService.addFirestation(newFirestation);
		return firestationService.getAllFireStation();
	}

	@DeleteMapping("/firestation")
	public List<Firestation> deleteFirestation(@RequestBody Firestation deletedFirestation)
			throws JsonMappingException, JsonProcessingException, IOException {
		firestationService.deleteFirestation(deletedFirestation);
		return firestationService.getAllFireStation();
	}
}
