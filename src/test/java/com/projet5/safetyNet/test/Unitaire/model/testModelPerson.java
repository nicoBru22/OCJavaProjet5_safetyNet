package com.projet5.safetyNet.test.Unitaire.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.projet5.safetyNet.model.Person;

public class testModelPerson {

	@Test
	void testConstructorAndGetterPerson() {
		Person person = new Person("Nicolas", "Brunet", "addressTest", "cityTest", "zipTest", "123456789",
				"email@test.fr");

		assertThat(person.getFirstName()).isEqualTo("Nicolas");
		assertThat(person.getLastName()).isEqualTo("Brunet");
		assertThat(person.getAddress()).isEqualTo("addressTest");
		assertThat(person.getCity()).isEqualTo("cityTest");
		assertThat(person.getZip()).isEqualTo("zipTest");
		assertThat(person.getPhone()).isEqualTo("123456789");
		assertThat(person.getEmail()).isEqualTo("email@test.fr");
	}

	@Test
	void testSetterPerson() {
		Person person = new Person("Nicolas", "Brunet", "addressTest", "cityTest", "zipTest", "123456789",
				"email@test.fr");

		person.setFirstName("Sarah");
		person.setLastName("Piet");
		person.setAddress("addressTestModif");
		person.setCity("cityTestModif");
		person.setZip("22630");
		person.setPhone("987654321");
		person.setEmail("emailSarah@test.fr");

		assertThat(person.getFirstName()).isEqualTo("Sarah");
		assertThat(person.getLastName()).isEqualTo("Piet");
		assertThat(person.getAddress()).isEqualTo("addressTestModif");
		assertThat(person.getCity()).isEqualTo("cityTestModif");
		assertThat(person.getZip()).isEqualTo("22630");
		assertThat(person.getPhone()).isEqualTo("987654321");
		assertThat(person.getEmail()).isEqualTo("emailSarah@test.fr");
	}

	@Test
	void testPersonToString() {
		Person person = new Person("Nicolas", "Brunet", "addressTest", "cityTest", "zipTest", "123456789",
				"email@test.fr");

		String expectedToString = "Person(firstName=Nicolas, lastName=Brunet, address=addressTest, city=cityTest, zip=zipTest, phone=123456789, email=email@test.fr)";

		assertThat(person.toString()).isEqualTo(expectedToString);
	}

	@Test
	void testPersonHashCode() {
		Person person1 = new Person("Nicolas", "Brunet", "addressTest", "cityTest", "zipTest", "123456789",
				"email@test.fr");
		Person person2 = new Person("Nicolas", "Brunet", "addressTest", "cityTest", "zipTest", "123456789",
				"email@test.fr");
		Person person3 = new Person("Sarah", "Brunet", "addressTest", "cityTest", "zipTest", "123456789",
				"email@test.fr");

		assertThat(person1.hashCode()).isEqualTo(person2.hashCode());
		assertThat(person1.hashCode()).isNotEqualTo(person3.hashCode());
	}

	@Test
	void testPersonEquals() {
		Person person1 = new Person("Nicolas", "Brunet", "addressTest", "cityTest", "zipTest", "123456789",
				"email@test.fr");
		Person person2 = new Person("Nicolas", "Brunet", "addressTest", "cityTest", "zipTest", "123456789",
				"email@test.fr");
		Person person3 = new Person("Sarah", "Brunet", "addressTest", "cityTest", "zipTest", "123456789",
				"email@test.fr");

		assertThat(person1).isEqualTo(person2);
		assertThat(person1).isNotEqualTo(person3);
	}
}
