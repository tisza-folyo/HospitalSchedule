package com.hospital.backend.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DoctorAssistantWorkDto {
    private LocalDate workDay;
    private int uTaj;
    private DoctorDto doctor;
    private AssistantDto assistant;
}
