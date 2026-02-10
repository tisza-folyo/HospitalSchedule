package com.hospital.backend.repository;

import com.hospital.backend.model.Appointment;
import com.hospital.backend.model.Doctor;
import com.hospital.backend.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    Appointment findByDoctorAndDay(Doctor doctor, LocalDate day);

    List<Appointment> findAllByDoctorAndDay(Doctor doctor, LocalDate day);

    boolean existsByDoctorAndDayAndTimeSlot(Doctor doctor, LocalDate day, LocalTime timeSlot);

    List<Appointment> findAllByDoctor(Doctor doctor);

    List<Appointment> findAllByDay(LocalDate day);

    List<Appointment> findAllByPatient(Patient patient);
}
