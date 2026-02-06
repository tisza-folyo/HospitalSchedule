package com.hospital.backend.repository;

import com.hospital.backend.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image,Long> {
    Optional<Image> findByPatient_Taj(String patientTaj);

    List<Image> findAllByPatient_Taj(String pTaj);

    Optional<Image> findByDwnUrl(String dwnUrl);
}
