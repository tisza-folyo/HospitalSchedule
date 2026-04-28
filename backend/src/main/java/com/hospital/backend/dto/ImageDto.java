package com.hospital.backend.dto;

import lombok.Data;


@Data
public class ImageDto {
    private String fileName;
    private String fileType;
    private String dwnUrl;
    private String base64Data;
    private String patientTaj;
}
