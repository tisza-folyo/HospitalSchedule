package com.hospital.backend.mapper;

import com.hospital.backend.dto.NurseDto;
import com.hospital.backend.model.Nurse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface NurseMapper extends GenericMapper<Nurse, NurseDto> {
}
