package com.hospital.backend.dto;

import jakarta.persistence.Lob;
import lombok.Data;

import java.sql.Blob;

@Data
public class ImageDto {
    private String fileName;
    private String fileType;
    private String dwnUrl;
    private String base64Data;
    private String patientTaj;
}
