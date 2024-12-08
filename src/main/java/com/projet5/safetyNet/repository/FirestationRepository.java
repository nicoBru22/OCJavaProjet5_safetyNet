package com.projet5.safetyNet.repository;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.projet5.safetyNet.model.DataModel;
import com.projet5.safetyNet.model.Firestation;

@Repository
public class FirestationRepository {

    private final DataRepository dataRepository;
    private List<Firestation> firestationList;

    public FirestationRepository(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        this.firestationList = List.of();
    }

    // Méthode pour récupérer la liste des firestations
    private void loadFirestationsList() throws IOException {
        DataModel dataModel = dataRepository.readFile();

        if (dataModel != null && dataModel.getFireStations() != null) {
            this.firestationList = dataModel.getFireStations();
        } else {
            this.firestationList = List.of();
        }
    }

    // Méthode pour obtenir toutes les firestations
    public List<Firestation> getAllFirestations() {
        if (firestationList.isEmpty()) {
            try {
                loadFirestationsList();
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors du chargement des casernes de pompiers", e);
            }
        }
        return firestationList;
    }

    // Méthode pour forcer un rechargement de la liste des firestations
    public void reloadFirestations() {
        try {
            loadFirestationsList();
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du rechargement des casernes de pompiers", e);
        }
    }
}
