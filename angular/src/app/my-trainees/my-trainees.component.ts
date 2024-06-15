import { Component } from '@angular/core';
import {TrainingService} from "../training/training.service";
import {ModalBoxComponent} from "../modal-box/modal-box.component";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";

@Component({
  selector: 'app-my-trainees',
  templateUrl: './my-trainees.component.html',
  styleUrls: ['./my-trainees.component.css']
})
export class MyTraineesComponent {

  rowData: any = [];

  columnData = [
    {field: 'traineeName'},
    {field: 'trainingType'},
    {field: 'trainingName'},
    {field: 'trainingDate'},
    {field: 'visited', onCellClicked: (params: any) => this.onCellClicked(params) },
    {field: 'id'}
  ];

  constructor(private trainingService: TrainingService, private modalBox: ModalBoxComponent, private toaster: ToastrService, private router: Router) {
    this.trainingService.getTrainings()
      .subscribe(response => {
        this.rowData = response;
      });
  }

  onCellClicked(event: any) {
    const data = event.data;

    this.trainingService.toggleTrainingStatus(data.id)
      .subscribe(response => {
        this.trainingService.getTrainings()
          .subscribe(response => {
            this.rowData = response;
          });
        this.toaster.success("Updated training visited status")
      });

  }

  routerToReportPage() {
    this.router.navigate(["/reports"]);
  }

}
