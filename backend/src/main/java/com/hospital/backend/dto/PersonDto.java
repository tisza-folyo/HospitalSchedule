package com.hospital.backend.dto;

import com.hospital.backend.model.Role;
import lombok.Data;

@Data
public class PersonDto {
    private String taj;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private RoleDto role;
}
