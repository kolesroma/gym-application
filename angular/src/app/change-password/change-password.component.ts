import {Component} from '@angular/core';
import {ChangePasswordModel} from "../student/student.model";
import {AuthService} from "../auth/auth.service";
import {HttpErrorResponse} from "@angular/common/http";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent {
  formData: ChangePasswordModel = {
    oldPassword: '',
    newPassword: '',
    newPasswordRepeat: ''
  };

  isOldPasswordInvalid: boolean = false;
  isNewPasswordInvalid: boolean = false;
  isNewPasswordRepeatInvalid: boolean = false;
  isPasswordsEqualityInvalid: boolean = false;
  isLoading: boolean = false;

  onSubmitChangePassword() {
    if (this.formData.oldPassword.length === 0
      || this.formData.newPassword.length === 0
      || this.formData.newPasswordRepeat.length === 0) {
      this.isOldPasswordInvalid = this.formData.oldPassword.length === 0;
      this.isNewPasswordInvalid = this.formData.newPassword.length === 0;
      this.isNewPasswordRepeatInvalid = this.formData.newPasswordRepeat.length === 0;
      return;
    } else {
      this.isOldPasswordInvalid = false;
      this.isNewPasswordInvalid = false;
      this.isNewPasswordRepeatInvalid = false;
    }
    if (this.formData.newPassword !== this.formData.newPasswordRepeat) {
      this.isPasswordsEqualityInvalid = true;
      return;
    } else {
      this.isPasswordsEqualityInvalid = false;
    }

    this.isLoading = true;
    setTimeout(() => {
      this.authService.changePassword(this.formData)
        .subscribe(response => {
            this.toastr.success(response.message, "Changed password");
            this.router.navigate(["/home"]);
          },
          (error: HttpErrorResponse) => {
            this.toastr.error(error.error.message, "Error happened");
          });
      this.isLoading = false;
    }, 2000);
  }

  constructor(private authService: AuthService, private toastr: ToastrService, private router: Router) {
  }

}
