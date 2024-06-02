import { createAction, props } from '@ngrx/store';
import {Student} from "./student.model";

export const loadStudents = createAction('[Students] Load');
export const loadStudentsSuccess = createAction('[Students] Load Success', props<{ students: Student[] }>());
export const loadStudentsFailure = createAction('[Students] Load Failure', props<{ error: any }>());

export const registerStudent = createAction('[Students] Register', props<Student>());
export const registerStudentSuccess = createAction('[Students] Register Success', props<{ username: string, password: string }>());
export const registerStudentFailure = createAction('[Students] Register Failure', props<{ error: any }>());
