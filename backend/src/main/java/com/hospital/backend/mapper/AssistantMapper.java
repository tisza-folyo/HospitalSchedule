package com.hospital.backend.mapper;

import com.hospital.backend.dto.AssistantDto;
import com.hospital.backend.model.Assistant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface AssistantMapper extends GenericMapper<Assistant, AssistantDto> {
}
