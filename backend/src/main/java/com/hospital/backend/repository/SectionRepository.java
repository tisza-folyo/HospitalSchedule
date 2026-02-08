package com.hospital.backend.repository;

import com.hospital.backend.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section,Long> {
    boolean existsBySectionName(String sectionName);

    Optional<Section> findBySectionName(String sectionName);
}
