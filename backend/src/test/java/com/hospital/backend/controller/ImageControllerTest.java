package com.hospital.backend.controller;

import com.hospital.backend.dto.ImageDto;
import com.hospital.backend.service.image.ImageService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(ImageController.class)
@Import(HospitalConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ImageService imageService;

    @MockitoBean
    private HospitalPersonDetailsService personDetailsService;
    @MockitoBean
    private JwtAuthEntryPoint jwtAuthEntryPoint;
    @MockitoBean
    private JwtUtils jwtUtils;

    private ImageDto imageDto;

    @BeforeEach
    void setUp() {
        imageDto = new ImageDto();
        imageDto.setFileName("lelet.png");
        imageDto.setPatientTaj("123456789");
        imageDto.setDwnUrl("http://download.com/1");
    }

    @Test
    void getPatientImages_ShouldReturnList() throws Exception {
        when(imageService.getPatientImages("123456789")).thenReturn(Collections.singletonList(imageDto));

        mockMvc.perform(get("/hospital/images/123456789/images"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data[0].fileName").value("lelet.png"));
    }

    @Test
    void saveImagesToPatient_ShouldReturnSaved() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "files",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "image content".getBytes()
        );

        when(imageService.saveImages(anyList(), eq("123456789"))).thenReturn(Arrays.asList(imageDto));

        mockMvc.perform(multipart("/hospital/images/upload-to-patient")
                        .file(file)
                        .param("pTaj", "123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Saved"));
    }

    @Test
    void saveImageToAppointment_ShouldReturnSaved() throws Exception {
        MockMultipartFile file = new MockMultipartFile("files", "test.png", "image/png", "data".getBytes());

        when(imageService.saveImages(anyList(), anyLong())).thenReturn(Arrays.asList(imageDto));

        mockMvc.perform(multipart("/hospital/images/upload-to-appointment")
                        .file(file)
                        .param("appointmentId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Saved"));
    }

    @Test
    void deleteImagesByTaj_ShouldReturnDeleted() throws Exception {
        mockMvc.perform(delete("/hospital/images/delete/123456789/images"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Deleted"));
    }

    @Test
    void deleteImageById_ShouldReturnDeleted() throws Exception {
        mockMvc.perform(delete("/hospital/images/delete/1/image"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Deleted"));
    }
}