import { Component, inject } from '@angular/core';
import { DoctorService } from './doctor.service';
import { AppService } from '../../app.service';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Appointment } from '../appointment.model';
import { FileModel } from '../file.model';
import { environment } from '../../../environments/environment';
import { PatientModel } from '../patient/patient.model';
import { AssistantModel } from '../assistant/assistant.model';
import { Assistant } from '../assistant/assistant';
import { AssistantRequest } from '../assistant/assistant.requrest';

@Component({
  selector: 'app-doctor',
  imports: [DatePipe, FormsModule, CommonModule],
  templateUrl: './doctor.html',
  styleUrl: './doctor.css',
})
export class Doctor {
  appService = inject(AppService);
  appointments: Appointment[] = [];
  patient: PatientModel | null = null;
  description: string = '';
  statusFilter: string = '';
  files: FileModel[] = [];
  baseUrl = environment.apiUrl.replace('/hospital', '');
  patients: PatientModel[] = [];
  filter: string = '';
  filteredPatients: PatientModel[] = [];
  assistantName: string = '';
  assistantTaj: string = '';
  dateFilter: string = '';
  assistants:AssistantModel[] = [];
  filteredAssistants: AssistantModel[] = [];
  assistant: Assistant | null = null;
  isFilterSubmitted: boolean = false;
  today: string = '';

  constructor(private doctorService: DoctorService) { }

  ngOnInit() {
    this.today = new Date().toISOString().split('T')[0];
     this.doctorService.getAllPatients(this.appService.getTaj()!).subscribe({
      next: (response) => {
        this.patients = response.data;
        this.filteredPatients = this.patients;
      },
      error: (err) => {
        console.error('Error fetching patients:', err);
      }
    });
  }
  selectPatient(p: PatientModel) {
    this.patient = p;
  }

  navigateToAppointments(){
      this.doctorService.getAppointmentsByDoctor(this.appService.getTaj()!).subscribe({
        next: (response) => {
          this.appointments = this.appService.formatData(response.data);
        },
        error: (err) => {
          console.error('Error fetching appointments:', err);
        }
      });
  }

  navigateToAssistant() {
    this.assistant = null;
    this.assistants = [];
    this.filteredAssistants = [];
  }

  onAssistantSearch() {
    this.isFilterSubmitted = true;

    if (!this.dateFilter) {
      return;
    }

    this.doctorService.getFreeAssistants(this.dateFilter).subscribe({
      next: (response) => {
        this.assistants = response.data;
        this.filteredAssistants = this.assistants;
      },
      error: (err) => {
        console.error('Error fetching assistants:', err);
      }
    });
  }

  filterAssistants() {
    if(!this.filter) {
      this.filteredAssistants = this.assistants;
      return;
    }
    this.filteredAssistants = this.assistants.filter((a: any) => {
      const fullName = this.appService.nameConcatenator(a.firstName, a.lastName).toLowerCase();
      return fullName.includes(this.filter.toLowerCase()) || a.taj.toString().toLowerCase().includes(this.filter.toLowerCase());
    });
  }

  selectAssistant(a: AssistantModel) {
    const request: AssistantRequest = {
      dTaj: this.appService.getTaj()!,
      aTaj: a.taj,
      day: this.dateFilter,
      uTaj: this.appService.getTaj()!,
    };
    this.doctorService.assignAssistant(request).subscribe({
      next: (response) => {
        this.appService.successPopup("Siker");
        this.resetField('date');
      },
      error: (err) => {
        this.appService.errorPopup("Hiba");
      }
    });
  }

  resetField(field: string) {
    if (field === 'date') {
    this.dateFilter = '';
    this.isFilterSubmitted = false;
    this.filteredAssistants = [];
  }

  }

  navigateToPatients() {
    this.patient = null;
  }



  filterPatients(){
    if (!this.filter) {
      this.filteredPatients = this.patients;
      return;
    }
    this.filteredPatients = this.patients.filter((p: any) => {
      const fullName = this.appService.nameConcatenator(p.firstName, p.lastName).toLowerCase();
      return fullName.includes(this.filter.toLowerCase()) || p.taj.toString().toLowerCase().includes(this.filter.toLowerCase());
    });
  }

  selectAppointment(appointment: Appointment) {
    if (appointment.patientTaj) {
      this.getPatientByTaj(appointment.patientTaj);
    }
    
    this.description = appointment.description || '';
    this.files = appointment.files || [];
  }

  downloadPdf(file: any) {
    const byteCharacters = atob(file.base64Data);
    const byteNumbers = new Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);

    const blob = new Blob([byteArray], { type: 'application/pdf' });

    const fileURL = URL.createObjectURL(blob);
    window.open(fileURL, '_blank');
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
