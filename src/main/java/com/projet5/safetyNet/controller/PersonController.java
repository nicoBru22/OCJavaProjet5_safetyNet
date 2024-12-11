package com.projet5.safetyNet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.projet5.safetyNet.model.Person;
import com.projet5.safetyNet.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    // Injection du service via le constructeur
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Récupérer toutes les personnes.
     * @return Liste des personnes.
     */
    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        List<Person> persons = personService.getAllPersons();
        return ResponseEntity.ok(persons);
    }

    /**
     * Ajouter une nouvelle personne.
     * @param person Objet `Person` reçu en JSON dans le corps de la requête.
     * @return Message de confirmation.
     */
    @PostMapping
    public ResponseEntity<String> addPerson(@RequestBody Person person) {
        personService.addPerson(person);
        return ResponseEntity.ok("Personne ajoutée avec succès !");
    }

    /**
     * Mettre à jour les informations d'une personne.
     * @param person Objet `Person` reçu en JSON dans le corps de la requête.
     * @return Message de confirmation.
     */
    @PutMapping
    public ResponseEntity<String> updatePerson(@RequestBody Person person) {
        personService.updatePerson(person);
        return ResponseEntity.ok("Personne mise à jour avec succès !");
    }

    /**
     * Supprimer une personne.
     * @param person Objet `Person` reçu en JSON dans le corps de la requête.
     * @return Message de confirmation.
     */
    @DeleteMapping
    public ResponseEntity<String> deletePerson(@RequestBody Person person) {
        personService.deletePerson(person.getFirstName(), person.getLastName(), person.getPhone());
        return ResponseEntity.ok("Personne supprimée avec succès.");
    }
}
