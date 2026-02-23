import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Appointment } from '../appointment.model';
import { Patient } from '../patient/patient';
import { PatientModel } from '../patient/patient.model';
import { AssistantModel } from '../assistant/assistant.model';
import { AssistantRequest } from '../assistant/assistant.requrest';



@Injectable({ providedIn: 'root' })
export class DoctorService {
    private apiUrl = environment.apiUrl;

    private getAppointmentsByDoctorEndpoint = (dTaj : string) => `${this.apiUrl}/appointments/all-by-doctor/${dTaj}`;
    private getPatientByTajEndpoint = (taj: string) => `${this.apiUrl}/people/patient/${taj}`;
    private getAllPatientsEndpoint = (taj: string) => `${this.apiUrl}/people/patients/${taj}`;
    private getFreeAssistantsEndpoint = (date: string) => `${this.apiUrl}/works/assistants/free?day=${date}`;
    private assignAssistantEndpoint = `${this.apiUrl}/works/assign`;

    constructor(private http: HttpClient) { }

    getAppointmentsByDoctor(dTaj: string): Observable<{msg: string, data: Appointment[]}> {
        return this.http.get<{msg: string, data: Appointment[]}>(this.getAppointmentsByDoctorEndpoint(dTaj));
    }

    getPatientByTaj(taj: string): Observable<{msg: string, data: PatientModel}> {
        return this.http.get<{msg: string, data: PatientModel}>(this.getPatientByTajEndpoint(taj));
    }

    getAllPatients(dTaj: string): Observable<{msg: string, data: PatientModel[]}> {
        return this.http.get<{msg: string, data: PatientModel[]}>(this.getAllPatientsEndpoint(dTaj));
    }
    getFreeAssistants(date: string): Observable<{msg: string, data: AssistantModel[]}> {
        return this.http.get<{msg: string, data: AssistantModel[]}>(this.getFreeAssistantsEndpoint(date));
    }

    assignAssistant(request: AssistantRequest): Observable<{msg: string, data: any}> {
        return this.http.put<{msg: string, data: any}>(this.assignAssistantEndpoint, request);
    }
}