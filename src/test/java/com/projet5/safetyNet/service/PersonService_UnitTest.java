package com.projet5.safetyNet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.projet5.safetyNet.Exception.InvalidRequestException;
import com.projet5.safetyNet.Exception.PersonNotFoundException;
import com.projet5.safetyNet.model.Medicalrecord;
import com.projet5.safetyNet.model.Person;
import com.projet5.safetyNet.repository.PersonRepository;

@SpringBootTest
public class PersonService_UnitTest {

	@MockBean
	private PersonRepository personRepository;

	@MockBean
	private MedicalrecordService medicalrecordService;

	@Autowired
	private PersonService personService;

	@Test
	void testGetAllPersonsThrowsException() throws Exception {
		doThrow(new RuntimeException("Erreur simulée")).when(personRepository).getAllPerson();

		Exception exception = assertThrows(RuntimeException.class, () -> {
			personService.getAllPersons();
		});

		String expectedMessage = "Erreur lors de la récupération des personnes.";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testGetAllPersonsEmpty() throws Exception {
		List<Person> personListEmpty = List.of();
		when(personRepository.getAllPerson()).thenReturn(personListEmpty);

		Exception exception = assertThrows(Exception.class, () -> {
			personService.getAllPersons();
		});

		String expectedResponse = "Aucune personne n'a été trouvée.";
		assertEquals(expectedResponse, exception.getMessage());
	}

	@Test
	void testDeletePersonError() throws Exception {
		Person person1 = new Person("Nico", "Bru", "addressTest", "villeTest", "22630", "0123456789", "emailTest");

		List<Person> personList = List.of(person1);

		when(personRepository.getAllPerson()).thenReturn(personList);

		doThrow(new RuntimeException("Erreur simulée")).when(personRepository).deletePerson(person1.getFirstName(),
				person1.getLastName(), person1.getPhone());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			personService.deletePerson(person1.getFirstName(), person1.getLastName(), person1.getPhone());
		});

