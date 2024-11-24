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
import com.projet5.safetyNet.model.Medicalrecord;
import com.projet5.safetyNet.service.MedicalrecordService;

@RestController
public class MedicalRecordController {

	private final MedicalrecordService medicalrecordService;

	public MedicalRecordController(MedicalrecordService medicalRecordService) {
		this.medicalrecordService = medicalRecordService;
	}

	@GetMapping("/medicalrecord")
	public List<Medicalrecord> getMedicalRecords() {
		return medicalrecordService.getAllMedicalRecords();
	}

	@PostMapping("medicalrecord")
	public List<Medicalrecord> addMedicalRecord(@RequestBody Medicalrecord newMedicalrecord)
			throws JsonMappingException, JsonProcessingException, IOException {
		medicalrecordService.addMedicalRecord(newMedicalrecord);
		return medicalrecordService.getAllMedicalRecords();
	}

	@DeleteMapping("/medicalrecord")
	public List<Medicalrecord> deleteMedicalrecord(@RequestBody Medicalrecord deletedMedicalrecord)
			throws JsonMappingException, JsonProcessingException, IOException {
		medicalrecordService.deleteMedicarecord(deletedMedicalrecord);
		return medicalrecordService.getAllMedicalRecords();
	}
}
