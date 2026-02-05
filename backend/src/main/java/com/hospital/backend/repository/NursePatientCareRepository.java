package com.hospital.backend.repository;

import com.hospital.backend.model.NursePatientCare;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NursePatientCareRepository extends JpaRepository<NursePatientCare,Long> {
}
