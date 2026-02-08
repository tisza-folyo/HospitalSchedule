package com.hospital.backend.service.person;

import com.hospital.backend.dto.DoctorDto;
import com.hospital.backend.dto.PatientDto;
import com.hospital.backend.request.RegisterRequest;

public interface IPersonService {
    void deletePerson(String taj, String roleName);

    Object addPerson(RegisterRequest request);

    DoctorDto setSection(String dTaj, String roleName, String sectionName);
}
