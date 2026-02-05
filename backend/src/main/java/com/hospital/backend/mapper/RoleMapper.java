package com.hospital.backend.mapper;

import com.hospital.backend.dto.RoleDto;
import com.hospital.backend.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper extends GenericMapper<Role, RoleDto> {
}
