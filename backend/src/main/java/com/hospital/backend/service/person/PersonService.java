package com.hospital.backend.service.person;

import com.hospital.backend.dto.*;
import com.hospital.backend.exception.AlreadyExistsException;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.mapper.*;
import com.hospital.backend.model.*;
import com.hospital.backend.repository.*;
import com.hospital.backend.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonService implements IPersonService {
    private final PersonRepository personRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final NurseRepository nurseRepository;
    private final AssistantRepository assistantRepository;
    private final AdminRepository adminRepository;
    private final RoleRepository roleRepository;
    private final PersonMapper personMapper;
    private final SpecialtyRepository specialtyRepository;


    @Override
    public void deletePerson(String taj, String roleName) {
        Role role = roleRepository.findByRoleName(roleName).orElseThrow(() -> new ResourceNotFoundException(roleName));
        Person person = personRepository.findByTajAndRole(taj,role).orElseThrow(() -> new ResourceNotFoundException(taj + " " + roleName));
        personRepository.delete(person);
    }

    @Override
    public Object addPerson(RegisterRequest request) {
        String roleName = request.getRole().getRoleName();
        Role role = roleRepository.findByRoleName(request.getRole().getRoleName()).orElseThrow(() -> new ResourceNotFoundException(request.getRole().getRoleName()));
        return switch (roleName) {
            case "PATIENT" -> savePatient(request,role);
            case "ADMIN" -> saveAdmin(request,role);
            case "NURSE" -> saveNurse(request,role);
            case "ASSISTANT" -> saveAssistant(request,role);
            case "DOCTOR" -> saveDoctor(request,role);
            default -> throw new ResourceNotFoundException(roleName);
        };
    }

    private PatientDto savePatient(RegisterRequest request, Role role) {
        if(patientRepository.existsByTaj(request.getTaj())) throw new AlreadyExistsException("TAJ");
        Patient patient = new Patient();
        personMapper.updatePersonFromRequest(request, patient);
        patient.setRole(role);
        return personMapper.toPatientDto(patientRepository.save(patient));
    }

    private AdminDto saveAdmin(RegisterRequest request, Role role){
        if(adminRepository.existsByTaj(request.getTaj())) throw new AlreadyExistsException("TAJ");
        Admin admin = new Admin();
        personMapper.updatePersonFromRequest(request,admin);
        admin.setRole(role);
        return personMapper.toAdminDto(adminRepository.save(admin));
    }

    private NurseDto saveNurse(RegisterRequest request, Role role){
        if (nurseRepository.existsByTaj(request.getTaj())) throw new AlreadyExistsException("TAJ");
        Nurse nurse = new Nurse();
        personMapper.updatePersonFromRequest(request,nurse);
        nurse.setRole(role);
        return personMapper.toNurseDto(nurseRepository.save(nurse));
    }

    private AssistantDto saveAssistant(RegisterRequest request,Role role){
        if (assistantRepository.existsByTaj(request.getTaj())) throw new AlreadyExistsException("TAJ");
        Assistant assistant = new Assistant();
        personMapper.updatePersonFromRequest(request,assistant);
        assistant.setRole(role);
        return personMapper.toAssistantDto(assistantRepository.save(assistant));
    }

    private DoctorDto saveDoctor(RegisterRequest request, Role role){
        if (doctorRepository.existsByTaj(request.getTaj())) throw new AlreadyExistsException("TAJ");
        Specialty spec = specialtyRepository.findBySpecialtyName(request.getSpecialty().getSpecialtyName()).orElseThrow(() -> new ResourceNotFoundException(request.getSpecialty().getSpecialtyName()));
        Doctor doctor = new Doctor();
        personMapper.updatePersonFromRequest(request,doctor);
        doctor.setRole(role);
        doctor.setSpecialty(spec);
        return personMapper.toDoctorDto(doctorRepository.save(doctor));
    }
}
