import { Routes } from '@angular/router';
import { Registration } from './components/registration/registration';
import { Login } from './components/login/login';

export const routes: Routes = [
    {
        path: 'registration',
        component: Registration
    },
    {
        path: 'login',
        component: Login
    }
];
