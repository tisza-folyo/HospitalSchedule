package com.hospital.backend.repository;

import com.hospital.backend.model.Bed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BedRepository extends JpaRepository<Bed,Long> {
}
