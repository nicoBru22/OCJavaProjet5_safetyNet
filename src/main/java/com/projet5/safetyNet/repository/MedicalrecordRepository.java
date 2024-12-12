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
	public DataModel dataModel;

	public MedicalrecordRepository(DataRepository dataRepository) {
		this.dataRepository = dataRepository;
		this.dataModel = dataRepository.readFile();
		this.medicalrecordList = dataModel.getMedicalrecords();
	}

	// Méthode pour obtenir toutes les medicalrecord
	public List<Medicalrecord> getAllMedicalrecord() throws IOException {
		return medicalrecordList;
	}

	// Méthode pour créer un nouveau médicalrecord
	public void addMedicalrecord(Medicalrecord newMedicalrecord) {
		medicalrecordList.add(newMedicalrecord);
		dataModel.setMedicalrecords(medicalrecordList);
		dataRepository.writeFile(dataModel);
	}

	// Méthode pour supprimer un nouveau médicalrecord
	public void deleteMedicalrecord(Medicalrecord deletedMedicalrecord) {
		medicalrecordList.removeIf(
				medicalrecord -> medicalrecord.getFirstName().equalsIgnoreCase(deletedMedicalrecord.getFirstName())
						&& medicalrecord.getLastName().equalsIgnoreCase(deletedMedicalrecord.getLastName()));
		dataModel.setMedicalrecords(medicalrecordList);
		dataRepository.writeFile(dataModel);
	}

	// Méthode pour modifier un nouveau médicalrecord
	public void updateMedicalrecord(Medicalrecord updatedMedicalrecord) throws Exception {
		boolean updated = medicalrecordList.stream()
			.filter(medicalrecord -> medicalrecord.getFirstName().equals(updatedMedicalrecord.getFirstName()) 
					&& medicalrecord.getLastName().equals(updatedMedicalrecord.getLastName()))
			.findFirst()
			.map(medicalrecord -> {
				medicalrecordList.set(medicalrecordList.indexOf(medicalrecord), updatedMedicalrecord);
				return true;
			})
			.orElse(false);
		
		if (updated) {
			dataModel.setMedicalrecords(medicalrecordList);
			dataRepository.writeFile(dataModel);
		} else {
			throw new Exception("la personne n'a pas été trouvée");
		}
	}

}
