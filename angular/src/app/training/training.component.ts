import {Component} from '@angular/core';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {FormsModule} from "@angular/forms";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from "@angular/material/core";
import {DatePipe} from "@angular/common";
import {TrainingService} from "./training.service";
import {MyProfileService} from "../my-profile/my-profile.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-training',
  templateUrl: './training.component.html',
  styleUrls: ['./training.component.css']
})
export class TrainingComponent {
  role: string = '';

  rowData: any = [];

  columnDataStudent = [
    {field: 'trainingDate'},
    {field: 'trainingName'},
    {field: 'trainingType'},
    {field: 'trainerName'},
    {field: 'trainingDuration'},
  ];

  columnDataTrainer = [
    {field: 'trainingDate'},
    {field: 'trainingName'},
    {field: 'trainingType'},
    {field: 'traineeName'},
    {field: 'trainingDuration'},
  ];

  constructor(private trainingService: TrainingService, private profileService: MyProfileService, private router: Router) {
    this.profileService.getUserRole()
      .subscribe(response => {
        this.role = response.authority;
      });
    this.trainingService.getRowData().subscribe(rowData => {
      this.rowData = rowData;
    });
  }

  addTrainingStudent() {
    this.router.navigate(["training/add/student"]);
  }

  addTrainingTrainer() {
    this.router.navigate(["training/add/trainer"]);
  }
}

@Component({
  selector: 'app-form-filed-trainings-student',
  templateUrl: 'form-field-trainings-student.html',
  styleUrls: ['training.component.css', '../button/button.component.css'],
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, MatSelectModule, MatDatepickerModule, MatNativeDateModule, FormsModule],
})
export class FormFieldTrainingsStudent {
  trainerName: string = '';
  trainingType: string = '';
  dateFrom: string = '';
  dateTo: string = '';

  constructor(private datePipe: DatePipe, private trainingService: TrainingService) {
  }

  reformatDate(dateRaw: string) {
    return this.datePipe.transform(dateRaw, 'yyyy/MM/dd') || '';
  }

  searchTrainingsForStudent() {
    this.trainingService.getTrainings()
      .subscribe(response => {
        this.trainingService.setRowData(this.filterTrainings(response));
      });
  }

  filterTrainings(response: any) {
    if (this.trainerName) {
      response = response.filter((training: any) => (training.trainerName ??= '').toLowerCase() === this.trainerName.toLowerCase());
    }
    if (this.trainingType) {
      response = response.filter((training: any) => (training.trainingType ??= '').toLowerCase() === this.trainingType.toLowerCase());
    }
    if (this.dateFrom && this.dateTo) {
      response = response.filter((training: any) => this.isDateInRange(
        new Date(training.trainingDate),
        new Date(this.dateFrom),
        new Date(this.dateTo)
      ));
    }
    return response;
  }

  isDateInRange(date: Date, dateFrom: Date, dateTo: Date): boolean {
    return date >= dateFrom && date <= dateTo;
  }
}

@Component({
  selector: 'app-form-filed-trainings-trainer',
  templateUrl: 'form-field-trainings-trainer.html',
  styleUrls: ['training.component.css', '../button/button.component.css'],
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, MatSelectModule, MatDatepickerModule, MatNativeDateModule, FormsModule],
})
export class FormFieldTrainingsTrainer {
  studentName: string = '';
  trainingType: string = '';
  dateFrom: string = '';
  dateTo: string = '';

  constructor(private datePipe: DatePipe, private trainingService: TrainingService) {
  }

  reformatDate(dateRaw: string) {
    return this.datePipe.transform(dateRaw, 'yyyy/MM/dd') || '';
  }

  searchTrainingsForTrainer() {
    this.trainingService.getTrainings()
      .subscribe(response => {
        this.trainingService.setRowData(this.filterTrainings(response));
      });
  }

  filterTrainings(response: any) {
    if (this.studentName) {
      response = response.filter((training: any) => (training.traineeName ??= '').toLowerCase() === this.studentName.toLowerCase());
    }
    if (this.trainingType) {
      response = response.filter((training: any) => (training.trainingType ??= '').toLowerCase() === this.trainingType.toLowerCase());
    }
    if (this.dateFrom && this.dateTo) {
      response = response.filter((training: any) => this.isDateInRange(
        new Date(training.trainingDate),
        new Date(this.dateFrom),
        new Date(this.dateTo)
      ));
    }
    return response;
  }

  isDateInRange(date: Date, dateFrom: Date, dateTo: Date): boolean {
    return date >= dateFrom && date <= dateTo;
  }
}
