import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { WorkModel } from '../work.model';
import { Observable } from 'rxjs';
import { CareModel } from '../care.model';




@Injectable({ providedIn: 'root' })
export class AdminService {
    private apiUrl = environment.apiUrl;

    private getAllWorksBetweenEndpoint = (dayA: string, dayB: string) => `${this.apiUrl}/works/all-in-interval?dayAfter=${dayA}&dayBefore=${dayB}`;
    private getAllActiveCaresEndpoint : string = `${this.apiUrl}/cares/all/actives`;

    constructor(private http: HttpClient){}

    getAllWorksBetween(dayA: string, dayB: string): Observable<{ msg: string, data: WorkModel[] }> {
        return this.http.get<{ msg: string, data: WorkModel[] }>(this.getAllWorksBetweenEndpoint(dayA, dayB));
    }
    getAllActiveCares(): Observable<{ msg: string, data: CareModel[] }> {
        return this.http.get<{ msg: string, data: CareModel[] }>(this.getAllActiveCaresEndpoint);
    }
}