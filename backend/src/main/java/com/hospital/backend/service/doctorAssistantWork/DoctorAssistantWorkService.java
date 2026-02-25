package com.hospital.backend.service.doctorAssistantWork;

import com.hospital.backend.dto.AssistantDto;
import com.hospital.backend.dto.DoctorAssistantWorkDto;
import com.hospital.backend.dto.DoctorDto;
import com.hospital.backend.exception.AlreadyExistsException;
import com.hospital.backend.exception.CollisionException;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.mapper.DoctorAssistantWorkMapper;
import com.hospital.backend.mapper.PersonMapper;
import com.hospital.backend.model.*;
import com.hospital.backend.repository.AssistantRepository;
import com.hospital.backend.repository.DoctorAssistantWorkRepository;
import com.hospital.backend.repository.DoctorRepository;
import com.hospital.backend.repository.RoleRepository;
import com.hospital.backend.request.UpdateAssistantWorkRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorAssistantWorkService implements IDoctorAssistantWorkService {
    private final DoctorAssistantWorkRepository doctorAssistantWorkRepository;
    private final DoctorAssistantWorkMapper doctorAssistantWorkMapper;
    private final DoctorRepository doctorRepository;
    private final AssistantRepository assistantRepository;
    private final RoleRepository roleRepository;
    private final PersonMapper personMapper;

    @Override
    public List<DoctorAssistantWorkDto> getAllWorks(){
        return doctorAssistantWorkMapper.toDtoList(doctorAssistantWorkRepository.findAll());
    }

    @Override
    public List<DoctorAssistantWorkDto> getAllWorksAfterDay(LocalDate day){
        List<DoctorAssistantWork> works = doctorAssistantWorkRepository.findAllByAssistantIsNullAndWorkDayAfter(day);
        return doctorAssistantWorkMapper.toDtoList(works);
    }

    @Override
    public List<DoctorAssistantWorkDto> getAllWorksByAssistantAndDate(String aTaj, LocalDate workDayAfter, LocalDate workDayBefore){
        Assistant assistant = assistantRepository.findByTaj(aTaj).orElseThrow(() -> new ResourceNotFoundException("Assistant"));
        List<DoctorAssistantWork> works = doctorAssistantWorkRepository.findAllByAssistantAndWorkDayBetween(assistant,workDayAfter,workDayBefore);
        return doctorAssistantWorkMapper.toDtoList(works);
    }

    @Override
    public List<DoctorAssistantWorkDto> getDoctorWorks(String doctorTaj){
        Doctor doctor = getDoctorByTaj(doctorTaj);
        List<DoctorAssistantWork> works = doctorAssistantWorkRepository.findAllByDoctor(doctor);
        return doctorAssistantWorkMapper.toDtoList(works);
    }

    @Override
    public List<DoctorAssistantWorkDto> getAssistantWorks(String assistantTaj){
        Assistant assistant = getAssistantByTaj(assistantTaj);
        List<DoctorAssistantWork> works = doctorAssistantWorkRepository.findAllByAssistant(assistant);
        return doctorAssistantWorkMapper.toDtoList(works);
    }

    @Override
    public DoctorAssistantWorkDto getWorkByDateAndDTaj(LocalDate day, String doctorTaj){
        Doctor doctor = doctorRepository.findByTaj(doctorTaj).orElseThrow(() -> new ResourceNotFoundException("Doctor"));
        DoctorAssistantWork work = doctorAssistantWorkRepository.findByDoctorAndWorkDay(doctor, day).orElseThrow(() -> new ResourceNotFoundException("Work"));
        return doctorAssistantWorkMapper.toDto(work);
    }

    @Override
    public List<AssistantDto> getFreeAssistants(LocalDate day){
        List<Assistant> freeAssistants = assistantRepository.findFreeAssistantsByDay(day);
        return freeAssistants.stream().map(personMapper::toAssistantDto).toList();
    }

    @Override
    public List<DoctorDto> getFreeDoctors(LocalDate day){
        List<Doctor> freeDoctors = doctorRepository.findDoctorsWithoutAssistant(day);
        return freeDoctors.stream().map(personMapper::toDoctorDto).toList();
    }

    @Override
    public DoctorAssistantWorkDto addWork(DoctorAssistantWorkDto doctorAssistantWorkDto){
        Doctor doctor = doctorRepository.findByTaj(doctorAssistantWorkDto.getDoctor().getTaj()).orElseThrow(() -> new ResourceNotFoundException("Doctor"));
        Assistant assistant = assistantRepository.findByTaj(doctorAssistantWorkDto.getAssistant().getTaj()).orElseThrow(() -> new ResourceNotFoundException("Assistant"));
        DoctorAssistantWork work = doctorAssistantWorkMapper.toEntity(doctorAssistantWorkDto);
        if (doctorAssistantWorkRepository.existsByDoctorAndWorkDay(doctor, work.getWorkDay())) throw new AlreadyExistsException("Work");
        work.setDoctor(doctor);
        work.setAssistant(assistant);
        doctorAssistantWorkRepository.save(work);
        return doctorAssistantWorkMapper.toDto(work);
    }

    @Override
    public DoctorAssistantWorkDto addWork(LocalDate day, String dTaj, String uTaj){
        Doctor doctor = doctorRepository.findByTaj(dTaj).orElseThrow(() ->  new ResourceNotFoundException("Doctor"));
        DoctorAssistantWork work = new DoctorAssistantWork();
        work.setDoctor(doctor);
        work.setWorkDay(day);
        work.setUTaj(uTaj);
        doctorAssistantWorkRepository.save(work);
        return doctorAssistantWorkMapper.toDto(work);
    }

    @Override
    public DoctorAssistantWorkDto assignAssistant(UpdateAssistantWorkRequest request){
        Doctor doctor = doctorRepository.findByTaj(request.getDTaj()).orElseThrow(() ->  new ResourceNotFoundException("Doctor"));
        Assistant assistant = assistantRepository.findByTaj(request.getATaj()).orElseThrow(() -> new ResourceNotFoundException("Assistant"));
        DoctorAssistantWork work = doctorAssistantWorkRepository.findByDoctorAndWorkDay(doctor,request.getDay()).orElseThrow(() -> new ResourceNotFoundException("Work"));
        if (work.getAssistant() != null) throw new CollisionException("Assistant");
        if (doctorAssistantWorkRepository.existsByAssistantAndWorkDay(assistant, request.getDay())) throw new CollisionException("Work");
        work.setAssistant(assistant);
        work.setUTaj(request.getUTaj());
        return doctorAssistantWorkMapper.toDto(doctorAssistantWorkRepository.save(work));
    }

    @Override
    public DoctorAssistantWorkDto assignAssistant(String aTaj, String uTaj, LocalDate day){
        Assistant assistant = assistantRepository.findByTaj(aTaj).orElseThrow(() -> new ResourceNotFoundException("Assistant"));
        DoctorAssistantWork work = doctorAssistantWorkRepository.findFirstByAssistantIsNullAndWorkDay(day).orElseThrow(() -> new ResourceNotFoundException("Work"));
        work.setAssistant(assistant);
        work.setUTaj(uTaj);
        return doctorAssistantWorkMapper.toDto(doctorAssistantWorkRepository.save(work));
    }

    @Override
    public DoctorAssistantWorkDto changeAssistantByAssistant(UpdateAssistantWorkRequest request){
        Doctor doctor = doctorRepository.findByTaj(request.getDTaj()).orElseThrow(() ->  new ResourceNotFoundException("Doctor"));
        Assistant newAssistant = assistantRepository.findByTaj(request.getATaj()).orElseThrow(() -> new ResourceNotFoundException("Assistant"));
        DoctorAssistantWork work = doctorAssistantWorkRepository.findByDoctorAndWorkDay(doctor,request.getDay()).orElseThrow(() -> new ResourceNotFoundException("Work"));
        if (doctorAssistantWorkRepository.existsByAssistantAndWorkDay(newAssistant, request.getDay())) throw new CollisionException("Work");
        if (!work.getAssistant().getTaj().equals(request.getUTaj())) throw new ResourceNotFoundException("Assistant");
        work.setAssistant(newAssistant);
        work.setUTaj(request.getUTaj());
        return doctorAssistantWorkMapper.toDto(doctorAssistantWorkRepository.save(work));
    }

    @Override
    public void deleteWork(Long workId){
        DoctorAssistantWork work = doctorAssistantWorkRepository.findById(workId).orElseThrow(() -> new ResourceNotFoundException("Work"));
        doctorAssistantWorkRepository.delete(work);
    }

    private Doctor getDoctorByTaj(String dTaj){
        return doctorRepository.findByTaj(dTaj).orElseThrow(() -> new ResourceNotFoundException("Doctor"));
    }

    private Assistant getAssistantByTaj(String aTaj){
        return assistantRepository.findByTaj(aTaj).orElseThrow(() -> new ResourceNotFoundException("Assistant"));
    }
}
