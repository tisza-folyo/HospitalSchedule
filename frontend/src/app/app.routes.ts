import { Routes } from '@angular/router';
import { Registration } from './components/registration/registration';
import { Login } from './components/login/login';
import { Patient } from './components/patient/patient';
import { Nurse } from './components/nurse/nurse';
import { Admin } from './components/admin/admin';
import { Assistant } from './components/assistant/assistant';
import { Doctor } from './components/doctor/doctor';

export const routes: Routes = [
    {
        path: 'registration',
        component: Registration
    },
    {
        path: 'login',
        component: Login
    },
    {
        path: 'patient',
        component: Patient
    },
    {
        path: 'nurse',
        component: Nurse
    },
    {
        path: 'admin',
        component: Admin
    },
    {
        path: 'assistant',
        component: Assistant
    },
    {
        path: 'doctor',
        component: Doctor
    }
];
