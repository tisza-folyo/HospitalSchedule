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
    public ResponseEntity<ApiResponse> getAllWorks() {
        List<DoctorAssistantWorkDto> results = doctorAssistantWorkService.getAllWorks();
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @GetMapping("/doctors/all")
    public ResponseEntity<ApiResponse> getDoctorWorks(@RequestParam String dTaj) {
        List<DoctorAssistantWorkDto> results = doctorAssistantWorkService.getDoctorWorks(dTaj);
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @GetMapping("/assistants/all")
    public ResponseEntity<ApiResponse> getAssistantWork(@RequestParam String aTaj) {
        List<DoctorAssistantWorkDto> results = doctorAssistantWorkService.getAssistantWorks(aTaj);
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @GetMapping("/all/after")
    public ResponseEntity<ApiResponse> getAllWorksAfterDay(@RequestParam LocalDate day) {
        List<DoctorAssistantWorkDto> results = doctorAssistantWorkService.getAllWorksAfterDay(day);
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @GetMapping("/all/between")
    public ResponseEntity<ApiResponse> getAllAssistantWorksBetweenDays(@RequestParam String aTaj,@RequestParam LocalDate dayAfter, @RequestParam LocalDate dayBefore) {
        List<DoctorAssistantWorkDto> results = doctorAssistantWorkService.getAllWorksByAssistantAndDate(aTaj,dayAfter,dayBefore);
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @GetMapping("/doctor/day")
    public ResponseEntity<ApiResponse> getWorkByDayAndDoctor(@RequestParam String dTaj, @RequestParam LocalDate day) {
        try {
            DoctorAssistantWorkDto result = doctorAssistantWorkService.getWorkByDateAndDTaj(day, dTaj);
            return ResponseEntity.ok(new ApiResponse("Success", result));
        } catch (ResourceNotFoundException e) {
            if (e.getMessage().contains("Work")) {
                return ResponseEntity.ok(new ApiResponse("Free work space", null));
            }
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/assistants/free")
    public ResponseEntity<ApiResponse> getFreeAssistants(@RequestParam LocalDate day) {
        List<AssistantDto> results = doctorAssistantWorkService.getFreeAssistants(day);
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @GetMapping("/doctors/free")
    public ResponseEntity<ApiResponse> getFreeDoctors(@RequestParam LocalDate day) {
        List<DoctorDto> results = doctorAssistantWorkService.getFreeDoctors(day);
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @PostMapping("/add-with-assistant")
    public ResponseEntity<ApiResponse> addWorkByDto(@RequestBody DoctorAssistantWorkDto doctorAssistantWorkDto) {
        DoctorAssistantWorkDto result = doctorAssistantWorkService.addWork(doctorAssistantWorkDto);
        return ResponseEntity.ok(new ApiResponse("Saved", result));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addWork(@RequestParam LocalDate day, @RequestParam String dTaj, @RequestParam String uTaj) {
        DoctorAssistantWorkDto result = doctorAssistantWorkService.addWork(day, dTaj, uTaj);
        return ResponseEntity.ok(new ApiResponse("Saved", result));
    }

    @PutMapping("/assign")
    public ResponseEntity<ApiResponse> assignAssistant(@RequestBody UpdateAssistantWorkRequest request) {
        DoctorAssistantWorkDto result = doctorAssistantWorkService.assignAssistant(request);
        return ResponseEntity.ok(new ApiResponse("Saved", result));
    }

    @PutMapping("/assign-first")
    public ResponseEntity<ApiResponse> assign(@RequestParam String aTaj, @RequestParam String uTaj, @RequestBody LocalDate day) {
        DoctorAssistantWorkDto result = doctorAssistantWorkService.assignAssistant(aTaj, uTaj, day);
        return ResponseEntity.ok(new ApiResponse("Saved", result));
    }

    @PutMapping("/replace")
    public ResponseEntity<ApiResponse> changeAssistantByAssistant(@RequestBody UpdateAssistantWorkRequest request) {
        DoctorAssistantWorkDto result = doctorAssistantWorkService.changeAssistantByAssistant(request);
        return ResponseEntity.ok(new ApiResponse("Saved", result));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteWork(@RequestParam Long workId) {
        doctorAssistantWorkService.deleteWork(workId);
        return ResponseEntity.ok(new ApiResponse("Deleted", null));
    }
}
