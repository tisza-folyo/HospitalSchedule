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

@Component({
  selector: 'app-patient',
  imports: [DatePipe ,FormsModule, CommonModule],
  templateUrl: './patient.html',
  styleUrl: './patient.css',
})
export class Patient {

  sections: string[] = [];
  doctors: Doctor[] = [];
  section: string | null = null;
  doctor: Doctor | null = null;
  date: string | null = null;
  availableSlots: string[] = [];
  selectedSlot: Appointment | null = null;
  appointments: Appointment[] = [];
  today: string = '';
  missingDate: boolean = false;
  showProfile: boolean = false;
  statusFilter: string = '';
  appService = inject(AppService);
  selectedFiles: File[] = [];

  constructor(private patientService: PatientService, private router: Router) {}

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
    this.patientService.getAppointmentsByPatient(this.appService.getTaj()!).subscribe({
      next: (response) => {
        this.appointments = this.formatData(response.data);
        console.log(this.appointments);
      },
      error: (err) => {
        console.error('Error fetching patient appointments:', err);
      }
    });
  }

  searchAppointments() {
    this.missingDate = false;
    if (this.date) {
      if (this.doctor) {
        this.patientService.getAppointmentsByDoctor(this.doctor.taj, this.date).subscribe({
          next: (response) => {
            this.appointments = this.formatData(response.data).filter((a: Appointment) => a.status === 'FREE');
          },
          error: (err) => {
            console.error('Error fetching appointments by doctor:', err);
          }
        });
      } else if (this.section) {
        this.patientService.getAppointmentsBySection(this.section, this.date).subscribe({
          next: (response) => {
            this.appointments = this.formatData(response.data);
          },
          error: (err) => {
            console.error('Error fetching appointments by section:', err);
          }
        });
      } else {
        this.patientService.getAppointmentsByDate(this.date).subscribe({
          next: (response) => {
            this.appointments = this.formatData(response.data);
          },
          error: (err) => {
            console.error('Error fetching appointments by date:', err);
          }
        });
      }
    }else {
      this.missingDate = true;
    }
  }

  setSelectedSlot(appointment: Appointment) {
    this.selectedSlot = appointment;
  }

  postAppointment() {
    const hours = this.selectedSlot!.timeSlot.getHours().toString().padStart(2, '0');
    const minutes = this.selectedSlot!.timeSlot.getMinutes().toString().padStart(2, '0');
    const formattedTime = `${hours}:${minutes}:00`;
    const request : AppointmentRequest = {
      doctorTaj: this.selectedSlot!.doctorTaj,
      patientTaj: this.appService.getTaj()!,
      timeSlot: formattedTime,
      day: this.selectedSlot!.timeSlot.toISOString().split('T')[0],
      description: 'Beteg által lefoglalt időpont'
    }
    this.patientService.postAppointment(request).subscribe({
      next: (response) => {
        this.searchAppointments();
        this.successPopup();
      },
      error: (err) => {
        console.error('Error booking appointment:', err);
        this.errorPopup();
      }
    });

  }

  private formatData(data: any): Appointment[] {
    return data.map((item: any) => ({
    timeSlot: new Date(`${item.day}T${item.timeSlot}`),
    doctorTaj: item.doctor?.taj, 
    patientTaj: item.patient?.taj,
    description: item.description,
    status: item.status
  }));
  }

  searchDoctorNameByTaj(taj: string): string  {
    const doctor = this.doctors.find(doc => doc.taj === taj);
    return doctor ? `${doctor.lastName} ${doctor.firstName}` : 'Ismeretlen orvos';
  }

  onFilesSelected(event: any) {
  const files: FileList = event.target.files;
  this.selectedFiles = Array.from(files);
}

onUpload() {
  if (this.selectedFiles.length === 0) {
    this.errorPopup();
    return;
  }

  const pTaj = this.appService.getTaj()!;

  this.patientService.uploadToPatient(this.selectedFiles, pTaj).subscribe({
    next: (res) => {
      this.successPopup();
      this.selectedFiles = []; 
    },
    error: (err) => {
      this.errorPopup();
      console.error(err);
    }
  });
}

  private successPopup() {
    Swal.fire({
        title: 'Sikeres foglalás!',
        icon: 'success',
        background: '#f8f9fa',
        confirmButtonColor: '#0d6efd',
        confirmButtonText: 'Szuper!',
        timerProgressBar: true
      });
  }

  private errorPopup() {
    Swal.fire({
        title: 'Hiba történt!',
        icon: 'error',
        confirmButtonColor: '#dc3545',
        confirmButtonText: 'Értem'
      });
  }

  
}
