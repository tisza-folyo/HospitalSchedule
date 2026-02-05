package com.hospital.backend.dto;

import lombok.Data;

@Data
public class NursePatientDto {
    private PatientDto patient;
    private NurseDto nurse;
    private RoomDto room;
    private BedDto bed;
    private int uTaj;
}
