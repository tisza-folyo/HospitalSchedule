package com.hospital.backend.service.person;

import com.hospital.backend.dto.AdminDto;
import com.hospital.backend.dto.PatientDto;
import com.hospital.backend.exception.AlreadyExistsException;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.mapper.*;
import com.hospital.backend.model.Admin;
import com.hospital.backend.model.Patient;
import com.hospital.backend.model.Person;
import com.hospital.backend.model.Role;
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
    private final RoleMapper roleMapper;


    @Override
    public void deletePatient(String taj){
        Patient patient = patientRepository.findByTaj(taj).orElseThrow(() -> new ResourceNotFoundException("Patient"));
        patientRepository.delete(patient);
    }

    @Override
    public Object addPerson(RegisterRequest request) { // Object vagy egy közös PersonDto
        String roleName = request.getRole().getRoleName();
        Role role = roleRepository.findByRoleName(request.getRole().getRoleName()).orElseThrow(() -> new ResourceNotFoundException(request.getRole().getRoleName()));
        return switch (roleName) {
            case "PATIENT" -> savePatient(request,role);
            case "ADMIN" -> saveAdmin(request,role);
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
}
