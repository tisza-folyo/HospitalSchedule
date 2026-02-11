package com.hospital.backend.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateAssistantWorkRequest {
    String dTaj;
    String aTaj;
    LocalDate day;
    String uTaj;
}
