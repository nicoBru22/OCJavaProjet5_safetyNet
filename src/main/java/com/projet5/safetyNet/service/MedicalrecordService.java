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

}
