package com.hospital.backend.repository;

import com.hospital.backend.model.Bed;
import com.hospital.backend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BedRepository extends JpaRepository<Bed,Long> {
    Optional<Bed> findByRoomAndBedNumber(Room room, int bedNumber);
}
