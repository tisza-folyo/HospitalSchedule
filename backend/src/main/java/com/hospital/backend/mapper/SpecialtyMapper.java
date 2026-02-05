package com.hospital.backend.mapper;

import com.hospital.backend.dto.SpecialtyDto;
import com.hospital.backend.model.Specialty;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpecialtyMapper extends GenericMapper<Specialty, SpecialtyDto> {
}
