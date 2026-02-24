import { Component, inject } from '@angular/core';
import { DoctorService } from './doctor.service';
import { AppService } from '../../app.service';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FileModel } from '../file.model';
import { environment } from '../../../environments/environment';
import { PatientModel } from '../patient/patient.model';
import { AssistantModel } from '../assistant/assistant.model';
import { Assistant } from '../assistant/assistant';
import { AssistantRequest } from '../assistant/assistant.requrest';
import { WorkModel } from '../work.model';
import { firstValueFrom } from 'rxjs';
import { AppointmentModel } from '../appointment.model';

@Component({
  selector: 'app-doctor',
  imports: [DatePipe, FormsModule, CommonModule],
  templateUrl: './doctor.html',
  styleUrl: './doctor.css',
})
export class Doctor {
  appService = inject(AppService);
  appointments: AppointmentModel[] = [];
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
  assistants: AssistantModel[] = [];
  filteredAssistants: AssistantModel[] = [];
  assistant: Assistant | null = null;
  isFilterSubmitted: boolean = false;
  today: string = '';
  works: WorkModel[] = [];
  startDate: string = '';
  endDate: string = '';

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

  navigateToAppointments() {
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
    if (!this.filter) {
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
    if (field === 'date-to-date') {
      this.startDate = '';
      this.endDate = '';
      this.isFilterSubmitted = false;
      this.works = [];
    }

  }

  navigateToPatients() {
    this.patient = null;
  }



  filterPatients() {
    if (!this.filter) {
      this.filteredPatients = this.patients;
      return;
    }
    this.filteredPatients = this.patients.filter((p: any) => {
      const fullName = this.appService.nameConcatenator(p.firstName, p.lastName).toLowerCase();
      return fullName.includes(this.filter.toLowerCase()) || p.taj.toString().toLowerCase().includes(this.filter.toLowerCase());
    });
  }

  selectAppointment(appointment: AppointmentModel) {
    if (appointment.patientTaj) {
      this.getPatientByTaj(appointment.patientTaj);
    }

    this.description = appointment.description || '';
    this.files = appointment.files || [];
  }

  

  async onWorkSearch() {
    this.isFilterSubmitted = true;
    if (!this.startDate || !this.endDate) return;

    this.works = [];
    let current = new Date(this.startDate);
    const last = new Date(this.endDate);

    while (current <= last) {
      const dateForRequest = current.toISOString().split('T')[0];
      try {
        const response = await firstValueFrom(
          this.doctorService.getWorksByDoctorAndDate(this.appService.getTaj()!, dateForRequest)
        );
        if (response.data === null) {
          this.works.push({
            workId: null,
            workDay: dateForRequest,
            uTaj: this.appService.getTaj()!,
            doctor: null,
            assistant: null
          });
        } else { 
          this.works.push(response.data);
        }
      } catch (err) {
        console.error('Hiba:', err);
      }
      current.setDate(current.getDate() + 1);
    }
    this.works.sort((a, b) => new Date(a.workDay).getTime() - new Date(b.workDay).getTime());
  }

  addWork(day: string) {
    this.doctorService.postWork(day, this.appService.getTaj()!, this.appService.getTaj()!).subscribe({
      next: (response) => {
        this.appService.successPopup("Munka hozzáadva");
        this.onWorkSearch();
        console.log(response.data);
      },
      error: (err) => {
        this.appService.errorPopup("Hiba");
      }
    });
  }

  removeWork(workId: number) {
    if (workId === null) {
      this.appService.errorPopup("Hiba: Nincs ilyen munka");
      return;
    }
    this.appService.questionPopup("Biztosan le akarod adni ezt a munkát?").then((result) => {
      if (result.isConfirmed) {
        this.doctorService.deleteWork(workId).subscribe({
          next: (response) => {
            this.appService.successPopup("Munka törölve");
            this.onWorkSearch();
          },
          error: (err) => {
            this.appService.errorPopup("Hiba");
          }
        });
      }
    });  
  }


  get maxEndDate(): string {
    if (!this.startDate) return '';
    const date = new Date(this.startDate);
    date.setDate(date.getDate() + 7);
    return date.toISOString().split('T')[0];
  }

  get minStartDate(): string {
    if (!this.endDate) return '';
    const date = new Date(this.endDate);
    date.setDate(date.getDate() - 7);
    return date.toISOString().split('T')[0];
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
