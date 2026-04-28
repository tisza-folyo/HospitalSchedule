package com.hospital.backend.dto;

import lombok.Data;


@Data
public class RoomDto {
    private Long roomId;
    private int floor;
    private int roomNumber;
}
