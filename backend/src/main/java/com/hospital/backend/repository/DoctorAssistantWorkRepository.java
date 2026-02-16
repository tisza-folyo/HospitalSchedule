package com.hospital.backend.repository;

import com.hospital.backend.model.Assistant;
import com.hospital.backend.model.Doctor;
import com.hospital.backend.model.DoctorAssistantWork;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DoctorAssistantWorkRepository extends JpaRepository<DoctorAssistantWork,Long> {
    List<DoctorAssistantWork> findAllByDoctor(Doctor doctor);

    List<DoctorAssistantWork> findAllByAssistant(Assistant assistant);

    List<DoctorAssistantWork> findAllByWorkDay(LocalDate workDay);

    boolean existsByDoctorAndWorkDay(Doctor doctor, LocalDate workDay);

    Optional<DoctorAssistantWork> findByDoctorAndWorkDay(Doctor doctor, LocalDate workDay);

    boolean existsByAssistantAndWorkDay(Assistant assistant, LocalDate day);

    Optional<DoctorAssistantWork> findFirstByAssistantIsNullAndWorkDay(LocalDate day);
}
