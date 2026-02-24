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
  selectedSlot: AppointmentModel | null = null;
  selectedFiles: File[] = [];

  appoId: number | null = null;
  filter: string= '';
  dateFilter: string = '';
  today: string = '';
  isFilterSubmitted: boolean = false;
  startDate: string = '';
  endDate: string = '';
  isBookingMode: boolean = false;
  date: string | null = null;
  missingDate: boolean = false;
  section: string = '';
  sections: string[] = [];

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
    this.selectedSlot = null;
    this.date = null;
    this.section = '';
    this.doctor = null;
    if(this.isBookingMode){
      this.appoints = [];
    }
    if(req && !this.isBookingMode){
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
  async updateFile(a: AppointmentModel) {
    const result = await this.appService.filePopUp('Válasszon fájlokat');
    if (result.isConfirmed && result.value) {
      const files: FileList = result.value;
      this.selectedFiles = Array.from(files);
      this.assistantService.uploadToAppointment(this.selectedFiles,a.appointmentId).subscribe({
            next: (res) => {
                this.appService.successPopup('Sikeres feltöltés!');
                this.searchAppointments();
            },
            error: (err) => this.appService.errorPopup('Hiba a feltöltésnél!')
        });
    }
  }

  selectAppo(a: AppointmentModel){
    if(this.appoId === a.appointmentId){
      this.appoId = null;
    }else{
      this.appoId = a.appointmentId;
    }
  }

  resetField(field:String){
    if(field === 'book'){
      this.date = null;
      this.section = '';
      this.doctor = null;
    }
  }

  onDoctorSearch(){}

  filterDoctors(){}

  selectDoctor(d: DoctorModel){}

  onWorkSearch(){}

  changeAssistant(w: WorkModel){}

  searchAppointments(){
    this.missingDate = false;
    if (this.date) {
      if (this.doctor) {
        this.assistantService.getAppointmentsByDoctor(this.doctor.taj, this.date).subscribe({
          next: (response) => {
            this.appoints = this.appService.formatData(response.data).filter((a: AppointmentModel) => a.status === 'FREE');
          },
          error: (err) => {
            console.error('Error fetching appointments by doctor:', err);
          }
        });
      } else if (this.section) {
        this.assistantService.getAppointmentsBySection(this.section, this.date).subscribe({
          next: (response) => {
            this.appoints = this.appService.formatData(response.data).filter((a: AppointmentModel) => a.status === 'FREE');
          },
          error: (err) => {
            console.error('Error fetching appointments by section:', err);
          }
        });
      } else {
        this.assistantService.getAppointmentsByDate(this.date).subscribe({
          next: (response) => {
            this.appoints = this.appService.formatData(response.data).filter((a: AppointmentModel) => a.status === 'FREE');
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

  setSelectedSlot(s: AppointmentModel){
    this.selectedSlot = s;
  }

  searchDoctorNameByTaj(taj: string){
    const doctor = this.doctors.find(doc => doc.taj === taj);
    return doctor ? `${doctor.lastName} ${doctor.firstName}` : 'Ismeretlen orvos';
  }

  async postAppointment() {
  const textResult = await this.appService.inputPopUp(
    'Időpont foglalás', 
    'Panasz leírása', 
    'Írja le a tüneteket...'
  );

  if (!textResult.isConfirmed) return;

  const description = textResult.value;
  const hours = this.selectedSlot!.timeSlot.getHours().toString().padStart(2, '0');
  const minutes = this.selectedSlot!.timeSlot.getMinutes().toString().padStart(2, '0');
  const formattedTime = `${hours}:${minutes}:00`;

  const request: AppointmentRequest = {
    doctorTaj: this.selectedSlot!.doctorTaj,
    patientTaj: this.patient?.taj!,
    timeSlot: formattedTime, 
    day: this.selectedSlot!.timeSlot.toISOString().split('T')[0],
    description: description
  };

  this.assistantService.postAppointment(request).subscribe({
    next: () => {
      this.appService.successPopup('Foglalás sikeres!');
      this.isBookingMode = false;
      this.searchAppointments();
    },
    error: (err) => this.appService.errorPopup('Hiba történt!')
  });

  this.selectedSlot = null;
}

  navigateToAppoints(){
    this.filter = '';
    this.patient = null;
    this.appoints = [];
    this.appoId = null;
    this.selectedSlot = null;
    this.date = null;
    this.section = '';
    this.doctor = null;
  }

  navigateToPatient(){
    this.filter = '';
    this.patient = null;
  }

  switchToBooking(){
    if(this.isBookingMode){
      this.isBookingMode = false;
      this.patient = null;
      this.section = '';
      this.doctor = null;
      this.navigateToAppoints();
    }else{
      this.isBookingMode = true;
      this.appoints = [];
      this.assistantService.getAllDoctors().subscribe({
          next: (response) => {
            this.doctors = response.data;
          },
          error: (err) => {
            console.error('Error fetching', err);
          }
        });
        this.assistantService.getAllSections().subscribe({
          next: (response) => {
            this.sections = response.data.map((s: any) => s.sectionName);
          },
          error: (err) => {
            console.error('Error fetching', err);
          }
        });
    }

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
