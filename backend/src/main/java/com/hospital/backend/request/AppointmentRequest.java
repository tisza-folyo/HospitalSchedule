package com.hospital.backend.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequest {
    private String doctorTaj;
    private String patientTaj;
    private LocalDate day;
    private LocalTime timeSlot;
    private String description;
}
