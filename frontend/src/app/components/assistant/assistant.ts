import { Component, inject } from '@angular/core';
import { AssistantService } from './assistant.service';
import { AppService } from '../../app.service';
import { PatientModel } from '../patient/patient.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DoctorModel } from '../doctor/doctor.model';
import { WorkModel } from '../work.model';
import { AppointmentModel } from '../appointment.model';

@Component({
  selector: 'app-assistant',
  imports: [FormsModule,CommonModule],
  templateUrl: './assistant.html',
  styleUrl: './assistant.css',
})
export class Assistant {
  appService = inject(AppService);
  patients: PatientModel[] = [];
  filteredPatients: PatientModel[] = [];
  patient: PatientModel | null = null;
  doctors: DoctorModel[] = [];
  filteredDoctors: DoctorModel[] = [];
  doctor:DoctorModel | null = null;
  works: WorkModel[] = [];
  appoints: AppointmentModel[] = [];
  appoId: number | null = null;
  filter: string= '';
  dateFilter: string = '';
  today: string = '';
  isFilterSubmitted: boolean = false;
  startDate: string = '';
  endDate: string = '';

  constructor(private assistantService: AssistantService){}

  ngOnInit(){
    this.today = new Date().toISOString().split('T')[0];
    this.assistantService.getAllPatients(this.appService.getTaj()!).subscribe({
      next: (response) => {
        this.patients = response.data;
        this.filteredPatients = this.patients;
      },
      error: (err) => {
        console.error('Error fetching patients:', err);
      }
    });
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

  selectPatient(p: PatientModel, req: boolean){
    this.patient = p;
    if(req){
      this.assistantService.getAppointmentsByPatient(p.taj).subscribe({
          next: (response) => {
            this.appoints = this.appService.formatData(response.data);
          },
          error: (err) => {
            console.error('Error fetching appointments', err);
          }
        });
    }
  }

  selectAppo(a: AppointmentModel){
    if(this.appoId === a.id){
      this.appoId = null;
    }else{
      this.appoId = a.id;
    }
  }

  resetField(field:String){}

  onDoctorSearch(){}

  filterDoctors(){}

  selectDoctor(d: DoctorModel){}

  onWorkSearch(){}

  changeAssistant(w: WorkModel){}

  navigateToAppoints(){
    this.filter = '';
    this.patient = null;
    this.appoints = [];
    this.appoId = null;
  }

  navigateToPatient(){
    this.filter = '';
    this.patient = null;
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
}
