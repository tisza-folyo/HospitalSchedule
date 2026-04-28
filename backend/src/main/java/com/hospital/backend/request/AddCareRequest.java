package com.hospital.backend.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AddCareRequest {
    @JsonProperty("pTaj")
    private String pTaj;
    @JsonProperty("nTaj")
    private String nTaj;
    private long roomId;
    private int bedNumber;
    @JsonProperty("uTaj")
    private String uTaj;
    private LocalDate entryDay;
}
