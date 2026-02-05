package com.hospital.backend.mapper;

import com.hospital.backend.dto.BedDto;
import com.hospital.backend.model.Bed;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RoomMapper.class})
public interface BedMapper extends GenericMapper<Bed, BedDto> {
}
