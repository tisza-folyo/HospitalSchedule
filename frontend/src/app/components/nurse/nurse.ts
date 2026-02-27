import { Component, inject } from '@angular/core';
import { CareModel } from '../care.model';
import { NurseService } from './nurse.service';
import { AppService } from '../../app.service';
import { CommonModule } from '@angular/common';
import { NurseModel } from './nurse.model';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-nurse',
  imports: [FormsModule,CommonModule],
  templateUrl: './nurse.html',
  styleUrl: './nurse.css',
})
export class Nurse {
  appService = inject(AppService);
  cares: CareModel[] = [];
  care: CareModel | null = null;
  nurses: NurseModel[] = [];
  filteredNurses: NurseModel[] = [];
  nurse: NurseModel | null = null;
  filter: string = '';


  constructor(private nurseService: NurseService){}

  ngOnInit(){
    this.loadCares(this.appService.getTaj()!, true);
  }

  navigateToOwnCares(){
    this.loadCares(this.appService.getTaj()!, true);
    this.care = null;
  }

  navigateToCares(){
    this.loadCaresForCare();
  }

  onAssignCare(c: CareModel){
    this.nurseService.updateNurse(this.appService.getTaj()!,c.patient.taj, this.appService.getTaj()!).subscribe({
      next: (response) => {
        this.appService.successPopup("Felvéve!");
        this.loadCaresForCare();
      },
      error: (err) => {
        this.appService.errorPopup("Hiba!");
        console.log(err.error.message);
        
      }
    });
  }

  selectCare(c: CareModel){
    if(this.care == c){
      this.care = null;
    }else{
      this.care = c;
      this.loadFreeNurses();
    }
  }

  selectNurse(n: NurseModel, c: CareModel){
    if(n === null) return;
    if(c === null) return;

    this.nurseService.updateNurse(n.taj,c.patient.taj, this.appService.getTaj()!).subscribe({
      next: (response) => {
        this.appService.successPopup("Átadva!");
        this.loadCares(this.appService.getTaj()!,true);
      },
      error: (err) => {
        this.appService.errorPopup("Hiba!");
      }
    });

  }

  onSearchNurse(){
    this.filteredNurses = this.nurses.filter((n: NurseModel) => {
      const fullName = this.appService.nameConcatenator(n.firstName, n.lastName).toLowerCase();
      return fullName.includes(this.filter.toLowerCase()) || n.taj.toString().toLowerCase().includes(this.filter.toLowerCase());
    });
  }

  private loadCares(taj:string,active: boolean){
    if(active){
      this.nurseService.getAllActiveCares(taj).subscribe({
      next: (response) => {
        this.cares=response.data;
      },
      error: (err) => {
        console.error('Error fetching patients:', err);
        
      }
    });
    }else if(!active){
      this.nurseService.getAllNonActiveCares(taj).subscribe({
      next: (response) => {
        this.cares=response.data;
      },
      error: (err) => {
        console.error('Error fetching patients:', err);
      }
    });
    }
  }

  private loadFreeNurses(){
    this.nurseService.getAllFreeNurses().subscribe({
      next: (response) => {
        this.nurses=response.data.filter(n => this.appService.getTaj() !== n.taj);
        this.filteredNurses = this.nurses;
        this.filter = '';
      },
      error: (err) => {
        console.error('Error fetching patients:', err);
        
      }
    });
  }

  private loadCaresForCare(){
    this.nurseService.getAllCaresForCare().subscribe({
      next: (response) => {
        this.cares=response.data;
      },
      error: (err) => {
        console.error('Error fetching patients:', err);
        
      }
    });
  }
}
