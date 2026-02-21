package com.hospital.backend.service.appointment;

import com.hospital.backend.dto.AppointmentDto;
import com.hospital.backend.exception.CollisionException;
import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.mapper.AppointmentMapper;
import com.hospital.backend.model.*;
import com.hospital.backend.repository.*;
import com.hospital.backend.request.AppointmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService implements IAppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final SectionRepository sectionRepository;
    private final DoctorRepository doctorRepository;
    private final RoleRepository roleRepository;
    private final PatientRepository patientRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    public AppointmentDto addAppointment(AppointmentRequest request){
        Doctor doctor = getDoctorByTaj(request.getDoctorTaj());
        Patient patient = getPatientByTaj(request.getPatientTaj());
        if(!isAppointmentPlaceable(doctor, request.getDay(), request.getTimeSlot())) throw new CollisionException("Appointment");
        Appointment appointment = createAppointment(doctor, patient, request.getDay(), request.getTimeSlot(), request.getDescription(), Status.LOCKED);
        appointmentRepository.save(appointment);
        return appointmentMapper.toDto(appointment);
    }


    @Override
    public AppointmentDto addAppointment(AppointmentDto appointmentDto){
        Appointment appointment = appointmentMapper.toEntity(appointmentDto);
        appointment.setStatus(Status.LOCKED);
        return appointmentMapper.toDto(appointmentRepository.save(appointment));
    }

    @Override
    public List<AppointmentDto> getAllAppointments(){
        return appointmentMapper.toDtoList(appointmentRepository.findAll());
    }

    @Override
    public List<AppointmentDto> getAllAppointmentsByDoctor(String dTaj){
        Doctor doctor = getDoctorByTaj(dTaj);
        return appointmentMapper.toDtoList(appointmentRepository.findAllByDoctor(doctor));
    }

    @Override
    public List<AppointmentDto> getAllAppointmentByDay(LocalDate day){
        return appointmentMapper.toDtoList(appointmentRepository.findAllByDay(day));
    }

    @Override
    public List<AppointmentDto> getAllAppointmentsByPatient(String pTaj){
        Patient patient = getPatientByTaj(pTaj);
        return appointmentMapper.toDtoList(appointmentRepository.findAllByPatient(patient));
    }

    @Override
    public void deleteAppointment(Long id){
        if(!appointmentRepository.existsById(id)) throw new ResourceNotFoundException("Appointment");
        appointmentRepository.deleteById(id);
    }

    @Override
    public AppointmentDto updateAppointmentStatus(Long id, Status status){
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Appointment"));
        appointment.setStatus(status);
        return appointmentMapper.toDto(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentDto updateAppointmentSymptomDescription(Long id, String description){
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Appointment"));
        appointment.setSymptomDescription(description);
        return appointmentMapper.toDto(appointmentRepository.save(appointment));
    }


    @Override
    public List<AppointmentDto> getAllAndFreeAppointments(String dTaj, LocalDate day){
        if(isWeekend(day)) return appointmentMapper.toDtoList(new ArrayList<>());
        Doctor doctor = getDoctorByTaj(dTaj);
        List<Appointment> appointments = appointmentRepository.findAllByDoctorAndDay(doctor, day);
        List<LocalTime> times = appointments.stream().map(Appointment::getTimeSlot).toList();
        List<Appointment> freeAppointments = new ArrayList<>();
        if (doctor.getWorkHoursStart() == null || doctor.getWorkHoursEnd() == null) return appointmentMapper.toDtoList(freeAppointments);
        for (LocalTime t = doctor.getWorkHoursStart(); t.isBefore(doctor.getWorkHoursEnd()); t = t.plusMinutes(doctor.getSpecialty().getTreatmentTimeInMinutes())) {
            if (!times.contains(t)) freeAppointments.add(createAppointment(doctor, null, day, t, null, Status.FREE));
        }
        appointments.addAll(freeAppointments);
        return appointmentMapper.toDtoList(appointments);
    }

    @Override
    public List<AppointmentDto> getAllAndFreeAppointments(LocalDate day){
        if(isWeekend(day)) return appointmentMapper.toDtoList(new ArrayList<>());
        List<AppointmentDto> freeAppointments = new ArrayList<>();
        for (Doctor doctor : doctorRepository.findAll()) freeAppointments.addAll(getAllAndFreeAppointments(doctor.getTaj(), day));
        return freeAppointments;
    }

    @Override
    public List<AppointmentDto> getAllAndFreeAppointments(LocalDate day, String sectionName){
        if(isWeekend(day)) return appointmentMapper.toDtoList(new ArrayList<>());
        Section section = sectionRepository.findBySectionName(sectionName).orElseThrow(() -> new ResourceNotFoundException(sectionName));
        List<AppointmentDto> appointments = new ArrayList<>();
        List<Doctor> doctors = doctorRepository.findAllBySection(section);
        for (Doctor d: doctors){
            appointments.addAll(getAllAndFreeAppointments(d.getTaj(), day));
        }
        return appointments;
    }

    private boolean isAppointmentPlaceable(Doctor doctor, LocalDate day, LocalTime timeSlot){
        if (timeSlot.isBefore(doctor.getWorkHoursStart()) || timeSlot.isAfter(doctor.getWorkHoursEnd().minusMinutes(doctor.getSpecialty().getTreatmentTimeInMinutes())) || isWeekend(day)) return false;
        return !appointmentRepository.existsByDoctorAndDayAndTimeSlot(doctor, day, timeSlot);
    }

    private Appointment createAppointment(Doctor doctor, Patient patient, LocalDate day, LocalTime timeSlot, String description, Status status){
        Appointment appointment = new Appointment();
        appointment.setDay(day);
        appointment.setTimeSlot(timeSlot);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStatus(status);
        appointment.setSymptomDescription(description);
        appointment.setSymptomImg(new ArrayList<>());
        return appointment;
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    private Doctor getDoctorByTaj(String dTaj){
        return doctorRepository.findByTaj(dTaj).orElseThrow(() -> new ResourceNotFoundException("Doctor"));
    }

    private Patient getPatientByTaj(String pTaj){
        return patientRepository.findByTaj(pTaj).orElseThrow(() -> new ResourceNotFoundException("Patient"));
    }

}
