package com.projet5.safetyNet.test.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projet5.safetyNet.model.Medicalrecord;
import com.projet5.safetyNet.model.Person;
import com.projet5.safetyNet.service.MedicalrecordService;
import com.projet5.safetyNet.service.PersonService;

@SpringBootTest
public class PersonServiceTest {

	@Autowired
	PersonService personService;

	@Autowired
	MedicalrecordService medicalrecordService;

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
		Person person = new Person("John", "Doe", "addressTest", "villeTest", "22630", "0123456789", "emailTest");
		Person updatedPerson = new Person("John", "Doe", "addressUpdated", "villeUpdated", "35000", "0123456789",
				"emailTest");

		personService.addPerson(person);
		List<Person> personsBeforeUpdate = personService.getAllPersons();
		assertThat(personsBeforeUpdate).anyMatch(p -> p.getFirstName().equals("John") && p.getLastName().equals("Doe")
				&& p.getAddress().equals("addressTest"));

		personService.updatePerson(updatedPerson);

		List<Person> personsAfterUpdate = personService.getAllPersons();
		assertThat(personsAfterUpdate).noneMatch(p -> p.getFirstName().equals("John") && p.getLastName().equals("Doe")
				&& p.getAddress().equals("addressTest"));

		assertThat(personsAfterUpdate).anyMatch(p -> p.getFirstName().equals("John") && p.getLastName().equals("Doe")
				&& p.getAddress().equals("addressUpdated"));

		personService.deletePerson("John", "Doe", "0123456789");
	}

	@Test
	void testGetCommunityEmail() throws Exception {
		String cityTested = "Malo";
		Person person = new Person("John", "Doe", "addressTest", "Malo", "12345", "0123456789", "emailTest");

		personService.addPerson(person);

		List<String> listOfEmail = personService.getCommunityEmail(cityTested);
		assertNotNull(listOfEmail);
		assertThat(listOfEmail).contains("emailTest");

		personService.deletePerson("John", "Doe", "0123456789");
	}

	@Test
	void testGetChildListFromAddress() throws Exception {
		String address = "addressTest";

		Person newChild = new Person("John", "Doe", address, "Malo", "12345", "0123456789", "emailTest");
		List<String> medications = Arrays.asList("Paracetamol 500mg", "Aspirin 100mg");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");
		Medicalrecord newMedicalrecordChild = new Medicalrecord("John", "Doe", "24/09/2010", medications, allergies);

		personService.addPerson(newChild);
		medicalrecordService.addMedicalrecord(newMedicalrecordChild);

		List<String> listOfChildTested = personService.getChildListFromAddress(address);

		assertThat(listOfChildTested).isNotNull();

		assertThat(listOfChildTested).contains("John Doe, 14 ans");

		personService.deletePerson("John", "Doe", "0123456789");
		medicalrecordService.deleteMedicalrecord(newMedicalrecordChild);
	}

	@Test
	void testPersonInfo() throws Exception {
		String lastNameTested = "Doe";

		Person newChild = new Person("John", "Doe", "address", "Malo", "12345", "0123456789", "emailTest");
		List<String> medications = Arrays.asList("Paracetamol 500mg", "Aspirin 100mg");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");
		Medicalrecord newMedicalrecord = new Medicalrecord("John", "Doe", "24/09/2010", medications, allergies);

		personService.addPerson(newChild);
		medicalrecordService.addMedicalrecord(newMedicalrecord);

		Map<String, Object> personInfoTested = personService.personInfo(lastNameTested);

		assertThat(personInfoTested).isNotNull();

		assertThat(personInfoTested).containsKey("count");
		assertThat(personInfoTested).containsKey("personInfo");

		int count = (int) personInfoTested.get("count");
		assertThat(count).isEqualTo(1);

		Object personInfoObj = personInfoTested.get("personInfo");

		assertThat(personInfoObj).isInstanceOf(List.class);

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> personInfoList = (List<Map<String, Object>>) personInfoObj;

		assertThat(personInfoList).isNotEmpty();

		Map<String, Object> personInfo = personInfoList.get(0);

		assertThat(personInfo).containsEntry("firstName", "John");
		assertThat(personInfo).containsEntry("lastName", "Doe");
		assertThat(personInfo).containsEntry("birthdate", "24/09/2010");
		assertThat(personInfo).containsEntry("address", "address");
		assertThat(personInfo).containsEntry("phone", "0123456789");
		assertThat(personInfo).containsEntry("medications", medications);
		assertThat(personInfo).containsEntry("allergies", allergies);

		personService.deletePerson("John", "Doe", "0123456789");
		medicalrecordService.deleteMedicalrecord(newMedicalrecord);
	}

	@Test
	void testIsChild() {
		String birthdate = "24/09/2010";

		boolean testIsChild = personService.isChild(birthdate);

		assertTrue(testIsChild);
	}

	@Test
	void testIsNotChild() {
		String birthdate = "24/09/1991";
		boolean testIsChild = personService.isChild(birthdate);

		assertFalse(testIsChild);
	}

}
