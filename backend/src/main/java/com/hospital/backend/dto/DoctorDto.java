package com.hospital.backend.dto;

import com.hospital.backend.model.Role;
import com.hospital.backend.model.Section;
import com.hospital.backend.model.Specialty;
import lombok.Data;

import java.time.LocalTime;

@Data
public class DoctorDto {
    private String taj;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private RoleDto role;
    private SpecialtyDto specialty;
    private SectionDto section;
    private LocalTime workHoursStart;
    private LocalTime workHoursEnd;
}
