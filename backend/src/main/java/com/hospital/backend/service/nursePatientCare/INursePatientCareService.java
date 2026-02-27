package com.hospital.backend.service.nursePatientCare;

import com.hospital.backend.dto.NursePatientCareDto;
import com.hospital.backend.request.AddCareRequest;

import java.time.LocalDate;
import java.util.List;

public interface INursePatientCareService {
    List<NursePatientCareDto> getAllNursePatientCares();

    List<NursePatientCareDto> getAllNonCares();

    List<NursePatientCareDto> getAllCaresForCare();

    List<NursePatientCareDto> getAllCaresByNurse(String nTaj);

    List<NursePatientCareDto> getAllCaresByPatient(String pTaj);

    NursePatientCareDto getActiveCareByPatient(String pTaj);

    List<NursePatientCareDto> getAllActivesByNurse(String nTaj);

    List<NursePatientCareDto> getAllNonActivesByNurse(String nTaj);

    NursePatientCareDto addCare(AddCareRequest request);

    NursePatientCareDto assignNurseToPatient(String nTaj, String pTaj, String uTaj);

    NursePatientCareDto assignNurseToPatient(String nTaj, String uTaj);

    NursePatientCareDto exitPatientCare(String pTaj, LocalDate exitDay, String uTaj);

    NursePatientCareDto changeNurse(String nTaj, String pTaj, String uTaj);

    void deleteCare(Long careId);
}
