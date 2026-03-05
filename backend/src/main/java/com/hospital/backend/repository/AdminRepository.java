package com.hospital.backend.repository;

import com.hospital.backend.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin,String> {
    boolean existsByTaj(String taj);

    boolean existsByEmail(String email);

    Optional<Admin> findByTaj(String aTaj);
}
