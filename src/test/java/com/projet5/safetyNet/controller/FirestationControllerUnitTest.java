package com.projet5.safetyNet.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet5.safetyNet.model.Firestation;
import com.projet5.safetyNet.repository.FirestationRepository;
import com.projet5.safetyNet.service.FirestationService;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(FirestationController.class)
@AutoConfigureMockMvc
public class FirestationControllerUnitTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private FirestationService firestationService;
	
	@MockBean
	private FirestationRepository firestationRepository;

	@Test
	void testGetAllFirestation() throws Exception {
		Firestation firestation1 = new Firestation("addressTest1", "1");
		Firestation firestation2 = new Firestation("addressTest2", "2");
		List<Firestation> firestationListTest = Arrays.asList(firestation1, firestation2);

		when(firestationService.getAllFireStations()).thenReturn(firestationListTest);

		mockMvc.perform(MockMvcRequestBuilders.get("/firestations"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].address").value("addressTest1"))
				.andExpect(jsonPath("$[1].address").value("addressTest2"))
				.andReturn();
	}

    @Test
    void testAddFirestation() throws Exception {
        Firestation firestation1 = new Firestation("addressTest1", "1");

        doNothing().when(firestationService).addFirestation(firestation1);

        ObjectMapper objectMapper = new ObjectMapper();
        String firestationJson = objectMapper.writeValueAsString(firestation1);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/firestations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firestationJson))
                .andExpect(status().isCreated())
                .andReturn();

        String expectedResponse = "La caserne a été ajoutée avec succès !";
        String actualResponse = result.getResponse().getContentAsString();
        assertEquals(expectedResponse, actualResponse);
        
    }
    
    @Test
    void testControllerDeleteFirestation() throws Exception {
        Firestation firestation1 = new Firestation("addressTest1", "1");
        
        doNothing().when(firestationService).deleteFirestation(firestation1);
        
        String firestationJson = objectMapper.writeValueAsString(firestation1);
        
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/firestations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firestationJson))
                .andExpect(status().isOk())
                .andReturn();
        
        String expectedResponse = "Caserne supprimée avec succès.";
        String actualResponse = result.getResponse().getContentAsString();
        assertEquals(expectedResponse, actualResponse);
    }
    
    @Test
    void testControllerUpdateFirestation() throws Exception {
        Firestation firestation1 = new Firestation("addressTest1", "1");
        
        doNothing().when(firestationService).deleteFirestation(firestation1);
        
        String firestationJson = objectMapper.writeValueAsString(firestation1);
        
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/firestations")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(firestationJson))
        		.andExpect(status().isOk())
        		.andReturn();
        
        String expectedResponse = "Caserne modifiée avec succès.";
        String actualResponse = result.getResponse().getContentAsString();
        assertEquals(expectedResponse, actualResponse);
        
    }
    
    @Test
    void testControllerPhoneToAlert() throws Exception {
        String addressTest = "AddressTest";
        List<String> phoneList = List.of("0123456789", "9876543210");
        
        when(firestationService.phoneAlert(addressTest)).thenReturn(phoneList);
        
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/phoneAlert")
                .param("station", addressTest))
                .andExpect(status().isOk())
                .andReturn();
        
        String actualResponse = result.getResponse().getContentAsString();
        
        assertTrue(actualResponse.contains("0123456789"));
        assertTrue(actualResponse.contains("9876543210"));
    }
    
    @Test
    void testControllerPersonFromFirestation() throws Exception {
        String stationNumberTest = "1";
        List<String> personFromFirestation1 = List.of("Nicolas", "Sarah", "Oceane");
        
        when(firestationService.personFromStationNumber(stationNumberTest)).thenReturn(personFromFirestation1);
        
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/firestation/person")
                .param("stationNumber", stationNumberTest))
                .andExpect(status().isOk())
                .andReturn();
        
        String actualResponse = result.getResponse().getContentAsString();
        
        assertTrue(actualResponse.contains("Nicolas"));
        assertTrue(actualResponse.contains("Sarah"));
        assertTrue(actualResponse.contains("Oceane"));
    }
    
    @Test 
    void testFloodPerson() throws Exception {
    	String stationNumberTest = "2";
    	
    	mockMvc.perform(MockMvcRequestBuilders.get("/flood/station")
    			.param("stationNumber", stationNumberTest))
    			.andExpect(status().isOk())
    			.andReturn();
    }
    
    @Test
    void testFire() throws Exception {
    	String addressTest = "test";
    	
    	mockMvc.perform(MockMvcRequestBuilders.get("/fire")
    			.param("address", addressTest))
    			.andExpect(status().isOk())
    			.andReturn();
    }
    
    @Test
    void testFirestation() throws Exception {
    	String stationTest = "1";
    	
    	mockMvc.perform(MockMvcRequestBuilders.get("/firestation")
    			.param("station", stationTest))
    			.andExpect(status().isOk())
    			.andReturn();
    }


}
