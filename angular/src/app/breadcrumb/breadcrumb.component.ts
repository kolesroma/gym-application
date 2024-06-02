import { Component } from '@angular/core';
import { IBreadCrumb } from './breadcrumb';

@Component({
  selector: 'app-breadcrumb',
  templateUrl: './breadcrumb.component.html',
  styleUrls: ['./breadcrumb.component.css']
})
export class BreadcrumbComponent {
  public breadcrumbs: IBreadCrumb[] = [
    {label: "some1 > ", url: "url1"},
    {label: " some2 > ", url: "url2"},
    {label: " some3", url: "url3"}
  ];
}
