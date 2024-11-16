package com.projet5.safetyNet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projet5.safetyNet.model.PersonModel;
import com.projet5.safetyNet.repository.PersonsRepository;

@Service
public class PersonsService {

	@Autowired
	private PersonsRepository personRepository;

	public PersonModel addPerson(PersonModel person) {
		return personRepository.save(person);
	}

	public List<PersonModel> getAllPersons() {
		return personRepository.findAll();
	}

	public Optional<PersonModel> getPersonByName(long id) {
		return personRepository.findById(id);
	}

	public void deletePerson(long id) {
		personRepository.deleteById(id);
	}
}
