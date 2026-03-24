package com.hospital.backend.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateAssistantWorkRequest {
    @JsonProperty("dTaj")
    String dTaj;
    @JsonProperty("aTaj")
    String aTaj;
    LocalDate day;
    @JsonProperty("uTaj")
    String uTaj;
}
