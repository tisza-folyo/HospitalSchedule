package com.hospital.backend.request;

import com.hospital.backend.dto.RoleDto;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegistPatientRequest {
    private String taj;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private String password;
    private RoleDto role;
}
