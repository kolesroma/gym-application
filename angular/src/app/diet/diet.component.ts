import {Component} from '@angular/core';
import {DietService} from "./diet.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-diet',
  templateUrl: './diet.component.html',
  styleUrls: ['./diet.component.css']
})
export class DietComponent {

  rowData: any = [];
  rowDataAll: any = [];

  columnDataStudent = [
    {field: 'massInGrams'},
    {field: 'desc'},
    {field: 'proteins'},
    {field: 'fats'},
    {field: 'carbohydrates'},
  ];

  createDietDto: { numberOfMeals: number, kcal: number, proteinsCoefficient: number, fatsCoefficient: number, carbohydratesCoefficient: number } = {
    numberOfMeals: 5,
    kcal: 2500,
    proteinsCoefficient: 0.4,
    fatsCoefficient: 0.3,
    carbohydratesCoefficient: 0.3
  }

  constructor(private dietService: DietService) {
  }

  onSubmit() {
    this.dietService.createDiet(this.createDietDto)
      .subscribe(response => {
        console.log(response);
        this.rowDataAll = response;
        this.rowData = response[0].dishes;
      });
  }

  invalidCreateDto(): boolean {
    return this.createDietDto.proteinsCoefficient + this.createDietDto.fatsCoefficient + this.createDietDto.carbohydratesCoefficient !== 1
      || this.createDietDto.kcal > 5000;
  }

  downloadRation() {
    const json = JSON.stringify(this.rowDataAll);
    const blob = new Blob([json], {type: 'application/json'});
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = "daily_ration";
    link.click();
  }

}
