package com.projet5.safetyNet.exception;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet5.safetyNet.model.Medicalrecord;
import com.projet5.safetyNet.service.MedicalrecordService;

@SpringBootTest
@AutoConfigureMockMvc
public class ExceptionMedicalrecordTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MedicalrecordService medicalrecordService;
	
	@Test
	void addMedicalrecordInvalidRequestTest() throws Exception {
		List<String> medications = Arrays.asList("Paracetamol", "Aspirin");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");

		Medicalrecord medicalrecord1 = new Medicalrecord("", "", "24/09/1991", medications, allergies);
		
		String medicalrecordJson = objectMapper.writeValueAsString(medicalrecord1);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/medicalrecords")
				.contentType(MediaType.APPLICATION_JSON)
				.content(medicalrecordJson))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Les champs prénom, nom et date de naissance sont obligatoires.")));
	}
	
	@Test
	void deleteMedicalrecordInvalidRequestTest() throws Exception {
		List<String> medications = Arrays.asList("Paracetamol", "Aspirin");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");

		Medicalrecord medicalrecord1 = new Medicalrecord("", "", "24/09/1991", medications, allergies);
		
		String medicalrecordJson = objectMapper.writeValueAsString(medicalrecord1);
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/medicalrecords")
				.contentType(MediaType.APPLICATION_JSON)
				.content(medicalrecordJson))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Les champs prénom et nom sont obligatoires.")));
	}
	
	@Test
	void updateMedicalrecordInvalidRequestTest() throws Exception {
		List<String> medications = Arrays.asList("Paracetamol", "Aspirin");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");

		Medicalrecord medicalrecord1 = new Medicalrecord("", "", "24/09/1991", medications, allergies);
		
		String medicalrecordJson = objectMapper.writeValueAsString(medicalrecord1);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/medicalrecords")
				.contentType(MediaType.APPLICATION_JSON)
				.content(medicalrecordJson))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Les champs prénom et nom sont obligatoires.")));
	}
	
	@Test
	void deleteMedicalrecordNotFoundTest() throws Exception {
		List<String> medications = Arrays.asList("Paracetamol", "Aspirin");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");

		Medicalrecord medicalrecord1 = new Medicalrecord("Test", "Test", "24/09/1991", medications, allergies);
		
		String medicalrecordJson = objectMapper.writeValueAsString(medicalrecord1);
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/medicalrecords")
				.contentType(MediaType.APPLICATION_JSON)
				.content(medicalrecordJson))
				.andExpect(status().isNotFound())
				.andExpect(content().string(containsString("Le dossier médical n'existe pas pour cette personne.")));
	}
	
	@Test
	void updateMedicalrecordNotFoundTest() throws Exception {
		List<String> medications = Arrays.asList("Paracetamol", "Aspirin");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");

		Medicalrecord medicalrecord1 = new Medicalrecord("Test", "Test", "24/09/1991", medications, allergies);
		
		String medicalrecordJson = objectMapper.writeValueAsString(medicalrecord1);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/medicalrecords")
				.contentType(MediaType.APPLICATION_JSON)
				.content(medicalrecordJson))
				.andExpect(status().isNotFound())
				.andExpect(content().string(containsString("Le dossier médical n'existe pas pour cette personne.")));
	}
	
	@Test
	void addMedicalrecordExistingTest() throws Exception {
		List<String> medications = Arrays.asList("Paracetamol", "Aspirin");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");

		Medicalrecord medicalrecordTest = new Medicalrecord("John", "Doe", "24/09/1991", medications, allergies);
		
		medicalrecordService.addMedicalrecord(medicalrecordTest);
		
		String medicalrecordJson = objectMapper.writeValueAsString(medicalrecordTest);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/medicalrecords")
				.contentType(MediaType.APPLICATION_JSON)
				.content(medicalrecordJson))
			.andExpect(status().isConflict())
			.andExpect(content().string(containsString("Un dossier médical existe déjà pour cette personne.")));
		
		medicalrecordService.deleteMedicalrecord(medicalrecordTest);
		
	}
	
	

}
