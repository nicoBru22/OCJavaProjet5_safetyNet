package com.projet5.safetyNet.controller;

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

/**
 * Contrôleur REST pour la gestion des dossiers médicaux. *
 * 
 * Ce contrôleur fournit des points d'entrée pour :
 * <ul>
 * <li>Récupérer tout les dossiers médicaux.</li>
 * <li>Ajouter un nouveau dossier médical.</li>
 * <li>Mettre à jour un dossier médical.</li>
 * <li>Supprimer un dossier médical.</li>
 * </ul>
 * Il fait appel au {@link MedicalrecordService} pour effectuer les opérations
 * métier.
 */
@RestController
public class MedicalRecordController {

	private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);
	private final MedicalrecordService medicalrecordService;

	/**
	 * Constructeur du contrôleur des dossiers médicaux.
	 *
	 * @param medicalRecordService le service permettant de gérer les opérations sur
	 *                             les dossiers médicaux.
	 */
	public MedicalRecordController(MedicalrecordService medicalRecordService) {
		this.medicalrecordService = medicalRecordService;
	}

	/**
	 * Récupère la liste des dossiers médicaux.
	 * <p>
	 * Cette méthode permet de récupérer tous les dossiers médicaux à partir de
	 * l'endpoint /medicalrecords.
	 * </p>
	 *
	 * @return ResponseEntity contenant une liste des dossiers médicaux en cas de
	 *         succès ou un message d'erreur en cas d'échec.
	 */
	@GetMapping("/medicalrecords")
	public ResponseEntity<List<Medicalrecord>> getMedicalRecords() {
		logger.debug("Requête GET pour récupérer tous les dossiers médicaux.");
		List<Medicalrecord> medicalrecordList = medicalrecordService.getAllMedicalrecord();
		logger.info("La liste des dossiers médicaux a été récupérée avec succès.");
		logger.debug("Récupération réussie de {} dossiers médicaux.", medicalrecordList.size());
		return ResponseEntity.ok(medicalrecordList);
	}

	/**
	 * Ajoute un nouveau dossier médical.
	 * <p>
	 * Cette méthode permet d'ajouter un nouveau dossier médical dans le système à
	 * partir de l'endpoint /medicalrecords.
	 * </p>
	 *
	 * @param newMedicalrecord le dossier médical à ajouter.
	 * @return ResponseEntity contenant un message de réussite en cas de succès ou
	 *         un message d'erreur en cas d'échec.
	 */
	@PostMapping("/medicalrecords")
	public ResponseEntity<String> addMedicalrecord(@RequestBody Medicalrecord newMedicalrecord) {
		logger.debug("Requête POST pour ajouter un nouveau dossier médical : {}", newMedicalrecord);
		medicalrecordService.addMedicalrecord(newMedicalrecord);
		logger.info("Ajout réussi du dossier médical.");
		logger.debug("Le dossier médical {} a été ajouté avec succès", newMedicalrecord);
		return ResponseEntity.status(HttpStatus.CREATED).body("Le dossier médical a été ajouté avec succès.");
	}

	/**
	 * Supprime un dossier médical.
	 * <p>
	 * Cette méthode permet de supprimer un dossier médical du système à partir de
	 * l'endpoint /medicalrecords.
	 * </p>
	 *
	 * @param deletedMedicalrecord le dossier médical à supprimer.
	 * @return ResponseEntity contenant un message de réussite en cas de succès ou
	 *         un message d'erreur en cas d'échec.
	 */
	@DeleteMapping("/medicalrecords")
	public ResponseEntity<String> deleteMedicalrecord(@RequestBody Medicalrecord deletedMedicalrecord) {
		logger.debug("Requête DELETE pour supprimer un dossier médical : {}", deletedMedicalrecord);
		medicalrecordService.deleteMedicalrecord(deletedMedicalrecord);
		logger.info("Suppression réussie du dossier médical.");
		logger.debug("Le dossier médical {} a été supprimé avec succès", deletedMedicalrecord);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Le dossier médical a été supprimé avec succès.");
	}

	/**
	 * Met à jour un dossier médical.
	 * <p>
	 * Cette méthode permet de mettre à jour un dossier médical existant à partir de
	 * l'endpoint /medicalrecords.
	 * </p>
	 * 
	 * @param updatedMedicalrecord le dossier médical contenant les nouvelles
	 *                             informations.
	 * @return ResponseEntity contenant un message de réussite en cas de succès ou
	 *         un message d'erreur en cas d'échec.
	 */
	@PutMapping("/medicalrecords")
	public ResponseEntity<String> updateMedicalrecord(@RequestBody Medicalrecord updatedMedicalrecord) {
		logger.debug("Requête PUT pour mettre à jour un dossier médical : {}", updatedMedicalrecord);
		medicalrecordService.updateMedicalrecord(updatedMedicalrecord);
		logger.info("Mise à jour réussie du dossier médical.");
		logger.debug("Le dossier médical {} a été mis à jour avec succès", updatedMedicalrecord);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Le dossier médical a été modifié avec succès.");
	}

}
