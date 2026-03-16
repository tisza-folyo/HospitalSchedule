package com.hospital.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.backend.dto.*;
import com.hospital.backend.model.Person;
import com.hospital.backend.request.RegisterRequest;
import com.hospital.backend.service.person.IPersonService;
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

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(PersonController.class)
@Import(HospitalConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IPersonService personService;

    @MockitoBean
    private HospitalPersonDetailsService personDetailsService;
    @MockitoBean
    private JwtAuthEntryPoint jwtAuthEntryPoint;
    @MockitoBean
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    private DoctorDto doctorDto;
    private PatientDto patientDto;
    private RoleDto roleDto;

    @BeforeEach
    void setUp() {
        doctorDto = new DoctorDto();
        doctorDto.setTaj("111222333");
        doctorDto.setFirstName("Dr. Teszt");

        patientDto = new PatientDto();
        patientDto.setTaj("444555666");
        patientDto.setFirstName("Beteg");

        roleDto = new RoleDto();
        roleDto.setRoleName("ROLE_DOCTOR");
    }


    @Test
    void getPersonDetails_ShouldReturnPerson() throws Exception {

        when(personService.getPersonByTajAndRole(anyString(), anyString())).thenReturn(new Person() {});

        mockMvc.perform(get("/hospital/people/111222333/person")
                        .param("role", "ROLE_DOCTOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void getAllRolesForPerson_ShouldReturnList() throws Exception {
        when(personService.getAllRolesForPerson("111222333")).thenReturn(Collections.singletonList(roleDto));

        mockMvc.perform(get("/hospital/people/111222333/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].roleName").value("ROLE_DOCTOR"));
    }

    @Test
    void getAllDoctor_ShouldReturnList() throws Exception {
        when(personService.getAllDoctor()).thenReturn(Collections.singletonList(doctorDto));

        mockMvc.perform(get("/hospital/people/doctors/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    void getPatientDetails_ShouldReturnPatient() throws Exception {
        when(personService.getPatientByTaj("444555666")).thenReturn(patientDto);

        mockMvc.perform(get("/hospital/people/patient/444555666"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.taj").value("444555666"));
    }

    @Test
    void getAllPatientsByDoctorTaj_ShouldReturnList() throws Exception {
        when(personService.getAllPatientsByDoctorTaj("111222333")).thenReturn(Collections.singletonList(patientDto));

        mockMvc.perform(get("/hospital/people/patients/111222333"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].firstName").value("Beteg"));
    }

    @Test
    void getAllPatientsByAssistantTaj_ShouldReturnList() throws Exception {
        when(personService.getAllPatientsByAssistantTaj("999")).thenReturn(Collections.singletonList(patientDto));

        mockMvc.perform(get("/hospital/people/doctors/patients/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void getAllFreeNurse_ShouldReturnList() throws Exception {
        NurseDto nurseDto = new NurseDto();
        when(personService.getAllFreeNurse()).thenReturn(Collections.singletonList(nurseDto));

        mockMvc.perform(get("/hospital/people/nurses/all/free"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void addPerson_ShouldReturnSaved() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setTaj("123");
        when(personService.addPerson(any(RegisterRequest.class))).thenReturn(patientDto);

        mockMvc.perform(post("/hospital/people/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Saved"));
    }

    @Test
    void deletePerson_ShouldReturnDeleted() throws Exception {
        doNothing().when(personService).deletePerson("444555666", "ROLE_PATIENT");

        mockMvc.perform(delete("/hospital/people/delete/444555666/person")
                        .param("role", "ROLE_PATIENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Deleted"));
    }

    @Test
    void setDoctorSection_ShouldReturnUpdated() throws Exception {
        when(personService.setSection(anyString(), anyString(), anyString())).thenReturn(doctorDto);

        mockMvc.perform(put("/hospital/people/set-section/doctor")
                        .param("taj", "111")
                        .param("role", "ROLE_DOCTOR")
                        .param("section", "Sebészet"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Updated"));
    }

    @Test
    void updatePassword_ShouldReturnUpdated() throws Exception {
        Map<String, String> passwords = new HashMap<>();
        passwords.put("oldPassword", "old");
        passwords.put("newPassword", "new");

        mockMvc.perform(put("/hospital/people/111/change-password")
                        .param("role", "ROLE_DOCTOR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwords)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Updated"));
    }

    @Test
    void updateSection_ShouldReturnUpdated() throws Exception {
        mockMvc.perform(put("/hospital/people/111/change-section")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"Új Részleg\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Updated"));
    }
}