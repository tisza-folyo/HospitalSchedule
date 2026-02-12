package com.hospital.backend.service.nursePatientCare;

import com.hospital.backend.dto.NursePatientCareDto;
import com.hospital.backend.exception.CollisionException;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.mapper.NursePatientCareMapper;
import com.hospital.backend.model.*;
import com.hospital.backend.repository.*;
import com.hospital.backend.request.AddCareRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NursePatientCareService implements INursePatientCareService {
    private final NursePatientCareRepository nursePatientCareRepository;
    private final NursePatientCareMapper nursePatientCareMapper;
    private final PatientRepository patientRepository;
    private final NurseRepository nurseRepository;
    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;

    @Override
    public List<NursePatientCareDto> getAllNursePatientCares(){
        return nursePatientCareMapper.toDtoList(nursePatientCareRepository.findAll());
    }

    @Override
    public List<NursePatientCareDto> getAllNonCares(){
        List<NursePatientCare> cares = nursePatientCareRepository.findPatientsWaitingForNurse();
        return nursePatientCareMapper.toDtoList(cares);
    }

    @Override
    public List<NursePatientCareDto> getAllCaresByNurse(String nTaj){
        Nurse nurse = getNurseByTaj(nTaj);
        List<NursePatientCare> cares = nursePatientCareRepository.findAllByNurse(nurse);
        return nursePatientCareMapper.toDtoList(cares);
    }

    @Override
    public List<NursePatientCareDto> getAllCaresByPatient(String pTaj){
        Patient patient = getPatientByTaj(pTaj);
        List<NursePatientCare> cares = nursePatientCareRepository.findAllByPatient(patient);
        return nursePatientCareMapper.toDtoList(cares);
    }

    @Override
    public NursePatientCareDto getActiveCareByPatient(String pTaj){
        Patient patient = getPatientByTaj(pTaj);
        NursePatientCare care = nursePatientCareRepository.findByPatientActiveCare(patient).orElseThrow(() -> new ResourceNotFoundException("Care"));
        return nursePatientCareMapper.toDto(care);
    }

    @Override
    public NursePatientCareDto addCare(AddCareRequest request){
        Room room = getRoom(request.getRoom().getRoomId());
        Bed bed = getBed(room, request.getBed().getBedNumber());
        Patient patient = getPatientByTaj(request.getPatient().getTaj());
        if (nursePatientCareRepository.existsByPatientAndExitDayIsNull(patient)) throw new CollisionException("Care exitDay");
        NursePatientCare care = new NursePatientCare();
        care.setPatient(patient);
        care.setEntryDay(request.getEntryDay());
        care.setRoom(room);
        care.setBed(bed);
        care.setUTaj(request.getUTaj());
        nursePatientCareRepository.save(care);
        return nursePatientCareMapper.toDto(care);
    }

    @Override
    public NursePatientCareDto assignNurseToPatient(String nTaj, String pTaj, String uTaj){
        Nurse nurse = getNurseByTaj(nTaj);
        Patient patient = getPatientByTaj(pTaj);
        NursePatientCare care = nursePatientCareRepository.findByPatientActiveCare(patient).orElseThrow(() -> new ResourceNotFoundException("Care"));
        if (care.getNurse() != null) throw new CollisionException("Nurse");
        care.setNurse(nurse);
        care.setUTaj(uTaj);
        return nursePatientCareMapper.toDto(care);
    }

    @Override
    public NursePatientCareDto assignNurseToPatient(String nTaj, String uTaj){
        NursePatientCare care = nursePatientCareRepository.findFirstByNurseIsNullAndExitDayIsNull().orElseThrow(() -> new ResourceNotFoundException("Care"));
        Nurse nurse = getNurseByTaj(nTaj);
        care.setNurse(nurse);
        care.setUTaj(uTaj);
        return nursePatientCareMapper.toDto(care);
    }

    @Override
    public NursePatientCareDto exitPatientCare(String pTaj, LocalDate exitDay, String uTaj){
        Patient patient = getPatientByTaj(pTaj);
        NursePatientCare care = nursePatientCareRepository.findByPatientActiveCare(patient).orElseThrow(() -> new ResourceNotFoundException("Care"));
        care.setExitDay(exitDay);
        care.setUTaj(uTaj);
        return nursePatientCareMapper.toDto(care);
    }

    @Override
    public NursePatientCareDto changeNurse(String nTaj, String pTaj, String uTaj){
        Nurse nurse = getNurseByTaj(nTaj);
        Patient patient = getPatientByTaj(pTaj);
        NursePatientCare care = nursePatientCareRepository.findByPatientActiveCare(patient).orElseThrow(() -> new ResourceNotFoundException("Care"));
        if (!care.getNurse().getTaj().equals(nTaj)) throw new ResourceNotFoundException("Nurse");
        care.setNurse(nurse);
        care.setUTaj(uTaj);
        return nursePatientCareMapper.toDto(care);
    }

    @Override
    public void deleteCare(Long careId){
        NursePatientCare care = nursePatientCareRepository.findById(careId).orElseThrow(() -> new ResourceNotFoundException("Care"));
        nursePatientCareRepository.delete(care);
    }

    private Patient getPatientByTaj(String pTaj){
        return patientRepository.findByTaj(pTaj).orElseThrow(() -> new ResourceNotFoundException("Patient"));
    }

    private Nurse getNurseByTaj(String nTaj){
        return nurseRepository.findByTaj(nTaj).orElseThrow(() -> new ResourceNotFoundException("Nurse"));
    }

    private Room getRoom(Long roomId){
        return roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room"));
    }
    private Bed getBed(Room room, int bedNumber){
        return bedRepository.findByRoomAndBedNumber(room,bedNumber).orElseThrow(() -> new ResourceNotFoundException("Bed"));
    }
}
