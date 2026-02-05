package com.hospital.backend.dto;

import com.hospital.backend.model.Bed;
import lombok.Data;

import java.util.List;

@Data
public class RoomDto {
    private int floor;
    private String roomNumber;
    private List<BedDto> beds;
}
