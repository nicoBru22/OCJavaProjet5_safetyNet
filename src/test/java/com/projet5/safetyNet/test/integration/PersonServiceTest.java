package com.projet5.safetyNet.test.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projet5.safetyNet.model.Person;
import com.projet5.safetyNet.service.PersonService;

@SpringBootTest
public class PersonServiceTest {

	@Autowired
	PersonService personService;

	@Test
	void testGetAllPerson() {
		List<Person> result = personService.getAllPersons();

		assertThat(result).isNotNull();
		assertThat(result).isNotEmpty();
		assertThat(result.get(0)).isInstanceOf(Person.class);
	}

	@Test
	void testDeletePerson() throws Exception {
		Person person = new Person("John", "Doe", "addressTest", "villeTest", "22630", "0123456789", "emailTest");
		personService.addPerson(person);

		List<Person> personsBeforeDeletion = personService.getAllPersons();
		assertThat(personsBeforeDeletion).anyMatch(p -> p.getFirstName().equals("John") && p.getLastName().equals("Doe")
				&& p.getPhone().equals("0123456789"));

		personService.deletePerson("John", "Doe", "0123456789");

		List<Person> personsAfterDeletion = personService.getAllPersons();
		assertThat(personsAfterDeletion).noneMatch(p -> p.getFirstName().equals("John") && p.getLastName().equals("Doe")
				&& p.getPhone().equals("0123456789"));
	}

	@Test
	void testAddPerson() throws Exception {
		Person person = new Person("John", "Doe", "addressTest", "villeTest", "22630", "0123456789", "emailTest");

		List<Person> personsBeforeAdd = personService.getAllPersons();
		assertThat(personsBeforeAdd).noneMatch(p -> p.getFirstName().equals("John") && p.getLastName().equals("Doe")
				&& p.getPhone().equals("0123456789"));

		personService.addPerson(person);

		List<Person> personsAfterAdd = personService.getAllPersons();
		assertThat(personsAfterAdd).anyMatch(p -> p.getFirstName().equals("John") && p.getLastName().equals("Doe")
				&& p.getPhone().equals("0123456789"));

		personService.deletePerson("John", "Doe", "0123456789");
	}

	@Test
	void testUpdatePerson() throws Exception {
		// Personne originale
		Person person = new Person("John", "Doe", "addressTest", "villeTest", "22630", "0123456789", "emailTest");
		Person updatedPerson = new Person("John", "Doe", "addressUpdated", "villeUpdated", "35000", "0123456789",
				"emailTest");

		// Ajouter la personne
		personService.addPerson(person);
		List<Person> personsBeforeUpdate = personService.getAllPersons();
		assertThat(personsBeforeUpdate).anyMatch(p -> p.getFirstName().equals("John") && p.getLastName().equals("Doe")
				&& p.getAddress().equals("addressTest"));

		// Mise à jour de la personne
		personService.updatePerson(updatedPerson);

		// Vérifier que l'ancienne version de la personne n'existe plus
		List<Person> personsAfterUpdate = personService.getAllPersons();
		assertThat(personsAfterUpdate).noneMatch(p -> p.getFirstName().equals("John") && p.getLastName().equals("Doe")
				&& p.getAddress().equals("addressTest"));

		// Vérifier que la nouvelle version est bien présente
		assertThat(personsAfterUpdate).anyMatch(p -> p.getFirstName().equals("John") && p.getLastName().equals("Doe")
				&& p.getAddress().equals("addressUpdated"));
		
		personService.deletePerson("John", "Doe", "0123456789");
	}

}
