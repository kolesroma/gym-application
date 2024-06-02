import { Component } from '@angular/core';
import {DatePipe} from "@angular/common";
import {TrainingService} from "../training/training.service";
import {ToastrService} from "ngx-toastr";
import {HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-training-add-student',
  templateUrl: './training-add-student.component.html',
  styleUrls: ['./training-add-student.component.css', '../button/button.component.css']
})
export class TrainingAddStudentComponent {
  name: string = '';
  trainingType: string = '';
  trainerName: string = '';
  date: string = '';
  duration: number = 60;

  constructor(private datePipe: DatePipe, private trainingService: TrainingService, private toastr: ToastrService, private router: Router) {
  }

  reformatDate(dateRaw: string) {
    return this.datePipe.transform(dateRaw, 'yyyy-MM-dd') || '';
  }

  addTrainingStudent() {
    const training = {
      "traineeName": null,
      "trainerName": this.trainerName,
      "trainingName": this.name,
      "trainingDate": this.reformatDate(this.date),
      "trainingDuration": this.duration,
      "trainingType": this.trainingType
    };
    this.trainingService.saveTraining(training)
      .subscribe(response => {
        this.toastr.success("Training was added", "Success");
        this.router.navigate(["/home"]);
      }, (error: HttpErrorResponse) => {
        this.toastr.error(error.error.message, "Error");
      });
  }
}
