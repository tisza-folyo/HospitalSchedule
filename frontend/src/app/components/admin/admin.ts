import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AdminService } from './admin.service';
import { AppService } from '../../app.service';
import { WorkModel } from '../work.model';
import { CareModel } from '../care.model';

@Component({
  selector: 'app-admin',
  imports: [FormsModule,CommonModule],
  templateUrl: './admin.html',
  styleUrl: './admin.css',
})
export class Admin {
  private interval = 30;
  appService = inject(AppService);
  works: WorkModel[] = [];
  cares: CareModel[] = [];

  startDate: string = '';
  endDate: string = '';

  constructor(private adminService: AdminService){}



  resetField(field:string){
    if(field === "date-to-date"){
      this.startDate = '';
      this.endDate = '';
      this.works = [];
    }
  }

  loadWorks(){
    this.adminService.getAllWorksBetween(this.startDate,this.endDate).subscribe({
      next: (response) => {
        this.works = response.data;
      },
      error: (err) => {
        console.error('Error fetching', err);
        console.log(err.error.message);
        
      }
    });
  }

  loadCares(){
    this.adminService.getAllActiveCares().subscribe({
      next: (response) => {
        this.cares = response.data;
      },
      error: (err) => {
        console.error('Error fetching', err);
        console.log(err.error.message);
        
      }
    });
  }

  get maxEndDate(): string {
    if (!this.startDate) return '';
    const date = new Date(this.startDate);
    date.setDate(date.getDate() + this.interval);
    return date.toISOString().split('T')[0];
  }

  get minStartDate(): string {
    if (!this.endDate) return '';
    const date = new Date(this.endDate);
    date.setDate(date.getDate() - this.interval);
    return date.toISOString().split('T')[0];
  }
}
