package com.hospital.backend.repository;

import com.hospital.backend.model.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NurseRepository extends JpaRepository<Nurse,String> {
    boolean existsByTaj(String taj);

    Optional<Nurse> findByTaj(String nTaj);

    boolean existsByEmail(String email);

    @Query("SELECT n FROM Nurse n " +
            "LEFT JOIN NursePatientCare npc ON npc.nurse = n AND npc.exitDay IS NULL " +
            "GROUP BY n " +
            "HAVING COUNT(npc) <= :maxPatientsPerNurse")
    List<Nurse> findAvailableNurses(@Param("maxPatientsPerNurse") int maxPatientsPerNurse);
}
