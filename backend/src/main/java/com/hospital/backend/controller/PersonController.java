package com.hospital.backend.controller;

import com.hospital.backend.dto.PatientDto;
import com.hospital.backend.request.RegistPatientRequest;
import com.hospital.backend.response.ApiResponse;
import com.hospital.backend.service.person.IPersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/people")
public class PersonController {
    private final IPersonService personService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> addPatient(@RequestBody RegistPatientRequest registPatientRequest){
        try {
            PatientDto patientDto = personService.addPatient(registPatientRequest);
            return ResponseEntity.ok(new ApiResponse("Saved", patientDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
