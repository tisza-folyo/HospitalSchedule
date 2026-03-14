import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { AppointmentModel } from './components/appointment.model';
import { environment } from '../environments/environment';



@Injectable({ providedIn: 'root' })
export class AppService {
    private router = inject(Router);
    private refreshTimer: any;
    token = signal<string>(localStorage.getItem('token') || '');
    taj = signal<string | null>(localStorage.getItem('taj'));
    roleName = signal<string | null>(localStorage.getItem('roleName'));
    firstName = signal<string | null>(localStorage.getItem('firstName'));
    lastName = signal<string | null>(localStorage.getItem('lastName'));
    speciality = signal<string | null>(localStorage.getItem('speciality'));
    section = signal<string | null>(localStorage.getItem('section'));

    private apiUrl = environment.apiUrl;
    private updatePasswordEndpoint = (taj: string, role: string, oldPassword: string, newPassword: string) => `${this.apiUrl}/people/${taj}/change-password?role=${role}`;
    private getAllSectionsEndpoint = `${this.apiUrl}/sections/all`;
    private updateSectionEndpoint = (taj: string) => `${this.apiUrl}/people/${taj}/change-section`;

    updatePassword(taj: string, role: string, oldPassword: string, newPassword: string): Observable<{ msg: string, data: any }> {
        const body = {
            oldPassword: oldPassword,
            newPassword: newPassword
        };
        return this.http.put<{ msg: string, data: any }>(this.updatePasswordEndpoint(taj, role, oldPassword, newPassword), body);
    }

    updateSection(taj: string, section: string): Observable<{ msg: string, data: any }> {
        return this.http.put<{ msg: string, data: any }>(this.updateSectionEndpoint(taj), section);
    }

    getSections(): Observable<{msg: string, data: any}> {
        return this.http.get<{msg: string, data: any}>(this.getAllSectionsEndpoint);
    }

    private readonly ROLE_MAP: Record<string, string> = {
        'ADMIN': 'Adminisztrátor',
        'DOCTOR': 'Orvos',
        'PATIENT': 'Páciens',
        'ASSISTANT': 'Asszisztens',
        'NURSE': 'Ápoló'
    };

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
        if (newToken) {
            this.autoLogout(newToken);
        }
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

    private getExpirationTimeout(token: string): number {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            const expiresAt = payload.exp * 1000;
            return expiresAt - Date.now();
        } catch (e) {
            return -1;
        }
    }

    private autoLogout(token: string) {
        if (this.refreshTimer) clearTimeout(this.refreshTimer);

        const timeout = this.getExpirationTimeout(token);

        if (timeout <= 0) {
            this.logout();
        } else {
            this.refreshTimer = setTimeout(() => {
                this.logout();
            }, timeout);
        }
    }

    logout() {
        localStorage.clear();
        this.setToken('');
        this.roleName.set(null);
        this.firstName.set(null);
        this.lastName.set(null);
        this.speciality.set(null);
        this.section.set(null);
        this.taj.set(null);
        if (this.refreshTimer) clearTimeout(this.refreshTimer);
        this.router.navigate(['/']);
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

    translateToHU(techName: string): string {
        return this.ROLE_MAP[techName] || techName;
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
    infoPopup(msg: string) {
        Swal.fire({
            title: msg,
            icon: 'info',
            background: '#f8f9fa',
            confirmButtonColor: '#0dcaf0',
            confirmButtonText: 'OK',
            allowOutsideClick: false
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
        const savedToken = this.token();
        if (savedToken) {
            this.autoLogout(savedToken);
        }
    }


}