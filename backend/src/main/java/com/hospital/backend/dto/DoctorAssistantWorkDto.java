package com.hospital.backend.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DoctorAssistantWorkDto {
    private Long workId;
    private LocalDate workDay;
    private String uTaj;
    private DoctorDto doctor;
    private AssistantDto assistant;
}
