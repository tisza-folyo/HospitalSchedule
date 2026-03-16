package com.hospital.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.backend.dto.NursePatientCareDto;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.request.AddCareRequest;

import com.hospital.backend.security.config.HospitalConfig;
import com.hospital.backend.security.jwt.JwtAuthEntryPoint;
import com.hospital.backend.security.jwt.JwtUtils;
import com.hospital.backend.security.person.HospitalPersonDetailsService;

import com.hospital.backend.service.nursePatientCare.INursePatientCareService;
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
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(NursePatientCareController.class)
@Import(HospitalConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class NursePatientCareControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private INursePatientCareService careService;

    @MockitoBean
    private HospitalPersonDetailsService personDetailsService;
    @MockitoBean
    private JwtAuthEntryPoint jwtAuthEntryPoint;
    @MockitoBean
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    private NursePatientCareDto careDto;

    @BeforeEach
    void setUp() {
        careDto = new NursePatientCareDto();
        careDto.setUTaj("123456789");
        careDto.setEntryDay(LocalDate.now());
    }

    @Test
    void getAllCares_ShouldReturnList() throws Exception {
        when(careService.getAllNursePatientCares()).thenReturn(Collections.singletonList(careDto));

        mockMvc.perform(get("/hospital/cares/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data[0].uTaj").value("123456789"));
    }

    @Test
    void getActiveCareByPatient_ShouldReturnNoActiveCare_WhenNotFound() throws Exception {
        when(careService.getActiveCareByPatient("P123"))
                .thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/hospital/cares/active")
                        .param("pTaj", "P123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("No active care"));
    }

    @Test
    void addCare_ShouldReturnSaved() throws Exception {
        AddCareRequest request = new AddCareRequest();
        request.setPTaj("P123");
        request.setNTaj("N123");
        request.setUTaj("U123");
        request.setEntryDay(LocalDate.now());

        when(careService.addCare(any(AddCareRequest.class))).thenReturn(careDto);

        mockMvc.perform(post("/hospital/cares/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void assignNurseToPatient_ShouldReturnSuccess() throws Exception {
        when(careService.assignNurseToPatient(anyString(), anyString(), anyString())).thenReturn(careDto);

        mockMvc.perform(post("/hospital/cares/assign")
                        .param("nTaj", "N111")
                        .param("pTaj", "P222")
                        .param("uTaj", "U333"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void exitPatient_ShouldReturnSuccess() throws Exception {
        LocalDate exitDay = LocalDate.now().plusDays(1);
        when(careService.exitPatientCare(anyString(), any(LocalDate.class), anyString())).thenReturn(careDto);

        mockMvc.perform(put("/hospital/cares/exit")
                        .param("pTaj", "P222")
                        .param("exitDay", exitDay.toString())
                        .param("uTaj", "U333"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void deleteCare_ShouldReturnDeleted() throws Exception {
        mockMvc.perform(delete("/hospital/cares/delete")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Deleted"));
    }
}