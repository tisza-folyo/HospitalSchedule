package com.hospital.backend.service.appointment;

import com.hospital.backend.dto.AppointmentDto;
import com.hospital.backend.model.Doctor;
import com.hospital.backend.model.Section;
import com.hospital.backend.model.Status;
import com.hospital.backend.request.AppointmentRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IAppointmentService {

    AppointmentDto addAppointment(AppointmentRequest request);

    AppointmentDto addAppointment(AppointmentDto appointmentDto);

    List<AppointmentDto> getAllAppointments();

    List<AppointmentDto> getAllAppointmentsByDoctor(String dTaj);

    List<AppointmentDto> getAllAppointmentByDay(LocalDate day);

    List<AppointmentDto> getAllAppointmentsByPatient(String pTaj);

    void deleteAppointment(Long id);

    AppointmentDto updateAppointmentStatus(Long id, Status status);

    AppointmentDto updateAppointmentSymptomDescription(Long id, String description);

    List<AppointmentDto> getAllAndFreeAppointments(String dTaj, LocalDate day);

    List<AppointmentDto> getAllAndFreeAppointments(LocalDate day);

    List<AppointmentDto> getAllAndFreeAppointments(LocalDate day, String sectionName);
}
