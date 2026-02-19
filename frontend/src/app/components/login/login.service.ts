import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';



@Injectable({ providedIn: 'root' })
export class LoginService {
    private apiUrl = environment.apiUrl;

    private getRolesEndpoint = `${this.apiUrl}/roles/all`;
    private loginEndpoint = `${this.apiUrl}/auth/login`;
    private getPersonInfoEndpoint = (taj: string, role: string) => `${this.apiUrl}/people/${taj}/person?role=${role}`;

    constructor(private http: HttpClient) {}

    getRoles(): Observable<{msg: string, data: any}> {
        return this.http.get<{msg: string, data: any}>(this.getRolesEndpoint);
    }

    loginPerson(loginRequest: PersonLoginRequest): Observable<{msg: string, data: {taj: string, token: string}}> {
        return this.http.post<{msg: string, data: any}>(this.loginEndpoint, loginRequest);
    }

    getPersonInfo(taj: string, role: string): Observable<{msg: string, data: any}> {
        return this.http.get<{msg: string, data: any}>(this.getPersonInfoEndpoint(taj, role));
    }
}