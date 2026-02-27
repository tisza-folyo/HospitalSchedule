import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CareModel } from '../care.model';
import { NurseModel } from './nurse.model';



@Injectable({ providedIn: 'root' })
export class NurseService {
    private apiUrl = environment.apiUrl;

    private getAllActiveCaresEndpoint = (taj: string) => `${this.apiUrl}/cares/actives?nTaj=${taj}`;
    private getAllNonActiveCaresEndpoint = (taj: string) => `${this.apiUrl}/cares/non-actives?nTaj=${taj}`;
    private getAllFreeNursesEndpoint: string = `${this.apiUrl}/people/nurses/all/free`;
    private getAllCaresForCareEndpoint: string = `${this.apiUrl}/cares/all-for-care`;

    private updateNurseEndpoint = (nTaj:string, pTaj: string, uTaj: string) => `${this.apiUrl}/cares/change?nTaj=${nTaj}&pTaj=${pTaj}&uTaj=${uTaj}`;
    private assignNurseEndpoint = (nTaj:string, pTaj: string, uTaj: string) => `${this.apiUrl}/cares/assign?nTaj=${nTaj}&pTaj=${pTaj}&uTaj=${uTaj}`;

    constructor(private http: HttpClient){};

    getAllActiveCares(taj: string): Observable<{msg:string, data: CareModel[]}>{
        return this.http.get<{msg:string, data: CareModel[]}>(this.getAllActiveCaresEndpoint(taj));
    }
    getAllNonActiveCares(taj: string): Observable<{msg:string, data: CareModel[]}>{
        return this.http.get<{msg:string, data: CareModel[]}>(this.getAllNonActiveCaresEndpoint(taj));
    }
    getAllFreeNurses(): Observable<{ msg: string, data: NurseModel[] }> {
        return this.http.get<{ msg: string, data: NurseModel[] }>(this.getAllFreeNursesEndpoint);
    }
    getAllCaresForCare(): Observable<{msg:string, data: CareModel[]}>{
        return this.http.get<{msg:string, data: CareModel[]}>(this.getAllCaresForCareEndpoint);
    }
    updateNurse(nTaj: string, pTaj: string, uTaj: string): Observable<{ msg: string, data: CareModel }> {
        return this.http.put<{ msg: string, data: CareModel }>(this.updateNurseEndpoint(nTaj,pTaj,uTaj),"");
    }
    assigNurse(nTaj: string, pTaj: string, uTaj: string): Observable<{ msg: string, data: CareModel }> {
        return this.http.post<{ msg: string, data: CareModel }>(this.assignNurseEndpoint(nTaj,pTaj,uTaj),"");
    }

}