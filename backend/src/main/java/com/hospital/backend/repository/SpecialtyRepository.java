package com.hospital.backend.repository;

import com.hospital.backend.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecialtyRepository extends JpaRepository<Specialty,Long> {
    boolean existsBySpecialtyName(String specialtyName);

    Optional<Specialty> findBySpecialtyName(String specialtyName);
}
