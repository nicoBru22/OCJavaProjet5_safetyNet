package com.projet5.safetyNet.exception;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.CoreMatchers.containsString;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet5.safetyNet.model.Firestation;

@SpringBootTest
@AutoConfigureMockMvc
public class ExceptionFirestationTest {

	@Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    

    @Test
    void AddFirestationExistingExceptionTest() throws Exception {
        Firestation firestationTest = new Firestation("29 15th St", "2");
        
        String firestationJson = objectMapper.writeValueAsString(firestationTest);
        
        mockMvc.perform(MockMvcRequestBuilders.post("/firestations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firestationJson))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("La caserne existe déjà")));
    }
    
    @Test
    void AddFirestationInvalidRequestTest() throws Exception {
        Firestation firestationTest = new Firestation("", "");
        
        String firestationJson = objectMapper.writeValueAsString(firestationTest);
        
        mockMvc.perform(MockMvcRequestBuilders.post("/firestations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firestationJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Les champs adresse et numéro de station sont obligatoires.")));
    }
    
    @Test
    void DeleteFirestationInvalidRequestTest() throws Exception {
        Firestation firestationTest = new Firestation("", "");
        
        String firestationJson = objectMapper.writeValueAsString(firestationTest);
        
        mockMvc.perform(MockMvcRequestBuilders.delete("/firestations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firestationJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Les champs adresse et station sont obligatoires.")));
    }
    
    @Test
    void DeleteFirestationNotExistTest() throws Exception {
        Firestation firestationTest = new Firestation("addressTest", "99");
        
        String firestationJson = objectMapper.writeValueAsString(firestationTest);
        
        mockMvc.perform(MockMvcRequestBuilders.delete("/firestations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firestationJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("La caserne n'existe pas.")));
    }
    
    @Test
    void UpdateFirestationInvalidRequestTest() throws Exception {
        Firestation firestationTest = new Firestation("", "99");
        
        String firestationJson = objectMapper.writeValueAsString(firestationTest);
        
        mockMvc.perform(MockMvcRequestBuilders.put("/firestations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firestationJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Le champ adresse est obligatoire.")));
    }
    
    @Test
    void UpdateFirestationNotExistTest() throws Exception {
        Firestation firestationTest = new Firestation("addressTest", "99");
        
        String firestationJson = objectMapper.writeValueAsString(firestationTest);
        
        mockMvc.perform(MockMvcRequestBuilders.put("/firestations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firestationJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("La firestation n'existe pas à cette adresse.")));
    }
    
    @Test
    void PersonFromFirestationInvalidRequestTest() throws Exception {
        String stationNumberTest = "";
        
        mockMvc.perform(MockMvcRequestBuilders.get("/firestation/person")
                .param("stationNumber", stationNumberTest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Le numéro de station ne peut pas être vide.")));
    	
    }
    
    @Test
    void PersonFromFirestationNotFoundTest() throws Exception {
        String stationNumberTest = "50";
        
        mockMvc.perform(MockMvcRequestBuilders.get("/firestation/person")
                .param("stationNumber", stationNumberTest))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Il n'existe pas de firestation avec ce numéro.")));
    	
    }
    
}
