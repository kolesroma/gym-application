import {Component} from '@angular/core';
import {ReportService} from "./reports.service";
import {ColumnApi, GridApi, GridReadyEvent} from "ag-grid-community";

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.css']
})
export class ReportsComponent {

  private gridApiVisited!: GridApi;
  private gridColumnApiVisited!: ColumnApi;
  private gridApiVelocity!: GridApi;
  private gridColumnApiVelocity!: ColumnApi;

  rowDataVisited: any = [];
  rowDataVelocity: any = [];

  columnDataVisited = [
    {field: 'traineeUsername'},
    {field: 'totalDuration'},
    {field: 'visitedCount'},
    {field: 'totalCount'}
  ];

  columnDataVelocity = [
    {field: 'trainingDate'},
    {field: 'trainingCount'},
    {field: 'totalDuration'},
    {field: 'velocity'}
  ];

  constructor(private reportService: ReportService) {
  }

  onGenerateVisitedReportClick() {
    this.reportService.generateVisitedReport()
      .subscribe(response => {
        this.rowDataVisited = response;
        this.gridApiVisited.setRowData(this.rowDataVisited);
        this.gridApiVisited.exportDataAsCsv();
      });
  }

  onGridReadyVisited(params: GridReadyEvent) {
    this.gridApiVisited = params.api;
    this.gridColumnApiVisited = params.columnApi;
  }

  onGenerateVelocityReportClick() {
    this.reportService.generateVelocityReport()
      .subscribe(response => {
        this.rowDataVelocity = response;
        this.gridApiVelocity.setRowData(this.rowDataVelocity);
        this.gridApiVelocity.exportDataAsCsv();
      });
  }

  onGridReadyVelocity(params: GridReadyEvent) {
    this.gridApiVelocity = params.api;
    this.gridColumnApiVelocity = params.columnApi;
  }

}
