package com.hospital.backend.service.person;

import com.hospital.backend.dto.*;
import com.hospital.backend.exception.AlreadyExistsException;
import com.hospital.backend.exception.CollisionException;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.mapper.*;
import com.hospital.backend.model.*;
import com.hospital.backend.repository.*;
import com.hospital.backend.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private final SectionRepository sectionRepository;
    private final PersonMapper personMapper;
    private final SpecialtyRepository specialtyRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void deletePerson(String taj, String roleName) {
        Role role = roleRepository.findByRoleName(roleName).orElseThrow(() -> new ResourceNotFoundException(roleName));
        Person person = personRepository.findByTajAndRole(taj,role).orElseThrow(() -> new ResourceNotFoundException(taj + " " + roleName));
        personRepository.delete(person);
    }

    @Override
    public Person getPersonByTajAndRole(String taj, String roleName) {
        Role role = roleRepository.findByRoleName(roleName).orElseThrow(() -> new ResourceNotFoundException(roleName));
        return personRepository.findByTajAndRole(taj,role).orElseThrow(() -> new ResourceNotFoundException(taj + " " + roleName));
    }

    @Override
    public PatientDto getPatientByTaj(String taj){
        Patient patient = patientRepository.findByTaj(taj).orElseThrow(() -> new ResourceNotFoundException(taj));
        return personMapper.toPatientDto(patient);
    }

    @Override
    public List<DoctorDto> getAllDoctor(){
        return doctorRepository.findAll().stream().map(personMapper::toDoctorDto).toList();
    }

    @Override
    public Object addPerson(RegisterRequest request) {
        Role role = roleRepository.findByRoleName(request.getRoleName()).orElseThrow(() -> new ResourceNotFoundException(request.getRoleName()));
        String roleName = role.getRoleName();
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        return switch (roleName) {
            case "PATIENT" -> savePatient(request,role);
            case "ADMIN" -> saveAdmin(request,role);
            case "NURSE" -> saveNurse(request,role);
            case "ASSISTANT" -> saveAssistant(request,role);
            case "DOCTOR" -> saveDoctor(request,role);
            default -> throw new ResourceNotFoundException(roleName);
        };
    }

    @Override
    public DoctorDto setSection(String dTaj, String roleName, String sectionName){
        Role role = roleRepository.findByRoleName(roleName).orElseThrow(() -> new ResourceNotFoundException(roleName));
        Section section = sectionRepository.findBySectionName(sectionName).orElseThrow(() -> new ResourceNotFoundException(sectionName));
        Doctor doctor = doctorRepository.findByTajAndRole(dTaj,role).orElseThrow(() -> new ResourceNotFoundException(dTaj + " " + roleName));
        doctor.setSection(section);
        doctorRepository.save(doctor);
        return personMapper.toDoctorDto(doctor);
    }

    private PatientDto savePatient(RegisterRequest request, Role role) {
        if(patientRepository.existsByTaj(request.getTaj())) throw new AlreadyExistsException("TAJ");
        if (patientRepository.existsByEmail(request.getEmail())) throw new CollisionException("Email");
        Patient patient = new Patient();
        personMapper.updatePersonFromRequest(request, patient);
        patient.setRole(role);
        return personMapper.toPatientDto(patientRepository.save(patient));
    }

    private AdminDto saveAdmin(RegisterRequest request, Role role){
        if(adminRepository.existsByTaj(request.getTaj())) throw new AlreadyExistsException("TAJ");
        if (adminRepository.existsByEmail(request.getEmail())) throw new CollisionException("Email");
        Admin admin = new Admin();
        personMapper.updatePersonFromRequest(request,admin);
        admin.setRole(role);
        return personMapper.toAdminDto(adminRepository.save(admin));
    }

    private NurseDto saveNurse(RegisterRequest request, Role role){
        if (nurseRepository.existsByTaj(request.getTaj())) throw new AlreadyExistsException("TAJ");
        if (nurseRepository.existsByEmail(request.getEmail())) throw new CollisionException("Email");
        Nurse nurse = new Nurse();
        personMapper.updatePersonFromRequest(request,nurse);
        nurse.setRole(role);
        nurse.setWorkHoursStart(request.getWorkHoursStart());
        nurse.setWorkHoursEnd(request.getWorkHoursEnd());
        return personMapper.toNurseDto(nurseRepository.save(nurse));
    }

    private AssistantDto saveAssistant(RegisterRequest request,Role role){
        if (assistantRepository.existsByTaj(request.getTaj())) throw new AlreadyExistsException("TAJ");
        if (assistantRepository.existsByEmail(request.getEmail())) throw new CollisionException("Email");
        Assistant assistant = new Assistant();
        personMapper.updatePersonFromRequest(request,assistant);
        assistant.setRole(role);
        assistant.setWorkHoursStart(request.getWorkHoursStart());
        assistant.setWorkHoursEnd(request.getWorkHoursEnd());
        return personMapper.toAssistantDto(assistantRepository.save(assistant));
    }

    private DoctorDto saveDoctor(RegisterRequest request, Role role){
        if (doctorRepository.existsByTaj(request.getTaj())) throw new AlreadyExistsException("TAJ");
        if (doctorRepository.existsByEmail(request.getEmail())) throw new CollisionException("Email");
        Specialty spec = specialtyRepository.findBySpecialtyName(request.getSpecialtyName()).orElseThrow(() -> new ResourceNotFoundException(request.getSpecialtyName()));
        Doctor doctor = new Doctor();
        personMapper.updatePersonFromRequest(request,doctor);
        doctor.setRole(role);
        doctor.setSpecialty(spec);
        doctor.setWorkHoursStart(request.getWorkHoursStart());
        doctor.setWorkHoursEnd(request.getWorkHoursEnd());
        return personMapper.toDoctorDto(doctorRepository.save(doctor));
    }
}
