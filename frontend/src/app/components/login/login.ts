import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { LoginService } from './login.service';
import { Router } from '@angular/router';
import { AppService } from '../../app.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  appService = inject(AppService);
  email = '';
  password = '';
  roleName = '';
  roleNames: string[] = [];

  showPassword = false;
  submitAttempted = false;
  errorHappened = false;


  

  

  resetError() {
    if (this.errorHappened) {
      this.errorHappened = false;
      this.submitAttempted = false;
    }
  }

  constructor(private loginService: LoginService, private router: Router) { }

  ngOnInit(): void {
    this.loginService.getRoles().subscribe({
      next: (response) => {
        this.roleNames = response.data.map((role: any) => role.roleName);
      },
      error: (error) => {
        console.error('Error fetching roles:', error);
      }
    });
  }

  onLogin(form: NgForm) {
    this.submitAttempted = true;
    this.errorHappened = false;
    if (form.invalid) {
      return;
    }

    const person: PersonLoginRequest = {
      email: this.email,
      password: this.password,
      roleName: this.roleName
    };

    this.loginService.loginPerson(person).subscribe({
      next: (response) => {
        console.log("Login response:", response);
        const token = response.data.token;
        this.appService.setToken(token);
        this.appService.setRoleName(person.roleName);
        this.loginService.getPersonInfo(response.data.taj, person.roleName).subscribe({
          next: (infoResponse) => {
            console.log("Person info response:", infoResponse);
            const d = infoResponse.data;
            this.appService.setTaj(d.taj ?? null);
            this.appService.setFirstName(d.firstName ?? null);
            this.appService.setLastName(d.lastName ?? null);
            this.appService.setSpeciality(d.speciality ?? null);
            if(d.section){
              this.appService.setSection(d.section.sectionName ?? null);
            }
            this.appService.navigateByRole(person.roleName);
          },
          error: (infoError) => {
            console.error("Error fetching person info:", infoError);
          }
        });
        
      },
      error: (error) => {
        console.error("Login error:", error.error.message);
        this.errorHappened = true;
      }
    });
  }
}
