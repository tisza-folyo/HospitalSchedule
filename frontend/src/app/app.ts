import { Component, inject, signal } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';
import { AppService } from './app.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');

  appService = inject(AppService);

  constructor(private router: Router) {}

  onLogout() {
  Swal.fire({
    title: 'Kijelentkezés',
    text: 'Valóban ki szeretnél jelentkezni?',
    icon: 'question',
    showCancelButton: true,
    confirmButtonColor: '#0d6efd', 
    cancelButtonColor: '#6c757d',  
    confirmButtonText: 'Igen',
    cancelButtonText: 'Mégse',
    heightAuto: false 
  }).then((result) => {
    if (result.isConfirmed) {
      localStorage.clear();
      this.appService.setToken('');
      this.appService.roleName.set(null);
      this.appService.firstName.set(null);
      this.appService.lastName.set(null);
      this.appService.speciality.set(null);
      this.appService.section.set(null);
      this.appService.taj.set(null);
      this.router.navigate(['/']);
    }
  });
}
}
