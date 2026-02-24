import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { AppointmentModel } from './components/appointment.model';



@Injectable({ providedIn: 'root' })
export class AppService {
    private router = inject(Router);
    token = signal<string>(localStorage.getItem('token') || '');
    taj = signal<string | null>(localStorage.getItem('taj'));
    roleName = signal<string | null>(localStorage.getItem('roleName'));
    firstName = signal<string | null>(localStorage.getItem('firstName'));
    lastName = signal<string | null>(localStorage.getItem('lastName'));
    speciality = signal<string | null>(localStorage.getItem('speciality'));
    section = signal<string | null>(localStorage.getItem('section'));

    getToken(): string {
        return this.token();
    }
    getRoleName(): string | null {
        return this.roleName();
    }

    getTaj(): string | null {
        return this.taj();
    }

    setToken(newToken: string): void {
        this.updateStorageAndSignal('token', newToken, this.token);
    }
    setTaj(newTaj: string | null): void {
        this.updateStorageAndSignal('taj', newTaj, this.taj);
    }

    setRoleName(newRoleName: string | null): void {
        this.updateStorageAndSignal('roleName', newRoleName, this.roleName);
    }

    setFirstName(newFirstName: string | null): void {
        this.updateStorageAndSignal('firstName', newFirstName, this.firstName);
    }

    setLastName(newLastName: string | null): void {
        this.updateStorageAndSignal('lastName', newLastName, this.lastName);
    }

    setSpeciality(newSpeciality: string | null): void {
        this.updateStorageAndSignal('speciality', newSpeciality, this.speciality);
    }

    setSection(newSection: string | null): void {
        this.updateStorageAndSignal('section', newSection, this.section);
    }

    private updateStorageAndSignal(key: string, value: string | null, sig: any) {
        if (value) {
            localStorage.setItem(key, value);
        } else {
            localStorage.removeItem(key);
        }
        sig.set(value);
    }

    navigateByRole(role: string) {
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

    statusConverter(status: string): string {
        switch (status) {
            case 'FREE':
                return 'Szabad';
            case 'LOCKED':
                return 'Lefoglalt';
            case 'DONE':
                return 'Befejezett';
            default:
                return status;
        }
    }

    stausColorConverter(status: string): string {
        switch (status) {
            case 'FREE':
                return 'info';
            case 'LOCKED':
                return 'warning';
            case 'DONE':
                return 'success';
            default:
                return 'secondary';
        }
    }

    successPopup(msg: string) {
        Swal.fire({
            title: msg,
            icon: 'success',
            background: '#f8f9fa',
            confirmButtonColor: '#0d6efd',
            confirmButtonText: 'Szuper!',
            timerProgressBar: true
        });
    }

    errorPopup(msg: string) {
        Swal.fire({
            title: msg,
            icon: 'error',
            confirmButtonColor: '#dc3545',
            confirmButtonText: 'Értem'
        });
    }

    questionPopup(msg: string) {
        return Swal.fire({
            title: msg,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#dc3545',
            cancelButtonColor: '#6c757d',
            confirmButtonText: 'Igen',
            cancelButtonText: 'Nem'
        });
    }

    inputPopUp(title: string, label: string, placeholder: string) {
        return Swal.fire({
            title: title,
            input: 'textarea',
            inputLabel: label,
            inputPlaceholder: placeholder,
            inputAttributes: {
                'aria-label': 'Ide írjon'
            },
            showCancelButton: true
        })
    }
    filePopUp(title: string) {
        return Swal.fire({
            title: title,
            input: 'file',
            inputAttributes: {
                'accept': 'image/*,application/pdf',
                'multiple': 'multiple'
            },
            showCancelButton: true,
            confirmButtonText: 'Feltöltés',
            cancelButtonText: 'Kihagyás'
        });
    }

    formatData(data: any): AppointmentModel[] { 
        console.log(data);
        
        return data.map((item: any) => ({
            appointmentId: item.appointmentId,
            timeSlot: new Date(`${item.day}T${item.timeSlot}`),
            doctorTaj: item.doctor?.taj,
            patientTaj: item.patient?.taj,
            description: item.symptomDescription,
            status: item.status,
            files: item.symptomImg
        }));
    }

    nameConcatenator(firstName: string | null, lastName: string | null): string {
        if (firstName && lastName) {
            return lastName + ' ' + firstName;
        } else if (firstName) {
            return firstName;
        } else if (lastName) {
            return lastName;
        } else {
            return '';
        }
    }

    downloadPdf(file: any) {
    const byteCharacters = atob(file.base64Data);
    const byteNumbers = new Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);

    const blob = new Blob([byteArray], { type: file.fileType });

    const fileURL = URL.createObjectURL(blob);
    window.open(fileURL, '_blank');
  }
    constructor(private http: HttpClient) {
    }


}