import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Patient } from '../patient/patient';
import { PatientModel } from '../patient/patient.model';
import { AssistantModel } from '../assistant/assistant.model';
import { AssistantRequest } from '../assistant/assistant.requrest';
import { WorkModel } from '../work.model';
import { AppointmentModel } from '../appointment.model';



@Injectable({ providedIn: 'root' })
export class AssistantService {
    private apiUrl = environment.apiUrl;

    private getPatientByTajEndpoint = (taj: string) => `${this.apiUrl}/people/patient/${taj}`;
    private getAllPatientsByAssistantEndPoint = (taj: string) => `${this.apiUrl}/people/doctors/patients/${taj}`;
    private getAppointmentsByPatientEndpoint = (taj: string) => `${this.apiUrl}/appointments/all-by-patient/${taj}`;

    constructor(private http: HttpClient){}

    getPatientByTaj(taj: string): Observable<{msg: string, data: PatientModel}> {
        return this.http.get<{msg: string, data: PatientModel}>(this.getPatientByTajEndpoint(taj));
    }

    getAllPatients(taj: string): Observable<{msg: string, data: PatientModel[]}>{
        return this.http.get<{msg: string, data: PatientModel[]}>(this.getAllPatientsByAssistantEndPoint(taj));
    }

    getAppointmentsByPatient(dTaj: string): Observable<{ msg: string, data: AppointmentModel[] }> {
        return this.http.get<{ msg: string, data: AppointmentModel[] }>(this.getAppointmentsByPatientEndpoint(dTaj));
    }
}