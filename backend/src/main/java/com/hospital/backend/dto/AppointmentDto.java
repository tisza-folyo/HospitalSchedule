package com.hospital.backend.dto;

import com.hospital.backend.model.Status;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class AppointmentDto {
    private Long appointmentId;
    private LocalDate day;
    private LocalTime timeSlot;
    private DoctorDto doctor;
    private PatientDto patient;
    private Status status;
    private List<ImageDto> symptomImg;
}
