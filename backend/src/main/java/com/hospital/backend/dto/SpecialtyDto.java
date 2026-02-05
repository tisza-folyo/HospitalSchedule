package com.hospital.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SpecialtyDto {
    private String specialtyName;
    private int treatmentTimeInMinutes;
}
