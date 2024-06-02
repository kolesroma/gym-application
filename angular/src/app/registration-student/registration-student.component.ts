import {Component} from '@angular/core';
import {StudentRegistrationState} from "../student/student.reducer";
import {Observable} from "rxjs";
import {Store} from "@ngrx/store";
import {registerStudent} from "../student/student.actions";
import {Student} from "../student/student.model";
import {Router} from "@angular/router";

@Component({
  selector: 'app-registration-student',
  templateUrl: './registration-student.component.html',
  styleUrls: ['./registration-student.component.css']
})
export class RegistrationStudentComponent {
  studentRegistration$: Observable<StudentRegistrationState>;

  constructor(private store: Store<{ studentRegistration: StudentRegistrationState }>, private router: Router) {
    this.studentRegistration$ = this.store.select("studentRegistration");
    this.studentRegistration$.subscribe((registrationState) => {
      this.credentials.username = registrationState.username;
      this.credentials.password = registrationState.password;
      this.errorText = registrationState.error;
    });
  }

  formData: Student = {
    firstName: '',
    lastName: '',
    email: '',
    dateOfBirth: '',
    address: '',
    trainers: []
  };

  credentials: {username: string | null, password: string | null} = {
    username: null,
    password: null
  };

  errorText: string | null = null;

  isFirstNameInvalid: boolean = false;
  isLastNameInvalid: boolean = false;
  isEmailInvalid: boolean = false;
  isLoading: boolean = false;
  isCredentialVisible: boolean = false;

  onSubmitRegisterStudent() {
    if (this.formData.firstName.length === 0 || this.formData.lastName.length === 0 || this.formData.email.length === 0) {
      this.isFirstNameInvalid = this.formData.firstName.length === 0;
      this.isLastNameInvalid = this.formData.lastName.length === 0;
      this.isEmailInvalid = this.formData.email.length === 0;
      return;
    } else {
      this.isFirstNameInvalid = false;
      this.isLastNameInvalid = false;
      this.isEmailInvalid = false;
    }

    this.isLoading = true;
    setTimeout(() => {
      this.store.dispatch(registerStudent(this.formData));
      console.log("registered student");
      this.isLoading = false;
    }, 2000);

    this.isCredentialVisible = true;
  }

  closeCredentials() {
    this.isCredentialVisible = false;
    this.router.navigate(["/login"]);
  }
}
