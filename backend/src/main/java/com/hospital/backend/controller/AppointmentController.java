package com.hospital.backend.controller;

import com.hospital.backend.dto.AppointmentDto;
import com.hospital.backend.model.Status;
import com.hospital.backend.request.AppointmentRequest;
import com.hospital.backend.service.appointment.IAppointmentService;
import lombok.RequiredArgsConstructor;
import com.hospital.backend.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/appointments")
public class AppointmentController {
    private final IAppointmentService appointmentService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllAppointments() {
        List<AppointmentDto> results = appointmentService.getAllAppointments();
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @GetMapping("/all-by-doctor/{dTaj}")
    public ResponseEntity<ApiResponse> getAllAppointmentsByDoctor(@PathVariable String dTaj) {
        List<AppointmentDto> results = appointmentService.getAllAppointmentsByDoctor(dTaj);
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @GetMapping("/all-by-day")
    public ResponseEntity<ApiResponse> getAllAppointmentsByDay(@RequestBody LocalDate day) {
        List<AppointmentDto> results = appointmentService.getAllAppointmentByDay(day);
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @GetMapping("/all-by-patient/{pTaj}")
    public ResponseEntity<ApiResponse> getAllAppointmentsByPatient(@PathVariable String pTaj) {
        List<AppointmentDto> results = appointmentService.getAllAppointmentsByPatient(pTaj);
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @GetMapping("/doctor/{dTaj}")
    public ResponseEntity<ApiResponse> getByDoctor(@PathVariable String dTaj, @RequestParam LocalDate day) {
        List<AppointmentDto> results = appointmentService.getAllAndFreeAppointments(dTaj, day);
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @GetMapping("/daily")
    public ResponseEntity<ApiResponse> getByDay(@RequestParam LocalDate day) {
        List<AppointmentDto> results = appointmentService.getAllAndFreeAppointments(day);
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @GetMapping("/section/{section}")
    public ResponseEntity<ApiResponse> getBySection(@PathVariable String section, @RequestParam LocalDate day) {
        List<AppointmentDto> results = appointmentService.getAllAndFreeAppointments(day, section);
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addAppointmentWithDto(@RequestBody AppointmentDto appointmentDto) {
        AppointmentDto result = appointmentService.addAppointment(appointmentDto);
        return ResponseEntity.ok(new ApiResponse("Success", result));
    }

    @PostMapping("/add-request")
    public ResponseEntity<ApiResponse> addAppointment(@RequestBody AppointmentRequest request) {
        AppointmentDto result = appointmentService.addAppointment(request);
        return ResponseEntity.ok(new ApiResponse("Success", result));
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<ApiResponse> cancelAppointment(@RequestParam long appointmentId) {
        appointmentService.deleteAppointment(appointmentId);
        return ResponseEntity.ok(new ApiResponse("Deleted", null));
    }

    @PutMapping("/update-status")
    public ResponseEntity<ApiResponse> updateAppointmentStatus(@RequestBody Status status, @RequestParam long appointmentId) {
        AppointmentDto result = appointmentService.updateAppointmentStatus(appointmentId, status);
        return ResponseEntity.ok(new ApiResponse("Updated", result));
    }

    @PutMapping("/update-description")
    public ResponseEntity<ApiResponse> updateAppointmentDescription(@RequestBody String description, @RequestParam long id) {
        AppointmentDto result = appointmentService.updateAppointmentSymptomDescription(id, description);
        return ResponseEntity.ok(new ApiResponse("Updated", result));
    }
}