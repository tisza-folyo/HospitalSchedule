import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { AdminService } from './admin.service';
import { AppService } from '../../app.service';
import { WorkModel } from '../work.model';
import { CareModel } from '../care.model';
import { RegistrationRequest } from '../registration.request.model';
import { PersonModel } from '../person.model';

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
  rolesForPerson: string[] = [];
  avalaibleRoles: string [] = [];
  specialties: string[] = [];
  personAccounts: PersonModel[] = [];
  taj: string = '';
  firstName : string = '';
  lastName : string = '';
  email: string = '';
  age: number = 0;
  specialty: string = '';
  workHourStart: string = '';
  workHourEnd: string = '';
  role: string = '';
  grantRole: string = '';
  password: string = '';
  showForm: boolean = false;
  searched: boolean = false;
  showGrantForm: boolean = false;

  startDate: string = '';
  endDate: string = '';

  constructor(private adminService: AdminService){}


  searchPerson(){
    if(this.appService.getTaj() === this.taj){
      this.appService.infoPopup("Magadra nem szűrhetsz!")
      return;
    }
    this.adminService.getRolesForPerson(this.taj).subscribe({
      next: (response) => {
        this.rolesForPerson = response.data.map((role: any) => role.roleName);
        this.calculateAvalaibleRoles();
        this.rolesForPerson.forEach(r => {
          this.adminService.getPerson(this.taj, r).subscribe({
            next: (response) => {
              const data = response.data;
              this.personAccounts.push({
                taj : data.taj,
                role : data.role.roleName,
                firstName : data.firstName,
                lastName : data.lastName,
                age : data.age,
                email: data.email
              })
              this.searched = true;
            },
            error: (error) => {
              console.log(error);
            }
          });
        });
      },
      error: (error) => {
        if (error.status === 404) {
        this.appService.infoPopup("Nincs ilyen személy!");
      } else {
        this.appService.errorPopup("Hiba!");
      }
      }
    });
    
  }

  modifyRole(){
    this.showGrantForm = true;
  }

  deletePerson(r: string) {
  this.appService.questionPopup("Biztos törölni akarja?").then((result) => {
    if (result.isConfirmed) {
      this.adminService.deletePerson(this.taj, r).subscribe({
        next: (response) => {
          this.appService.successPopup("Siker!");
          this.resetField("search-person");
          this.searched = false;
        },
        error: (error) => {
          this.appService.errorPopup("Hiba!");
        }
      });
    }
  });
}

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
      this.loadRoles();
      this.loadSpecialties();
    }else if(field === "search-person"){
      this.role = '';
      this.firstName = '';
      this.lastName = '';
      this.age = 0;
      this.email = '';
      this.password = '';
      this.workHourEnd = '';
      this.workHourStart = '';
      this.specialty = '';
      this.personAccounts = [];
      this.showGrantForm = false;
      this.searched = false;
    }
  }
  onSubmit(form: NgForm){
    this.workHourStart = this.workHourStart + ":00";
    this.workHourEnd = this.workHourEnd + ":00";
    this.postRegist();
  }

  onGrantRole(){
    this.role = this.grantRole;
    const person = this.personAccounts[0];
    this.firstName = person.firstName;
    this.lastName = person.lastName;
    this.age = person.age;
    this.postRegist();
    this.resetField("search-person")
  }

  loadSpecialties(){
    this.adminService.getSpecialties().subscribe({
      next: (response) => {
        this.specialties = response.data.map((spec: any) => spec.specialtyName);
      },
      error: (error) => {
        console.error('Error fetching roles:', error);
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

  loadRolesForPerson(){
    this.adminService.getRolesForPerson(this.taj).subscribe({
      next: (response) => {
        this.rolesForPerson = response.data.map((r:any) => r.roleName);
      },
      error: (error) => {
        console.log(error);
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

  private calculateAvalaibleRoles(){
    this.avalaibleRoles = this.roles.filter(r => !this.rolesForPerson.includes(r));
  }

  private postRegist(){
    const request: RegistrationRequest={
      taj: this.taj,
      firstName: this.firstName,
      lastName: this.lastName,
      age: this.age,
      email: this.email,
      password: this.taj + "_" + this.firstName,
      roleName: this.role,
      workHoursStart: this.workHourStart,
      workHoursEnd: this.workHourEnd,
      specialtyName: this.specialty
    }
    console.log(request);
    
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
