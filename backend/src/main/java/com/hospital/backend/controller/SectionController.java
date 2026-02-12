package com.hospital.backend.controller;

import com.hospital.backend.dto.SectionDto;
import com.hospital.backend.exception.AlreadyExistsException;
import com.hospital.backend.exception.CollisionException;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.response.ApiResponse;
import com.hospital.backend.service.section.ISectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/sections")
public class SectionController {
    private final ISectionService sectionService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllSections() {
        List<SectionDto> results = sectionService.getAllSections();
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addSection(@RequestParam String name) {
        SectionDto result = sectionService.addSection(name);
        return ResponseEntity.ok(new ApiResponse("Saved", result));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteSection(@RequestParam String name) {
        sectionService.deleteSection(name);
        return ResponseEntity.ok(new ApiResponse("Deleted", null));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateSection(@RequestParam String name, @RequestParam String newName) {
        SectionDto result = sectionService.updateSection(name, newName);
        return ResponseEntity.ok(new ApiResponse("Updated", result));
    }
}
