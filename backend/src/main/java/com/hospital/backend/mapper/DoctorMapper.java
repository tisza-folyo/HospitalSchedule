package com.hospital.backend.mapper;

import com.hospital.backend.dto.DoctorDto;
import com.hospital.backend.model.Doctor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RoleMapper.class, SectionMapper.class, SpecialtyMapper.class})
public interface DoctorMapper extends GenericMapper<Doctor, DoctorDto> {
}
