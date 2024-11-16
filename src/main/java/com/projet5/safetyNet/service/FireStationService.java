package com.projet5.safetyNet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projet5.safetyNet.model.FireStationModel;
import com.projet5.safetyNet.repository.FireStationRepository;

@Service
public class FireStationService {
	
	@Autowired
	FireStationRepository fireStationRepository;
	
	public FireStationModel addFireStation(FireStationModel fireStation) {
		return fireStationRepository.save(fireStation);
	}

	public List<FireStationModel> getAllFireStation() {
		return fireStationRepository.findAll();
	}

	public Optional<FireStationModel> getFireStationBy(Long id) {
		return fireStationRepository.findById(id);
	}

	public void deleteFireStation(long id) {
		fireStationRepository.deleteById(id);
	}

}
