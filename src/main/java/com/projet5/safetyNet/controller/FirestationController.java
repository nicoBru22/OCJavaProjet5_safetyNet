package com.projet5.safetyNet.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
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
}
