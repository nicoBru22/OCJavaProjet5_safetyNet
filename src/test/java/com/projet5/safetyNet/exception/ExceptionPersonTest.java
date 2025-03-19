package com.projet5.safetyNet.exception;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet5.safetyNet.model.Person;
import com.projet5.safetyNet.repository.PersonRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ExceptionPersonTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PersonRepository personRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	void getCommunityEmailInvalidRequestTest() throws Exception {
		String cityTest = "";
		
		mockMvc.perform(MockMvcRequestBuilders.get("/communityEmail")
				.param("city", cityTest))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Le champ 'city' ne peut pas être nul ou vide.")));
	}
	
	@Test
	void getCommunityEmailNotFoundTest() throws Exception {
		String cityTest = "cityTest";
		
		mockMvc.perform(MockMvcRequestBuilders.get("/communityEmail")
				.param("city", cityTest))
				.andExpect(status().isNotFound())
				.andExpect(content().string(containsString("Aucune personne trouvée pour cette ville.")));
	}
	
	@Test
	void getPersonListNotFoundTest() throws Exception {
		when(personRepository.getAllPerson()).thenReturn(List.of());
		
		mockMvc.perform(MockMvcRequestBuilders.get("/persons"))
				.andExpect(status().isNotFound())
				.andExpect(content().string(containsString("Aucune personne n'a été trouvée.")));
	}
	
	@Test
	void deletePersonNotFoundTest() throws Exception {
		Person personTest = new Person("Nicolas", "Brunet", "addressTest", "cityTest", "zipTest", "123456789",
				"email@test.fr");
		
		String personJson = objectMapper.writeValueAsString(personTest);
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/persons")
				.contentType(MediaType.APPLICATION_JSON)
				.content(personJson))
				.andExpect(status().isNotFound())
				.andExpect(content().string(containsString("La personne n'existe pas en base de données.")));
	}
	
	@Test
	void addPersonInvalidRequestTest() throws Exception {
		Person personTest = new Person("", "", "addressTest", "cityTest", "zipTest", "123456789",
				"email@test.fr");
		
		String personJson = objectMapper.writeValueAsString(personTest);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/persons")
				.contentType(MediaType.APPLICATION_JSON)
				.content(personJson))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Les champs 'firstName', 'lastName' et 'phone' sont obligatoires et ne peuvent être nuls ou vides.")));
	}
	
	@Test
	void addPersonExistingTest() throws Exception {
	    Person personTest = new Person("Nicolas", "Brunet", "addressTest", "cityTest", "zipTest", "123456789",
	            "email@test.fr");

	    List<Person> existingPersons = List.of(personTest);
	    when(personRepository.getAllPerson()).thenReturn(existingPersons);

	    String personJson = objectMapper.writeValueAsString(personTest);

	    mockMvc.perform(MockMvcRequestBuilders.post("/persons")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(personJson))
	            .andExpect(status().isConflict())
	            .andExpect(content().string(containsString("La personne existe déjà dans la base de données.")));
	}
	
	@Test
	void updatePersonInvalidRequestTest() throws Exception {
		Person personTest = new Person("", "", "addressTest", "cityTest", "zipTest", "123456789",
				"email@test.fr");
		
		String personJson = objectMapper.writeValueAsString(personTest);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/persons")
				.contentType(MediaType.APPLICATION_JSON)
				.content(personJson))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Les champs 'firstName' et 'lastName' sont obligatoires pour une mise à jour.")));
	}
	
	@Test
	void updatePersonNotFoundTest() throws Exception {
		Person personTest = new Person("Nicolas", "Brunet", "addressTest", "cityTest", "zipTest", "123456789",
				"email@test.fr");
		
		String personJson = objectMapper.writeValueAsString(personTest);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/persons")
				.contentType(MediaType.APPLICATION_JSON)
				.content(personJson))
				.andExpect(status().isNotFound())
				.andExpect(content().string(containsString("Cette personne n'existe pas.")));
	}
	
	@Test
	void getChildListInvalidRequestTest() throws Exception {
		String addressTest = "";
		
		mockMvc.perform(MockMvcRequestBuilders.get("/childAlert")
				.param("address", addressTest))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Le champ 'address' est obligatoire.")));
	}


}
