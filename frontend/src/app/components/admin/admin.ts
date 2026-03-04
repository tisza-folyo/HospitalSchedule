import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { AdminService } from './admin.service';
import { AppService } from '../../app.service';
import { WorkModel } from '../work.model';
import { CareModel } from '../care.model';
import { RegistrationRequest } from '../registration.request.model';

@Component({
  selector: 'app-admin',
  imports: [FormsModule,CommonModule],
  templateUrl: './admin.html',
  styleUrl: './admin.css',
})
export class Admin {
  private interval = 30;
  appService = inject(AppService);
  works: WorkModel[] = [];
  cares: CareModel[] = [];
  roles: string[] = [];
  taj: string = '';
  firstName : string = '';
  lastName : string = '';
  email: string = '';
  age: number = 0;
  specialty: string = '';
  workHourStart: string = '';
  workHourEnd: string = '';
  role: string = '';
  password: string = '';
  showForm: boolean = false;

  startDate: string = '';
  endDate: string = '';

  constructor(private adminService: AdminService){}


  searchPerson(){}

  resetField(field:string){
    if(field === "date-to-date"){
      this.startDate = '';
      this.endDate = '';
      this.works = [];
    }else if(field === "cancel-add"){
      this.role = '';
      this.showForm = false;
      this.taj = '';
    }else if(field === "add-person"){
      this.role = '';
      this.showForm = true;
      this.taj = '';
      this.firstName = '';
      this.lastName = '';
      this.age = 0;
      this.email = '';
      this.password = '';
      this.workHourEnd = '';
      this.workHourStart = '';
      this.specialty = '';
    }
  }
  onSubmit(form: NgForm){
    const request: RegistrationRequest={
      taj: this.taj,
      firstName: this.firstName,
      lastName: this.lastName,
      age: this.age,
      email: this.email,
      password:this.password,
      roleName: this.role,
      workHoursStart: this.workHourStart + ":00",
      workHoursEnd: this.workHourEnd + ":00",
      specialty: this.specialty
    }
    this.adminService.registerPerson(request).subscribe({
      next: (response) => {
        this.appService.successPopup("Hozzáadva!");
        this.resetField("add-person");
        this.showForm = false;
      },
      error: (error) => {
        this.appService.errorPopup("Hiba!");
      }
    });
  }

  loadRoles(){
    this.adminService.getRoles().subscribe({
      next: (response) => {
        this.roles = response.data.map((role: any) => role.roleName);
      },
      error: (error) => {
        console.error('Error fetching roles:', error);
      }
    });
  }

  loadWorks(){
    this.adminService.getAllWorksBetween(this.startDate,this.endDate).subscribe({
      next: (response) => {
        this.works = response.data;
      },
      error: (err) => {
        console.error('Error fetching', err);
        console.log(err.error.message);
        
      }
    });
  }

  loadCares(){
    this.adminService.getAllActiveCares().subscribe({
      next: (response) => {
        this.cares = response.data;
      },
      error: (err) => {
        console.error('Error fetching', err);
        console.log(err.error.message);
        
      }
    });
  }

  get maxEndDate(): string {
    if (!this.startDate) return '';
    const date = new Date(this.startDate);
    date.setDate(date.getDate() + this.interval);
    return date.toISOString().split('T')[0];
  }

  get minStartDate(): string {
    if (!this.endDate) return '';
    const date = new Date(this.endDate);
    date.setDate(date.getDate() - this.interval);
    return date.toISOString().split('T')[0];
  }
}
