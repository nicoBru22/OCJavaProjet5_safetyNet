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

	private List<Person> persons;
	String cheminFichier = "src/main/resources/data.json";

	public PersonService() throws IOException {
		// Lire le fichier complet
		String contenuFichier = Files.readString(Paths.get(cheminFichier));
		ObjectMapper objectMapper = new ObjectMapper();
		DataModel dataModel = objectMapper.readValue(contenuFichier, DataModel.class);

		// Initialiser les listes
		this.persons = dataModel.getPersons();
	}

	public List<Person> getAllPersons() {
		return persons;
	}

	public void addPerson(Person newPerson) throws IOException {
		// Lire le fichier complet avant d'ajouter une nouvelle personne
		ObjectMapper objectMapper = new ObjectMapper();
		DataModel dataModel = objectMapper.readValue(Files.readString(Paths.get(cheminFichier)), DataModel.class);

		// Ajouter la nouvelle personne à la liste des personnes
		dataModel.getPersons().add(newPerson);

		// Mettre à jour le fichier avec toutes les données
		String updatedContenuFichier = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataModel);
		Files.write(Paths.get(cheminFichier), updatedContenuFichier.getBytes());
	}

	public void deletePerson(Person deletedPerson) throws IOException {
		// Lire le fichier complet avant de supprimer une personne
		ObjectMapper objectMapper = new ObjectMapper();
		DataModel dataModel = objectMapper.readValue(Files.readString(Paths.get(cheminFichier)), DataModel.class);

		// Supprimer la personne de la liste des personnes
		if (dataModel.getPersons()
				.removeIf(person -> person.getFirstName().equals(deletedPerson.getFirstName())
						&& person.getLastName().equals(deletedPerson.getLastName())
						&& person.getPhone().equals(deletedPerson.getPhone()))) {

			// Mettre à jour le fichier avec toutes les données
			String updatedContenuFichier = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataModel);
			Files.write(Paths.get(cheminFichier), updatedContenuFichier.getBytes());
		} else {
			System.out.println("Person not found in the list.");
		}
	}

	public void setPerson() {
		// Si cette méthode est à implémenter plus tard, il faudra en tenir compte ici
	}
}
