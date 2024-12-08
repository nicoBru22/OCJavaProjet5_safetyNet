package com.projet5.safetyNet.controller;


import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import com.projet5.safetyNet.model.Person;
import com.projet5.safetyNet.service.PersonService;

import java.util.List;

@RestController
public class PersonController {
	private final PersonService personService;

	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	@GetMapping("/person")
	public List<Person> getAllPersons() {
		return personService.getAllPersons();
	}

}