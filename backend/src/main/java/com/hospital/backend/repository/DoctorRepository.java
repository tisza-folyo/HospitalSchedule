package com.hospital.backend.repository;

import com.hospital.backend.model.Doctor;
import com.hospital.backend.model.Role;
import com.hospital.backend.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor,String> {
    boolean existsBySpecialtySpecialtyName(String specialtyName);

    boolean existsByTaj(String taj);

    boolean existsBySection(Section section);


    Optional<Doctor> findByTajAndRole(String taj, Role role);
}
