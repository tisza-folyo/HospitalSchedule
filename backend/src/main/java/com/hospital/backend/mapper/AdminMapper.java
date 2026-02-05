package com.hospital.backend.mapper;

import com.hospital.backend.dto.AdminDto;
import com.hospital.backend.model.Admin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface AdminMapper extends GenericMapper<Admin, AdminDto> {
}
