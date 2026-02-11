package com.hospital.backend.service.doctorAssistantWork;

import com.hospital.backend.dto.AssistantDto;
import com.hospital.backend.dto.DoctorAssistantWorkDto;
import com.hospital.backend.dto.DoctorDto;
import com.hospital.backend.model.Doctor;

import java.time.LocalDate;
import java.util.List;

public interface IDoctorAssistantWorkService {
    List<DoctorAssistantWorkDto> getAllWorks();

    List<DoctorAssistantWorkDto> getDoctorWorks(String doctorTaj);

    List<DoctorAssistantWorkDto> getAssistantWorks(String assistantTaj);

    List<AssistantDto> getFreeAssistants(LocalDate day);

    List<DoctorDto> getFreeDoctors(LocalDate day);

    DoctorAssistantWorkDto addWork(DoctorAssistantWorkDto doctorAssistantWorkDto);

    DoctorAssistantWorkDto addWork(LocalDate day, String dTaj, String uTaj);

    DoctorAssistantWorkDto assignAssistant(String dTaj, String aTaj, LocalDate day, String uTaj);

    DoctorAssistantWorkDto changeAssistantByAssistant(String dTaj, String aTaj, LocalDate day, String uTaj);

    void deleteWork(Long workId);
}
