package com.hospital.backend.service.image;

import com.hospital.backend.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    List<ImageDto> saveImages(List<MultipartFile> files, String pTaj);

    void deleteImagesByTaj(String pTaj);

    void deleteImageByDwnUrl(String dwnUrl);

    List<ImageDto> getPatientImages(String pTaj);

    void updateImage(MultipartFile file, String dwnUrl);
}
