package com.hospital.backend.repository;

import com.hospital.backend.model.Assistant;
import com.hospital.backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AssistantRepository extends JpaRepository<Assistant,String> {
    boolean existsByTaj(String taj);

    Optional<Assistant> findByTajAndRole(String taj, Role role);

    @Query("SELECT a FROM Assistant a WHERE a NOT IN " +
            "(SELECT daw.assistant FROM DoctorAssistantWork daw WHERE daw.workDay = :day)")
    List<Assistant> findFreeAssistantsByDay(@Param("day") LocalDate day);

    Optional<Assistant> findByTaj(String aTaj);

    boolean existsByEmail(String email);


}
