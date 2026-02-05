package com.hospital.backend.repository;

import com.hospital.backend.model.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NurseRepository extends JpaRepository<Nurse,Long> {
}
