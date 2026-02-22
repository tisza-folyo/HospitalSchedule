import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';



@Injectable({ providedIn: 'root' })
export class DoctorService {
    private apiUrl = environment.apiUrl;

    private getAppointmentsByDoctorEndpoint = (dTaj : string) => `${this.apiUrl}/appointments/all-by-doctor/${dTaj}`;
    private getPatientByTajEndpoint = (taj: string) => `${this.apiUrl}/people/patient/${taj}`;

    constructor(private http: HttpClient) { }

    getAppointmentsByDoctor(dTaj: string): Observable<{msg: string, data: Appointment[]}> {
        return this.http.get<{msg: string, data: Appointment[]}>(this.getAppointmentsByDoctorEndpoint(dTaj));
    }

    getPatientByTaj(taj: string): Observable<{msg: string, data: Patient}> {
        return this.http.get<{msg: string, data: Patient}>(this.getPatientByTajEndpoint(taj));
    }
}