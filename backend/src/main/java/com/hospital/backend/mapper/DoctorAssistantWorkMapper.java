package com.hospital.backend.mapper;

import com.hospital.backend.dto.DoctorAssistantWorkDto;
import com.hospital.backend.model.DoctorAssistantWork;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DoctorMapper.class, AssistantMapper.class})
public interface DoctorAssistantWorkMapper extends GenericMapper<DoctorAssistantWork, DoctorAssistantWorkDto> {
}
