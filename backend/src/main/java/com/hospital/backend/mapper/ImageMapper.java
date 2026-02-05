package com.hospital.backend.mapper;

import com.hospital.backend.dto.ImageDto;
import com.hospital.backend.model.Image;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PatientMapper.class})
public interface ImageMapper extends GenericMapper<Image, ImageDto> {
}
