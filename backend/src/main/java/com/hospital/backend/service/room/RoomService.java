package com.hospital.backend.service.room;

import com.hospital.backend.dto.BedDto;
import com.hospital.backend.dto.RoomDto;
import com.hospital.backend.exception.AlreadyExistsException;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.mapper.BedMapper;
import com.hospital.backend.mapper.RoomMapper;
import com.hospital.backend.model.Bed;
import com.hospital.backend.model.Room;
import com.hospital.backend.repository.BedRepository;
import com.hospital.backend.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {
    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;
    private final RoomMapper roomMapper;
    private final BedMapper bedMapper;


    @Override
    public List<BedDto> getRoomBeds(long roomId){
        List<Bed> beds = roomRepository.findRoomByRoomId(roomId).orElseThrow(() -> new ResourceNotFoundException("Room")).getBeds();
        if (beds.isEmpty()) throw new ResourceNotFoundException("No beds");
        return bedMapper.toDtoList(beds);
    }

    @Override
    public List<RoomDto> getAllRooms(){
        return roomMapper.toDtoList(roomRepository.findAll());
    }

    @Override
    public RoomDto addRoom(RoomDto roomDto){
        if (roomRepository.existsByFloorAndRoomNumber(roomDto.getFloor(), roomDto.getRoomNumber())) throw new AlreadyExistsException("Room on this floor");
        Room room = roomMapper.toEntity(roomDto);
        room.setBeds(new ArrayList<>());
        roomRepository.save(room);
        return roomMapper.toDto(room);
    }

    @Override
    public RoomDto addBeds(List<Integer> bedNumbers, long roomId){
        Room room = roomRepository.findRoomByRoomId(roomId).orElseThrow(() -> new ResourceNotFoundException("Room"));
        List<Bed> beds = bedNumbers.stream().filter(n -> room.getBeds().stream().noneMatch(b -> b.getBedNumber() == n)).map(n -> new Bed(n,room)).toList();
        if (beds.isEmpty()) throw new AlreadyExistsException("All beds");
        bedRepository.saveAll(beds);
        room.getBeds().addAll(beds);
        return roomMapper.toDto(room);
    }

    @Override
    public void deleteRoom(long roomId){
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room"));
        roomRepository.delete(room);
    }

    @Transactional
    @Override
    public void clearBeds(long roomId){
        Room room = roomRepository.findRoomByRoomId(roomId).orElseThrow(() -> new ResourceNotFoundException("Room"));
        room.getBeds().clear();
        roomRepository.save(room);
    }
}
