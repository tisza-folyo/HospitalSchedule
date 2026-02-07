package com.hospital.backend.repository;

import com.hospital.backend.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,String> {
    boolean existsByTaj(String taj);
}
