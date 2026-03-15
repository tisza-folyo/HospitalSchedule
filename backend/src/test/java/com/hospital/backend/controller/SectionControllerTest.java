package com.hospital.backend.controller;

import com.hospital.backend.dto.SectionDto;
import com.hospital.backend.service.section.ISectionService;
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
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(SectionController.class)
@Import(HospitalConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class SectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ISectionService sectionService;

    @MockitoBean
    private HospitalPersonDetailsService personDetailsService;
    @MockitoBean
    private JwtAuthEntryPoint jwtAuthEntryPoint;
    @MockitoBean
    private JwtUtils jwtUtils;

    private SectionDto s1;
    private SectionDto s2;

    @BeforeEach
    void setUp() {
        s1 = new SectionDto();
        s1.setSectionName("Sebészet");

        s2 = new SectionDto();
        s2.setSectionName("Belgyógyászat");
    }

    @Test
    void getAllSections_ShouldReturnSuccess() throws Exception {
        List<SectionDto> sections = Arrays.asList(s1, s2);
        when(sectionService.getAllSections()).thenReturn(sections);

        mockMvc.perform(get("/hospital/sections/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data[0].sectionName").value("Sebészet"))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void addSection_ShouldReturnSaved() throws Exception {
        when(sectionService.addSection("Sebészet")).thenReturn(s1);

        mockMvc.perform(post("/hospital/sections/add")
                        .param("name", "Sebészet"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Saved"))
                .andExpect(jsonPath("$.data.sectionName").value("Sebészet"));
    }

    @Test
    void deleteSection_ShouldReturnDeleted() throws Exception {
        mockMvc.perform(delete("/hospital/sections/delete")
                        .param("name", "Sebészet"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Deleted"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void updateSection_ShouldReturnUpdated() throws Exception {
        SectionDto updated = new SectionDto();
        updated.setSectionName("Új Sebészet");

        when(sectionService.updateSection("Sebészet", "Új Sebészet")).thenReturn(updated);

        mockMvc.perform(put("/hospital/sections/update")
                        .param("name", "Sebészet")
                        .param("newName", "Új Sebészet"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Updated"))
                .andExpect(jsonPath("$.data.sectionName").value("Új Sebészet"));
    }
}