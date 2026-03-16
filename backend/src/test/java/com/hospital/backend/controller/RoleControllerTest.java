package com.hospital.backend.controller;

import com.hospital.backend.dto.RoleDto;
import com.hospital.backend.service.role.IRoleService;
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
@WebMvcTest(RoleController.class)
@Import(HospitalConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IRoleService roleService;

    @MockitoBean
    private HospitalPersonDetailsService personDetailsService;
    @MockitoBean
    private JwtAuthEntryPoint jwtAuthEntryPoint;
    @MockitoBean
    private JwtUtils jwtUtils;

    private RoleDto adminRole;
    private RoleDto doctorRole;

    @BeforeEach
    void setUp() {
        adminRole = new RoleDto();
        adminRole.setRoleName("ROLE_ADMIN");

        doctorRole = new RoleDto();
        doctorRole.setRoleName("ROLE_DOCTOR");
    }

    @Test
    void getAllRoles_ShouldReturnList() throws Exception {
        List<RoleDto> roles = Arrays.asList(adminRole, doctorRole);
        when(roleService.getAllRoles()).thenReturn(roles);

        mockMvc.perform(get("/hospital/roles/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data[0].roleName").value("ROLE_ADMIN"))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void addRole_ShouldReturnSaved() throws Exception {
        when(roleService.addRole("ROLE_ADMIN")).thenReturn(adminRole);

        mockMvc.perform(post("/hospital/roles/add")
                        .param("name", "ROLE_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Saved"))
                .andExpect(jsonPath("$.data.roleName").value("ROLE_ADMIN"));
    }

    @Test
    void deleteRole_ShouldReturnDeleted() throws Exception {
        mockMvc.perform(delete("/hospital/roles/delete")
                        .param("roleName", "ROLE_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Deleted"));
    }

    @Test
    void updateRole_ShouldReturnUpdated() throws Exception {
        RoleDto updatedRole = new RoleDto();
        updatedRole.setRoleName("ROLE_SUPERUSER");

        when(roleService.updateRole("ROLE_ADMIN", "ROLE_SUPERUSER")).thenReturn(updatedRole);

        mockMvc.perform(put("/hospital/roles/update")
                        .param("roleName", "ROLE_ADMIN")
                        .param("newRoleName", "ROLE_SUPERUSER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Updated"))
                .andExpect(jsonPath("$.data.roleName").value("ROLE_SUPERUSER"));
    }
}