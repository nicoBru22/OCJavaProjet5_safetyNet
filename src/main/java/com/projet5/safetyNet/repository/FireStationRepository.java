package com.projet5.safetyNet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projet5.safetyNet.model.FireStationModel;

@Repository
public interface FireStationRepository extends JpaRepository<FireStationModel, Long> {

}
