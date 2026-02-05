package com.hospital.backend.mapper;

import com.hospital.backend.dto.PersonDto;
import com.hospital.backend.model.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface PersonMapper extends GenericMapper<Person, PersonDto> {
}
