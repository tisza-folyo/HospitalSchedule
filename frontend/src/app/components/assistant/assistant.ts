import { Component, inject } from '@angular/core';
import { AssistantService } from './assistant.service';
import { AppService } from '../../app.service';
import { PatientModel } from '../patient/patient.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DoctorModel } from '../doctor/doctor.model';
import { WorkModel } from '../work.model';
import { AppointmentModel } from '../appointment.model';
import { AssistantRequest } from './assistant.requrest';
import { AssistantModel } from './assistant.model';

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
  appoints: AppointmentModel[] = [];
  selectedSlot: AppointmentModel | null = null;
  selectedFiles: File[] = [];
  work : WorkModel | null = null;
  works: WorkModel[] = [];
  assistants: AssistantModel[] = [];
  assistant: AssistantModel | null = null;

  appoId: number | null = null;
  filter: string= '';
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
                this.selectPatient(this.patient!,true);
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
    }else if(field = 'date-to-date'){
      this.date = null;
      this.endDate = '';
      this.startDate = '';
      this.works = [];
      this.work = null;
      this.assistants = [];
      this.assistant = null;
    }
  }


  onWorkSearch(){
    this.assistantService.getAllWorksBetween(this.appService.getTaj()!, this.startDate, this.endDate).subscribe({
      next: (response) => {
        this.works = response.data;
        console.log(response.data);
        
      },
      error: (err) => {
        console.error('Error fetching', err);
      }
    });
  }

  selectWork(w: WorkModel){
    if(w.workId == this.work?.workId){
      this.work = null;
    }else{
      this.work = w;
      this.loadOwnWorks(w.workDay);
    }
  }



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
      this.selectedSlot = null;
      this.patient = null;
      this.appoints = [];
      this.appoId = null;
      this.searchAppointments();
    },
    error: (err) => this.appService.errorPopup('Hiba történt!')
  });

  
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
    this.isBookingMode = false;
  }

  navigatToSchedule(){
    this.date = null;
    this.work = null;
    this.works = [];
    this.isBookingMode = false;
    this.endDate = '';
    this.startDate = '';
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

  switchBetweenWorks(field : string){
    if(field == 'own'){
      this.isBookingMode = false;
    }else if(field == 'other'){
      this.isBookingMode = true;
      this.loadAvaliableWorks();
    }
  }

  assign(w: WorkModel) {
    this.updateAssistant(w.doctor!.taj, this.appService.getTaj()!, w.workDay, this.appService.getTaj()!,"assign");
  }

  changeAssistant(w: WorkModel, a: AssistantModel){
    this.updateAssistant(w.doctor!.taj, a!.taj, w.workDay, this.appService.getTaj()!,"replace");
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

  private loadAvaliableWorks(){
    this.works = [];
    this.assistantService.getAllWorksAfterDay(this.today).subscribe({
      next: (response) => {
        this.works = response.data;
      },
      error: (err) => {
        console.error('Error fetching', err);
      }
    });
  }

  private loadOwnWorks(day:string){
    this.assistantService.getAllFreeAssistants(day).subscribe({
      next: (response) => {
        this.assistants = response.data;
      },
      error: (err) => {
        console.error('Error fetching', err);
      }
    });
  }

  private updateAssistant(dTaj: string, aTaj: string, day: string, uTaj: string, mode: string) {
    const req: AssistantRequest = {
      dTaj: dTaj,
      aTaj: aTaj,
      day: day,
      uTaj: uTaj
    };

    if (mode == 'assign') {
      this.assistantService.updateAssistant(req).subscribe({
        next: (response) => {
          this.appService.successPopup("Siker!");
          this.loadAvaliableWorks();
        },
        error: (err) => {
          this.appService.errorPopup("Hiba!");
          console.log(err.error.message);

        }
      });
    }else if(mode == 'replace'){
      this.assistantService.replaceAssistant(req).subscribe({
        next: (response) => {
          this.appService.successPopup("Siker!");
          this.loadOwnWorks(req.day);
          this.work = null;
        },
        error: (err) => {
          this.appService.errorPopup("Hiba!");
          console.log(err.error.message);

        }
      });
    }
  }
}
