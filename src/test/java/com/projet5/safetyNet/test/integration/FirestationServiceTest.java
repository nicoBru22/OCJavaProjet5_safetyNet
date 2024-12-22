package com.projet5.safetyNet.test.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projet5.safetyNet.model.Firestation;
import com.projet5.safetyNet.service.FirestationService;

@SpringBootTest
public class FirestationServiceTest {

	@Autowired
	FirestationService firestationService;

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
	void testAgeOfPerson() throws Exception {
		String birthdateTested = "24/09/1991";
		int age = 33;

		int ageTested = firestationService.ageOfPerson(birthdateTested);

		assertThat(ageTested).isNotNull();
		assertThat(ageTested).isEqualTo(age);

	}

	@Test
	void testFirestationEmpty() {
		Firestation newFirestationEmpty1 = new Firestation("", "");
		Firestation newFirestationEmpty2 = new Firestation("", "1");
		Firestation newFirestationEmpty3 = new Firestation("addressTest", "");
		try {
			firestationService.addFirestation(newFirestationEmpty1);
		} catch (Exception e) {
			assertEquals("Les champs adresse et station sont obligatoires.", e.getMessage());
		}
		try {
			firestationService.addFirestation(newFirestationEmpty2);
		} catch (Exception e) {
			assertEquals("Les champs adresse et station sont obligatoires.", e.getMessage());
		}
		try {
			firestationService.addFirestation(newFirestationEmpty3);
		} catch (Exception e) {
			assertEquals("Les champs adresse et station sont obligatoires.", e.getMessage());
		}
	}

	@Test
	void testAgeOfPersonNull() {
		String birthdateTested = "24/13/1991";
		try {
			firestationService.ageOfPerson(birthdateTested);
		} catch (Exception e) {
			assertEquals("Impossible de calculer l'âge de la perosnne.", e.getMessage());
		}
	}

	@Test
	void testDeleteFirestationError() {
		Firestation firestationDeletedTested1 = new Firestation("addressTest", "5");
		Firestation firestationDeletedTested2 = new Firestation("addressTest", "");
		Firestation firestationDeletedTested3 = new Firestation("", "5");
		Firestation firestationDeletedTested4 = new Firestation("", "");

		try {
			firestationService.deleteFirestation(firestationDeletedTested1);
		} catch (Exception e) {
			assertEquals("Données invalides pour la suppression de la firestation : {}", e.getMessage());
		}
		try {
			firestationService.deleteFirestation(firestationDeletedTested2);
		} catch (Exception e) {
			assertEquals("Données invalides pour la suppression de la firestation : {}", e.getMessage());
		}
		try {
			firestationService.deleteFirestation(firestationDeletedTested3);
		} catch (Exception e) {
			assertEquals("Données invalides pour la suppression de la firestation : {}", e.getMessage());
		}
		try {
			firestationService.deleteFirestation(firestationDeletedTested4);
		} catch (Exception e) {
			assertEquals("Données invalides pour la suppression de la firestation : {}", e.getMessage());
		}

	}
	
	@Test
	void testUpdateFirestationError() {
		Firestation firestationUpdatedTested1 = new Firestation("addressTest", "5");
		Firestation firestationUpdatedTested2 = new Firestation("addressTest", "");
		Firestation firestationUpdatedTested3 = new Firestation("", "5");
		Firestation firestationUpdatedTested4 = new Firestation("", "");
		
		try {
			firestationService.updateFirestation(firestationUpdatedTested4);
		} catch (Exception e) {
			assertEquals("Le champ adresse est obligatoire.", e.getMessage());
		}
		try {
			firestationService.updateFirestation(firestationUpdatedTested3);
		} catch (Exception e) {
			assertEquals("Le champ adresse est obligatoire.", e.getMessage());
		}
		try {
			firestationService.updateFirestation(firestationUpdatedTested2);
		} catch (Exception e) {
			assertEquals("Erreur lors de la mise à jour de la firestation", e.getMessage());
		}
		try {
			firestationService.updateFirestation(firestationUpdatedTested1);
		} catch (Exception e) {
			assertEquals("Erreur lors de la mise à jour de la firestation", e.getMessage());
		}
	}
	
	@Test
	void testPersonFromStationNumberError() {
		String stationNumberTestedEmpty = "";
		String stationNumberTestedNull = null;
		String stationNumberTested = "6";
		
		try {
			firestationService.personFromStationNumber(stationNumberTestedEmpty);
		} catch (Exception e) {
			assertEquals("Le numéro de station ne peut pas être vide.", e.getMessage());
		}
		try {
			firestationService.personFromStationNumber(stationNumberTestedNull);
		} catch (Exception e) {
			assertEquals("Le numéro de station ne peut pas être vide.", e.getMessage());
		}
		try {
			firestationService.personFromStationNumber(stationNumberTested);
		} catch (Exception e) {
			assertEquals("Erreur lors de la récupération des personnes liées à la station de pompiers.", e.getMessage());
		}
	}

}
