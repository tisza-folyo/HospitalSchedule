import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PatientRegistrationRequest } from './patient.registration.request.model';
import { environment } from '../../../environments/environment';



@Injectable({ providedIn: 'root' })
export class RegistrationService {
  private apiUrl = environment.apiUrl;

  private registrationEndpoint = `${this.apiUrl}/people/register`;

  constructor(private http: HttpClient) {}

  registerPerson(request: PatientRegistrationRequest): Observable<{msg: string, data: any}> {
    return this.http.post<{msg: string, data: any}>(this.registrationEndpoint, request);
  }

}