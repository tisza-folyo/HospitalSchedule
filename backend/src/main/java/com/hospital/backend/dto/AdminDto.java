package com.hospital.backend.dto;

import lombok.Data;

@Data
public class AdminDto {
    private String taj;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private RoleDto role;
}
