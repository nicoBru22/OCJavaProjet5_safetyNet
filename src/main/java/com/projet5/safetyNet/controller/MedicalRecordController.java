package com.projet5.safetyNet.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.projet5.safetyNet.model.Medicalrecord;
import com.projet5.safetyNet.service.MedicalrecordService;

@RestController
public class MedicalRecordController {

	private final MedicalrecordService medicalrecordService;

	public MedicalRecordController(MedicalrecordService medicalRecordService) {
		this.medicalrecordService = medicalRecordService;
	}

	@GetMapping("/medicalrecords")
	public List<Medicalrecord> getMedicalRecords() throws IOException {
		return medicalrecordService.getAllMedicalrecord();
	}
	
	@PostMapping("medicalrecords")
	public void addMedicalrecord(@RequestBody Medicalrecord newMedicalrecord) throws Exception {
		medicalrecordService.addMedicalrecord(newMedicalrecord);
	}
	
	@DeleteMapping("/medicalrecords")
	public void deleteMedicalrecord(@RequestBody Medicalrecord deletedMedicalrecord) throws Exception {
		medicalrecordService.deleteMedicalrecord(deletedMedicalrecord);
	}
	
	@PutMapping("/medicalrecords")
	public void updateMedicalrecord(@RequestBody Medicalrecord updatedMedicalrecord) throws Exception {
		medicalrecordService.updateMedicalrecord(updatedMedicalrecord);
	}
}
