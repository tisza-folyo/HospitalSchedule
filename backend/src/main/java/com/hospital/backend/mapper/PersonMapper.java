package com.hospital.backend.mapper;

import com.hospital.backend.dto.AdminDto;
import com.hospital.backend.dto.DoctorDto;
import com.hospital.backend.dto.NurseDto;
import com.hospital.backend.dto.PatientDto;
import com.hospital.backend.model.*;
import com.hospital.backend.request.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {
        RoleMapper.class,
        ImageMapper.class,
        SectionMapper.class,
        SpecialtyMapper.class
})
public interface PersonMapper {

    PatientDto toPatientDto(Patient patient);

    DoctorDto toDoctorDto(Doctor doctor);

    NurseDto toNurseDto(Nurse nurse);

    AdminDto toAdminDto(Admin admin);

    void updatePersonFromRequest(RegisterRequest request, @MappingTarget Person person);
}
