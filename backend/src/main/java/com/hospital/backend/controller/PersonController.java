package com.hospital.backend.controller;

import com.hospital.backend.dto.DoctorDto;
import com.hospital.backend.dto.PatientDto;
import com.hospital.backend.model.Person;
import com.hospital.backend.request.RegisterRequest;
import com.hospital.backend.response.ApiResponse;
import com.hospital.backend.service.person.IPersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/people")
public class PersonController {
    private final IPersonService personService;

    @GetMapping("/{pTaj}/person")
    public ResponseEntity<ApiResponse> getPersonDetails(@PathVariable String pTaj, @RequestParam String role) {
        Person result = personService.getPersonByTajAndRole(pTaj, role);
        return ResponseEntity.ok(new ApiResponse("Success", result));
    }

    @GetMapping("/doctors/all")
    public ResponseEntity<ApiResponse> getAllDoctor(){
        List<DoctorDto> results = personService.getAllDoctor();
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @GetMapping("/patient/{pTaj}")
    public ResponseEntity<ApiResponse> getPatientDetails(@PathVariable String pTaj) {
        PatientDto result = personService.getPatientByTaj(pTaj);
        return ResponseEntity.ok(new ApiResponse("Success", result));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> addPerson(@RequestBody RegisterRequest registerPatientRequest) {
        Object result = personService.addPerson(registerPatientRequest);
        return ResponseEntity.ok(new ApiResponse("Saved", result));
    }

    @DeleteMapping("/delete/{pTaj}/person")
    public ResponseEntity<ApiResponse> deletePatient(@PathVariable String pTaj, @RequestParam String role) {
        personService.deletePerson(pTaj, role);
        return ResponseEntity.ok(new ApiResponse("Deleted", null));
    }

    @PutMapping("/set-section/doctor")
    public ResponseEntity<ApiResponse> setDoctorSection(@RequestParam String taj, @RequestParam String role, @RequestParam String section) {
        DoctorDto result = personService.setSection(taj, role, section);
        return ResponseEntity.ok(new ApiResponse("Updated", result));
    }
}
