package com.hospital.backend.controller;

import com.hospital.backend.dto.ImageDto;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.response.ApiResponse;
import com.hospital.backend.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/{pTaj}/images")
    public ResponseEntity<ApiResponse> getPatientImages(@PathVariable String pTaj){
        try {
            List<ImageDto> images = imageService.getPatientImages(pTaj);
            return ResponseEntity.ok(new ApiResponse("Success", images));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/upload-to-patient")
    public ResponseEntity<ApiResponse> saveImagesToPatient(@RequestBody List<MultipartFile> files, @RequestParam String pTaj){
        try {
            List<ImageDto> imageDtos = imageService.saveImages(files, pTaj);
            return ResponseEntity.ok(new ApiResponse("Saved", imageDtos));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/upload-to-appointment")
    public ResponseEntity<ApiResponse> saveImageToAppointment(@RequestParam List<MultipartFile> files, @RequestParam Long appointmentId){
        try {
            List<ImageDto> imageDtos = imageService.saveImages(files,appointmentId);
            return ResponseEntity.ok(new ApiResponse("Saved", imageDtos));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{pTaj}/images")
    public ResponseEntity<ApiResponse> deleteImages(@PathVariable String pTaj){
        try {
            imageService.deleteImagesByTaj(pTaj);
            return ResponseEntity.ok(new ApiResponse("Deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{dwnUrl}/image")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable String dwnUrl){
        try {
            imageService.deleteImageByDwnUrl(dwnUrl);
            return ResponseEntity.ok(new ApiResponse("Deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
