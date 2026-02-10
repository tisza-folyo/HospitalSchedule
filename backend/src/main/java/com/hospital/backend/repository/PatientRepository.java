package com.hospital.backend.repository;

import com.hospital.backend.model.Patient;
import com.hospital.backend.model.Person;
import com.hospital.backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient,String> {
    Optional<Patient> findByTaj(String taj);

    Optional<Patient> findByTajAndRole(String taj, Role role);

    boolean existsByEmail(String email);

    boolean existsByTaj(String taj);
}
