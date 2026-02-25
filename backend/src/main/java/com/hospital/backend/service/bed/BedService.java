package com.hospital.backend.service.bed;

import com.hospital.backend.dto.BedDto;
import com.hospital.backend.exception.AlreadyExistsException;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.mapper.BedMapper;
import com.hospital.backend.model.Bed;
import com.hospital.backend.model.Room;
import com.hospital.backend.repository.BedRepository;
import com.hospital.backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BedService implements IBedService {
    private final BedRepository bedRepository;
    private final RoomRepository roomRepository;
    private final BedMapper bedMapper;

    @Override
    public List<BedDto> getAllFreeBeds(){
        List<Bed> beds = bedRepository.findUnassignedBeds();
        return bedMapper.toDtoList(beds);
    }

    @Override
    public BedDto addBed(long roomId, int bedNumber){
        Room room = roomRepository.findRoomByRoomId(roomId).orElseThrow(() -> new ResourceNotFoundException("Room"));
        if (room.getBeds().stream().anyMatch(b -> b.getBedNumber() == bedNumber)) throw new AlreadyExistsException("Bed");
        Bed bed = new Bed();
        bed.setRoom(room);
        bed.setBedNumber(bedNumber);
        room.addBedToRoom(bed);
        bedRepository.save(bed);
        return bedMapper.toDto(bed);
    }

    @Override
    public void deleteBed(long bedId){
        Bed bed = bedRepository.findById(bedId).orElseThrow(() -> new ResourceNotFoundException("Bed"));
        Room room = bed.getRoom();
        room.removeBedFromRoom(bed);
        bedRepository.delete(bed);
    }

    @Override
    public void deleteBed(long roomId, int bedNumber){
        Room room = roomRepository.findRoomByRoomId(roomId).orElseThrow(() -> new ResourceNotFoundException("Room"));
        Bed bed = room.getBeds().stream().filter(b -> b.getBedNumber() == bedNumber).findFirst().orElseThrow(() -> new ResourceNotFoundException("Bed"));
        room.removeBedFromRoom(bed);
        bedRepository.delete(bed);
    }
}
