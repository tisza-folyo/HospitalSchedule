package com.hospital.backend.controller;

import com.hospital.backend.dto.AssistantDto;
import com.hospital.backend.dto.DoctorAssistantWorkDto;
import com.hospital.backend.dto.DoctorDto;
import com.hospital.backend.exception.AlreadyExistsException;
import com.hospital.backend.exception.CollisionException;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.request.UpdateAssistantWorkRequest;
import com.hospital.backend.service.doctorAssistantWork.IDoctorAssistantWorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hospital.backend.response.ApiResponse;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/works")
public class DoctorAssistantWorkController {
    private final IDoctorAssistantWorkService doctorAssistantWorkService;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllWorks(){
        try {
            List<DoctorAssistantWorkDto> results = doctorAssistantWorkService.getAllWorks();
            return ResponseEntity.ok(new ApiResponse("Success", results));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/doctor/all")
    public ResponseEntity<ApiResponse> getDoctorWorks(@RequestParam String dTaj){
        try {
            List<DoctorAssistantWorkDto> results = doctorAssistantWorkService.getDoctorWorks(dTaj);
            return ResponseEntity.ok(new ApiResponse("Success", results));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/assistant/all")
    public ResponseEntity<ApiResponse> getAssistantWork(@RequestParam String aTaj){
        try {
            List<DoctorAssistantWorkDto> results = doctorAssistantWorkService.getAssistantWorks(aTaj);
            return ResponseEntity.ok(new ApiResponse("Success", results));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/assistant/free")
    public ResponseEntity<ApiResponse> getFreeAssistants(@RequestParam LocalDate day){
        try {
            List<AssistantDto> results = doctorAssistantWorkService.getFreeAssistants(day);
            return ResponseEntity.ok(new ApiResponse("Success", results));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/doctor/free")
    public ResponseEntity<ApiResponse> getFreeDoctors(@RequestParam LocalDate day){
        try {
            List<DoctorDto> results = doctorAssistantWorkService.getFreeDoctors(day);
            return ResponseEntity.ok(new ApiResponse("Success", results));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add-with-assistant")
    public ResponseEntity<ApiResponse> addWorkByDto(@RequestBody DoctorAssistantWorkDto doctorAssistantWorkDto){
        try {
            DoctorAssistantWorkDto result = doctorAssistantWorkService.addWork(doctorAssistantWorkDto);
            return ResponseEntity.ok(new ApiResponse("Saved", result));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (AlreadyExistsException e){
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null ));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addWork(@RequestParam LocalDate day, @RequestParam String dTaj, @RequestParam String uTaj){
        try {
            DoctorAssistantWorkDto result = doctorAssistantWorkService.addWork(day, dTaj, uTaj);
            return ResponseEntity.ok(new ApiResponse("Saved", result));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/assign")
    public ResponseEntity<ApiResponse> assignAssistant(@RequestBody UpdateAssistantWorkRequest request){
        try {
            DoctorAssistantWorkDto result = doctorAssistantWorkService.assignAssistant(request);
            return ResponseEntity.ok(new ApiResponse("Saved", result));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (CollisionException e){
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null ));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/replace")
    public ResponseEntity<ApiResponse> changeAssistantByAssistant(@RequestBody UpdateAssistantWorkRequest request){
        try {
            DoctorAssistantWorkDto result = doctorAssistantWorkService.changeAssistantByAssistant(request);
            return ResponseEntity.ok(new ApiResponse("Saved", result));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (CollisionException e){
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null ));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteWork(@RequestParam Long workId){

            doctorAssistantWorkService.deleteWork(workId);
            return ResponseEntity.ok(new ApiResponse("Deleted", null));

    }
}
