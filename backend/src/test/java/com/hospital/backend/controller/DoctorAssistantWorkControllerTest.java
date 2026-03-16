package com.hospital.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.backend.dto.AssistantDto;
import com.hospital.backend.dto.DoctorAssistantWorkDto;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.security.config.HospitalConfig;
import com.hospital.backend.security.jwt.JwtAuthEntryPoint;
import com.hospital.backend.security.jwt.JwtUtils;
import com.hospital.backend.security.person.HospitalPersonDetailsService;
import com.hospital.backend.service.doctorAssistantWork.IDoctorAssistantWorkService;
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
@WebMvcTest(DoctorAssistantWorkController.class)
@Import(HospitalConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class DoctorAssistantWorkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IDoctorAssistantWorkService workService;

    @MockitoBean
    private HospitalPersonDetailsService personDetailsService;
    @MockitoBean
    private JwtAuthEntryPoint jwtAuthEntryPoint;
    @MockitoBean
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    private DoctorAssistantWorkDto workDto;

    @BeforeEach
    void setUp() {
        workDto = new DoctorAssistantWorkDto();
        workDto.setWorkId(1L);
        workDto.setWorkDay(LocalDate.now());
        workDto.setUTaj("123456789");
    }

    @Test
    void getDoctorWorks_ShouldReturnList() throws Exception {
        when(workService.getDoctorWorks("D123")).thenReturn(Collections.singletonList(workDto));

        mockMvc.perform(get("/hospital/works/doctors/all")
                        .param("dTaj", "D123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data[0].uTaj").value("123456789"));
    }

    @Test
    void getAllByDate_ShouldReturnList() throws Exception {
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(7);
        when(workService.getAllWorksByDate(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(workDto));

        mockMvc.perform(get("/hospital/works/all-in-interval")
                        .param("dayAfter", start.toString())
                        .param("dayBefore", end.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void getWorkByDayAndDoctor_ShouldReturnFreeWorkSpace_WhenNotFound() throws Exception {
        when(workService.getWorkByDateAndDTaj(any(LocalDate.class), anyString()))
                .thenThrow(new ResourceNotFoundException("Work not found"));

        mockMvc.perform(get("/hospital/works/doctor/day")
                        .param("dTaj", "D123")
                        .param("day", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Free work space"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void getFreeAssistants_ShouldReturnList() throws Exception {
        AssistantDto assistant = new AssistantDto();
        when(workService.getFreeAssistants(any(LocalDate.class))).thenReturn(Arrays.asList(assistant));

        mockMvc.perform(get("/hospital/works/assistants/free")
                        .param("day", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void addWork_ByParams_ShouldReturnSaved() throws Exception {
        when(workService.addWork(any(LocalDate.class), anyString(), anyString())).thenReturn(workDto);

        mockMvc.perform(post("/hospital/works/add")
                        .param("day", LocalDate.now().toString())
                        .param("dTaj", "D123")
                        .param("uTaj", "U999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Saved"));
    }

    @Test
    void assignFirst_ShouldReturnSaved() throws Exception {
        when(workService.assignAssistant(anyString(), anyString(), any(LocalDate.class))).thenReturn(workDto);

        mockMvc.perform(put("/hospital/works/assign-first")
                        .param("aTaj", "A777")
                        .param("uTaj", "U999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(LocalDate.now())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Saved"));
    }

    @Test
    void deleteWork_ShouldReturnDeleted() throws Exception {
        mockMvc.perform(delete("/hospital/works/delete")
                        .param("workId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Deleted"));
    }
}