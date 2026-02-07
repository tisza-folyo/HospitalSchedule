package com.hospital.backend.request;

import com.hospital.backend.dto.RoleDto;
import com.hospital.backend.dto.SectionDto;
import com.hospital.backend.dto.SpecialtyDto;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegisterRequest {
    private String taj;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private String password;
    private RoleDto role;
    private SpecialtyDto specialty;
}
