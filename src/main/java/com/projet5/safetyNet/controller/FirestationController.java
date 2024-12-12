package com.projet5.safetyNet.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.projet5.safetyNet.model.Firestation;
import com.projet5.safetyNet.service.FirestationService;

@RestController
public class FirestationController {

    private final FirestationService firestationService;

    public FirestationController(FirestationService fireStationService) {
        this.firestationService = fireStationService;
    }

    @GetMapping("/firestation")
    public List<Firestation> getAllFireStation() {
    	return firestationService.getAllFireStations();
    }
    
    @PostMapping("/firestation")
    public void addFirestation(@RequestBody Firestation newFirestation) throws Exception {
    	firestationService.addFirestation(newFirestation);
    }
    
    @DeleteMapping("/firestation")
    public void deleteFirestation(@RequestBody Firestation deletedFirestation) throws Exception {
    	
    	System.out.println("Dans le controlleur, la station à supprimer : " + deletedFirestation);
    	firestationService.deleteFirestation(deletedFirestation);
    }
    
    @PutMapping("/firestation")
    public void updateFirestation(@RequestBody Firestation updatedFirestation) throws Exception {
    	firestationService.updateFirestation(updatedFirestation);
	}
}
