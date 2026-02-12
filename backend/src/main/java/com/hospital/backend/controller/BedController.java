package com.hospital.backend.controller;

import com.hospital.backend.dto.BedDto;
import com.hospital.backend.exception.AlreadyExistsException;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.response.ApiResponse;
import com.hospital.backend.service.bed.IBedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/beds")
public class BedController {
    private final IBedService bedService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addBed(@RequestParam long roomId, @RequestParam int bedNumber) {
        BedDto result = bedService.addBed(roomId, bedNumber);
        return ResponseEntity.ok(new ApiResponse("Saved", result));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteBed(@RequestParam long roomId, @RequestParam int bedNumber) {
        bedService.deleteBed(roomId, bedNumber);
        return ResponseEntity.ok(new ApiResponse("Deleted", null));
    }

    @DeleteMapping("/delete-by-id")
    public ResponseEntity<ApiResponse> deleteBedById(@RequestParam long bedId) {
        bedService.deleteBed(bedId);
        return ResponseEntity.ok(new ApiResponse("Deleted", null));
    }
}