package com.projet5.safetyNet.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet5.safetyNet.model.DataModel;

/**
 * Classe Repository pour gérer les opérations de lecture et d'écriture du
 * fichier JSON. Cette classe permet de lire et d'écrire les données au format
 * JSON dans le fichier spécifié, et fournit des méthodes pour gérer les données
 * de l'application.
 */
@Repository
public class DataRepository {

	private static final String FILE_PATH = "src/main/resources/data.json";

	private final ObjectMapper objectMapper;

	private static final Logger logger = LogManager.getLogger(DataRepository.class);

	/**
	 * Constructeur de DataRepository qui initialise l'ObjectMapper.
	 * 
	 * @param objectMapper L'ObjectMapper utilisé pour la conversion JSON
	 */
	public DataRepository(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	/**
	 * Lit le fichier JSON à partir du chemin spécifié et le désérialise en un objet
	 * DataModel.
	 * 
	 * @return L'objet DataModel représentant le contenu du fichier
	 * @throws RuntimeException Si une erreur survient lors de la lecture du fichier
	 */
	public DataModel readFile() {
		try {
			String contenuFichier = Files.readString(Paths.get(FILE_PATH));
			logger.info("Lecture du fichier JSON réussie à l'emplacement : " + FILE_PATH);
			return objectMapper.readValue(contenuFichier, DataModel.class);
		} catch (IOException e) {
			logger.error("Erreur lors de la lecture du fichier JSON à l'emplacement : " + FILE_PATH, e);
			throw new RuntimeException("Erreur lors de la lecture du fichier JSON", e);
		}
	}

	/**
	 * Écrit un objet DataModel dans le fichier JSON, en remplaçant le contenu
	 * précédent.
	 * 
	 * @param updatedData L'objet DataModel à écrire dans le fichier
	 * @throws RuntimeException Si une erreur survient lors de l'écriture dans le
	 *                          fichier
	 */
	public void writeFile(DataModel updatedData) {
		try {
			String jsonContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(updatedData);
			Files.write(Paths.get(FILE_PATH), jsonContent.getBytes());
			logger.info("Données écrites avec succès dans le fichier JSON à l'emplacement : " + FILE_PATH);
		} catch (IOException e) {
			logger.error("Erreur lors de l'écriture dans le fichier JSON à l'emplacement : " + FILE_PATH, e);
			throw new RuntimeException("Erreur lors de l'écriture dans le fichier JSON à l'emplacement : " + FILE_PATH,
					e);
		}
	}
}
