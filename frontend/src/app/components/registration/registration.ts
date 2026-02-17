import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { RegistrationService } from './registration.service';
import { Route, Router } from '@angular/router';
import { PersonRegistrationRequest } from './registration.request'; 
import { provideHttpClient } from '@angular/common/http';

@Component({
  selector: 'app-registration',
  imports: [CommonModule, FormsModule],
  templateUrl: './registration.html',
  styleUrl: './registration.css',
})
export class Registration {
  taj = '';
  firstName = '';
  lastName = '';
  age = 0;
  email = '';
  password = '';
  passwordConfirm = '';
  roleName = 'PATIENT';

  constructor(private registrationService: RegistrationService, private router: Router) {}

  submitAttempted = false;
  showPassword = false;
  showConfirmPassword = false;

  onRegister(form: NgForm) {
    this.submitAttempted = true;

    if (form.invalid || this.password !== this.passwordConfirm) {
      console.log("Password mismatch");
      return;
    }
    const person: PersonRegistrationRequest = {
      taj: this.taj,
      firstName: this.firstName,
      lastName: this.lastName,
      age: this.age,
      email: this.email,
      password: this.password,
      roleName: this.roleName
    };

    this.registrationService.registerPerson(person).subscribe({
      next: (response) => {
        console.log('Registration successful:', response);
        this.router.navigate(['/login']);
        this.submitAttempted = false;
      },
      error: (error) => {
        console.error('Registration failed:', error);
      }
    });

  }
}
