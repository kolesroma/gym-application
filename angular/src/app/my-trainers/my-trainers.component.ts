import {Component} from '@angular/core';
import {TrainingService} from "../training/training.service";
import {DialogAddTrainer, ModalBoxComponent} from "../modal-box/modal-box.component";

@Component({
  selector: 'app-my-trainers',
  templateUrl: './my-trainers.component.html',
  styleUrls: ['./my-trainers.component.css', '../button/button.component.css']
})
export class MyTrainersComponent {

  rowData: any = [];

  columnData = [
    {field: 'trainerName'},
    {field: 'trainingType'}
  ];

  constructor(private trainingService: TrainingService, private modalBox: ModalBoxComponent,) {
    this.trainingService.getTrainings()
      .subscribe(response => {
        this.rowData = this.filterUnique(response);
      });
  }

  filterUnique(rowData: any[]): any[] {
    const uniquePairs: any[] = [];

    rowData.forEach(training => {
      const existingPair = uniquePairs.find(pair =>
        pair.trainerName === training.trainerName && pair.trainingType === training.trainingType
      );

      if (!existingPair) {
        uniquePairs.push({
          trainerName: training.trainerName,
          trainingType: training.trainingType,
        });
      }
    });

    return uniquePairs;
  }

  openAddTrainerBox() {
    this.modalBox.openDialog(DialogAddTrainer, '0ms', '100ms');
  }
}
