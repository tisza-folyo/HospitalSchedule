import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { App } from '../../app';
import { Appointment } from '../appointment.model';
import { DoctorModel } from '../doctor/doctor.model';



@Injectable({ providedIn: 'root' })
export class PatientService {
    private apiUrl = environment.apiUrl;

    private getAppointmentsByDoctorEnpoint= (dTaj: string, day: string): string => `${this.apiUrl}/appointments/doctor/${dTaj}?day=${day}`;
    private getAppointmentsByPatientEnpoint= (pTaj: string): string => `${this.apiUrl}/appointments/all-by-patient/${pTaj}`;
    private getAppointmentsBySectionEnpoint= (section: string, day: string): string => `${this.apiUrl}/appointments/section/${section}?day=${day}`;
    private getAppointmentsByDateEnpoint= (date: string): string => `${this.apiUrl}/appointments/daily?day=${date}`;

    private getAllSectionsEnpoint: string = `${this.apiUrl}/sections/all`;
    private getAllDoctorsEnpoint: string = `${this.apiUrl}/people/doctors/all`;

    private postAppointmentEnpoint: string = `${this.apiUrl}/appointments/add-request`;
    private postFilesEnpoint = (pTaj: string): string => `${this.apiUrl}/images/upload-to-patient?pTaj=${pTaj}`;
    private cancelAppointmentEnpoint = (appointmentId: number): string => `${this.apiUrl}/appointments/cancel?appointmentId=${appointmentId}`;

    constructor(private http: HttpClient) {}

    getAppointmentsByDoctor(dTaj: string, day: string): Observable<{msg: string, data: Appointment[]}> {
        return this.http.get<{msg: string, data: Appointment[]}>(this.getAppointmentsByDoctorEnpoint(dTaj, day));
    }

    getAppointmentsByPatient(pTaj: string): Observable<{msg: string, data: Appointment[]}> {
        return this.http.get<{msg: string, data: Appointment[]}>(this.getAppointmentsByPatientEnpoint(pTaj));
    }

    getAppointmentsBySection(section: string, day: string): Observable<{msg: string, data: Appointment[]}> {
        return this.http.get<{msg: string, data: Appointment[]}>(this.getAppointmentsBySectionEnpoint(section, day));
    }
    
    getAppointmentsByDate(date: string): Observable<{msg: string, data: Appointment[]}> {
        return this.http.get<{msg: string, data: Appointment[]}>(this.getAppointmentsByDateEnpoint(date));
    }

    getAllSections(): Observable<{msg: string, data: any}> {
        return this.http.get<{msg: string, data: any}>(this.getAllSectionsEnpoint);
    }

    getAllDoctors(): Observable<{msg: string, data: DoctorModel[]}> {
        return this.http.get<{msg: string, data: DoctorModel[]}>(this.getAllDoctorsEnpoint);
    }

    postAppointment(request: AppointmentRequest): Observable<{msg: string,data: any}> {
        return this.http.post<{msg: string,data: any}>(this.postAppointmentEnpoint, request);
    }

    uploadToPatient(files: File[], pTaj: string): Observable<{msg: string,data: any}> {
        const formData = new FormData();
        files.forEach(file => {
            formData.append('files', file);
        });
        return this.http.post<{msg: string,data: any}>(this.postFilesEnpoint(pTaj), formData);
    }

    cancelAppointment(appointmentId: number): Observable<{msg: string,data: any}> {
        return this.http.delete<{msg: string,data: any}>(this.cancelAppointmentEnpoint(appointmentId));
    }

}