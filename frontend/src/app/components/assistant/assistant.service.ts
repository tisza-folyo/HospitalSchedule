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
import { DoctorModel } from '../doctor/doctor.model';



@Injectable({ providedIn: 'root' })
export class AssistantService {
    private apiUrl = environment.apiUrl;

    private getPatientByTajEndpoint = (taj: string) => `${this.apiUrl}/people/patient/${taj}`;
    private getAllPatientsByAssistantEndPoint = (taj: string) => `${this.apiUrl}/people/doctors/patients/${taj}`;
    private getAppointmentsByPatientEndpoint = (taj: string) => `${this.apiUrl}/appointments/all-by-patient/${taj}`;
    private getAppointmentsBySectionEnpoint= (section: string, day: string): string => `${this.apiUrl}/appointments/section/${section}?day=${day}`;
    private getAppointmentsByDateEnpoint= (date: string): string => `${this.apiUrl}/appointments/daily?day=${date}`;
    private getAppointmentsByDoctorEnpoint= (dTaj: string, day: string): string => `${this.apiUrl}/appointments/doctor/${dTaj}?day=${day}`;
    private getAllWorksAfterDayEnpoint= (day: string) => `${this.apiUrl}/works/all/after?day=${day}`;
    private getAllWorksBetweenEndpoint = (taj: string, dayA: string, dayB: string) => `${this.apiUrl}/works/all/between?aTaj=${taj}&dayAfter=${dayA}&dayBefore=${dayB}`;
    private getAllFreeAssistantsEndpoint = (day: string) => `${this.apiUrl}/works/assistants/free?day=${day}`;

    private getAllSectionsEnpoint: string = `${this.apiUrl}/sections/all`;
    private getAllDoctorsEnpoint: string = `${this.apiUrl}/people/doctors/all`;
    private postAppointmentEnpoint: string = `${this.apiUrl}/appointments/add-request`;
    private postFileEnpoint = (id: number): string => `${this.apiUrl}/images/upload-to-appointment?appointmentId=${id}`;
    private updateAssistantEnpoint: string = `${this.apiUrl}/works/assign`;
    private replaceAssistantEnpoint: string = `${this.apiUrl}/works/replace`;

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

    getAllSections(): Observable<{ msg: string, data: any }> {
        return this.http.get<{ msg: string, data: any }>(this.getAllSectionsEnpoint);
    }

    getAllDoctors(): Observable<{ msg: string, data: DoctorModel[] }> {
        return this.http.get<{ msg: string, data: DoctorModel[] }>(this.getAllDoctorsEnpoint);
    }
    getAppointmentsBySection(section: string, day: string): Observable<{msg: string, data: AppointmentModel[]}> {
        return this.http.get<{msg: string, data: AppointmentModel[]}>(this.getAppointmentsBySectionEnpoint(section, day));
    }
    
    getAppointmentsByDate(date: string): Observable<{msg: string, data: AppointmentModel[]}> {
        return this.http.get<{msg: string, data: AppointmentModel[]}>(this.getAppointmentsByDateEnpoint(date));
    }
    getAppointmentsByDoctor(dTaj: string, day: string): Observable<{msg: string, data: AppointmentModel[]}> {
        return this.http.get<{msg: string, data: AppointmentModel[]}>(this.getAppointmentsByDoctorEnpoint(dTaj, day));
    }

    getAllWorksAfterDay(day: string): Observable<{msg: string, data: WorkModel[]}>{
        return this.http.get<{msg: string, data: WorkModel[]}>(this.getAllWorksAfterDayEnpoint(day));
    }

    getAllWorksBetween(taj: string, dayA: string, dayB: string): Observable<{msg: string, data: WorkModel[]}>{
        return this.http.get<{msg: string, data: WorkModel[]}>(this.getAllWorksBetweenEndpoint(taj,dayA,dayB));
    }

    getAllFreeAssistants(day: string):Observable<{msg: string, data: AssistantModel[]}>{
        return this.http.get<{msg: string, data: AssistantModel[]}>(this.getAllFreeAssistantsEndpoint(day));
    }

    postAppointment(request: AppointmentRequest): Observable<{msg: string,data: any}> {
        return this.http.post<{msg: string,data: any}>(this.postAppointmentEnpoint, request);
    }

    updateAssistant(request: AssistantRequest): Observable<{msg: string,data: AssistantModel}>{
        return this.http.put<{msg: string,data: AssistantModel}>(this.updateAssistantEnpoint,request);
    }
    replaceAssistant(request: AssistantRequest): Observable<{msg: string,data: AssistantModel}>{
        return this.http.put<{msg: string,data: AssistantModel}>(this.replaceAssistantEnpoint,request);
    }


    uploadToAppointment(files: File[], id: number): Observable<{msg: string,data: any}> {
        const formData = new FormData();
        files.forEach(file => {
            formData.append('files', file);
        });
        return this.http.post<{msg: string,data: any}>(this.postFileEnpoint(id), formData);
    }


}