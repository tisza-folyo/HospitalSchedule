package com.hospital.backend.repository;

import com.hospital.backend.model.Bed;
import com.hospital.backend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BedRepository extends JpaRepository<Bed,Long> {
    Optional<Bed> findByRoomAndBedNumber(Room room, int bedNumber);

    @Query("SELECT DISTINCT b FROM Bed b WHERE b.bedId NOT IN (SELECT npc.bed.bedId FROM NursePatientCare npc)")
    List<Bed> findUnassignedBeds();
}
