package com.hospital.backend.mapper;

import com.hospital.backend.dto.PatientDto;
import com.hospital.backend.model.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RoleMapper.class, ImageMapper.class})
public interface PatientMapper extends GenericMapper<Patient, PatientDto> {
}
