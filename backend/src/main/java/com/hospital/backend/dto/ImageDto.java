package com.hospital.backend.dto;

import jakarta.persistence.Lob;
import lombok.Data;

import java.sql.Blob;

@Data
public class ImageDto {
    private String fileName;
    private String fileType;
    @Lob
    private Blob image;
    private PatientDto patient;
}
