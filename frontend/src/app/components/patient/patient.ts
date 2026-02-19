import { Component } from '@angular/core';

@Component({
  selector: 'app-patient',
  imports: [],
  templateUrl: './patient.html',
  styleUrl: './patient.css',
})
export class Patient {

  sections: string[] = [];
  doctors: string[] = [];
  section: string = '';
  date: string = '';
  availableSlots: any[] = [];
  selectedSlot: any = null;
}
