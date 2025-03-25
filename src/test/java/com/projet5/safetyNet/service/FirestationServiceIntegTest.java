package com.projet5.safetyNet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projet5.safetyNet.model.Firestation;
import com.projet5.safetyNet.model.Person;
import com.projet5.safetyNet.repository.FirestationRepository;
import com.projet5.safetyNet.repository.PersonRepository;

@SpringBootTest
public class FirestationServiceIntegTest {

	@Autowired
	FirestationService firestationService;
	
	@Autowired
	FirestationRepository firestationRepository;
	
	@Autowired
	PersonRepository personRepository;

	@Test
	void testGetAllFirestation() {
		List<Firestation> result = firestationService.getAllFireStations();

		assertThat(result).isNotNull();
		assertThat(result).isNotEmpty();
		assertThat(result.get(0)).isInstanceOf(Firestation.class);

	}

	@Test
	void testAddFirestation() throws Exception {
		Firestation firestation = new Firestation("addressTest", "7");

		List<Firestation> firestationListBeforeAdd = firestationService.getAllFireStations();
		assertThat(firestationListBeforeAdd).noneMatch(f -> f.getAddress().equals(firestation.getAddress())
				&& f.getAddress().equals(firestation.getStation()));

		firestationService.addFirestation(firestation);
		List<Firestation> firestationListAfterAdd = firestationService.getAllFireStations();
		assertThat(firestationListAfterAdd)
				.anyMatch(f -> f.getAddress().equals("addressTest") && f.getStation().equals("7"));

		firestationService.deleteFirestation(firestation);

	}

	@Test
	void testDeleteFirestation() throws Exception {
		Firestation firestation = new Firestation("addressTest", "7");
		firestationService.addFirestation(firestation);
		List<Firestation> firestationListBeforeDelete = firestationService.getAllFireStations();
		assertThat(firestationListBeforeDelete)
				.anyMatch(f -> f.getAddress().equals("addressTest") && f.getStation().equals("7"));

		firestationService.deleteFirestation(firestation);
		List<Firestation> firestationListAfterDelete = firestationService.getAllFireStations();
		assertThat(firestationListAfterDelete)
				.noneMatch(f -> f.getAddress().equals("addressTest") && f.getStation().equals("7"));
	}

	@Test
	void testUpdateFirestation() throws Exception {
		Firestation firestation = new Firestation("addressTest", "7");
		firestationService.addFirestation(firestation);
		List<Firestation> firestationListBeforeDelete = firestationService.getAllFireStations();
		assertThat(firestationListBeforeDelete)
				.anyMatch(f -> f.getAddress().equals("addressTest") && f.getStation().equals("7"));

		Firestation firestationUpdated = new Firestation("addressTest", "10");
		firestationService.updateFirestation(firestationUpdated);
		List<Firestation> firestationListUpdated = firestationService.getAllFireStations();
		assertThat(firestationListUpdated)
				.anyMatch(f -> f.getAddress().equals("addressTest") && f.getStation().equals("10"));
		assertThat(firestationListUpdated)
				.noneMatch(f -> f.getAddress().equals("addressTest") && f.getStation().equals("7"));

		firestationService.deleteFirestation(firestationUpdated);
	}

	@Test
	void testPersoFromStationNumber() throws Exception {
		String stationNumberTested = "1";

		List<String> personFromStationNumberTested = firestationService.personFromStationNumber(stationNumberTested);

		assertThat(personFromStationNumberTested).isNotEmpty();
		assertThat(personFromStationNumberTested).isNotNull();

	}

	@Test
	void testPhoneAlert() throws Exception {
		String stationNumberTest = "2";
		List<String> expectedPhoneList = Arrays.asList("841-874-6513", "841-874-7878", "841-874-7512", "841-874-7512",
				"841-874-7458", "Nombre total d'enfants : 1");
		List<String> result = firestationService.phoneAlert(stationNumberTest);

		assertThat(result).isEqualTo(expectedPhoneList);
	}

	@Test
	void testAgeOfPerson() throws Exception {
		String birthdateTested = "24/09/1991";
		int age = 33;

		int ageTested = firestationService.ageOfPerson(birthdateTested);

		assertThat(ageTested).isNotNull();
		assertThat(ageTested).isEqualTo(age);

	}

}
