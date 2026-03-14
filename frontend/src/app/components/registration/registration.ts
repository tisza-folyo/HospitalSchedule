import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { RegistrationService } from './registration.service';
import { Route, Router } from '@angular/router';
import { PatientRegistrationRequest } from './patient.registration.request.model';
import { provideHttpClient } from '@angular/common/http';
import { AppService } from '../../app.service';

@Component({
  selector: 'app-registration',
  imports: [CommonModule, FormsModule],
  templateUrl: './registration.html',
  styleUrl: './registration.css',
})
export class Registration {
  appService = inject(AppService);
  taj = '';
  firstName = '';
  lastName = '';
  age = 0;
  email = '';
  password = '';
  passwordConfirm = '';
  roleName = 'PATIENT';

  constructor(private registrationService: RegistrationService, private router: Router) { }

  submitAttempted = false;
  showPassword = false;
  showConfirmPassword = false;

  onRegister(form: NgForm) {
    this.submitAttempted = true;

    if (form.invalid || this.password !== this.passwordConfirm) {
      console.log("Password mismatch");
      return;
    }
    const person: PatientRegistrationRequest = {
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
        if (error.status === 409) {
          this.appService.errorPopup(this.appService.translateToHU(this.appService.getRoleName() ?? "PATIENT") + " már létezik ezzel az email címmel vagy tajszámmal!")
        } else {
          this.appService.errorPopup("Hiba!");
        }
      }
    });

  }
}
