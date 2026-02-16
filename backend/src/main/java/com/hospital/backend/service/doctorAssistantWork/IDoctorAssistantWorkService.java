package com.hospital.backend.service.doctorAssistantWork;

import com.hospital.backend.dto.AssistantDto;
import com.hospital.backend.dto.DoctorAssistantWorkDto;
import com.hospital.backend.dto.DoctorDto;
import com.hospital.backend.model.Doctor;
import com.hospital.backend.request.UpdateAssistantWorkRequest;

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

    DoctorAssistantWorkDto assignAssistant(UpdateAssistantWorkRequest request);

    DoctorAssistantWorkDto assignAssistant(String aTaj, String uTaj, LocalDate day);

    DoctorAssistantWorkDto changeAssistantByAssistant(UpdateAssistantWorkRequest request);

    void deleteWork(Long workId);
}
