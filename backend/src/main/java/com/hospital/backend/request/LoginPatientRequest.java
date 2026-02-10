package com.hospital.backend.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginPatientRequest {
    private String taj;
    private String roleName;
    private String email;
    private String password;
}