		String expectedMessage = "Erreur inattendue lors de la suppression de la personne.";
		assertEquals(expectedMessage, exception.getMessage());
	}

	@Test
	void testDeletePersonNotExist() throws Exception {
		String firstName = "John";
		String lastName = "Doe";
		String phone = "0123456789";

		when(personRepository.getAllPerson()).thenReturn(List.of());

		Exception exception = assertThrows(Exception.class, () -> {
			personService.deletePerson(firstName, lastName, phone);
		});

		String expectedMessage = "La personne n'existe pas en base de données.";
		assertEquals(expectedMessage, exception.getMessage());
	}

	@Test
	void testAddPersonEmpty() {
		Person personFirstNameEmpty = new Person("", "Bru", "addressTest", "villeTest", "22630", "0123456789",
				"emailTest");
		Person personLastNameEmpty = new Person("Nico", "", "addressTest", "villeTest", "22630", "0123456789",
				"emailTest");
		Person personPhoneEmpty = new Person("Nico", "Bru", "addressTest", "villeTest", "22630", "", "emailTest");

		Exception exception1 = assertThrows(Exception.class, () -> {
			personService.addPerson(personFirstNameEmpty);
		});

		String expectedMessage1 = "Les champs 'firstName', 'lastName' et 'phone' sont obligatoires et ne peuvent être nuls ou vides.";
		String actualMessage1 = exception1.getMessage();

		assertEquals(expectedMessage1, actualMessage1);

		Exception exception2 = assertThrows(Exception.class, () -> {
			personService.addPerson(personLastNameEmpty);
		});

		String expectedMessage2 = "Les champs 'firstName', 'lastName' et 'phone' sont obligatoires et ne peuvent être nuls ou vides.";
		String actualMessage2 = exception2.getMessage();

		assertEquals(expectedMessage2, actualMessage2);

		Exception exception3 = assertThrows(Exception.class, () -> {
			personService.addPerson(personPhoneEmpty);
		});

		String expectedMessage3 = "Les champs 'firstName', 'lastName' et 'phone' sont obligatoires et ne peuvent être nuls ou vides.";
		String actualMessage3 = exception3.getMessage();

		assertEquals(expectedMessage3, actualMessage3);

	}

	@Test
	void testAddPersonError() throws Exception {
		Person person1 = new Person("Nico", "Bru", "addressTest", "villeTest", "22630", "0123456789", "emailTest");

		doThrow(new RuntimeException("Erreur simulée.")).when(personRepository).addPerson(person1);

		Exception exception = assertThrows(Exception.class, () -> {
			personService.addPerson(person1);
		});

		String expectedMessage = "Une erreur interne est survenue.";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	void testAddPersonExist() throws Exception {
		Person personExisting = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512",
				"jaboyd@email.com");
		List<Person> personList = List.of(personExisting);

		when(personRepository.getAllPerson()).thenReturn(personList);

		Exception exception = assertThrows(Exception.class, () -> {
			personService.addPerson(personExisting);
		});

		String expectedMessage = "La personne existe déjà dans la base de données.";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	void testUpdatePersonNotFound() throws Exception {
		Person personToUpdate = new Person("Nico", "Bru", "addressTest", "villeTest", "22630", "0123456789",
				"emailTest");

		when(personRepository.getAllPerson()).thenReturn(List.of());

		PersonNotFoundException exception = assertThrows(PersonNotFoundException.class, () -> {
			personService.updatePerson(personToUpdate);
		});

		String expectedMessage = "Cette personne n'existe pas.";
		assertEquals(expectedMessage, exception.getMessage());
	}

	@Test
	void testUpdatePersonEmpty() throws Exception {
		Person personFirstNameEmpty = new Person("", "Bru", "addressTest", "villeTest", "22630", "0123456789",
				"emailTest");
		Person personLastNameEmpty = new Person("Nico", "", "addressTest", "villeTest", "22630", "0123456789",
				"emailTest");

		Exception exception = assertThrows(Exception.class, () -> {
			personService.updatePerson(personFirstNameEmpty);
		});

		String expectedResponse1 = "Les champs 'firstName' et 'lastName' sont obligatoires pour une mise à jour.";
		String actualResponse1 = exception.getMessage();
		assertEquals(expectedResponse1, actualResponse1);

		Exception exception2 = assertThrows(Exception.class, () -> {
			personService.updatePerson(personLastNameEmpty);
		});

		String expectedResponse2 = "Les champs 'firstName' et 'lastName' sont obligatoires pour une mise à jour.";
		String actualResponse2 = exception2.getMessage();
		assertEquals(expectedResponse2, actualResponse2);
	}

	@Test
	void testUpdatePersonError() throws Exception {
		Person personToUpdate = new Person("Nico", "Bru", "addressTest", "villeTest", "22630", "0123456789",
				"emailTest");

		when(personRepository.getAllPerson()).thenReturn(List.of(personToUpdate));

		doThrow(new Exception("Erreur simulée")).when(personRepository).updatePerson(personToUpdate);

		Exception exception = assertThrows(Exception.class, () -> {
			personService.updatePerson(personToUpdate);
		});

		String expectedResponse = "Erreur lors de la mise à jour de la personne.";
		String actualResponse = exception.getMessage();
		assertEquals(expectedResponse, actualResponse);

	}

	@Test
	void testGetchildFromAddressError() throws Exception {
		String address = "addressTest";

		doThrow(new RuntimeException("Erreur simulée.")).when(personRepository).getAllPerson();

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			personService.getChildListFromAddress(address);
		});

		String expectedResponse = "Une erreur interne est survenue.";
		String actualResponse = exception.getMessage();
		
		assertEquals(expectedResponse, actualResponse);

	}

	@Test
	void testGetchildFromAddressEmpty() throws Exception {
		String addressEmpty = "";

		InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
			personService.getChildListFromAddress(addressEmpty);
		});

		String expectedResponse = "Le champ 'address' est obligatoire.";
		String actualResponse = exception.getMessage();
		
		assertTrue(actualResponse.contains(expectedResponse));

	}

	@Test
	void testGetChildListFromAddress_PersonWithoutMedicalRecord() throws Exception {
		Person person1 = new Person("Nico", "Bru", "addressTest", "villeTest", "22630", "0123456789", "emailTest");

		List<Person> personList = List.of(person1);
		List<Medicalrecord> medicalRecords = List.of();

		when(personRepository.getAllPerson()).thenReturn(personList);
		when(medicalrecordService.getAllMedicalrecord()).thenReturn(medicalRecords);

		List<String> result = personService.getChildListFromAddress("addressTest");

		assertTrue(result.isEmpty());
	}

	// Test A faire pour une date de naissance invalide : créer une personne et
	// créer son dossier médical. Mocker les 2. et vérifier s'il y a bien retour
	// false.

	@Test
	void testGetCommunautyEmailEmptyAndNull() {
		String city = "";
		String cityNull = null;

		Exception exception = assertThrows(Exception.class, () -> {
			personService.getCommunityEmail(city);
		});

		String expectedResponse = "Le champ 'city' ne peut pas être vide.";
		String actualResponse = exception.getMessage();
		assertEquals(expectedResponse, actualResponse);

		Exception exception2 = assertThrows(Exception.class, () -> {
			personService.getCommunityEmail(cityNull);
		});

		String expectedResponse2 = "Le champ 'city' ne peut pas être nul.";
		String actualResponse2 = exception2.getMessage();
		assertEquals(expectedResponse2, actualResponse2);
	}

	@Test
	void testGetCommunautyEmailPersonNotFound() {
		String city = "Saint-Malo";

		Exception exception = assertThrows(Exception.class, () -> {
			personService.getCommunityEmail(city);
		});

		String expectedResponse = "Aucune personne trouvée pour cette ville.";
		String actualResponse = exception.getMessage();
		assertEquals(expectedResponse, actualResponse);
	}

	@Test
	void testGetCommunautyEmailError() throws Exception {
		String city = "Saint-Malo";

		doThrow(new Exception("Erreur simulée.")).when(personRepository).getAllPerson();

		Exception exception = assertThrows(Exception.class, () -> {
			personService.getCommunityEmail(city);
		});

		String expectedResponse = "Une erreur s'est produite dans la récupération des données.";
		String actualResponse = exception.getMessage();
		assertEquals(expectedResponse, actualResponse);
	}

	@Test
	void testNormalizeAddressNull() {
		String address = null;

		String result = personService.normalizeAddress(address);

		String expectedResult = "";

		assertEquals(expectedResult, result);
	}

	@Test
	void testIsChildInvalidDate() {
		String invalidDate = "invalid-date";

		Boolean result = personService.isChild(invalidDate);

		assertFalse(result);
	}

	@Test
	void testPersonInfoError() throws Exception {
		String lastName = "Bru";

		doThrow(new Exception("Erreur simulée.")).when(personRepository).getAllPerson();

		Exception exception = assertThrows(Exception.class, () -> {
			personService.personInfo(lastName);
		});

		String expectedResponse = "Erreur lors de la récupération des informations de la personne";
		String actualResponse = exception.getMessage();
		assertEquals(expectedResponse, actualResponse);
	}

}
