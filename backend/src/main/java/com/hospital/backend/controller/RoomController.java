package com.hospital.backend.controller;

import com.hospital.backend.dto.BedDto;
import com.hospital.backend.dto.RoomDto;
import com.hospital.backend.exception.AlreadyExistsException;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.response.ApiResponse;
import com.hospital.backend.service.room.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/rooms")
public class RoomController {
    private final IRoomService roomService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllRooms(){
        try {
            List<RoomDto> results = roomService.getAllRooms();
            return ResponseEntity.ok(new ApiResponse("Success", results));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{roomId}/beds")
    public ResponseEntity<ApiResponse> getRoomBeds(@PathVariable long roomId){
        try {
            List<BedDto> results = roomService.getRoomBeds(roomId);
            return ResponseEntity.ok(new ApiResponse("Success", results));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addRoomBed(@RequestBody RoomDto roomDto){
        try {
            RoomDto result = roomService.addRoom(roomDto);
            return ResponseEntity.ok(new ApiResponse("Saved", result));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null ));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/{roomId}/add-beds")
    public ResponseEntity<ApiResponse> addRoomBeds(@RequestBody List<Integer> bedNumbers, @PathVariable long roomId){
        try {
            RoomDto result = roomService.addBeds(bedNumbers,roomId);
            return ResponseEntity.ok(new ApiResponse("Saved", result));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (AlreadyExistsException e){
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null ));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{roomId}/clear")
    public ResponseEntity<ApiResponse> clearRoom(@PathVariable long roomId){
        try {
            roomService.clearBeds(roomId);
            return ResponseEntity.ok(new ApiResponse("Cleared", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteRoom(@RequestParam long roomId){
        try {
            roomService.deleteRoom(roomId);
            return ResponseEntity.ok(new ApiResponse("Deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
