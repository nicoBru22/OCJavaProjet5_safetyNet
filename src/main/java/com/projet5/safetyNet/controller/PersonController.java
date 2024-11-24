package com.projet5.safetyNet.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.projet5.safetyNet.model.Person;
import com.projet5.safetyNet.service.PersonService;

import java.io.IOException;
import java.util.List;

@RestController
public class PersonController {
	private final PersonService personService;

	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	@GetMapping("/person")
	public List<Person> getPersons() {
		return personService.getAllPersons();
	}

	@PostMapping("/person")
	public List<Person> addPerson(@RequestBody Person newPerson) throws IOException {
		personService.addPerson(newPerson); // Ajoute la nouvelle personne
		return personService.getAllPersons(); // Retourne la liste complète mise à jour
	}

	@DeleteMapping("/person")
	public List<Person> deletePerson(@RequestBody Person deletedPerson) throws IOException {
		personService.deletePerson(deletedPerson);
		return personService.getAllPersons();	
	}
	
	@PutMapping("/person")
	public List<Person> updatePerson(@RequestBody Person updatedPerson) throws IOException {
		personService.updatePerson(updatedPerson);
		return personService.getAllPersons();
	}

}