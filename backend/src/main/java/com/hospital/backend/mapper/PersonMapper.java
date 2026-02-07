package com.hospital.backend.mapper;

import com.hospital.backend.dto.*;
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

    AssistantDto toAssistantDto(Assistant assistant);

    void updatePersonFromRequest(RegisterRequest request, @MappingTarget Person person);
}
