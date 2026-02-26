package com.hospital.backend.repository;

import com.hospital.backend.model.Doctor;
import com.hospital.backend.model.Nurse;
import com.hospital.backend.model.NursePatientCare;
import com.hospital.backend.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface NursePatientCareRepository extends JpaRepository<NursePatientCare,Long> {

    @Query("SELECT npc FROM NursePatientCare npc " +
            "WHERE npc.nurse IS NULL AND npc.exitDay IS NULL "+
            "ORDER BY npc.entryDay ASC")
    List<NursePatientCare> findPatientsWaitingForNurse();

    List<NursePatientCare> findAllByNurse(Nurse nurse);

    @Query("SELECT npc FROM NursePatientCare npc " +
            "WHERE npc.exitDay IS NULL AND npc.patient = :patient")
    Optional<NursePatientCare> findByPatientActiveCare(@Param("patient") Patient patient);

    boolean existsByPatientAndExitDayIsNull(Patient patient);



    Optional<NursePatientCare> findFirstByNurseIsNullAndExitDayIsNull();

    List<NursePatientCare> findAllByPatient(Patient patient);
}
