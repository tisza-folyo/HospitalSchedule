package com.hospital.backend.service.section;

import com.hospital.backend.dto.SectionDto;

import java.util.List;

public interface ISectionService {
    SectionDto addSection(String sectionName);

    List<SectionDto> getAllSections();

    void deleteSection(String sectionName);

    SectionDto updateSection(String sectionName, String newSectionName);
}
