package com.projet5.safetyNet.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.projet5.safetyNet.model.DataModel;
import com.projet5.safetyNet.model.Firestation;

@Repository
public class FirestationRepository {

    public DataRepository dataRepository;
    public DataModel dataModel;
    public List<Firestation> firestationList;

    public FirestationRepository(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        this.dataModel = dataRepository.readFile();
        this.firestationList = dataModel.getFireStations();
    }

    // Méthode pour obtenir toutes les firestations
    public List<Firestation> getAllFirestations() {
        return firestationList;
    }
    
    //Méthode pour ajouter une nouvelle caserne
    public void addFirestation(Firestation newFirestation) {
    	firestationList.add(newFirestation);
    	dataModel.setFireStations(firestationList);
    	dataRepository.writeFile(dataModel);
    }
    
    //Méthode pour supprimer une caserne
    public void deleteFirestation(Firestation deletedFirestation) {
    	System.out.println("Dans le repo, la station à supprimer : " + deletedFirestation);
    	System.out.println("Dans le repo, l address station à supprimer : " + deletedFirestation.getStation());
    	System.out.println("Dans le repo, la station à supprimer : " + deletedFirestation.getAddress());
    	
    	firestationList.removeIf(firestation -> firestation.getAddress().equals(deletedFirestation.getAddress())
    			&& firestation.getStation().equals(deletedFirestation.getStation()));
    	dataModel.setFireStations(firestationList);
    	dataRepository.writeFile(dataModel);
    }
    
    //Méthode pour modifier une caserne
    public void updateFirestation(Firestation updatedFirestation) {
        boolean updated = firestationList.stream()
                .filter(firestation -> firestation.getAddress().equalsIgnoreCase(updatedFirestation.getAddress()))
                .findFirst()
                .map(firestation -> {
                    firestationList.set(firestationList.indexOf(firestation), updatedFirestation);
                    return true;
                })
                .orElse(false);

        if (updated) {
            dataModel.setFireStations(firestationList);
            dataRepository.writeFile(dataModel);
        } else {
            throw new IllegalArgumentException("Caserne non trouvée pour mise à jour.");
        }
    }
}
