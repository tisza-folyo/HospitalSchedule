package com.hospital.backend.service.bed;

import com.hospital.backend.dto.BedDto;

import java.util.List;

public interface IBedService {
    List<BedDto> getAllFreeBeds();

    BedDto addBed(long roomId, int bedNumber);

    void deleteBed(long bedId);

    void deleteBed(long roomId, int bedNumber);
}
