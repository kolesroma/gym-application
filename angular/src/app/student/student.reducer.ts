import { createReducer, on } from '@ngrx/store';
import {
  loadStudents,
  loadStudentsSuccess,
  loadStudentsFailure,
  registerStudent,
  registerStudentSuccess, registerStudentFailure
} from './student.actions';
import {Student} from "./student.model";

export const initialStateStudents: Student[] = [];

export const studentListReducer = createReducer(
  initialStateStudents,
  on(loadStudents, state => state),
  on(loadStudentsSuccess, (state, { students }) => [...students]),
  on(loadStudentsFailure, (state, { error }) => { console.error(error); return state; })
);

export interface StudentRegistrationState {
  username: string | null;
  password: string | null;
  error: string | null;
}

export const initialStateRegistration: StudentRegistrationState = {
  username: null,
  password: null,
  error: null,
};

export const studentRegistrationReducer = createReducer(
  initialStateRegistration,
  on(registerStudent, state => state),
  on(registerStudentSuccess, (state, { username, password }) => ({...state, username, password, error: null})),
  on(registerStudentFailure, (state, { error }) => ({...state, error: error.message}))
);
