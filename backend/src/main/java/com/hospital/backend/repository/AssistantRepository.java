package com.hospital.backend.repository;

import com.hospital.backend.model.Assistant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssistantRepository extends JpaRepository<Assistant,String> {
}
