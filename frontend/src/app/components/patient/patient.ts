import { Component } from '@angular/core';
import { Timestamp, delay, of } from 'rxjs';
import { App } from '../../app';
import { AppService } from '../../app.service';
import { Router } from '@angular/router';
import { PatientService } from './patient.service';
import { FormsModule } from '@angular/forms';
import { CommonModule, DatePipe } from '@angular/common';
import { inject } from '@angular/core';
import Swal from 'sweetalert2';
import { DoctorModel } from '../doctor/doctor.model';
import { AppointmentModel } from '../appointment.model';

@Component({
  selector: 'app-patient',
  imports: [DatePipe, FormsModule, CommonModule],
  templateUrl: './patient.html',
  styleUrl: './patient.css',
})
export class Patient {

  sections: string[] = [];
  doctors: DoctorModel[] = [];
  section: string | null = null;
  doctor: DoctorModel | null = null;
  date: string | null = null;
  availableSlots: string[] = [];
  selectedSlot: AppointmentModel | null = null;
  appointments: AppointmentModel[] = [];
  today: string = '';
  missingDate: boolean = false;
  showProfile: boolean = false;
  statusFilter: string = '';
  appService = inject(AppService);
  selectedFiles: File[] = [];

  constructor(private patientService: PatientService, private router: Router) { }

  ngOnInit() {
    this.loadInitialData();
  }

  private loadInitialData() {
    this.today = new Date().toISOString().split('T')[0];
    this.patientService.getAllSections().subscribe({
      next: (response) => {
        this.sections = response.data.map((s: any) => s.sectionName);
      },
      error: (err) => {
        console.error('Error fetching sections:', err);
      }
    });
    this.patientService.getAllDoctors().subscribe({
      next: (response) => {
        this.doctors = response.data;
      },
      error: (err) => {
        console.error('Error fetching doctors:', err);
      }
    });
  }

  resetField(field: string) {
    if (field === 'date') this.date = null;
    if (field === 'section') this.section = null;
    if (field === 'doctor') this.doctor = null;
  }

  onSwitch() {
    if (this.showProfile) {
      this.loadPatientAppointments();
    } else {
      this.appointments = [];
    }
  }

  searchAppointments() {
    this.missingDate = false;
    if (this.date) {
      if (this.doctor) {
        this.patientService.getAppointmentsByDoctor(this.doctor.taj, this.date).subscribe({
          next: (response) => {
            this.appointments = this.appService.formatData(response.data).filter((a: AppointmentModel) => a.status === 'FREE');
          },
          error: (err) => {
            console.error('Error fetching appointments by doctor:', err);
          }
        });
      } else if (this.section) {
        this.patientService.getAppointmentsBySection(this.section, this.date).subscribe({
          next: (response) => {
            this.appointments = this.appService.formatData(response.data).filter((a: AppointmentModel) => a.status === 'FREE');
          },
          error: (err) => {
            console.error('Error fetching appointments by section:', err);
          }
        });
      } else {
        this.patientService.getAppointmentsByDate(this.date).subscribe({
          next: (response) => {
            this.appointments = this.appService.formatData(response.data).filter((a: AppointmentModel) => a.status === 'FREE');
          },
          error: (err) => {
            console.error('Error fetching appointments by date:', err);
          }
        });
      }
    } else {
      this.missingDate = true;
    }
  }

  setSelectedSlot(appointment: AppointmentModel) {
    this.selectedSlot = appointment;
  }

  postAppointment() {
    const hours = this.selectedSlot!.timeSlot.getHours().toString().padStart(2, '0');
    const minutes = this.selectedSlot!.timeSlot.getMinutes().toString().padStart(2, '0');
    const formattedTime = `${hours}:${minutes}:00`;
    const request: AppointmentRequest = {
      doctorTaj: this.selectedSlot!.doctorTaj,
      patientTaj: this.appService.getTaj()!,
      timeSlot: formattedTime,
      day: this.selectedSlot!.timeSlot.toISOString().split('T')[0],
      description: 'Beteg által lefoglalt időpont'
    }
    this.patientService.postAppointment(request).subscribe({
      next: (response) => {
        this.searchAppointments();
        this.appService.successPopup('Foglalás sikeres!');
      },
      error: (err) => {
        console.error('Error booking appointment:', err);
        this.appService.errorPopup('Hiba történt a foglalás során!');
      }
    });

  }

  

  private loadPatientAppointments() {
    this.patientService.getAppointmentsByPatient(this.appService.getTaj()!).subscribe({
      next: (response) => {
        this.appointments = this.appService.formatData(response.data);
      },
      error: (err) => {
        console.error('Error fetching patient appointments:', err);
      }
    });
  }

  searchDoctorNameByTaj(taj: string): string {
    const doctor = this.doctors.find(doc => doc.taj === taj);
    return doctor ? `${doctor.lastName} ${doctor.firstName}` : 'Ismeretlen orvos';
  }

  onFilesSelected(event: any) {
    const files: FileList = event.target.files;
    this.selectedFiles = Array.from(files);
  }

  onCancelAppointment(appointmentId: number) {
    this.appService.questionPopup('Biztosan le szeretné mondani a foglalást?').then((result) => {
      if (result.isConfirmed) {
        this.patientService.cancelAppointment(appointmentId).subscribe({
          next: (res) => {
            this.appService.successPopup('Foglalás sikeresen lemondva!');
            this.loadPatientAppointments();
          },
          error: (err) => {
            this.appService.errorPopup('Hiba történt a foglalás lemondása során!');
          }
        });
      }
    });
  }

  onUpload() {
    if (this.selectedFiles.length === 0) {
      this.appService.errorPopup('Nincs kiválasztva fájl!');
      return;
    }

    const pTaj = this.appService.getTaj()!;

    this.patientService.uploadToPatient(this.selectedFiles, pTaj).subscribe({
      next: (res) => {
        this.appService.successPopup('Fájlok sikeresen feltöltve!');
        this.selectedFiles = [];
      },
      error: (err) => {
        this.appService.errorPopup('Hiba történt a fájlok feltöltése során!');
        console.error(err);
      }
    });
  }


}
