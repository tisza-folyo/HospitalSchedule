package com.hospital.backend.mapper;

import com.hospital.backend.dto.BedDto;
import com.hospital.backend.model.Bed;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BedMapper extends GenericMapper<Bed, BedDto> {
    @Override
    @Mapping(source = "room.roomNumber", target = "roomNumber")
    BedDto toDto(Bed entity);

    @Override
    @Mapping(source = "roomNumber", target = "room.roomNumber")
    Bed toEntity(BedDto dto);
}
