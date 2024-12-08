package com.projet5.safetyNet.repository;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.projet5.safetyNet.model.DataModel;
import com.projet5.safetyNet.model.Medicalrecord;

@Repository
public class MedicalrecordRepository {
	
	public DataRepository dataRepository;
	public List<Medicalrecord> medicalrecordList;
	
	public MedicalrecordRepository(DataRepository dataRepository) {
		this.dataRepository = dataRepository;
		this.medicalrecordList = List.of();
	}
	
    // Méthode pour récupérer la liste des medicalrecord
	private void loadMedicalrecordList() throws IOException {
		DataModel dataModel = dataRepository.readFile();
		
		if (dataModel != null && dataModel.getMedicalrecords() != null) {
			this.medicalrecordList = dataModel.getMedicalrecords();
		} else {
			this.medicalrecordList = List.of();
		}
	}
		
    // Méthode pour obtenir toutes les medicalrecord
    public List<Medicalrecord> getAllMedicalrecord() throws IOException {
        if (medicalrecordList.isEmpty()) {
        	try {
                loadMedicalrecordList();
        	} catch (IOException e) {
        		throw new RuntimeException("Erreur lors du chargement des medicalrecord", e);
        	}
        }
        return medicalrecordList;
    }
	
    //Méthode pour recharger la liste de médicalrecord
    public void reloadMedicalrecord() throws IOException {
        try {
        	loadMedicalrecordList();
        } catch (IOException e) {
        	throw new RuntimeException("Erreur lors du rechargement des medicalrecord", e);
        }
    }
}
