import { Component, inject } from '@angular/core';
import { DoctorService } from './doctor.service';
import { AppService } from '../../app.service';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Appointment } from '../appointment.model';
import { FileModel } from '../file.model';
import { SafeUrlPipe } from '../safeUrlPipe';

@Component({
  selector: 'app-doctor',
  imports: [DatePipe, SafeUrlPipe, FormsModule, CommonModule],
  templateUrl: './doctor.html',
  styleUrl: './doctor.css',
})
export class Doctor {
  appService = inject(AppService);
  appointments: Appointment[] = [];
  patient: Patient | null = null;
  description: string = '';
  statusFilter: string = '';
  files: FileModel[] = [];

  constructor(private doctorService: DoctorService) { }

  navigateToAppointments(){
      this.doctorService.getAppointmentsByDoctor(this.appService.getTaj()!).subscribe({
        next: (response) => {
          console.log(response.data);
          
          this.appointments = this.appService.formatData(response.data);
        },
        error: (err) => {
          console.error('Error fetching appointments:', err);
        }
      });
  }

  selectAppointment(appointment: Appointment) {
    if (appointment.patientTaj) {
      this.getPatientByTaj(appointment.patientTaj);
    }
    
    this.description = appointment.description || '';
    this.files = appointment.files || [];
  }

  private getPatientByTaj(taj: string) {
    this.doctorService.getPatientByTaj(taj).subscribe({
      next: (response) => {
        this.patient = response.data;
      },
      error: (err) => {
        console.error('Error fetching patient:', err);
      }
    });
  }
}
