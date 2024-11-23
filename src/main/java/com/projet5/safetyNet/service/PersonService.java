package com.projet5.safetyNet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet5.safetyNet.model.DataModel;
import com.projet5.safetyNet.model.Person;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class PersonService {
    private final List<Person> persons;

    public PersonService() throws IOException {
        // Charger le fichier JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String filePath = "src/main/resources/data.json";
        String jsonContent = Files.readString(Paths.get(filePath));

        // Désérialiser le fichier JSON en DataModel
        DataModel dataModel = objectMapper.readValue(jsonContent, DataModel.class);
        this.persons = dataModel.getPersons();
    }

    public List<Person> getAllPersons() {
        return persons;
    }
    
    public void createPerson() {
    	
    }
    
    public void setPerson() {
    	
    }
}
