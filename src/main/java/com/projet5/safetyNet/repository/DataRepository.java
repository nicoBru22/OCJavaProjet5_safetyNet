package com.projet5.safetyNet.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet5.safetyNet.model.DataModel;

@Repository
public class DataRepository {

    private static final String FILE_PATH = "src/main/resources/data.json";  // Chemin du fichier en dur
    private final ObjectMapper objectMapper;

    public DataRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DataRepository.class);

    // Méthode pour lire le fichier
    public DataModel readFile() {
        try {
            String contenuFichier = Files.readString(Paths.get(FILE_PATH));
            return objectMapper.readValue(contenuFichier, DataModel.class);
        } catch (IOException e) {
            logger.error("Erreur lors de la lecture du fichier JSON à l'emplacement : " + FILE_PATH, e);
            throw new RuntimeException("Erreur lors de la lecture du fichier JSON", e);
        }
    }

    // Méthode pour écrire dans le fichier
    public void writeFile(DataModel updatedData) {
        try {
            // Sérialisation de l'objet en JSON formaté
            String jsonContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(updatedData);
            // Écriture du contenu JSON dans le fichier
            Files.write(Paths.get(FILE_PATH), jsonContent.getBytes());
        } catch (IOException e) {
            // Lancer une exception Runtime avec un message clair
            throw new RuntimeException("Erreur lors de l'écriture dans le fichier JSON à l'emplacement : " + FILE_PATH, e);
        }
    }
}
