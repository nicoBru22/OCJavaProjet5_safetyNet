package com.projet5.safetyNet.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet5.safetyNet.model.Medicalrecord;
import com.projet5.safetyNet.service.MedicalrecordService;

@WebMvcTest(MedicalRecordController.class)
@AutoConfigureMockMvc
public class MedicalrecordControllerUnitTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MedicalrecordService medicalrecordService;

	@Test
	void testGetAllMedicarecord() throws Exception {
		List<String> medications = Arrays.asList("Paracetamol", "Aspirin");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");

		Medicalrecord medicalrecord1 = new Medicalrecord("John", "Doe", "24/09/1991", medications, allergies);
		Medicalrecord medicalrecord2 = new Medicalrecord("Nicolas", "Brunet", "17/05/1967", medications, allergies);

		List<Medicalrecord> medicalrecordList = List.of(medicalrecord1, medicalrecord2);

		when(medicalrecordService.getAllMedicalrecord()).thenReturn(medicalrecordList);

		mockMvc.perform(get("/medicalrecords")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].firstName").value("John"))
				.andExpect(jsonPath("$[1].firstName").value("Nicolas")).andReturn();
	}

	@Test
	void testControllerAddMedicalrecord() throws Exception {
		List<String> medications = Arrays.asList("Paracetamol", "Aspirin");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");

		Medicalrecord medicalrecord1 = new Medicalrecord("Nicolas", "Brunet", "17/05/1967", medications, allergies);

		doNothing().when(medicalrecordService).addMedicalrecord(medicalrecord1);

		String medicalrecord1Json = new ObjectMapper().writeValueAsString(medicalrecord1);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/medicalrecords")
				.contentType(MediaType.APPLICATION_JSON).content(medicalrecord1Json)).andExpect(status().isCreated())
				.andReturn();

		String expectedResult = "Le medicalrecord a été ajouté avec succès.";
		String actualResult = result.getResponse().getContentAsString();
		assertEquals(expectedResult, actualResult);
	}

	@Test
	void testControllerDeleteMedicalrecord() throws Exception {
		List<String> medications = Arrays.asList("Paracetamol", "Aspirin");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");

		Medicalrecord medicalrecord1 = new Medicalrecord("Nicolas", "Brunet", "17/05/1967", medications, allergies);

		doNothing().when(medicalrecordService).deleteMedicalrecord(medicalrecord1);

		String medicalrecord1Json = new ObjectMapper().writeValueAsString(medicalrecord1);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/medicalrecords")
				.contentType(MediaType.APPLICATION_JSON).content(medicalrecord1Json)).andExpect(status().isNoContent())
				.andReturn();

		String expectedResult = "Le medicalrecord a été supprimé avec succès.";
		String actualResult = result.getResponse().getContentAsString();
		assertEquals(expectedResult, actualResult);
	}

	@Test
	void testControllerUpdateMedicalrecord() throws Exception {
		List<String> medications = Arrays.asList("Paracetamol", "Aspirin");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");

		Medicalrecord medicalrecord1 = new Medicalrecord("Nicolas", "Brunet", "17/05/1967", medications, allergies);

		doNothing().when(medicalrecordService).deleteMedicalrecord(medicalrecord1);

		String medicalrecord1Json = new ObjectMapper().writeValueAsString(medicalrecord1);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/medicalrecords")
				.contentType(MediaType.APPLICATION_JSON).content(medicalrecord1Json)).andExpect(status().isNoContent())
				.andReturn();

		String expectedResult = "Le medicalrecord a été modifié avec succès.";
		String actualResult = result.getResponse().getContentAsString();
		assertEquals(expectedResult, actualResult);
	}

	@Test
	void testGetAllMedicalrecordError() throws Exception {
		doThrow(new RuntimeException("Erreur simulée.")).when(medicalrecordService).getAllMedicalrecord();

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/medicalrecords"))
				.andExpect(status().isInternalServerError()).andReturn();

		String actualResponse = result.getResponse().getContentAsString();
		String expectedResponse = "Erreur dans la récupération de la liste des dossiers médicaux.";

		assertTrue(actualResponse.contains(expectedResponse));
	}

	@Test
	void testAddMedicalrecordError() throws Exception {
		List<String> medications = Arrays.asList("Paracetamol", "Aspirin");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");

		Medicalrecord medicalrecord1 = new Medicalrecord("Nicolas", "Brunet", "17/05/1967", medications, allergies);
		String medicalrecordJson = new ObjectMapper().writeValueAsString(medicalrecord1);

		doThrow(new RuntimeException("Erreur simulée.")).when(medicalrecordService).addMedicalrecord(medicalrecord1);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/medicalrecords")
				.contentType(MediaType.APPLICATION_JSON).content(medicalrecordJson))
				.andExpect(status().isInternalServerError()).andReturn();

		String actualResponse = result.getResponse().getContentAsString();
		String expectedResponse = "Erreur dans l'ajout d'un nouveau medicalrecord.";

		assertTrue(actualResponse.contains(expectedResponse));
	}

	@Test
	void testDeleteMedicalrecordError() throws Exception {
		List<String> medications = Arrays.asList("Paracetamol", "Aspirin");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");

		Medicalrecord medicalrecord1 = new Medicalrecord("Nicolas", "Brunet", "17/05/1967", medications, allergies);
		String medicalrecordJson = new ObjectMapper().writeValueAsString(medicalrecord1);

		doThrow(new RuntimeException("Erreur simulée.")).when(medicalrecordService).deleteMedicalrecord(medicalrecord1);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/medicalrecords")
				.contentType(MediaType.APPLICATION_JSON).content(medicalrecordJson))
				.andExpect(status().isInternalServerError()).andReturn();

		String actualResponse = result.getResponse().getContentAsString();
		String expectedResponse = "Erreur lors de la suppression du medicalrecord.";

		assertTrue(actualResponse.contains(expectedResponse));
	}
	
	@Test
	void testUpdateMedicalrecordError() throws Exception {
		List<String> medications = Arrays.asList("Paracetamol", "Aspirin");
		List<String> allergies = Arrays.asList("pollen", "cacahuete");

		Medicalrecord medicalrecord1 = new Medicalrecord("Nicolas", "Brunet", "17/05/1967", medications, allergies);
		String medicalrecordJson = new ObjectMapper().writeValueAsString(medicalrecord1);

		doThrow(new RuntimeException("Erreur simulée.")).when(medicalrecordService).updateMedicalrecord(medicalrecord1);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/medicalrecords")
				.contentType(MediaType.APPLICATION_JSON).content(medicalrecordJson))
				.andExpect(status().isInternalServerError()).andReturn();

		String actualResponse = result.getResponse().getContentAsString();
		String expectedResponse = "Erreur lors de la modification du medicalrecord.";

		assertTrue(actualResponse.contains(expectedResponse));
	}

}