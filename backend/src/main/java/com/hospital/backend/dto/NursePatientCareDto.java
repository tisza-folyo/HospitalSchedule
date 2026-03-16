package com.hospital.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NursePatientCareDto {
    private PatientDto patient;
    private NurseDto nurse;
    private RoomDto room;
    private BedDto bed;
    @JsonProperty("uTaj")
    private String uTaj;
    private LocalDate entryDay;
    private LocalDate exitDay;
}
