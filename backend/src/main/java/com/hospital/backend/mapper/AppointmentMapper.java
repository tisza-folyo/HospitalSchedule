package com.hospital.backend.mapper;

import com.hospital.backend.dto.AppointmentDto;
import com.hospital.backend.model.Appointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface AppointmentMapper extends GenericMapper<Appointment, AppointmentDto> {
}
