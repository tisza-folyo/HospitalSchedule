package com.hospital.backend.controller;

import com.hospital.backend.dto.NursePatientCareDto;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.request.AddCareRequest;
import com.hospital.backend.service.nursePatientCare.INursePatientCareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hospital.backend.response.ApiResponse;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cares")
public class NursePatientCareController {

    private final INursePatientCareService nursePatientCareService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCares() {
        List<NursePatientCareDto> results = nursePatientCareService.getAllNursePatientCares();
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @GetMapping("/all-without-care")
    public ResponseEntity<ApiResponse> getAllWithoutCare() {
        List<NursePatientCareDto> results = nursePatientCareService.getAllNonCares();
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @GetMapping("/all-for-care")
    public ResponseEntity<ApiResponse> getAllCaresForCare() {
        List<NursePatientCareDto> results = nursePatientCareService.getAllCaresForCare();
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @GetMapping("/all-by-nurse")
    public ResponseEntity<ApiResponse> getAllCaresByNurse(@RequestParam String nTaj) {
        List<NursePatientCareDto> results = nursePatientCareService.getAllCaresByNurse(nTaj);
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @GetMapping("/all-by-patient")
    public ResponseEntity<ApiResponse> getAllCaresByPatient(@RequestParam String pTaj) {
        List<NursePatientCareDto> results = nursePatientCareService.getAllCaresByPatient(pTaj);
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse> getActiveCareByPatient(@RequestParam String pTaj) {
        try {
            NursePatientCareDto result = nursePatientCareService.getActiveCareByPatient(pTaj);
            return ResponseEntity.ok(new ApiResponse("Success", result));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse("No active care", null));
        }
    }

    @GetMapping("/actives")
    public ResponseEntity<ApiResponse> getAllActivesByPatient(@RequestParam String nTaj){
        List<NursePatientCareDto> results = nursePatientCareService.getAllActivesByNurse(nTaj);
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @GetMapping("/non-actives")
    public ResponseEntity<ApiResponse> getAllNonActiveByNurse(@RequestParam String nTaj){
        List<NursePatientCareDto> results = nursePatientCareService.getAllNonActivesByNurse(nTaj);
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCare(@RequestBody AddCareRequest request) {
        NursePatientCareDto result = nursePatientCareService.addCare(request);
        return ResponseEntity.ok(new ApiResponse("Success", result));
    }

    @PostMapping("/assign")
    public ResponseEntity<ApiResponse> assignNurseToPatient(@RequestParam String nTaj, @RequestParam String pTaj, @RequestParam String uTaj) {
        NursePatientCareDto result = nursePatientCareService.assignNurseToPatient(nTaj, pTaj, uTaj);
        return ResponseEntity.ok(new ApiResponse("Success", result));
    }

    @PostMapping("/assign-first")
    public ResponseEntity<ApiResponse> assignNurseToPatient(@RequestParam String nTaj, @RequestParam String uTaj) {
        NursePatientCareDto result = nursePatientCareService.assignNurseToPatient(nTaj, uTaj);
        return ResponseEntity.ok(new ApiResponse("Success", result));
    }

    @PutMapping("/exit")
    public ResponseEntity<ApiResponse> exitPatient(@RequestParam String pTaj, @RequestParam LocalDate exitDay, @RequestParam String uTaj) {
        NursePatientCareDto result = nursePatientCareService.exitPatientCare(pTaj, exitDay, uTaj);
        return ResponseEntity.ok(new ApiResponse("Success", result));
    }


    @PutMapping("/change")
    public ResponseEntity<ApiResponse> changeNurse(@RequestParam String nTaj, @RequestParam String pTaj, @RequestParam String uTaj) {
        NursePatientCareDto result = nursePatientCareService.changeNurse(nTaj, pTaj, uTaj);
        return ResponseEntity.ok(new ApiResponse("Success", result));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteCare(@RequestParam Long id) {
        nursePatientCareService.deleteCare(id);
        return ResponseEntity.ok(new ApiResponse("Deleted", null));
    }
}
