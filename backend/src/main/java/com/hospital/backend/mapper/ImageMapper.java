package com.hospital.backend.mapper;

import com.hospital.backend.dto.ImageDto;
import com.hospital.backend.model.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageMapper extends GenericMapper<Image, ImageDto> {
    @Override
    @Mapping(source = "patient.taj", target = "patientTaj")
    ImageDto toDto(Image entity);

    @Override
    @Mapping(source = "patientTaj", target = "patient.taj")
    Image toEntity(ImageDto dto);
}
