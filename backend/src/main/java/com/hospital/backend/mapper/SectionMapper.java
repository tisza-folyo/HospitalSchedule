package com.hospital.backend.mapper;

import com.hospital.backend.dto.SectionDto;
import com.hospital.backend.model.Section;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SectionMapper extends GenericMapper<Section, SectionDto> {
}
