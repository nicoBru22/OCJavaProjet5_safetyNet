package com.projet5.safetyNet.test.integration;

import static org.assertj.core.api.Assertions.assertThat;

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
	void testAddFirestation() {
		Firestation firestation = new Firestation("addressTest", "7");
		
		List<Firestation> firestationListBeforeAdd = firestationService.getAllFireStations();
		assertThat(firestationListBeforeAdd).noneMatch(f -> f.getAddress().equals(firestation.getAddress()) && f.getAddress().equals(firestation.getStation()));
		
		firestationService.addFirestation(firestation);
		List<Firestation> firestationListAfterAdd = firestationService.getAllFireStations();
		assertThat(firestationListAfterAdd).anyMatch(f -> f.getAddress().equals("addressTest") && f.getStation().equals("7"));
		
		firestationService.deleteFirestation(firestation);
		
	}
	
	@Test
	void testDeleteFirestation() {
		Firestation firestation = new Firestation("addressTest", "7");
		firestationService.addFirestation(firestation);
		List<Firestation> firestationListBeforeDelete = firestationService.getAllFireStations();
		assertThat(firestationListBeforeDelete).anyMatch(f -> f.getAddress().equals("addressTest") && f.getStation().equals("7"));
		
		firestationService.deleteFirestation(firestation);
		List<Firestation> firestationListAfterDelete = firestationService.getAllFireStations();
		assertThat(firestationListAfterDelete).noneMatch(f -> f.getAddress().equals("addressTest") && f.getStation().equals("7"));
	}
	
	@Test
	void testUpdateFirestation() {
		Firestation firestation = new Firestation("addressTest", "7");
		firestationService.addFirestation(firestation);
		List<Firestation> firestationListBeforeDelete = firestationService.getAllFireStations();
		assertThat(firestationListBeforeDelete).anyMatch(f -> f.getAddress().equals("addressTest") && f.getStation().equals("7"));
		
		Firestation firestationUpdated = new Firestation("addressTest", "10");
		firestationService.updateFirestation(firestationUpdated);
		List<Firestation> firestationListUpdated = firestationService.getAllFireStations();
		assertThat(firestationListUpdated).anyMatch(f -> f.getAddress().equals("addressTest") && f.getStation().equals("10"));
		assertThat(firestationListUpdated).noneMatch(f -> f.getAddress().equals("addressTest") && f.getStation().equals("7"));
		
		firestationService.deleteFirestation(firestationUpdated);
	}

}
