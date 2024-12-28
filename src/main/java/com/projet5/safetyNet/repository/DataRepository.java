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
 * fichier JSON.
 * 
 * Cette classe permet de :
 * <ul>
 * <li>Lire un fichier JSON depuis un fichier spécifié.
 * <li>Ecrire les données au format JSON dans un fichier spécifié.
 * </ul>
 */
@Repository
public class DataRepository {

	/**
	 * Le chemin de la ressource qu'il faut lire et sur laquelle il faut écrire.
	 */
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
		logger.info("DataRepository initialisé avec succès.");
	}

	/**
	 * Lit le fichier JSON depuis un emplacement spécifié et le convertit en un
	 * objet DataModel.
	 * <p>
	 * Cette méthode utilise le chemin de fichier défini dans {@link #FILE_PATH}
	 * pour lire un fichier JSON et le désérialiser en un objet DataModel.
	 * </p>
	 * 
	 * @return Un objet {@link DataModel} représentant le contenu du fichier JSON.
	 * @throws RuntimeException Si une erreur survient lors de la lecture du
	 *                          fichier.
	 */
	public DataModel readFile() {
		try {
			String contenuFichier = Files.readString(Paths.get(FILE_PATH));
			logger.info("Lecture du fichier JSON réussie");
			logger.debug("Lecture du fichier JSON réussie à l'emplacement : " + FILE_PATH);
			return objectMapper.readValue(contenuFichier, DataModel.class);
		} catch (IOException e) {
			logger.error("Erreur lors de la lecture du fichier JSON à l'emplacement : " + FILE_PATH, e);
			throw new RuntimeException("Erreur lors de la lecture du fichier JSON", e);
		}
	}

	/**
	 * Écrit un objet DataModel dans le fichier JSON.
	 * <p>
	 * Cette méthode remplace le contenu du fichier existant avec les données
	 * passées en argument. Le fichier est écrit à l'emplacement spécifié par
	 * {@link #FILE_PATH}.
	 * </p>
	 * 
	 * @param updatedData L'objet {@link DataModel} à écrire dans le fichier.
	 * @throws RuntimeException Si une erreur survient lors de l'écriture (problème
	 *                          de permission, erreur de format, etc.).
	 */
	public void writeFile(DataModel updatedData) {
		try {
			String jsonContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(updatedData);
			Files.write(Paths.get(FILE_PATH), jsonContent.getBytes());
			logger.info("Données écrites avec succès dans le fichier JSON");
			logger.debug("Données écrites avec succès dans le fichier JSON à l'emplacement : " + FILE_PATH);
			logger.debug("Le contenu de updateData {}. Le contenu de jsonContent : {}", updatedData, jsonContent);
		} catch (IOException e) {
			logger.error("Erreur lors de l'écriture dans le fichier JSON à l'emplacement : " + FILE_PATH, e);
			throw new RuntimeException("Erreur lors de l'écriture dans le fichier JSON à l'emplacement : " + FILE_PATH,
					e);
		}
	}
}
