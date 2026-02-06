package com.hospital.backend.service.person;

import com.hospital.backend.dto.PatientDto;
import com.hospital.backend.mapper.PatientMapper;
import com.hospital.backend.mapper.RoleMapper;
import com.hospital.backend.model.Patient;
import com.hospital.backend.repository.*;
import com.hospital.backend.request.RegistPatientRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonService implements IPersonService {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final NurseRepository nurseRepository;
    private final AssistantRepository assistantRepository;
    private final AdminRepository adminRepository;
    private final PatientMapper patientMapper;
    private final RoleMapper roleMapper;


    @Override
    public PatientDto addPatient(RegistPatientRequest patientRequest){
        Patient patient = registToPatient(patientRequest);
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.toDto(savedPatient);
    }

    @Override
    public void deletePatient(String taj){
        Patient patient = patientRepository.findByTaj(taj).orElseThrow(() -> new RuntimeException("Patient"));
        patientRepository.delete(patient);
    }

    private Patient registToPatient(RegistPatientRequest patientRequest){
        Patient patient = new Patient();
        patient.setTaj(patientRequest.getTaj());
        patient.setFirstName(patientRequest.getFirstName());
        patient.setLastName(patientRequest.getLastName());
        patient.setAge(patientRequest.getAge());
        patient.setEmail(patientRequest.getEmail());
        patient.setPassword(patient.getPassword());
        patient.setRole(roleMapper.toEntity(patientRequest.getRole()));
        return patient;
    }
}
