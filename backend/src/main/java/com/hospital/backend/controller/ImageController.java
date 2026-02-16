package com.hospital.backend.controller;

import com.hospital.backend.dto.ImageDto;
import com.hospital.backend.response.ApiResponse;
import com.hospital.backend.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/{pTaj}/images")
    public ResponseEntity<ApiResponse> getPatientImages(@PathVariable String pTaj) {
        List<ImageDto> results = imageService.getPatientImages(pTaj);
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @PostMapping("/upload-to-patient")
    public ResponseEntity<ApiResponse> saveImagesToPatient(@RequestBody List<MultipartFile> files, @RequestParam String pTaj) {
        List<ImageDto> results = imageService.saveImages(files, pTaj);
        return ResponseEntity.ok(new ApiResponse("Saved", results));
    }

    @PostMapping("/upload-to-appointment")
    public ResponseEntity<ApiResponse> saveImageToAppointment(@RequestParam List<MultipartFile> files, @RequestParam Long appointmentId) {
        List<ImageDto> results = imageService.saveImages(files, appointmentId);
        return ResponseEntity.ok(new ApiResponse("Saved", results));
    }

    @DeleteMapping("/delete/{pTaj}/images")
    public ResponseEntity<ApiResponse> deleteImages(@PathVariable String pTaj) {
        imageService.deleteImagesByTaj(pTaj);
        return ResponseEntity.ok(new ApiResponse("Deleted", null));
    }

    @DeleteMapping("/delete/{dwnUrl}/image")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable String dwnUrl) {
        imageService.deleteImageByDwnUrl(dwnUrl);
        return ResponseEntity.ok(new ApiResponse("Deleted", null));
    }
}
