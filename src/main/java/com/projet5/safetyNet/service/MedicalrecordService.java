package com.projet5.safetyNet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.projet5.safetyNet.model.Medicalrecord;
import com.projet5.safetyNet.repository.MedicalrecordRepository;

@Service
public class MedicalrecordService {

    private final MedicalrecordRepository medicalrecordRepository;

    public MedicalrecordService(MedicalrecordRepository medicalrecordRepository) {
        this.medicalrecordRepository = medicalrecordRepository;
    }

    // Méthode pour récupérer toutes les medicalrecord
    public List<Medicalrecord> getAllMedicalrecord() {
        try {
            List<Medicalrecord> medicalrecord = medicalrecordRepository.getAllMedicalrecord();
            if (medicalrecord.isEmpty()) {
                return medicalrecord;
            }
            return medicalrecord;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des medicalrecord", e);
        }
    }
    
    public void addMedicalrecord(Medicalrecord newMedicalrecord) throws Exception {
    	if (newMedicalrecord.getFirstName() == null 
    			|| newMedicalrecord.getLastName() == null
    			|| newMedicalrecord.getBirthdate() == null) {
    		throw new Exception("les champs firstname, lastname et birthdate sont obligatoires");
    	} else {
    		medicalrecordRepository.addMedicalrecord(newMedicalrecord);
    	}
    }
    
    public void deleteMedicalrecord(Medicalrecord deletedMedicalrecord) throws Exception {
    	if (deletedMedicalrecord.getFirstName() == null || deletedMedicalrecord.getLastName() == null) {
    		throw new Exception("les champs fistname et lastname sont obligatoires");
    	} else {
    		medicalrecordRepository.deleteMedicalrecord(deletedMedicalrecord);
    	}
    }
    
    public void updateMedicalrecord(Medicalrecord updatedMedicalrecord) throws Exception {
    	if (updatedMedicalrecord.getFirstName() == null || updatedMedicalrecord.getLastName() == null) {
    		throw new Exception("les champs fistname et lastname sont obligatoires");
	    } else {
	    	medicalrecordRepository.updateMedicalrecord(updatedMedicalrecord);
	    }
    }
}


