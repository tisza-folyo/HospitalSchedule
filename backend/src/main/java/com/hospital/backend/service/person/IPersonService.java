package com.hospital.backend.service.person;

import com.hospital.backend.dto.PatientDto;
import com.hospital.backend.request.RegistPatientRequest;

public interface IPersonService {
    PatientDto addPatient(RegistPatientRequest patientRequest);

    void deletePatient(String taj);
}
