import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';



@Injectable({ providedIn: 'root' })
export class AppService {
    token = signal<string>('');
    roleName = signal<string | null>(null);
    firstName = signal<string | null>(null);
    lastName = signal<string | null>(null);
    speciality = signal<string | null>(null);
    section = signal<string | null>(null);

    getToken(): string | null {
        return this.token();
    }

    setToken(newToken: string): void {
        this.token.set(newToken);
    }


    constructor(private http: HttpClient) { }

    setRole() {
        const token = this.getToken();
        if (token) {
            try {
                const payload = JSON.parse(atob(token.split('.')[1]));

                const userRole = payload.role;

                if (userRole) {
                    this.roleName.set(userRole);
                }

            } catch (e) {
                console.error("Decoding token failed:", e);
            }
        }
    }
}