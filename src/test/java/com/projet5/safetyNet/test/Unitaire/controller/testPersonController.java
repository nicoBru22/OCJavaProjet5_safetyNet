package com.projet5.safetyNet.test.Unitaire.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
	void testControllerAddPerson() {
		
	}
	
	@Test
	void testControllerDeletePerson() throws Exception {
	    // Création d'une personne à supprimer
	    Person person1 = new Person("Nicolas", "Brunet", "addressTest", "cityTest", "zipTest", "123456789", "email@test.fr");

	    // Simulation de la suppression avec doNothing()
	    doNothing().when(personService).deletePerson(person1.getFirstName(), person1.getLastName(), person1.getPhone());

	    // Sérialiser l'objet Person en JSON
	    String personJson = new ObjectMapper().writeValueAsString(person1);

	    // Effectuer la requête DELETE
	    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/persons")
	            .contentType(MediaType.APPLICATION_JSON) // Indiquer que c'est du JSON
	            .content(personJson)) // Passer le JSON sérialisé dans le corps
	            .andExpect(status().isOk()) // Vérifier que la réponse a un statut OK (200)
	            .andReturn(); // Récupérer le résultat de la requête

	    // Afficher la réponse réelle pour le débogage
	    System.out.println(result.getResponse().getContentAsString());

	    // Vérifier le contenu de la réponse
	    String expectedResponse = "Personne supprimée avec succès.";
	    String actualResponse = result.getResponse().getContentAsString();
	    assertEquals(expectedResponse, actualResponse); // Comparer les chaînes exactement
	}





}
