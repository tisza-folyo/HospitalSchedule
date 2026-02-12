package com.hospital.backend.repository;

import com.hospital.backend.model.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface NurseRepository extends JpaRepository<Nurse,String> {
    boolean existsByTaj(String taj);

    Optional<Nurse> findByTaj(String nTaj);
}
