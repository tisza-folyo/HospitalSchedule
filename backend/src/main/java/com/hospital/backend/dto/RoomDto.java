package com.hospital.backend.dto;

import com.hospital.backend.model.Bed;
import lombok.Data;

import java.util.List;

@Data
public class RoomDto {
    private Long roomId;
    private int floor;
    private int roomNumber;
}
