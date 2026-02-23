package com.hospital.backend.mapper;

import com.hospital.backend.dto.ImageDto;
import com.hospital.backend.model.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.mapstruct.Named;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

@Mapper(componentModel = "spring")
public interface ImageMapper extends GenericMapper<Image, ImageDto> {

    @Override
    @Mapping(source = "patient.taj", target = "patientTaj")
    @Mapping(source = "image", target = "base64Data", qualifiedByName = "blobToBase64")
    ImageDto toDto(Image entity);

    @Override
    @Mapping(source = "patientTaj", target = "patient.taj")
    Image toEntity(ImageDto dto);

    @Named("blobToBase64")
    default String blobToBase64(Blob blob) {
        if (blob == null) return null;
        try {
            byte[] bytes = blob.getBytes(1, (int) blob.length());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (SQLException e) {
            return null;
        }
    }
}
