package com.hospital.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.backend.dto.SpecialtyDto;
import com.hospital.backend.security.config.HospitalConfig;
import com.hospital.backend.security.jwt.JwtAuthEntryPoint;
import com.hospital.backend.security.jwt.JwtUtils;
import com.hospital.backend.security.person.HospitalPersonDetailsService;
import com.hospital.backend.service.specialty.ISpecialtyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SpecialtyController.class)
@ActiveProfiles("test")
@Import(HospitalConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class SpecialtyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ISpecialtyService specialtyService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private HospitalPersonDetailsService personDetailsService;

    @MockitoBean
    private JwtAuthEntryPoint jwtAuthEntryPoint;

    @MockitoBean
    private JwtUtils jwtUtils;

    private SpecialtyDto s1;
    private SpecialtyDto s2;

    @BeforeEach
    void setUp() {
        s1 = new SpecialtyDto("Kardiológia", 30);
        s2 = new SpecialtyDto("Bőrgyógyászat", 20);
    }

    @Test
    void getAllSpecialties_ShouldReturnSuccess() throws Exception {
        List<SpecialtyDto> specialties = Arrays.asList(s1, s2);
        when(specialtyService.getAllSpecialties()).thenReturn(specialties);

        mockMvc.perform(get("/hospital/specialties/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].specialtyName").value("Kardiológia"));
    }

    @Test
    void addSpecialty_ShouldReturnSaved() throws Exception {
        when(specialtyService.addSpecialty(any(SpecialtyDto.class))).thenReturn(s1);

        mockMvc.perform(post("/hospital/specialties/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(s1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Saved"))
                .andExpect(jsonPath("$.data.specialtyName").value("Kardiológia"));
    }

    @Test
    void addMultipleSpecialties_ShouldReturnSaved() throws Exception {
        List<SpecialtyDto> dtoList = Arrays.asList(s1, s2);
        when(specialtyService.addSpecialties(anyList())).thenReturn(dtoList);

        mockMvc.perform(post("/hospital/specialties/add-multiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Saved"))
                .andExpect(jsonPath("$.data[1].specialtyName").value("Bőrgyógyászat"));
    }

    @Test
    void deleteSpecialty_ShouldReturnDeleted() throws Exception {
        when(specialtyService.deleteSpecialty("Kardiológia")).thenReturn(s1);

        mockMvc.perform(delete("/hospital/specialties/delete")
                        .param("name", "Kardiológia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Deleted"))
                .andExpect(jsonPath("$.data.specialtyName").value("Kardiológia"));
    }
}