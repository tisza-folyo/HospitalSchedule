package com.hospital.backend.repository;

import com.hospital.backend.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor,String> {
    boolean existsBySpecialtySpecialtyName(String specialtyName);
}
