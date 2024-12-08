package com.projet5.safetyNet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.projet5.safetyNet.model.Firestation;
import com.projet5.safetyNet.repository.FirestationRepository;

@Service
public class FirestationService {

    private final FirestationRepository firestationRepository;

    public FirestationService(FirestationRepository firestationRepository) {
        this.firestationRepository = firestationRepository;
    }

    // Méthode pour récupérer toutes les firestations
    public List<Firestation> getAllFireStations() {
        try {
            List<Firestation> firestations = firestationRepository.getAllFirestations();
            if (firestations.isEmpty()) {
                return firestations;
            }
            return firestations;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des casernes de pompiers", e);
        }
    }

}
