import { Component } from '@angular/core';
import {Observable} from "rxjs";
import {TrainerRegistrationState} from "../trainer/trainer.reducer";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {registerStudent} from "../student/student.actions";
import {registerTrainer} from "../trainer/trainer.actions";
import {Trainer} from "../trainer/trainer.model";

@Component({
  selector: 'app-registration-trainer',
  templateUrl: './registration-trainer.component.html',
  styleUrls: ['./registration-trainer.component.css']
})
export class RegistrationTrainerComponent {
  trainerRegistration$: Observable<TrainerRegistrationState>;

  constructor(private store: Store<{ trainerRegistration: TrainerRegistrationState }>, private router: Router) {
    this.trainerRegistration$ = this.store.select("trainerRegistration");
    this.trainerRegistration$.subscribe((registrationState) => {
      this.credentials.username = registrationState.username;
      this.credentials.password = registrationState.password;
      this.errorText = registrationState.error;
    });
  }

  formData: Trainer = {
    username: '',
    firstName: '',
    lastName: '',
    email: '',
    specialization: ''
  };

  credentials: {username: string | null, password: string | null} = {
    username: null,
    password: null
  };

  errorText: string | null = null;

  isFirstNameInvalid: boolean = false;
  isLastNameInvalid: boolean = false;
  isSpecializationValid: boolean = false;
  isCredentialVisible: boolean = false;
  isLoading: boolean = false;

  onSubmitRegisterTrainer() {
    if (this.formData.firstName.length === 0 || this.formData.lastName.length === 0 || this.formData.specialization.length === 0) {
      this.isFirstNameInvalid = this.formData.firstName.length === 0;
      this.isLastNameInvalid= this.formData.lastName.length === 0;
      this.isSpecializationValid= this.formData.specialization.length === 0;
      return;
    } else {
      this.isFirstNameInvalid= false;
      this.isLastNameInvalid= false;
      this.isSpecializationValid= false;
    }

    this.isLoading = true;
    setTimeout(() => {
      this.store.dispatch(registerTrainer(this.formData));
      console.log("registered trainer");
      this.isLoading = false;
    }, 2000);

    this.isCredentialVisible = true;
  }

  closeCredentials() {
    this.isCredentialVisible = false;
    this.router.navigate(["/login"]);
  }
}
