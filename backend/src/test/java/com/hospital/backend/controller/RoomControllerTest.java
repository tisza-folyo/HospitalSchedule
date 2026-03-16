package com.hospital.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.backend.dto.BedDto;
import com.hospital.backend.dto.RoomDto;
import com.hospital.backend.service.room.IRoomService;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(RoomController.class)
@Import(HospitalConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IRoomService roomService;

    @MockitoBean
    private HospitalPersonDetailsService personDetailsService;
    @MockitoBean
    private JwtAuthEntryPoint jwtAuthEntryPoint;
    @MockitoBean
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    private RoomDto room1;

    @BeforeEach
    void setUp() {
        room1 = new RoomDto();
        room1.setRoomId(1L);
        room1.setFloor(2);
        room1.setRoomNumber(204);
    }

    @Test
    void getAllRooms_ShouldReturnList() throws Exception {
        when(roomService.getAllRooms()).thenReturn(Collections.singletonList(room1));

        mockMvc.perform(get("/hospital/rooms/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data[0].roomNumber").value(204));
    }

    @Test
    void getAllRoomsWithFreeSpace_ShouldReturnList() throws Exception {
        when(roomService.getAllRoomsWithFreeBeds()).thenReturn(Collections.singletonList(room1));

        mockMvc.perform(get("/hospital/rooms/all/free"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void getRoomBeds_ShouldReturnBeds() throws Exception {
        BedDto bed = new BedDto();
        when(roomService.getRoomBeds(1L)).thenReturn(List.of(bed));

        mockMvc.perform(get("/hospital/rooms/1/beds"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void addRoom_ShouldReturnSaved() throws Exception {
        when(roomService.addRoom(any(RoomDto.class))).thenReturn(room1);

        mockMvc.perform(post("/hospital/rooms/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Saved"))
                .andExpect(jsonPath("$.data.roomNumber").value(204));
    }

    @Test
    void addRoomBeds_ShouldReturnUpdatedRoom() throws Exception {
        List<Integer> bedNumbers = Arrays.asList(1, 2, 3);
        when(roomService.addBeds(eq(bedNumbers), eq(1L))).thenReturn(room1);

        mockMvc.perform(post("/hospital/rooms/1/add-beds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bedNumbers)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Saved"));
    }

    @Test
    void clearRoom_ShouldReturnCleared() throws Exception {
        mockMvc.perform(delete("/hospital/rooms/1/clear"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Cleared"));
    }

    @Test
    void deleteRoom_ShouldReturnDeleted() throws Exception {
        mockMvc.perform(delete("/hospital/rooms/delete")
                        .param("roomId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Deleted"));
    }
}