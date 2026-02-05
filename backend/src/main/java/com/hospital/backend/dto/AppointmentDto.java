package com.hospital.backend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class AppointmentDto {
    private LocalDate day;
    private LocalTime timeSlot;
    private DoctorDto doctor;
    private PatientDto patient;
    private List<ImageDto> symptomImg;
}
