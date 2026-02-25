package com.hospital.backend.repository;

import com.hospital.backend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room,Long> {
    Optional<Room> findRoomByRoomId(long roomNumber);

    List<Room> findAllByFloor(int floor);

    boolean existsByFloorAndRoomNumber(int floor, int roomNumber);

    @Query("SELECT DISTINCT r FROM Room r " +
            "JOIN r.beds b " +
            "WHERE b.bedId NOT IN (SELECT npc.bed.bedId FROM NursePatientCare npc)")
    List<Room> findRoomsWithAvailableBeds();
}
