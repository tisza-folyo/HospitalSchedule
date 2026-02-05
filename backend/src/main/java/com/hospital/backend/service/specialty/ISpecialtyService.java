package com.hospital.backend.service.specialty;


import com.hospital.backend.dto.SpecialtyDto;

import java.util.List;

public interface ISpecialtyService {
    SpecialtyDto addSpecialty(SpecialtyDto specialtyDto);

    List<SpecialtyDto> addSpecialties(List<SpecialtyDto> specialtyDtos);

    SpecialtyDto deleteSpecialty(String name);

    List<SpecialtyDto> getAllSpecialties();
}
