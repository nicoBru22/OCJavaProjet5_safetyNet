package com.projet5.safetyNet.service;


import com.projet5.safetyNet.model.Person;
import com.projet5.safetyNet.repository.PersonRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PersonService {
	
	private PersonRepository personRepository;

	public PersonService(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	public List<Person> getAllPersons() {
		return personRepository.getAllPerson();
	}
}
