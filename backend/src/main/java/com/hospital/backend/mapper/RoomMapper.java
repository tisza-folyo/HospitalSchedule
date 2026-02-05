package com.hospital.backend.mapper;

import com.hospital.backend.dto.RoomDto;
import com.hospital.backend.model.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BedMapper.class})
public interface RoomMapper extends GenericMapper<Room, RoomDto> {
}
