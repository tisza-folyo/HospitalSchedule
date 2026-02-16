package com.hospital.backend.controller;

import com.hospital.backend.dto.BedDto;
import com.hospital.backend.dto.RoomDto;
import com.hospital.backend.response.ApiResponse;
import com.hospital.backend.service.room.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/rooms")
public class RoomController {
    private final IRoomService roomService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllRooms() {
        List<RoomDto> results = roomService.getAllRooms();
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @GetMapping("/{roomId}/beds")
    public ResponseEntity<ApiResponse> getRoomBeds(@PathVariable long roomId) {
        List<BedDto> results = roomService.getRoomBeds(roomId);
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addRoomBed(@RequestBody RoomDto roomDto) {
        RoomDto result = roomService.addRoom(roomDto);
        return ResponseEntity.ok(new ApiResponse("Saved", result));
    }

    @PostMapping("/{roomId}/add-beds")
    public ResponseEntity<ApiResponse> addRoomBeds(@RequestBody List<Integer> bedNumbers, @PathVariable long roomId) {
        RoomDto result = roomService.addBeds(bedNumbers, roomId);
        return ResponseEntity.ok(new ApiResponse("Saved", result));
    }

    @DeleteMapping("/{roomId}/clear")
    public ResponseEntity<ApiResponse> clearRoom(@PathVariable long roomId) {
        roomService.clearBeds(roomId);
        return ResponseEntity.ok(new ApiResponse("Cleared", null));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteRoom(@RequestParam long roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.ok(new ApiResponse("Deleted", null));
    }
}
