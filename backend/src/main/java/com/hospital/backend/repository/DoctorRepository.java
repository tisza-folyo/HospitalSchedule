package com.hospital.backend.repository;

import com.hospital.backend.model.Assistant;
import com.hospital.backend.model.Doctor;
import com.hospital.backend.model.Role;
import com.hospital.backend.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor,String> {
    boolean existsBySpecialtySpecialtyName(String specialtyName);

    boolean existsByTaj(String taj);

    boolean existsBySection(Section section);


    Optional<Doctor> findByTajAndRole(String taj, Role role);

    List<Doctor> findAllBySection(Section section);

    @Query("SELECT daw.doctor FROM DoctorAssistantWork daw " +
            "WHERE daw.workDay = :day AND daw.assistant IS NULL")
    List<Doctor> findDoctorsWithoutAssistant(@Param("day") LocalDate day);

    Optional<Doctor> findByTaj(String dTaj);
}
