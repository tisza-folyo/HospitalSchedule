package com.hospital.backend.service.specialty;

import com.hospital.backend.dto.SpecialtyDto;
import com.hospital.backend.exception.AlreadyExistsException;
import com.hospital.backend.mapper.SpecialtyMapper;
import com.hospital.backend.model.Specialty;
import com.hospital.backend.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecialtyService implements ISpecialtyService {
    private final SpecialtyRepository specialtyRepository;
    private final SpecialtyMapper specialtyMapper;

    @Override
    public SpecialtyDto addSpecialty(SpecialtyDto specialtyDto){
        if(isSpecialtyExist(specialtyDto.getSpecialtyName())) throw new AlreadyExistsException("Specialty");
        Specialty newSpecialty = specialtyMapper.toEntity(specialtyDto);
        Specialty savedSpecialty = specialtyRepository.save(newSpecialty);

        return specialtyMapper.toDto(savedSpecialty);
    }

    private boolean isSpecialtyExist(String specialtyName){
        return specialtyRepository.existsBySpecialtyName(specialtyName);
    }
}
