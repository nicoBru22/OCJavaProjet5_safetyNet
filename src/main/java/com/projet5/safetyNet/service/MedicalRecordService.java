package com.projet5.safetyNet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projet5.safetyNet.model.MedicalRecordModel;
import com.projet5.safetyNet.repository.MedicalRecordRepository;

@Service
public class MedicalRecordService {

	@Autowired
	MedicalRecordRepository medicalRecordRepository;

	public MedicalRecordModel addMedicalRecord(MedicalRecordModel medicalRecord) {
		return medicalRecordRepository.save(medicalRecord);
	}
	
	public List<MedicalRecordModel> getAllMedicalRecord() {
		return medicalRecordRepository.findAll();
	}
	
	public Optional<MedicalRecordModel> getMedicalRecordById(long id) {
		return medicalRecordRepository.findById(id);
	}
	
	public void deleteMedicalRecord(long id) {
		medicalRecordRepository.deleteById(id);
	}
	
}
