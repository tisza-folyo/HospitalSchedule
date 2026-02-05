package com.hospital.backend.controller;

import com.hospital.backend.dto.SpecialtyDto;
import com.hospital.backend.exception.AlreadyExistsException;
import com.hospital.backend.exception.CollisionException;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.response.ApiResponse;
import com.hospital.backend.service.specialty.ISpecialtyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/specialties")
public class SpecialtyController {
    private final ISpecialtyService specialtyService;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllSpecialties(){
        try {
            List<SpecialtyDto> specialties = specialtyService.getAllSpecialties();
            return ResponseEntity.ok(new ApiResponse("Success", specialties));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addSpecialty(@RequestBody SpecialtyDto specialtyDto){
        try {
            SpecialtyDto result = specialtyService.addSpecialty(specialtyDto);
            return ResponseEntity.ok(new ApiResponse("Saved", result));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null ));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add-multiple")
    public ResponseEntity<ApiResponse> addSpecialties(@RequestBody List<SpecialtyDto> specialtyDtos){
        try {
            List<SpecialtyDto> results = specialtyService.addSpecialties(specialtyDtos);
            return ResponseEntity.ok(new ApiResponse("Saved", results));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null ));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteSpecialty(@RequestParam String name){
        try {
            SpecialtyDto result = specialtyService.deleteSpecialty(name);
            return ResponseEntity.ok(new ApiResponse("Deleted", result));
        } catch (CollisionException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null ));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null ));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
