package com.projet5.safetyNet.test.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
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

	@Test
	void testAddFirestationEmpty() {
		Firestation newFirestationEmpty1 = new Firestation("", "");
		Firestation newFirestationEmpty2 = new Firestation("", "1");
		Firestation newFirestationEmpty3 = new Firestation("addressTest", "");
		Firestation firestationExisting = new Firestation("908 73rd St", "1");

		assertThatThrownBy(() -> firestationService.addFirestation(newFirestationEmpty1)).isInstanceOf(Exception.class)
				.hasMessage("Erreur inattendue lors de l'ajout de la caserne.");
		assertThatThrownBy(() -> firestationService.addFirestation(newFirestationEmpty2)).isInstanceOf(Exception.class)
				.hasMessage("Erreur inattendue lors de l'ajout de la caserne.");
		assertThatThrownBy(() -> firestationService.addFirestation(newFirestationEmpty3)).isInstanceOf(Exception.class)
				.hasMessage("Erreur inattendue lors de l'ajout de la caserne.");

		assertThatThrownBy(() -> firestationService.addFirestation(firestationExisting)).isInstanceOf(Exception.class)
				.hasMessage("La caserne existe déjà.");
	}

	@Test
	void testAgeOfPersonNull() {
		String birthdateTested = "24/13/1991";
		assertThatThrownBy(() -> firestationService.ageOfPerson(birthdateTested)).isInstanceOf(Exception.class)
				.hasMessage("Impossible de calculer l'âge de la personne.");
	}

	@Test
	void testDeleteFirestationError() {
		Firestation firestationDeletedTested1 = new Firestation("addressTest", "5");
		Firestation firestationDeletedTested2 = new Firestation("addressTest", "");
		Firestation firestationDeletedTested3 = new Firestation("", "5");
		Firestation firestationDeletedTested4 = new Firestation("", "");

		assertThatThrownBy(() -> firestationService.deleteFirestation(firestationDeletedTested1))
				.isInstanceOf(Exception.class).hasMessage("Erreur inattendue lors de la suppression de la caserne.");
		assertThatThrownBy(() -> firestationService.deleteFirestation(firestationDeletedTested2))
				.isInstanceOf(Exception.class)
				.hasMessage("Données invalides pour la suppression : Les champs adresse et station sont obligatoires.");
		assertThatThrownBy(() -> firestationService.deleteFirestation(firestationDeletedTested3))
				.isInstanceOf(Exception.class)
				.hasMessage("Données invalides pour la suppression : Les champs adresse et station sont obligatoires.");

		assertThatThrownBy(() -> firestationService.deleteFirestation(firestationDeletedTested4))
				.isInstanceOf(Exception.class)
				.hasMessage("Données invalides pour la suppression : Les champs adresse et station sont obligatoires.");

	}

	@Test
	void testUpdateFirestationError() {
		Firestation firestationUpdatedTested1 = new Firestation("addressTest", "5");
		Firestation firestationUpdatedTested2 = new Firestation("addressTest", "");
		Firestation firestationUpdatedTested3 = new Firestation("", "5");
		Firestation firestationUpdatedTested4 = new Firestation("", "");

		assertThatThrownBy(() -> firestationService.updateFirestation(firestationUpdatedTested4))
				.isInstanceOf(Exception.class).hasMessage("Données invalide pour la mise à jour de la station.");
		assertThatThrownBy(() -> firestationService.updateFirestation(firestationUpdatedTested3))
				.isInstanceOf(Exception.class).hasMessage("Données invalide pour la mise à jour de la station.");
		assertThatThrownBy(() -> firestationService.updateFirestation(firestationUpdatedTested2))
				.isInstanceOf(Exception.class).hasMessage("Erreur lors de la mise à jour de la firestation");
		assertThatThrownBy(() -> firestationService.updateFirestation(firestationUpdatedTested1))
				.isInstanceOf(Exception.class).hasMessage("Erreur lors de la mise à jour de la firestation");
	}

	@Test
	void testPersonFromStationNumberError() {
		String stationNumberTestedEmpty = "";
		String stationNumberTestedNull = null;
		String stationNumberTested = "6";

		assertThatThrownBy(() -> firestationService.personFromStationNumber(stationNumberTestedEmpty))
				.isInstanceOf(Exception.class).hasMessage("Le numéro de station ne peut pas être vide.");

		assertThatThrownBy(() -> firestationService.personFromStationNumber(stationNumberTestedNull))
				.isInstanceOf(Exception.class).hasMessage("Le numéro de station ne peut pas être vide.");

		assertThatThrownBy(() -> firestationService.personFromStationNumber(stationNumberTested))
				.isInstanceOf(Exception.class)
				.hasMessage("Erreur lors de la récupération des personnes liées à la station de pompiers.");
	}

	@Test
	void testPhoneAlertError() {
		String stationNumberTest = "6";

		assertThatThrownBy(() -> firestationService.phoneAlert(stationNumberTest)).isInstanceOf(Exception.class)
				.hasMessage(
						"Erreur lors de la récupération des numéros de téléphone associés à la station de pompiers.");
	}

}
