package com.projet5.safetyNet.test.Unitaire.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet5.safetyNet.controller.PersonController;
import com.projet5.safetyNet.model.Person;
import com.projet5.safetyNet.service.PersonService;

@WebMvcTest(PersonController.class)
@AutoConfigureMockMvc
public class testPersonController {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PersonService personService;

	@Test
	void testControllerGetAllPerson() throws Exception {
		Person person1 = new Person("Nicolas", "Brunet", "addressTest", "cityTest", "zipTest", "123456789",
				"email@test.fr");
		Person person2 = new Person("Sarah", "Piet", "addressTest2", "cityTest2", "zipTest2", "987654321",
				"emailSarah@test.fr");
		List<Person> personList = Arrays.asList(person1, person2);

		when(personService.getAllPersons()).thenReturn(personList);

		mockMvc.perform(get("/persons")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].firstName").value("Nicolas"))
				.andExpect(jsonPath("$[1].firstName").value("Sarah"));
	}

	@Test
	void testControllerAddPerson() throws Exception {
		Person person1 = new Person("Nicolas", "Brunet", "addressTest", "cityTest", "zipTest", "123456789",
				"email@test.fr");

		doNothing().when(personService).addPerson(person1);

		String personJson = new ObjectMapper().writeValueAsString(person1);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/persons").contentType(MediaType.APPLICATION_JSON)
						.content(personJson))
				.andExpect(status().isOk())
				.andReturn();

		System.out.println(result.getResponse().getContentAsString());

		String expectedResponse = "Personne ajoutée avec succès !";
		String actualResponse = result.getResponse().getContentAsString();
		assertEquals(expectedResponse, actualResponse);

	}

	@Test
	void testControllerDeletePerson() throws Exception {
		Person person1 = new Person("Nicolas", "Brunet", "addressTest", "cityTest", "zipTest", "123456789",
				"email@test.fr");

		doNothing().when(personService).deletePerson(person1.getFirstName(), person1.getLastName(), person1.getPhone());

		String personJson = new ObjectMapper().writeValueAsString(person1);

		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.delete("/persons").contentType(MediaType.APPLICATION_JSON).content(personJson))
				.andExpect(status().isOk()).andReturn();

		// Afficher la réponse réelle pour le débogage
		System.out.println(result.getResponse().getContentAsString());

		// Vérifier le contenu de la réponse
		String expectedResponse = "Personne supprimée avec succès.";
		String actualResponse = result.getResponse().getContentAsString();
		assertEquals(expectedResponse, actualResponse); // Comparer les chaînes exactement
	}

	@Test
	void testControllerUpdatePerson() throws Exception {
		// Création de l'objet Person
		Person person1 = new Person("Nicolas", "Brunet", "addressTest", "cityTest", "zipTest", "123456789",
				"email@test.fr");

		// Simulation du service
		doNothing().when(personService).updatePerson(Mockito.any(Person.class));

		// Conversion en JSON
		String personJson = new ObjectMapper().writeValueAsString(person1);

		// Requête PUT
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.put("/persons").contentType(MediaType.APPLICATION_JSON).content(personJson))
				.andExpect(status().isOk()).andReturn();

		// Vérification de la réponse
		String expectedResponse = "Personne mise à jour avec succès !";
		String actualResponse = result.getResponse().getContentAsString();
		assertEquals(expectedResponse, actualResponse);
	}

	@Test
	void testControllerGetCommunityEmail() throws Exception {
		// Définir la ville de test et les e-mails attendus
		String city = "addressTest";
		List<String> expectedEmails = List.of("email1@test.fr", "email2@test.fr");

		// Simuler le comportement du service
		when(personService.getCommunityEmail(city)).thenReturn(expectedEmails);

		// Exécuter la requête avec le paramètre "city"
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/communityEmail").param("city", city) // Ajout du
																												// paramètre
																												// attendu
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()) // Vérifier le statut HTTP 200 OK
				.andReturn();

		// Convertir la réponse JSON en liste
		String actualResponse = result.getResponse().getContentAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		List<String> actualEmails = objectMapper.readValue(actualResponse, new TypeReference<List<String>>() {
		});

		// Vérifier que la réponse correspond à la liste attendue
		assertEquals(expectedEmails, actualEmails);
	}

	@Test
	void testControllerGetChildListFromAddress() throws Exception {

		String address = "addressTest";
		List<String> expectedChild = List.of("Nico", "Sarah", "Meghane", "Oceane", "Celenie");

		when(personService.getChildListFromAddress(address)).thenReturn(expectedChild);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/childAlert").param("address", address)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

		String actualResponse = result.getResponse().getContentAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		List<String> actualEmails = objectMapper.readValue(actualResponse, new TypeReference<List<String>>() {
		});
		assertEquals(expectedChild, actualEmails);

	}

	@Test
	void testControllerGetPersonInfoLastName() throws Exception {
		String lastNameTest = "lastNameTest";
		Map<String, Object> mapTest = Map.of("lastName", lastNameTest, "firstNames",
				List.of("Nico", "Sarah", "Meghane", "Oceane", "Celenie"), "emails",
				List.of("nico@test.com", "sarah@test.com", "meghane@test.com"));

		when(personService.personInfo(lastNameTest)).thenReturn(mapTest);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/personInfolastName")
				.param("lastName", lastNameTest).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andReturn();

		String actualResponse = result.getResponse().getContentAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> actualMap = objectMapper.readValue(actualResponse, new TypeReference<>() {
		});

		assertEquals(mapTest, actualMap);
	}

}
