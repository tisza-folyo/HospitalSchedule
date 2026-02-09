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
    public ResponseEntity<ApiResponse> addBed(@RequestParam long roomId, @RequestParam int bedNumber){
        try {
            BedDto bedDto = bedService.addBed(roomId, bedNumber);
            return ResponseEntity.ok(new ApiResponse("Saved", bedDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (AlreadyExistsException e){
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteBed(@RequestParam long roomId, @RequestParam int bedNumber){
        try {
            bedService.deleteBed(roomId, bedNumber);
            return ResponseEntity.ok(new ApiResponse("Deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete-by-id")
    public ResponseEntity<ApiResponse> deleteBedById(@RequestParam long bedId){
        try {
            bedService.deleteBed(bedId);
            return ResponseEntity.ok(new ApiResponse("Deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
