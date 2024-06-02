import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { loadStudents } from './student.actions';
import {Student} from "./student.model";

@Component({
  selector: 'app-students',
  templateUrl: './student.component.html'
})
export class StudentPageComponent implements OnInit {
  students$: Observable<Student[]>;

  constructor(private store: Store<{ students: Student[] }>) {
    this.students$ = store.select('students');
  }

  ngOnInit() {
    this.store.dispatch(loadStudents());
  }
}
