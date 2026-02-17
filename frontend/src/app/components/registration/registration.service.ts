import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PersonRegistrationRequest } from './registration.request';
import { environment } from '../../../environments/environment';



@Injectable({ providedIn: 'root' })
export class RegistrationService {
  private apiUrl = environment.apiUrl;

  private registrationEndpoint = `${this.apiUrl}/people/register`;

  constructor(private http: HttpClient) {}

  registerPerson(request: PersonRegistrationRequest): Observable<{msg: string, data: any}> {
    return this.http.post<{msg: string, data: any}>(this.registrationEndpoint, request);
  }

}