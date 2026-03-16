package com.hospital.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.backend.dto.AppointmentDto;
import com.hospital.backend.request.AppointmentRequest;
import com.hospital.backend.service.appointment.IAppointmentService;
import com.hospital.backend.security.config.HospitalConfig;
import com.hospital.backend.security.jwt.JwtAuthEntryPoint;
import com.hospital.backend.security.jwt.JwtUtils;
import com.hospital.backend.security.person.HospitalPersonDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(AppointmentController.class)
@Import(HospitalConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IAppointmentService appointmentService;

    @MockitoBean
    private HospitalPersonDetailsService personDetailsService;
    @MockitoBean
    private JwtAuthEntryPoint jwtAuthEntryPoint;
    @MockitoBean
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    private AppointmentDto appointmentDto;

    @BeforeEach
    void setUp() {
        appointmentDto = new AppointmentDto();
        appointmentDto.setAppointmentId(1L);
        appointmentDto.setDay(LocalDate.now().plusDays(1));
        appointmentDto.setTimeSlot(LocalTime.of(10, 0));
        appointmentDto.setSymptomDescription("Fejfájás");
    }

    @Test
    void getAllAppointmentsByDoctor_ShouldReturnList() throws Exception {
        when(appointmentService.getAllAppointmentsByDoctor("123456789")).thenReturn(Collections.singletonList(appointmentDto));

        mockMvc.perform(get("/hospital/appointments/all-by-doctor/123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data[0].appointmentId").value(1));
    }

    @Test
    void getAllAppointmentsByDay_ShouldReturnList() throws Exception {
        LocalDate day = LocalDate.now();
        when(appointmentService.getAllAppointmentByDay(any(LocalDate.class))).thenReturn(Collections.singletonList(appointmentDto));

        mockMvc.perform(get("/hospital/appointments/all-by-day")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(day)))
                .andExpect(status().isOk());
    }

    @Test
    void getBySection_ShouldReturnList() throws Exception {
        when(appointmentService.getAllAndFreeAppointments(any(LocalDate.class), eq("Kardiológia")))
                .thenReturn(Collections.singletonList(appointmentDto));

        mockMvc.perform(get("/hospital/appointments/section/Kardiológia")
                        .param("day", LocalDate.now().toString()))
                .andExpect(status().isOk());
    }

    @Test
    void addAppointment_WithRequest_ShouldReturnSaved() throws Exception {
        AppointmentRequest request = new AppointmentRequest();
        request.setDoctorTaj("111");
        request.setPatientTaj("222");
        request.setDay(LocalDate.now());
        request.setTimeSlot(LocalTime.of(14, 30));

        when(appointmentService.addAppointment(any(AppointmentRequest.class))).thenReturn(appointmentDto);

        mockMvc.perform(post("/hospital/appointments/add-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.symptomDescription").value("Fejfájás"));
    }

    @Test
    void cancelAppointment_ShouldReturnDeleted() throws Exception {
        mockMvc.perform(delete("/hospital/appointments/cancel")
                        .param("appointmentId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Deleted"));
    }

    @Test
    void updateAppointmentStatus_ShouldReturnUpdated() throws Exception {
        when(appointmentService.updateAppointmentStatus(eq(1L), anyString())).thenReturn(appointmentDto);

        mockMvc.perform(put("/hospital/appointments/update-status")
                        .param("appointmentId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"COMPLETED\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Updated"));
    }
}