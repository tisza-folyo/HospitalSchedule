package com.hospital.backend.controller;

import com.hospital.backend.dto.SpecialtyDto;
import com.hospital.backend.exception.AlreadyExistsException;
import com.hospital.backend.response.ApiResponse;
import com.hospital.backend.service.specialty.ISpecialtyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/specialties")
public class SpecialtyController {
    private final ISpecialtyService specialtyService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addSpecialty(@Valid @RequestBody SpecialtyDto specialtyDto){
        try {
            SpecialtyDto result = specialtyService.addSpecialty(specialtyDto);
            return ResponseEntity.ok(new ApiResponse("Saved", result));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null ));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
