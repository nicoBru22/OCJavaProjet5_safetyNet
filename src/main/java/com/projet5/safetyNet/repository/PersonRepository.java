package com.projet5.safetyNet.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.projet5.safetyNet.model.DataModel;
import com.projet5.safetyNet.model.Person;

@Repository
public class PersonRepository {

	public DataRepository dataRepository;
	public List<Person> personsList;
	public DataModel dataModel;
	
	public PersonRepository(DataRepository dataRepository, List<Person> personsList) {
		this.dataRepository = dataRepository;
		this.dataModel = dataRepository.readFile();
		this.personsList = dataModel.getPersonsList();
	}
		
	//Méthode pour récupérer toutes les personnes du fichier
	public List<Person> getAllPerson() {
		return personsList;
	}
	
}
