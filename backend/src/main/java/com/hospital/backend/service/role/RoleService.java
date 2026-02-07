package com.hospital.backend.service.role;

import com.hospital.backend.dto.RoleDto;
import com.hospital.backend.exception.AlreadyExistsException;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.mapper.RoleMapper;
import com.hospital.backend.model.Role;
import com.hospital.backend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleDto addRole(String roleName){
        if (roleRepository.existsByRoleName(roleName)) throw new AlreadyExistsException(roleName);
        Role role = new Role();
        role.setRoleName(roleName);
        Role savedRole = roleRepository.save(role);
        return roleMapper.toDto(savedRole);
    }

    @Override
    public List<RoleDto> getAllRoles(){
        return roleMapper.toDtoList(roleRepository.findAll());
    }

    @Override
    public void deleteRole(String roleName){
        Role role = roleRepository.findByRoleName(roleName).orElseThrow(() -> new ResourceNotFoundException(roleName));
        roleRepository.delete(role);
    }


    @Override
    public RoleDto updateRole(String roleName, String newRoleName){
        Role role = roleRepository.findByRoleName(roleName).orElseThrow(() -> new ResourceNotFoundException(roleName));
        role.setRoleName(newRoleName);
        return roleMapper.toDto(roleRepository.save(role));
    }


}
