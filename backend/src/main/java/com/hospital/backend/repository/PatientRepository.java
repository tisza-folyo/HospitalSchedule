package com.hospital.backend.repository;

import com.hospital.backend.model.Patient;
import com.hospital.backend.model.Person;
import com.hospital.backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient,String> {
    Optional<Patient> findByTaj(String taj);

    Optional<Patient> findByTajAndRole(String taj, Role role);

    boolean existsByEmail(String email);

    boolean existsByTaj(String taj);
    @Query("SELECT p FROM Patient p JOIN Appointment a ON p.taj = a.patient.taj WHERE a.doctor.taj = :doctorTaj")
    List<Patient> findAllByDoctorTaj(@Param("doctorTaj") String doctorTaj);
}
