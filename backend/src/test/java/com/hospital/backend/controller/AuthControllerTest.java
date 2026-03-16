package com.hospital.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.backend.request.LoginRequest;
import com.hospital.backend.security.jwt.JwtUtils;
import com.hospital.backend.security.person.HospitalPersonDetails;
import com.hospital.backend.security.person.HospitalPersonDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private HospitalPersonDetailsService hospitalPersonDetailsService;

    @MockitoBean
    private JwtUtils jwtUtils;

    private LoginRequest loginRequest;
    private HospitalPersonDetails mockPersonDetails;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@hospital.com");
        loginRequest.setPassword("password123");
        loginRequest.setRoleName("ROLE_DOCTOR");


        mockPersonDetails = mock(HospitalPersonDetails.class);
        when(mockPersonDetails.getUsername()).thenReturn("test@hospital.com");
        when(mockPersonDetails.getPassword()).thenReturn("encoded_password");
        when(mockPersonDetails.getTaj()).thenReturn("123456789");
        when(mockPersonDetails.getAuthorities()).thenReturn(Collections.emptyList());
    }

    @Test
    void login_ShouldReturnJwtResponse_WhenCredentialsAreValid() throws Exception {

        when(hospitalPersonDetailsService.loadUserByEmailAndRole(anyString(), anyString()))
                .thenReturn(mockPersonDetails);


        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);


        when(jwtUtils.generateTokenForUser(any(Authentication.class))).thenReturn("mocked-jwt-token");

        mockMvc.perform(post("/hospital/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login Success"))
                .andExpect(jsonPath("$.data.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.data.taj").value("123456789"));
    }

    @Test
    void login_ShouldReturnError_WhenPasswordIsWrong() throws Exception {

        when(hospitalPersonDetailsService.loadUserByEmailAndRole(anyString(), anyString()))
                .thenReturn(mockPersonDetails);


        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(post("/hospital/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}