package com.hospital.backend.mapper;

import com.hospital.backend.dto.NursePatientCareDto;
import com.hospital.backend.model.NursePatientCare;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PersonMapper.class, RoomMapper.class, BedMapper.class})
public interface NursePatientCareMapper extends GenericMapper<NursePatientCare, NursePatientCareDto> {
}
