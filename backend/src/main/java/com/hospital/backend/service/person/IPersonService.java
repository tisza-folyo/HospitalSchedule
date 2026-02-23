package com.hospital.backend.service.person;

import com.hospital.backend.dto.DoctorDto;
import com.hospital.backend.dto.PatientDto;
import com.hospital.backend.model.Person;
import com.hospital.backend.request.RegisterRequest;

import java.util.List;

public interface IPersonService {
    void deletePerson(String taj, String roleName);

    Person getPersonByTajAndRole(String taj, String roleName);

    PatientDto getPatientByTaj(String taj);

    List<PatientDto> getAllPatientsByDoctorTaj(String doctorTaj);

    List<DoctorDto> getAllDoctor();

    Object addPerson(RegisterRequest request);

    DoctorDto setSection(String dTaj, String roleName, String sectionName);
}
