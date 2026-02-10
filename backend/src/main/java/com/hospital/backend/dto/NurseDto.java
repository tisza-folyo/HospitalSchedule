package com.hospital.backend.dto;

import com.hospital.backend.model.Role;
import lombok.Data;

import java.time.LocalTime;

@Data
public class NurseDto {
    private String taj;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private RoleDto role;
    private LocalTime workHoursStart;
    private LocalTime workHoursEnd;
}
