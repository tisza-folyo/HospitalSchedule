package com.hospital.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class PatientDto {
    private String taj;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private RoleDto role;
    private List<ImageDto> personalDocImages;
}
