package com.hospital.backend.service.section;

import com.hospital.backend.dto.SectionDto;
import com.hospital.backend.exception.AlreadyExistsException;
import com.hospital.backend.exception.CollisionException;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.mapper.SectionMapper;
import com.hospital.backend.model.Section;
import com.hospital.backend.repository.DoctorRepository;
import com.hospital.backend.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionService implements ISectionService {
    private final SectionRepository sectionRepository;
    private final DoctorRepository doctorRepository;
    private final SectionMapper sectionMapper;

    @Override
    public SectionDto addSection(String sectionName){
        if (sectionRepository.existsBySectionName(sectionName)) throw new AlreadyExistsException(sectionName);
        Section section = new Section();
        section.setSectionName(sectionName);
        sectionRepository.save(section);
        return sectionMapper.toDto(section);
    }

    @Override
    public List<SectionDto> getAllSections(){
        return sectionMapper.toDtoList(sectionRepository.findAll());
    }

    @Override
    public void deleteSection(String sectionName){
        Section section = sectionRepository.findBySectionName(sectionName).orElseThrow(() -> new ResourceNotFoundException(sectionName));
        if (doctorRepository.existsBySection(section)) throw new CollisionException("Doctor");
        sectionRepository.delete(section);
    }

    @Override
    public SectionDto updateSection(String sectionName, String newSectionName){
        Section section = sectionRepository.findBySectionName(sectionName).orElseThrow(() -> new ResourceNotFoundException(sectionName));
        section.setSectionName(newSectionName);
        sectionRepository.save(section);
        return sectionMapper.toDto(section);
    }



}
