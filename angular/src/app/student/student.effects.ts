import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {of} from 'rxjs';
import {map, mergeMap, catchError} from 'rxjs/operators';
import {StudentService} from './student.service';
import * as StudentsActions from './student.actions';
import {loadStudentsFailure, registerStudentFailure} from "./student.actions";
import {Student} from "./student.model";

@Injectable()
export class StudentEffects {

  loadStudents$ = createEffect(() => this.actions.pipe(
      ofType(StudentsActions.loadStudents),
      mergeMap(() => this.studentsService.getAll()
        .pipe(
          map(students => StudentsActions.loadStudentsSuccess({students})),
          catchError(error => of(loadStudentsFailure(error)))
        ))
    )
  );

  registerStudent$ = createEffect(() => this.actions.pipe(
      ofType(StudentsActions.registerStudent.type),
      mergeMap((action: Student) => this.studentsService.registerStudent(action)
        .pipe(
          map(response => StudentsActions.registerStudentSuccess({username: response.username, password: response.password})),
          catchError(error => of(registerStudentFailure(error)))
        ))
    )
  );

  constructor(private actions: Actions, private studentsService: StudentService) {
  }
}
