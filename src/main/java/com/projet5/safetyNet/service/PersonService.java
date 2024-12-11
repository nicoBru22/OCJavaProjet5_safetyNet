package com.projet5.safetyNet.service;

import com.projet5.safetyNet.model.Person;
import com.projet5.safetyNet.repository.PersonRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    // Injection du repository via le constructeur
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Méthode pour récupérer la liste de toutes les personnes.
     * @return Liste des personnes.
     */
    public List<Person> getAllPersons() {
        return personRepository.getAllPerson();
    }

    /**
     * Méthode pour supprimer une personne.
     * Vérifie d'abord si la personne existe avant de tenter la suppression.
     * @param firstName Prénom de la personne.
     * @param lastName Nom de la personne.
     */
    public void deletePerson(String firstName, String lastName, String phone) {
        boolean personExists = personRepository.getAllPerson().stream()
            .anyMatch(person -> person.getFirstName().equalsIgnoreCase(firstName) &&
                                person.getLastName().equalsIgnoreCase(lastName) &&
                                person.getPhone().equalsIgnoreCase(phone));

        if (personExists) {
            personRepository.deletePerson(firstName, lastName, phone);
        } else {
            throw new IllegalArgumentException("La personne n'existe pas.");
        }
    }

    /**
     * Méthode pour ajouter une nouvelle personne.
     * Vérifie que les champs obligatoires sont présents et qu'une personne identique n'existe pas déjà.
     * @param newPerson Nouvelle personne à ajouter.
     */
    public void addPerson(Person newPerson) {
        // Validation des champs obligatoires
        if (newPerson.getFirstName() == null || newPerson.getFirstName().isEmpty() ||
            newPerson.getLastName() == null || newPerson.getLastName().isEmpty() ||
            newPerson.getPhone() == null || newPerson.getPhone().isEmpty()) {
            throw new IllegalArgumentException(
                "Les champs 'firstName', 'lastName' et 'phone' sont obligatoires."
            );
        }

        // Vérification de l'existence d'une personne identique
        boolean personExists = personRepository.getAllPerson().stream()
            .anyMatch(person ->
                person.getFirstName().equalsIgnoreCase(newPerson.getFirstName()) &&
                person.getLastName().equalsIgnoreCase(newPerson.getLastName()) &&
                person.getPhone().equals(newPerson.getPhone()));

        if (!personExists) {
            personRepository.addPerson(newPerson);
        } else {
            throw new IllegalArgumentException(
                "Une personne avec ce prénom et ce nom existe déjà."
            );
        }
    }

    /**
     * Méthode pour mettre à jour les informations d'une personne existante.
     * Recherche une correspondance par prénom et nom, puis met à jour les informations.
     * @param updatedPerson Personne avec les nouvelles informations.
     */
    public void updatePerson(Person updatedPerson) {
        // Validation des champs obligatoires
        if (updatedPerson.getFirstName() == null || updatedPerson.getFirstName().isEmpty() ||
            updatedPerson.getLastName() == null || updatedPerson.getLastName().isEmpty()) {
            throw new IllegalArgumentException(
                "Les champs 'firstName' et 'lastName' sont obligatoires pour une mise à jour."
            );
        }

        // Récupération de la liste des personnes
        List<Person> personsList = personRepository.getAllPerson();

        for (int i = 0; i < personsList.size(); i++) {
            Person currentPerson = personsList.get(i);

            // Vérification de la correspondance par prénom et nom
            if (currentPerson.getFirstName().equalsIgnoreCase(updatedPerson.getFirstName()) &&
                currentPerson.getLastName().equalsIgnoreCase(updatedPerson.getLastName())) {
                // Mise à jour des informations
                personRepository.putPerson(updatedPerson);
                return;
            }
        }

        // Si aucune personne correspondante n'est trouvée
        throw new IllegalArgumentException(
            "Cette personne (" + updatedPerson.getFirstName() + " " +
            updatedPerson.getLastName() + ") n'existe pas."
        );
    }
}
