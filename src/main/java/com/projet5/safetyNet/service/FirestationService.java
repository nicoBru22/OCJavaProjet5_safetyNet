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

	public void addFirestation(Firestation newFirestation) throws Exception {
		if ((newFirestation.getAddress() == null && newFirestation.getAddress().isEmpty())
				&& (newFirestation.getStation() == null && newFirestation.getStation().isEmpty())) {
			throw new Exception("les champs adresse et station sont obligatoires");
		} else {
			firestationRepository.addFirestation(newFirestation);
			System.out.println("la station à ajouter : " + newFirestation);
		}

	}

	public void deleteFirestation(Firestation deletedFirestation) {
		System.out.println("Dans le service, la station à supprimer : " + deletedFirestation);
		if (deletedFirestation == null || deletedFirestation.getAddress() == null
				|| deletedFirestation.getAddress().isEmpty() || deletedFirestation.getStation() == null
				|| deletedFirestation.getStation().isEmpty()) {
			throw new IllegalArgumentException("Les champs adresse et station sont obligatoires.");
		} else {
			System.out.println("la station n'est pas null");
			firestationRepository.deleteFirestation(deletedFirestation);
		}

	}

	public void updateFirestation(Firestation updatedFirestation) throws Exception {
		if (updatedFirestation.getAddress() == null || updatedFirestation.getAddress().isEmpty()) {
			throw new Exception("Le champ adresse est obligatoire");
		} else {
			List<Firestation> firestationList = firestationRepository.getAllFirestations();
			boolean stationExists = firestationList.stream().anyMatch(
					firestation -> firestation.getAddress().equalsIgnoreCase(updatedFirestation.getAddress()));

			if (!stationExists) {
				throw new Exception("La station n'existe pas à cette adresse.");
			}

			firestationRepository.updateFirestation(updatedFirestation);
		}
	}

}
