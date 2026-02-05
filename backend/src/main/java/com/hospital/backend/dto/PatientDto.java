package com.hospital.backend.dto;

import com.hospital.backend.model.Image;
import com.hospital.backend.model.Role;
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
