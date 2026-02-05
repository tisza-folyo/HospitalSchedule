package com.hospital.backend.service.specialty;

import com.hospital.backend.dto.SpecialtyDto;
import com.hospital.backend.exception.AlreadyExistsException;
import com.hospital.backend.exception.CollisionException;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.mapper.SpecialtyMapper;
import com.hospital.backend.model.Specialty;
import com.hospital.backend.repository.DoctorRepository;
import com.hospital.backend.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialtyService implements ISpecialtyService {
    private final SpecialtyRepository specialtyRepository;
    private final DoctorRepository doctorRepository;
    private final SpecialtyMapper specialtyMapper;

    @Override
    public SpecialtyDto addSpecialty(SpecialtyDto specialtyDto){
        if(isSpecialtyExist(specialtyDto.getSpecialtyName())) throw new AlreadyExistsException(specialtyDto.getSpecialtyName());
        Specialty newSpecialty = specialtyMapper.toEntity(specialtyDto);
        Specialty savedSpecialty = specialtyRepository.save(newSpecialty);

        return specialtyMapper.toDto(savedSpecialty);
    }

    @Override
    public List<SpecialtyDto> addSpecialties(List<SpecialtyDto> specialtyDtos){
        List<Specialty> specialties = specialtyDtos.stream().filter(s -> specialtyRepository.findBySpecialtyName(s.getSpecialtyName()).isEmpty()).map(specialtyMapper::toEntity).toList();
        if (specialties.isEmpty()) throw new AlreadyExistsException("All of them are");
        specialtyRepository.saveAll(specialties);
        return specialtyMapper.toDtoList(specialties);
    }

    @Override
    public SpecialtyDto deleteSpecialty(String name){
        Specialty specialty = specialtyRepository.findBySpecialtyName(name).orElseThrow(() -> new ResourceNotFoundException(name));
        if(isDoctorHasThisSpecialty(specialtyMapper.toDto(specialty))) throw new CollisionException("Doctor");
        specialtyRepository.delete(specialty);
        return specialtyMapper.toDto(specialty);
    }

    @Override
    public List<SpecialtyDto> getAllSpecialties(){
        return specialtyMapper.toDtoList(specialtyRepository.findAll());
    }

    private boolean isSpecialtyExist(String specialtyName){
        return specialtyRepository.existsBySpecialtyName(specialtyName);
    }

    private boolean isDoctorHasThisSpecialty(SpecialtyDto specialtyDto){
        return doctorRepository.existsBySpecialtySpecialtyName(specialtyDto.getSpecialtyName());
    }

}
