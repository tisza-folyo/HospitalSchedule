package com.hospital.backend.service.room;

import com.hospital.backend.dto.BedDto;
import com.hospital.backend.dto.RoomDto;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IRoomService {
    List<BedDto> getRoomBeds(long roomId);

    List<RoomDto> getAllRooms();

    RoomDto addRoom(RoomDto roomDto);


    RoomDto addBeds(List<Integer> bedNumbers, long roomId);

    void deleteRoom(long roomId);

    @Transactional
    void clearBeds(long roomId);
}
