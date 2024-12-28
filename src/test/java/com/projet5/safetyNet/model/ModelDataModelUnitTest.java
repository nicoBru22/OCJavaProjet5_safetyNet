package com.projet5.safetyNet.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class ModelDataModelUnitTest {
	@Test
	void testConstructorAndGetterDataModel() {
		DataModel dataModel = new DataModel();

		assertThat(dataModel.getFireStations()).isNull();
		assertThat(dataModel.getFireStations()).isNull();
		assertThat(dataModel.getMedicalrecords()).isNull();
	}

	@Test
	void testSetterDataModel() {
		Person person = new Person("Nicolas", "Brunet", "addressTest", "cityTest", "zipTest", "123456789",
				"email@test.fr");
		List<Person> personList = Arrays.asList(person);

		Firestation firestation = new Firestation("AddressTest", "5");
		List<Firestation> firestationList = Arrays.asList(firestation);

		Medicalrecord medicalrecord = new Medicalrecord("Nicolas", "Brunet", "24/09/1991",
				Arrays.asList("Paracetamol 500mg"), Arrays.asList("pollen"));
		List<Medicalrecord> medicalrecordList = Arrays.asList(medicalrecord);

		DataModel dataModel = new DataModel();
		dataModel.setPersonsList(personList);
		dataModel.setFireStations(firestationList);
		dataModel.setMedicalrecords(medicalrecordList);

		// Assertions
		assertThat(dataModel.getPersonsList()).isEqualTo(personList);
		assertThat(dataModel.getFireStations()).isEqualTo(firestationList);
		assertThat(dataModel.getMedicalrecords()).isEqualTo(medicalrecordList);
	}

	@Test
	void testDataModelToString() {
		Person person = new Person("Nicolas", "Brunet", "addressTest", "cityTest", "zipTest", "123456789",
				"email@test.fr");
		List<Person> personList = Arrays.asList(person);

		Firestation firestation = new Firestation("AddressTest", "5");
		List<Firestation> firestationList = Arrays.asList(firestation);

		Medicalrecord medicalrecord = new Medicalrecord("Nicolas", "Brunet", "24/09/1991",
				Arrays.asList("Paracetamol 500mg"), Arrays.asList("pollen"));
		List<Medicalrecord> medicalrecordList = Arrays.asList(medicalrecord);

		DataModel dataModel = new DataModel();
		dataModel.setPersonsList(personList);
		dataModel.setFireStations(firestationList);
		dataModel.setMedicalrecords(medicalrecordList);

		String expectedToString = "DataModel(personsList=[Person(firstName=Nicolas, lastName=Brunet, address=addressTest, city=cityTest, zip=zipTest, phone=123456789, email=email@test.fr)], "
				+ "fireStations=[Firestation(address=AddressTest, station=5)], "
				+ "medicalrecords=[Medicalrecord(firstName=Nicolas, lastName=Brunet, birthdate=24/09/1991, medications=[Paracetamol 500mg], allergies=[pollen])])";
		assertThat(dataModel.toString()).isEqualTo(expectedToString);
	}

	@Test
	void testHashCode() {
		List<Person> personList = Arrays.asList(
				new Person("Nicolas", "Brunet", "addressTest", "cityTest", "zipTest", "123456789", "email@test.fr"));
		List<Firestation> firestationList = Arrays.asList(new Firestation("AddressTest", "5"));
		List<Medicalrecord> medicalrecordList = Arrays.asList(new Medicalrecord("Nicolas", "Brunet", "24/09/1991",
				Arrays.asList("Paracetamol 500mg"), Arrays.asList("pollen")));

		DataModel dataModel1 = new DataModel();
		dataModel1.setPersonsList(personList);
		dataModel1.setFireStations(firestationList);
		dataModel1.setMedicalrecords(medicalrecordList);

		DataModel dataModel2 = new DataModel();
		dataModel2.setPersonsList(personList);
		dataModel2.setFireStations(firestationList);
		dataModel2.setMedicalrecords(medicalrecordList);

		assertThat(dataModel1.hashCode()).isEqualTo(dataModel2.hashCode());
	}

	@Test
	void testEquals() {
		List<Person> personList = Arrays.asList(
				new Person("Nicolas", "Brunet", "addressTest", "cityTest", "zipTest", "123456789", "email@test.fr"));
		List<Firestation> firestationList = Arrays.asList(new Firestation("AddressTest", "5"));
		List<Medicalrecord> medicalrecordList = Arrays.asList(new Medicalrecord("Nicolas", "Brunet", "24/09/1991",
				Arrays.asList("Paracetamol 500mg"), Arrays.asList("pollen")));

		DataModel dataModel1 = new DataModel();
		dataModel1.setPersonsList(personList);
		dataModel1.setFireStations(firestationList);
		dataModel1.setMedicalrecords(medicalrecordList);

		DataModel dataModel2 = new DataModel();
		dataModel2.setPersonsList(personList);
		dataModel2.setFireStations(firestationList);
		dataModel2.setMedicalrecords(medicalrecordList);

		DataModel dataModel3 = new DataModel();
		dataModel3.setPersonsList(Arrays.asList(
				new Person("Sarah", "Piet", "otherAddress", "otherCity", "otherZip", "987654321", "email2@test.fr")));

		assertThat(dataModel1).isEqualTo(dataModel2);
		assertThat(dataModel1).isNotEqualTo(dataModel3);
	}

}
