package com.projet5.safetyNet.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.projet5.safetyNet.model.DataModel;
import com.projet5.safetyNet.model.Person;

@Repository
public class PersonRepository {

    // Instance du dépôt de données principal
    public DataRepository dataRepository;
    // Liste des personnes chargée depuis le fichier JSON
    public List<Person> personsList;
    // Modèle de données complet contenant toutes les listes du fichier JSON
    public DataModel dataModel;

    // Constructeur pour initialiser le repository avec le DataRepository
    public PersonRepository(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        // Lire les données du fichier JSON pour initialiser dataModel
        this.dataModel = dataRepository.readFile();
        // Récupérer la liste des personnes à partir de dataModel
        this.personsList = dataModel.getPersonsList();
    }

    /**
     * Méthode pour récupérer toutes les personnes enregistrées.
     * @return Une liste de toutes les personnes présentes dans le fichier JSON.
     */
    public List<Person> getAllPerson() {
        return personsList;
    }

    /**
     * Méthode pour supprimer une personne spécifique de la liste.
     * @param firstName Prénom de la personne à supprimer.
     * @param lastName Nom de famille de la personne à supprimer.
     */

    public void deletePerson(String firstName, String lastName, String phone) {
        // Suppression de la personne correspondant aux critères
        personsList.removeIf(person -> person.getFirstName().equalsIgnoreCase(firstName) &&
                                        person.getLastName().equalsIgnoreCase(lastName) &&
                                        person.getPhone().equalsIgnoreCase(phone));

        // Mise à jour de la liste des personnes dans le modèle de données
        dataModel.setPersonsList(personsList);
        // Écriture des modifications dans le fichier
        dataRepository.writeFile(dataModel);
    }

    /**
     * Méthode pour ajouter une nouvelle personne à la liste.
     * @param newPerson La nouvelle personne à ajouter.
     */
    public void addPerson(Person newPerson) {
        // Ajouter la nouvelle personne à la liste
        personsList.add(newPerson);

        // Mettre à jour la liste des personnes dans dataModel
        dataModel.setPersonsList(personsList);

        // Écrire les modifications dans le fichier JSON
        dataRepository.writeFile(dataModel);
    }

    /**
     * Méthode pour mettre à jour les informations d'une personne existante.
     * @param updatedPerson La personne contenant les informations mises à jour.
     */
    public void putPerson(Person updatedPerson) {
        // Parcourir la liste pour trouver la personne correspondante
        for (int i = 0; i < personsList.size(); i++) {
            Person person = personsList.get(i);
            // Comparer les noms et prénoms pour identifier la personne à mettre à jour
            if (person.getFirstName().equalsIgnoreCase(updatedPerson.getFirstName())
                    && person.getLastName().equalsIgnoreCase(updatedPerson.getLastName())) {
                // Remplacer l'ancienne personne par la nouvelle version mise à jour
                personsList.set(i, updatedPerson);
                break;
            }
        }

        // Mettre à jour la liste des personnes dans dataModel
        dataModel.setPersonsList(personsList);

        // Écrire les modifications dans le fichier JSON
        dataRepository.writeFile(dataModel);
    }
}
