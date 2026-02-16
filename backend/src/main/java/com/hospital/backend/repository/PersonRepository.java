package com.hospital.backend.repository;

import com.hospital.backend.model.Person;
import com.hospital.backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person,String> {
    boolean existsByTaj(String taj);

    Optional<Person> findByTajAndRole(String taj, Role role);

    Optional<Person> findByEmail(String email);

    Optional<Person> findByEmailAndRole(String email, Role role);

}
