package com.hospital.backend.request;

import com.hospital.backend.dto.BedDto;
import com.hospital.backend.dto.NurseDto;
import com.hospital.backend.dto.PatientDto;
import com.hospital.backend.dto.RoomDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AddCareRequest {
    private String pTaj;
    private String nTaj;
    private long roomId;
    private int bedNumber;
    private String uTaj;
    private LocalDate entryDay;
}
