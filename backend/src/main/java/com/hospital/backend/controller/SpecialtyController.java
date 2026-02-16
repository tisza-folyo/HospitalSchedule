package com.hospital.backend.controller;

import com.hospital.backend.dto.SpecialtyDto;
import com.hospital.backend.response.ApiResponse;
import com.hospital.backend.service.specialty.ISpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/specialties")
public class SpecialtyController {
    private final ISpecialtyService specialtyService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllSpecialties() {
        List<SpecialtyDto> results = specialtyService.getAllSpecialties();
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addSpecialty(@RequestBody SpecialtyDto specialtyDto) {
        SpecialtyDto result = specialtyService.addSpecialty(specialtyDto);
        return ResponseEntity.ok(new ApiResponse("Saved", result));
    }

    @PostMapping("/add-multiple")
    public ResponseEntity<ApiResponse> addSpecialties(@RequestBody List<SpecialtyDto> specialtyDtos) {
        List<SpecialtyDto> results = specialtyService.addSpecialties(specialtyDtos);
        return ResponseEntity.ok(new ApiResponse("Saved", results));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteSpecialty(@RequestParam String name) {
        SpecialtyDto result = specialtyService.deleteSpecialty(name);
        return ResponseEntity.ok(new ApiResponse("Deleted", result));
    }
}