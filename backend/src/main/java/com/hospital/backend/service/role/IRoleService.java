package com.hospital.backend.service.role;

import com.hospital.backend.dto.RoleDto;

import java.util.List;

public interface IRoleService {
    RoleDto addRole(String roleName);

    List<RoleDto> getAllRoles();

    void deleteRole(String roleName);

    RoleDto updateRole(String roleName, String newRoleName);
}
