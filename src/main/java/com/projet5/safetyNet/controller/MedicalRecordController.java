package com.projet5.safetyNet.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.projet5.safetyNet.model.Medicalrecord;
import com.projet5.safetyNet.service.MedicalrecordService;

@RestController
public class MedicalRecordController {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);
    private final MedicalrecordService medicalrecordService;

    public MedicalRecordController(MedicalrecordService medicalRecordService) {
        this.medicalrecordService = medicalRecordService;
    }

    @GetMapping("/medicalrecords")
    public ResponseEntity<?> getMedicalRecords() throws IOException {
        logger.info("Requête GET pour récupérer tous les dossiers médicaux.");
        try {
            List<Medicalrecord> medicalrecordList = medicalrecordService.getAllMedicalrecord();
            logger.info("Récupération réussie de {} dossiers médicaux.", medicalrecordList.size());
            return ResponseEntity.ok(medicalrecordList);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des dossiers médicaux.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur dans la récupération de la liste des dossiers médicaux.");
        }
    }

    @PostMapping("/medicalrecords")
    public ResponseEntity<?> addMedicalrecord(@RequestBody Medicalrecord newMedicalrecord) throws Exception {
        logger.info("Requête POST pour ajouter un nouveau dossier médical : {}", newMedicalrecord);
        try {
            medicalrecordService.addMedicalrecord(newMedicalrecord);
            logger.info("Ajout réussi du dossier médical.");
            return ResponseEntity.status(HttpStatus.CREATED).body("Le medicalrecord a été ajouté avec succès.");
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout d'un nouveau dossier médical.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur dans l'ajout d'un nouveau medicalrecord.");
        }
    }

    @DeleteMapping("/medicalrecords")
    public ResponseEntity<?> deleteMedicalrecord(@RequestBody Medicalrecord deletedMedicalrecord) throws Exception {
        logger.info("Requête DELETE pour supprimer un dossier médical : {}", deletedMedicalrecord);
        try {
            medicalrecordService.deleteMedicalrecord(deletedMedicalrecord);
            logger.info("Suppression réussie du dossier médical.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Le medicalrecord a été supprimé avec succès.");
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du dossier médical.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression du medicalrecord.");
        }
    }

    @PutMapping("/medicalrecords")
    public ResponseEntity<?> updateMedicalrecord(@RequestBody Medicalrecord updatedMedicalrecord) throws Exception {
        logger.info("Requête PUT pour mettre à jour un dossier médical : {}", updatedMedicalrecord);
        try {
            medicalrecordService.updateMedicalrecord(updatedMedicalrecord);
            logger.info("Mise à jour réussie du dossier médical.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Le medicalrecord a été modifié avec succès.");
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du dossier médical.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la modification du medicalrecord.");
        }
    }
}
