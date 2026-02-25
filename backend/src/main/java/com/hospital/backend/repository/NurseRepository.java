package com.hospital.backend.repository;

import com.hospital.backend.model.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NurseRepository extends JpaRepository<Nurse,String> {
    boolean existsByTaj(String taj);

    Optional<Nurse> findByTaj(String nTaj);

    boolean existsByEmail(String email);

    @Query("SELECT DISTINCT n FROM Nurse n WHERE n NOT IN (SELECT npc.nurse FROM NursePatientCare npc) " +
            "OR n IN (SELECT npc2.nurse FROM NursePatientCare npc2 WHERE npc2.exitDay IS NOT NULL)")
    List<Nurse> findAvailableNurses();
}
