package com.hospital.backend.controller;

import com.hospital.backend.dto.BedDto;
import com.hospital.backend.service.bed.IBedService;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(BedController.class)
@Import(HospitalConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class BedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IBedService bedService;

    @MockitoBean
    private HospitalPersonDetailsService personDetailsService;
    @MockitoBean
    private JwtAuthEntryPoint jwtAuthEntryPoint;
    @MockitoBean
    private JwtUtils jwtUtils;

    private BedDto b1;

    @BeforeEach
    void setUp() {
        b1 = new BedDto();
        b1.setBedNumber(1);
        b1.setRoomNumber(204);
    }

    @Test
    void getAllFreeBeds_ShouldReturnList() throws Exception {
        List<BedDto> freeBeds = Collections.singletonList(b1);
        when(bedService.getAllFreeBeds()).thenReturn(freeBeds);

        mockMvc.perform(get("/hospital/beds/all/free"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data[0].bedNumber").value(1))
                .andExpect(jsonPath("$.data[0].roomNumber").value(204));
    }

    @Test
    void addBed_ShouldReturnSaved() throws Exception {
        when(bedService.addBed(1L, 1)).thenReturn(b1);

        mockMvc.perform(post("/hospital/beds/add")
                        .param("roomId", "1")
                        .param("bedNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Saved"))
                .andExpect(jsonPath("$.data.bedNumber").value(1));
    }

    @Test
    void deleteBed_ShouldReturnDeleted() throws Exception {
        mockMvc.perform(delete("/hospital/beds/delete")
                        .param("roomId", "1")
                        .param("bedNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Deleted"));
    }

    @Test
    void deleteBedById_ShouldReturnDeleted() throws Exception {
        mockMvc.perform(delete("/hospital/beds/delete-by-id")
                        .param("bedId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Deleted"));
    }
}