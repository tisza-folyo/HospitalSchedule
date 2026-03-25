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
  sections: string[] = [];

  constructor(private router: Router) { }


  onSetSection() {
    const optionsHtml = this.appService.sections().map(name =>
      `<option value="${name}">${name}</option>`
    ).join('');

    Swal.fire({
      title: 'Osztály beállítása',
      html: `
      <div class="text-start">
        <label class="form-label small text-muted mb-1">Válasszon osztályt:</label>
        <select id="sectionSelect" class="form-select" style="height: 45px;">
          <option value="" disabled selected hidden>Válasszon osztályt...</option>
          ${optionsHtml}
        </select>
      </div>
    `,
      confirmButtonText: 'Mentés',
      confirmButtonColor: '#198754',
      showCancelButton: true,
      cancelButtonText: 'Mégse',
      cancelButtonColor: '#6c757d',
      preConfirm: () => {
        const sectionId = (document.getElementById('sectionSelect') as HTMLSelectElement).value;
        if (!sectionId) {
          Swal.showValidationMessage('Kérjük, válasszon ki egy osztályt!');
          return false;
        }
        return sectionId;
      }
    }).then((result) => {
      if (result.isConfirmed) {
        const sec = result.value;
        this.appService.updateSection(this.appService.getTaj()!, sec).subscribe({
          next: () => {
            this.appService.successPopup("Siker!");
            this.appService.setSection(sec);
          },
          error: (error) => {
            this.appService.errorPopup("Hiba!");
          }
        });

      }
    });
  }

  onChangePassword() {
    const eyeSvg = `<svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#434343"><path d="M607.5-372.5Q660-425 660-500t-52.5-127.5Q555-680 480-680t-127.5 52.5Q300-575 300-500t52.5 127.5Q405-320 480-320t127.5-52.5Zm-204-51Q372-455 372-500t31.5-76.5Q435-608 480-608t76.5 31.5Q588-545 588-500t-31.5 76.5Q525-392 480-392t-76.5-31.5ZM214-281.5Q94-363 40-500q54-137 174-218.5T480-800q146 0 266 81.5T920-500q-54 137-174 218.5T480-200q-146 0-266-81.5ZM480-500Zm207.5 160.5Q782-399 832-500q-50-101-144.5-160.5T480-720q-113 0-207.5 59.5T128-500q50 101 144.5 160.5T480-280q113 0 207.5-59.5Z"/></svg>`;

    Swal.fire({
      title: 'Jelszó megváltoztatása',
      html: `
      <div class="text-start">
        <div class="input-group mb-3">
          <input type="password" id="oldPassword" class="form-control" style="height: 45px;" placeholder="Adja meg a jelenlegi jelszót">
          <button class="btn btn-outline-secondary d-flex align-items-center" type="button" id="toggleOld" style="border-color: #ced4da;">
            ${eyeSvg}
          </button>
        </div>

        <div class="input-group mb-3">
          <input type="password" id="newPassword" class="form-control" style="height: 45px;" placeholder="Adja meg az új jelszót (min. 8 karakter)">
          <button class="btn btn-outline-secondary d-flex align-items-center" type="button" id="toggleNew" style="border-color: #ced4da;">
            ${eyeSvg}
          </button>
        </div>
      </div>
    `,
      confirmButtonText: 'Módosítás',
      confirmButtonColor: '#198754',
      showCancelButton: true,
      cancelButtonText: 'Mégse',
      cancelButtonColor: '#6c757d',
      didOpen: () => {
        const setupToggle = (buttonId: string, inputId: string) => {
          const btn = document.getElementById(buttonId);
          const input = document.getElementById(inputId) as HTMLInputElement;
          if (btn && input) {
            const show = () => {
              input.type = 'text';
              btn.innerHTML = eyeSvg;
            };
            const hide = () => {
              input.type = 'password';
              btn.innerHTML = eyeSvg;
            };

            btn.addEventListener('mousedown', show);
            btn.addEventListener('mouseup', hide);
            btn.addEventListener('mouseleave', hide);
            btn.addEventListener('touchstart', (e) => { e.preventDefault(); show(); });
            btn.addEventListener('touchend', hide);
          }
        };
        setupToggle('toggleOld', 'oldPassword');
        setupToggle('toggleNew', 'newPassword');
      },
      preConfirm: () => {
        const oldPassword = (document.getElementById('oldPassword') as HTMLInputElement).value;
        const newPassword = (document.getElementById('newPassword') as HTMLInputElement).value;
        if (!oldPassword || !newPassword) {
          Swal.showValidationMessage('Kérjük, töltsön ki minden mezőt!');
          return false;
        }
        if (newPassword.length < 8) {
          Swal.showValidationMessage('Az új jelszó túl rövid!');
          return false;
        }
        return { oldPassword, newPassword };
      }
    }).then((result) => {
      if (result.isConfirmed) {
        const data = result.value;
        this.appService.updatePassword(this.appService.getTaj()!, this.appService.getRoleName()!, data.oldPassword, data.newPassword).subscribe({
          next: () => this.appService.successPopup("Siker!"),
          error: (error) => {
            if (error.status === 409) {
              this.appService.errorPopup("Helytelen jelszót adtál meg!");
            } else {
              this.appService.errorPopup("Hiba!");
              console.log(error.error.message);

            }
          }
        });
      }
    });
  }

  private loadSections() {

  }

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
        this.appService.logout();
      }
    });
  }
}
