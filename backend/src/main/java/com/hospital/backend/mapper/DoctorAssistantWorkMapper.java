package com.hospital.backend.mapper;

import com.hospital.backend.dto.DoctorAssistantWorkDto;
import com.hospital.backend.model.DoctorAssistantWork;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface DoctorAssistantWorkMapper extends GenericMapper<DoctorAssistantWork, DoctorAssistantWorkDto> {
    @Override
    @Mapping(source = "workId", target = "workId")
    DoctorAssistantWorkDto toDto(DoctorAssistantWork entity);
}
