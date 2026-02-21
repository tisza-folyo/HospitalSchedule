import { Component, OnInit } from '@angular/core';
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

  email = '';
  password = '';
  roleName = '';
  roleNames: string[] = [];

  showPassword = false;
  submitAttempted = false;
  errorHappened = false;


  private readonly ROLE_MAP: Record<string, string> = {
    'ADMIN': 'Adminisztrátor',
    'DOCTOR': 'Orvos',
    'PATIENT': 'Páciens',
    'ASSISTANT': 'Asszisztens',
    'NURSE': 'Ápoló'
  };

  private translateToHU(techName: string): string {
    return this.ROLE_MAP[techName] || techName;
  }


  private translateToEN(huName: string): string {
    const entry = Object.entries(this.ROLE_MAP).find(([en, hu]) => hu === huName);
    return entry ? entry[0] : huName;
  }

  private navigateByRole(role: string) {
    switch (role) {
      case 'ADMIN':
        this.router.navigate(['/admin']);
        break;
      case 'DOCTOR':
        this.router.navigate(['/doctor']);
        break;
      case 'PATIENT':
        this.router.navigate(['/patient']);
        break;
      case 'ASSISTANT':
        this.router.navigate(['/assistant']);
        break;
      case 'NURSE':
        this.router.navigate(['/nurse']);
        break;
      default:
        this.router.navigate(['/']);
    }
  }

  resetError() {
    if (this.errorHappened) {
      this.errorHappened = false;
      this.submitAttempted = false;
    }
  }

  constructor(private loginService: LoginService, private router: Router, private appService: AppService) { }

  ngOnInit(): void {
    this.loginService.getRoles().subscribe({
      next: (response) => {
        const rawRoles = response.data.map((role: any) => role.roleName);
        this.roleNames = rawRoles.map((techName: string) => this.translateToHU(techName));
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
      roleName: this.translateToEN(this.roleName)
    };

    this.loginService.loginPerson(person).subscribe({
      next: (response) => {
        console.log("Login response:", response);
        const token = response.data.token;
        this.appService.setToken(token);
        this.loginService.getPersonInfo(response.data.taj, person.roleName).subscribe({
          next: (infoResponse) => {
            console.log("Person info response:", infoResponse);
            const d = infoResponse.data;
            this.appService.setTaj(d.taj ?? null);
            this.appService.setRoleName(d.roleName ?? null);
            this.appService.setFirstName(d.firstName ?? null);
            this.appService.setLastName(d.lastName ?? null);
            this.appService.setSpeciality(d.speciality ?? null);
            this.appService.setSection(d.section ?? null);
            this.navigateByRole(person.roleName);
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
