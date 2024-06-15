import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { AgGridModule } from 'ag-grid-angular';
import { RouterModule } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import {CommonModule, DatePipe} from "@angular/common";
import { RoleSelectionComponent } from './role-selection/role-selection.component';
import { RegistrationStudentComponent } from './registration-student/registration-student.component';
import { RegistrationTrainerComponent } from './registration-trainer/registration-trainer.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { ButtonComponent } from './button/button.component';
import { JoinUsBoxComponent } from './join-us-box/join-us-box.component';
import { BoxComponent } from './box/box.component';
import { MyProfileComponent } from './my-profile/my-profile.component';
import { TableComponent } from './table/table.component';
import { NavigationComponent } from './navigation/navigation.component';
import { BreadcrumbComponent } from './breadcrumb/breadcrumb.component';
import { ModalBoxComponent } from './modal-box/modal-box.component';
import {MatButtonModule} from "@angular/material/button";
import { ToasterComponent } from './toaster/toaster.component';
import { DatePickerComponent } from './date-picker/date-picker.component';
import {MatInputModule} from "@angular/material/input";
import {MatDatepickerModule} from "@angular/material/datepicker";
import { MiniProfileComponent } from './mini-profile/mini-profile.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { HomeComponent } from './home/home.component';
import { RegistrationVerificationComponent } from './registration-verification/registration-verification.component';
import {
  FormFieldTrainingsStudent,
  FormFieldTrainingsTrainer,
  TrainingComponent
} from './training/training.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { AuthGuardComponent } from './auth-guard/auth-guard.component';
import {StudentPageComponent} from "./student/student.component";
import {HttpClientModule} from "@angular/common/http";
import {ActionReducer, ActionReducerMap, MetaReducer, StoreModule} from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import {StudentEffects} from "./student/student.effects";
import {StudentService} from "./student/student.service";
import {studentListReducer, studentRegistrationReducer} from "./student/student.reducer";
import {AuthService} from "./auth/auth.service";
import {authReducer} from "./auth/auth.reducer";
import { localStorageSync } from 'ngrx-store-localstorage';
import {AuthEffects} from "./auth/auth.effects";
import {AuthGuardLoggedInComponent} from "./auth-guard/auth-guard-logged-in.component";
import {TrainerEffects} from "./trainer/trainer.effects";
import {trainerRegistrationReducer} from "./trainer/trainer.reducer";
import {TrainerService} from "./trainer/trainer.service";
import {MyProfileService} from "./my-profile/my-profile.service";
import {MatSelectModule} from "@angular/material/select";
import {TrainingService} from "./training/training.service";
import { TrainingAddStudentComponent } from './training-add-student/training-add-student.component';
import { TrainingAddTrainerComponent } from './training-add-trainer/training-add-trainer.component';
import { MyTrainersComponent } from './my-trainers/my-trainers.component';
import { DietComponent } from './diet/diet.component';
import {DietService} from "./diet/diet.service";
import { NotificationSettingsComponent } from './notification-settings/notification-settings.component';
import {NotificationService} from "./notification-settings/notification.service";
import { MyTraineesComponent } from './my-trainees/my-trainees.component';
import { ReportsComponent } from './reports/reports.component';
import {ReportService} from "./reports/reports.service";
import {GridApi} from "ag-grid-community";

export function localStorageSyncReducer(reducer: ActionReducer<any>): ActionReducer<any> {
  return localStorageSync({ keys: ['isAuthenticated', 'auth'], rehydrate: true })(reducer);
}
const metaReducers: Array<MetaReducer<any, any>> = [localStorageSyncReducer];

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RoleSelectionComponent,
    RegistrationStudentComponent,
    RegistrationTrainerComponent,
    HeaderComponent,
    FooterComponent,
    ButtonComponent,
    JoinUsBoxComponent,
    BoxComponent,
    MyProfileComponent,
    TableComponent,
    NavigationComponent,
    BreadcrumbComponent,
    ToasterComponent,
    MiniProfileComponent,
    PageNotFoundComponent,
    HomeComponent,
    RegistrationVerificationComponent,
    TrainingComponent,
    ChangePasswordComponent,
    StudentPageComponent,
    TrainingAddStudentComponent,
    TrainingAddTrainerComponent,
    MyTrainersComponent,
    DietComponent,
    ReportsComponent,
    NotificationSettingsComponent,
    MyTraineesComponent,
    ReportsComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    CommonModule,
    AgGridModule,
    RouterModule.forRoot([
      {path: '', component: HomeComponent},
      {path: 'home', component: HomeComponent},
      {path: 'login', component: LoginComponent, canActivate: [AuthGuardLoggedInComponent]},
      {path: 'my-account', component: MyProfileComponent, canActivate: [AuthGuardComponent]},
      {path: 'training', component: TrainingComponent, canActivate: [AuthGuardComponent]},
      {path: 'join-us', component: JoinUsBoxComponent},
      {path: 'change-password', component: ChangePasswordComponent, canActivate: [AuthGuardComponent]},
      {path: 'registration', component: RoleSelectionComponent, canActivate: [AuthGuardLoggedInComponent]},
      {path: 'registration/student', component: RegistrationStudentComponent, canActivate: [AuthGuardLoggedInComponent]},
      {path: 'registration/trainer', component: RegistrationTrainerComponent, canActivate: [AuthGuardLoggedInComponent]},
      {path: 'training/add/student', component: TrainingAddStudentComponent, canActivate: [AuthGuardComponent]},
      {path: 'training/add/trainer', component: TrainingAddTrainerComponent, canActivate: [AuthGuardComponent]},
      {path: 'registration-verification', component: RegistrationVerificationComponent, canActivate: [AuthGuardComponent]},
      {path: 'diet', component: DietComponent, canActivate: [AuthGuardComponent]},
      {path: 'notification-settings', component: NotificationSettingsComponent, canActivate: [AuthGuardComponent]},
      {path: 'reports', component: ReportsComponent, canActivate: [AuthGuardComponent]},
      {path: '**', component: PageNotFoundComponent}
    ]),
    MatButtonModule,
    ModalBoxComponent,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    MatInputModule,
    MatDatepickerModule,
    DatePickerComponent,
    HttpClientModule,
    StoreModule.forRoot(
      {
        students: studentListReducer,
        studentRegistration: studentRegistrationReducer,
        trainerRegistration: trainerRegistrationReducer,
        auth: authReducer
      },
      {metaReducers}
    ),
    EffectsModule.forRoot([
      StudentEffects,
      TrainerEffects,
      AuthEffects
    ]),
    MatSelectModule,
    FormFieldTrainingsStudent,
    FormFieldTrainingsTrainer
  ],
  providers: [
    AuthGuardComponent,
    AuthGuardLoggedInComponent,
    StudentService,
    TrainerService,
    AuthService,
    ModalBoxComponent,
    MyProfileService,
    DatePipe,
    TrainingService,
    DietService,
    NotificationService,
    ReportService
  ],
  exports: [
    ButtonComponent,
    ButtonComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
