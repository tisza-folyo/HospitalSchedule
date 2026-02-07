package com.hospital.backend.controller;

import com.hospital.backend.dto.RoleDto;
import com.hospital.backend.exception.AlreadyExistsException;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.response.ApiResponse;
import com.hospital.backend.service.role.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/roles")
public class RoleController {
    private final IRoleService roleService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllRoles(){
        List<RoleDto> results = roleService.getAllRoles();
        return ResponseEntity.ok(new ApiResponse("Success", results));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addRole(@RequestParam String roleName){
        try {
            RoleDto result = roleService.addRole(roleName);
            return ResponseEntity.ok(new ApiResponse("Saved", result));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null ));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteRole(@RequestParam String roleName){
        try {
            roleService.deleteRole(roleName);
            return ResponseEntity.ok(new ApiResponse("Deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateRole(@RequestParam String roleName, @RequestParam String newRoleName){
        RoleDto result = roleService.updateRole(roleName, newRoleName);
        return ResponseEntity.ok(new ApiResponse("Updated", result));
    }
}
