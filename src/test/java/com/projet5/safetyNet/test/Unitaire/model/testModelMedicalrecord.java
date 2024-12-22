package com.projet5.safetyNet.test.Unitaire.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.projet5.safetyNet.model.Medicalrecord;

@SpringBootTest
public class testModelMedicalrecord {
	@Test
	void testConstructorAndGetterMedicalrecord() {
		List<String> medications = Arrays.asList("Paracetamol 500mg", "Aspirin 100mg");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");
		Medicalrecord medicalrecord = new Medicalrecord("Nicolas", "Brunet", "24/09/1991", medications, allergies);

		assertThat(medicalrecord.getFirstName()).isEqualTo("Nicolas");
		assertThat(medicalrecord.getLastName()).isEqualTo("Brunet");
		assertThat(medicalrecord.getBirthdate()).isEqualTo("24/09/1991");
		assertThat(medicalrecord.getMedications()).isEqualTo(medications);
		assertThat(medicalrecord.getAllergies()).isEqualTo(allergies);
	}

	@Test
	void testSettersMedicalrecord() {
		List<String> medications = Arrays.asList("Paracetamol 500mg", "Aspirin 100mg");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");
		List<String> allergiesModif = Arrays.asList("Abeille");
		List<String> medicationsModif = Arrays.asList("Doliprane 1g");
		Medicalrecord medicalrecord = new Medicalrecord("Nicolas", "Brunet", "24/09/1991", medications, allergies);

		medicalrecord.setAllergies(allergiesModif);
		medicalrecord.setMedications(medicationsModif);
		medicalrecord.setFirstName("Sarah");
		medicalrecord.setLastName("Piet");
		medicalrecord.setBirthdate("14/04/1993");

		assertThat(medicalrecord.getFirstName()).isEqualTo("Sarah");
		assertThat(medicalrecord.getLastName()).isEqualTo("Piet");
		assertThat(medicalrecord.getBirthdate()).isEqualTo("14/04/1993");
		assertThat(medicalrecord.getMedications()).isEqualTo(medicationsModif);
		assertThat(medicalrecord.getAllergies()).isEqualTo(allergiesModif);
	}
	
	@Test
	void testMedicalrecordToString() {
		List<String> medications = Arrays.asList("Doliprane", "Aspirin");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");
		Medicalrecord medicalrecord1 = new Medicalrecord("Nicolas", "Brunet", "24/09/1991", medications, allergies);
		
		String expectedToString = "Medicalrecord(firstName=Nicolas, lastName=Brunet, birthdate=24/09/1991, medications=[Doliprane, Aspirin], allergies=[pollen, cacahuete])";
		
		assertThat(medicalrecord1.toString()).isEqualTo(expectedToString);
		
	}
	
	@Test
	void testMedicalrecordHashCode() {
		List<String> medications = Arrays.asList("Paracetamol 500mg", "Aspirin 100mg");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");
		Medicalrecord medicalrecord1 = new Medicalrecord("Nicolas", "Brunet", "24/09/1991", medications, allergies);

		Medicalrecord medicalrecord2 = new Medicalrecord("Nicolas", "Brunet", "24/09/1991", medications, allergies);
		
		assertThat(medicalrecord1.hashCode()).isEqualTo(medicalrecord2.hashCode());
	}
	
	@Test
	void testMedicalrecordEquals() {
		List<String> medications = Arrays.asList("Paracetamol 500mg", "Aspirin 100mg");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");
		Medicalrecord medicalrecord1 = new Medicalrecord("Nicolas", "Brunet", "24/09/1991", medications, allergies);

		Medicalrecord medicalrecord2 = new Medicalrecord("Nicolas", "Brunet", "24/09/1991", medications, allergies);
		
		List<String> medicationsDifferent = Arrays.asList("Doliprane");
		List<String> allergiesDifferent = Arrays.asList("abeille", "cacahuete");
		Medicalrecord medicalrecord3 = new Medicalrecord("Nicolas", "Brunet", "24/09/1991", medicationsDifferent, allergiesDifferent);
		
		assertThat(medicalrecord1).isEqualTo(medicalrecord2);
		assertThat(medicalrecord1).isNotEqualTo(medicalrecord3);
		
	}
}
