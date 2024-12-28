package com.projet5.safetyNet.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projet5.safetyNet.model.Medicalrecord;

@SpringBootTest
public class MedicalrecordServiceIntegTest {

	@Autowired
	MedicalrecordService medicalrecordService;

	@Test
	void testGetAllMedicalrecord() {
		List<Medicalrecord> medicalrecordListTested = medicalrecordService.getAllMedicalrecord();

		assertThat(medicalrecordListTested).isNotEmpty();
		assertThat(medicalrecordListTested).isNotNull();
		assertThat(medicalrecordListTested.get(0)).isInstanceOf(Medicalrecord.class);
	}

	@Test
	void testAddMedicalrecord() throws Exception {
		List<String> medications = Arrays.asList("Paracetamol 500mg", "Aspirin 100mg");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");
		Medicalrecord newMedicalrecord = new Medicalrecord("John", "Doe", "24/09/1991", medications, allergies);

		List<Medicalrecord> medicalrecordListBeforeAdd = medicalrecordService.getAllMedicalrecord();
		assertThat(medicalrecordListBeforeAdd)
				.noneMatch(m -> m.getFirstName().equals("John") && m.getLastName().equals("Doe"));

		medicalrecordService.addMedicalrecord(newMedicalrecord);
		List<Medicalrecord> medicalrecordListAfterAdd = medicalrecordService.getAllMedicalrecord();
		assertThat(medicalrecordListAfterAdd).contains(newMedicalrecord);

		medicalrecordService.deleteMedicalrecord(newMedicalrecord);
	}

	@Test
	void testUpdateMedicalrecord() throws Exception {
		List<String> medications = Arrays.asList("Paracetamol 500mg", "Aspirin 100mg");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");
		Medicalrecord newMedicalrecord = new Medicalrecord("John", "Doe", "24/09/1991", medications, allergies);

		List<String> medicationsUpdated = Arrays.asList("Nurofen 500mg", "Doliprane 100mg");
		List<String> allergiesUpdated = Arrays.asList("");
		Medicalrecord MedicalrecordUpdated = new Medicalrecord("John", "Doe", "14/04/1993", medicationsUpdated,
				allergiesUpdated);

		medicalrecordService.addMedicalrecord(newMedicalrecord);
		List<Medicalrecord> medicalrecordListAfterAdd = medicalrecordService.getAllMedicalrecord();
		assertThat(medicalrecordListAfterAdd).contains(newMedicalrecord);

		medicalrecordService.updateMedicalrecord(MedicalrecordUpdated);
		List<Medicalrecord> medicalrecordListUpdated = medicalrecordService.getAllMedicalrecord();
		assertThat(medicalrecordListUpdated).noneMatch(m -> m.getFirstName().equals("John")
				&& m.getLastName().equals("Doe") && m.getBirthdate().equals("24/09/1991"));
		assertThat(medicalrecordListUpdated).anyMatch(m -> m.getFirstName().equals("John")
				&& m.getLastName().equals("Doe") && m.getBirthdate().equals("14/04/1993"));

		medicalrecordService.deleteMedicalrecord(MedicalrecordUpdated);
	}

	@Test
	void testDeleteMedicalrecord() throws Exception {
		List<String> medications = Arrays.asList("Paracetamol 500mg", "Aspirin 100mg");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");
		Medicalrecord newMedicalrecord = new Medicalrecord("John", "Doe", "24/09/1991", medications, allergies);

		medicalrecordService.addMedicalrecord(newMedicalrecord);
		List<Medicalrecord> medicalrecordListAfterAdd = medicalrecordService.getAllMedicalrecord();
		assertThat(medicalrecordListAfterAdd).contains(newMedicalrecord);

		medicalrecordService.deleteMedicalrecord(newMedicalrecord);
		List<Medicalrecord> medicalrecordListAfterDelete = medicalrecordService.getAllMedicalrecord();
		assertThat(medicalrecordListAfterDelete).noneMatch(m -> m.getFirstName().equals("John")
				&& m.getLastName().equals("Doe") && m.getBirthdate().equals("24/09/1991"));
	}

}
