package com.hospital.backend.service.bed;

import com.hospital.backend.dto.BedDto;

public interface IBedService {
    BedDto addBed(long roomId, int bedNumber);

    void deleteBed(long bedId);

    void deleteBed(long roomId, int bedNumber);
}
