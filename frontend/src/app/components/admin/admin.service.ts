import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { WorkModel } from '../work.model';
import { Observable } from 'rxjs';
import { CareModel } from '../care.model';
import { RegistrationRequest } from '../registration.request.model';




@Injectable({ providedIn: 'root' })
export class AdminService {
    private apiUrl = environment.apiUrl;

    private getAllWorksBetweenEndpoint = (dayA: string, dayB: string) => `${this.apiUrl}/works/all-in-interval?dayAfter=${dayA}&dayBefore=${dayB}`;
    private getAllActiveCaresEndpoint : string = `${this.apiUrl}/cares/all/actives`;
    private getRolesEndpoint = `${this.apiUrl}/roles/all`;
    private getSpecialtiesEndpoint = `${this.apiUrl}/specialties/all`;
    private registrationEndpoint = `${this.apiUrl}/people/register`;
    private getPersonEndpoint = (taj:string, role:string) => `${this.apiUrl}/people/${taj}/person?role=${role}`;
    private deletePersonEndpoint = (taj:string, role: string) => `${this.apiUrl}/people/delete/${taj}/person?role=${role}`;

    constructor(private http: HttpClient){}

    getAllWorksBetween(dayA: string, dayB: string): Observable<{ msg: string, data: WorkModel[] }> {
        return this.http.get<{ msg: string, data: WorkModel[] }>(this.getAllWorksBetweenEndpoint(dayA, dayB));
    }
    getAllActiveCares(): Observable<{ msg: string, data: CareModel[] }> {
        return this.http.get<{ msg: string, data: CareModel[] }>(this.getAllActiveCaresEndpoint);
    }
    getRoles(): Observable<{msg: string, data: any}> {
        return this.http.get<{msg: string, data: any}>(this.getRolesEndpoint);
    }
    getSpecialties(): Observable<{msg: string, data: any}> {
        return this.http.get<{msg: string, data: any}>(this.getSpecialtiesEndpoint);
    }

    registerPerson(request: RegistrationRequest): Observable<{ msg: string, data: any }> {
        return this.http.post<{ msg: string, data: any }>(this.registrationEndpoint, request);
    }
    getPerson(taj:string, role: string): Observable<{msg: string, data: any}>{
        return this.http.get<{msg: string, data: any}>(this.getPersonEndpoint(taj,role));
    }
    deletePerson(taj:string, role:string): Observable<{msg: string, data: any}>{
        return this.http.delete<{msg: string, data: any}>(this.deletePersonEndpoint(taj,role));
    }
}