package com.hospital.backend.service.person;

import com.hospital.backend.dto.PatientDto;
import com.hospital.backend.request.RegisterRequest;

public interface IPersonService {
    void deletePatient(String taj);

    Object addPerson(RegisterRequest request);
}
