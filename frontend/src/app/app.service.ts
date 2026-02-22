import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import Swal from 'sweetalert2';



@Injectable({ providedIn: 'root' })
export class AppService {
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
    constructor(private http: HttpClient) {
    }


}