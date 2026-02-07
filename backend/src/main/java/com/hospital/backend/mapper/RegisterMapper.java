package com.hospital.backend.mapper;

import com.hospital.backend.model.*;
import com.hospital.backend.request.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring")
public interface RegisterMapper {
    void updatePersonFromRequest(RegisterRequest request, @MappingTarget Person person);
}
