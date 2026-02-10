package com.hospital.backend.controller;

import com.hospital.backend.dto.AppointmentDto;
import com.hospital.backend.exception.CollisionException;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.model.Status;
import com.hospital.backend.request.AppointmentRequest;
import com.hospital.backend.service.appointment.IAppointmentService;
import lombok.RequiredArgsConstructor;
import com.hospital.backend.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;

import static org.springframework.http.HttpStatus.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/appointments")
public class AppointmentController {
    private final IAppointmentService appointmentService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllAppointments(){
        try {
            List<AppointmentDto> results = appointmentService.getAllAppointments();
            return ResponseEntity.ok(new ApiResponse("Success", results));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/all-by-doctor/{dTaj}")
    public ResponseEntity<ApiResponse> getAllAppointmentsByDoctor(@PathVariable String dTaj){
        try {
            List<AppointmentDto> results = appointmentService.getAllAppointmentsByDoctor(dTaj);
            return ResponseEntity.ok(new ApiResponse("Success", results));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/all-by-day")
    public ResponseEntity<ApiResponse> getAllAppointmentsByDay(@RequestBody LocalDate day){
        try {
            List<AppointmentDto> results = appointmentService.getAllAppointmentByDay(day);
            return ResponseEntity.ok(new ApiResponse("Success", results));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/all-by-patient/{pTaj}")
    public ResponseEntity<ApiResponse> getAllAppointmentsByPatient(@PathVariable String pTaj){
        try {
            List<AppointmentDto> results = appointmentService.getAllAppointmentsByPatient(pTaj);
            return ResponseEntity.ok(new ApiResponse("Success", results));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/doctor/{dTaj}")
    public ResponseEntity<ApiResponse> getByDoctor(@PathVariable String dTaj, @RequestParam LocalDate day) {
        return processGetRequest(() -> appointmentService.getAllAndFreeAppointments(dTaj, day));
    }

    @GetMapping("/daily")
    public ResponseEntity<ApiResponse> getByDay(@RequestParam LocalDate day) {
        return processGetRequest(() -> appointmentService.getAllAndFreeAppointments(day));
    }

    @GetMapping("/section/{section}")
    public ResponseEntity<ApiResponse> getBySection(@PathVariable String section, @RequestParam LocalDate day) {
        return processGetRequest(() -> appointmentService.getAllAndFreeAppointments(day, section));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addAppointmentWithDto(@RequestBody AppointmentDto appointmentDto) {
        return processPostRequest(() -> appointmentService.addAppointment(appointmentDto));
    }

    @PostMapping("/add-request")
    public ResponseEntity<ApiResponse> addAppointment(@RequestBody AppointmentRequest request){
        return processPostRequest(() -> appointmentService.addAppointment(request));
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<ApiResponse> cancelAppointment(@RequestParam long appointmentId){
        try {
            appointmentService.deleteAppointment(appointmentId);
            return ResponseEntity.ok(new ApiResponse("Deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update-status")
    public ResponseEntity<ApiResponse> updateAppointmentStatus(@RequestBody Status status, @RequestParam long appointmentId){
        try {
            AppointmentDto result = appointmentService.updateAppointmentStatus(appointmentId, status);
            return ResponseEntity.ok(new ApiResponse("Updated", result));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update-description")
    public ResponseEntity<ApiResponse> updateAppointmentDescription(@RequestBody String description, @RequestParam long id){
        try {
            AppointmentDto result = appointmentService.updateAppointmentSymptomDescription(id,description);
            return ResponseEntity.ok(new ApiResponse("Updated", result));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }


    private ResponseEntity<ApiResponse> processGetRequest(Supplier<List<AppointmentDto>> action) {
        try {
            return ResponseEntity.ok(new ApiResponse("Success", action.get()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Internal error", null));
        }
    }

    private ResponseEntity<ApiResponse> processPostRequest(Supplier<AppointmentDto> action) {
        try {
            return ResponseEntity.ok(new ApiResponse("Success", action.get()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (CollisionException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Internal error", null));
        }
    }

}
