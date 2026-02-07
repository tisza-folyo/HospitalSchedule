package com.hospital.backend.controller;

import com.hospital.backend.dto.PatientDto;
import com.hospital.backend.exception.AlreadyExistsException;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.request.RegisterRequest;
import com.hospital.backend.response.ApiResponse;
import com.hospital.backend.service.person.IPersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/people")
public class PersonController {
    private final IPersonService personService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> addPerson(@RequestBody RegisterRequest registPatientRequest){
        try {
            Object dto = personService.addPerson(registPatientRequest);
            return ResponseEntity.ok(new ApiResponse("Saved", dto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{pTaj}/person")
    public ResponseEntity<ApiResponse> deletePatient(@PathVariable String pTaj, @RequestParam String role){
        try {
            personService.deletePerson(pTaj,role);
            return ResponseEntity.ok(new ApiResponse("Deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
