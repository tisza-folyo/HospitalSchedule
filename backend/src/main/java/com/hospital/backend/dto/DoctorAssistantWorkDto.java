package com.hospital.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DoctorAssistantWorkDto {
    private Long workId;
    private LocalDate workDay;
    @JsonProperty("uTaj")
    private String uTaj;
    private DoctorDto doctor;
    private AssistantDto assistant;
}
