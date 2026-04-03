package com.hospital.backend.data;

import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.model.*;
import com.hospital.backend.repository.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final AssistantRepository assistantRepository;
    private final NurseRepository nurseRepository;
    private final PatientRepository patientRepository;

    private final RoleRepository roleRepository;
    private final SpecialtyRepository specialtyRepository;
    private final SectionRepository sectionRepository;
    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Override
    @Transactional
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {

        if (!"create".equals(ddlAuto)) {
            return;
        }

        createRoleIfNotFound("ADMIN");
        createRoleIfNotFound("DOCTOR");
        createRoleIfNotFound("ASSISTANT");
        createRoleIfNotFound("NURSE");
        createRoleIfNotFound("PATIENT");

        Specialty spec = createSpecialtyIfNotFound();
        Section sec = createSectionIfNotFound();

        if (roomRepository.count() == 0) {
            Room room101 = new Room();
            room101.setRoomNumber(101);
            room101 = roomRepository.save(room101);

            for (int i = 1; i <= 2; i++) {
                Bed bed = new Bed();
                bed.setBedNumber(i);
                bed.setRoom(room101);
                bedRepository.save(bed);
            }
        }

        if (adminRepository.count() == 0) {
            Admin admin = new Admin();
            admin.setTaj("000000000");
            admin.setFirstName("Ad");
            admin.setLastName("Min");
            admin.setEmail("admin@hospital.com");
            admin.setAge(40);
            admin.setPassword(passwordEncoder.encode(admin.getTaj() + "_" + admin.getFirstName()));
            admin.setRole(roleRepository.findByRoleName("ADMIN").orElseThrow(() -> new ResourceNotFoundException("Rolename")));
            adminRepository.save(admin);
        }

        if (doctorRepository.count() == 0) {
            Doctor doctor = new Doctor();
            doctor.setTaj("112112112");
            doctor.setFirstName("Dok");
            doctor.setLastName("Tor");
            doctor.setEmail("doctor@hospital.com");
            doctor.setAge(45);
            doctor.setPassword(passwordEncoder.encode(doctor.getTaj() + "_" + doctor.getFirstName()));
            doctor.setRole(roleRepository.findByRoleName("DOCTOR").orElseThrow(() -> new ResourceNotFoundException("Rolename")));
            doctor.setSpecialty(spec);
            doctor.setSection(sec);
            doctor.setWorkHoursStart(LocalTime.of(8, 0));
            doctor.setWorkHoursEnd(LocalTime.of(16, 0));
            doctorRepository.save(doctor);
        }

        if (assistantRepository.count() == 0) {
            for (int i = 1; i <= 2; i++) {
                Assistant assistant = new Assistant();
                assistant.setTaj("44444444" + i);
                assistant.setFirstName("Asz");
                assistant.setLastName("Szisztens " + i);
                assistant.setEmail("assistant" + i + "@hospital.com");
                assistant.setAge(28 + i);
                assistant.setPassword(passwordEncoder.encode(assistant.getTaj() + "_" + assistant.getFirstName()));
                assistant.setRole(roleRepository.findByRoleName("ASSISTANT").orElseThrow(() -> new ResourceNotFoundException("Rolename")));
                assistant.setWorkHoursStart(LocalTime.of(7, 0));
                assistant.setWorkHoursEnd(LocalTime.of(15, 0));
                assistantRepository.save(assistant);
            }
        }

        if (nurseRepository.count() == 0) {
            for (int i = 1; i <= 2; i++) {
                Nurse nurse = new Nurse();
                nurse.setTaj("55555555" + i);
                nurse.setFirstName("Áp");
                nurse.setLastName("Oló " + i);
                nurse.setEmail("nurse" + i + "@hospital.com");
                nurse.setAge(30 + i);
                nurse.setPassword(passwordEncoder.encode(nurse.getTaj() + "_" + nurse.getFirstName()));
                nurse.setRole(roleRepository.findByRoleName("NURSE").orElseThrow(() -> new ResourceNotFoundException("Rolename")));
                nurse.setWorkHoursStart(LocalTime.of(6, 0));
                nurse.setWorkHoursEnd(LocalTime.of(18, 0));
                nurseRepository.save(nurse);
            }
        }

        if (patientRepository.count() == 0) {
            for (int i = 1; i <= 2; i++) {
                Patient patient = new Patient();
                patient.setTaj("66666666" + i);
                patient.setFirstName("Be");
                patient.setLastName("Teg " + i);
                patient.setEmail("patient" + i + "@gmail.com");
                patient.setAge(20 + (i * 5));
                patient.setPassword(passwordEncoder.encode(patient.getTaj() + "_" + patient.getFirstName()));
                patient.setRole(roleRepository.findByRoleName("PATIENT").orElseThrow(() -> new ResourceNotFoundException("Rolename")));
                patientRepository.save(patient);
            }
        }

    }

    private void createRoleIfNotFound(String name) {
        if (roleRepository.findByRoleName(name).isEmpty()) {
            Role role = new Role();
            role.setRoleName(name);
            roleRepository.save(role);
        }
    }

    private Specialty createSpecialtyIfNotFound() {
        return specialtyRepository.findBySpecialtyName("Specialitás")
                .orElseGet(() -> {
                    Specialty s = new Specialty();
                    s.setSpecialtyName("Specialitás");
                    return specialtyRepository.save(s);
                });
    }

    private Section createSectionIfNotFound() {
        return sectionRepository.findBySectionName("Osztály")
                .orElseGet(() -> {
                    Section s = new Section();
                    s.setSectionName("Osztály");
                    return sectionRepository.save(s);
                });
    }
}