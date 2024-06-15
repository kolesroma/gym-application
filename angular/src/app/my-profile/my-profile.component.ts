import {Component} from '@angular/core';
import {DialogDeleteAccount, DialogUpdatePhoto, ModalBoxComponent} from "../modal-box/modal-box.component";
import {MyProfileService} from "./my-profile.service";
import {FullUserModel} from "./full-user.model";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";

@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.css']
})
export class MyProfileComponent {
  isEditing: boolean = false;
  role: string = '';

  profilePhotoUrl: string = this.profileService.getProfilePhotoOrDefault()

  user: FullUserModel = {
    firstName: null,
    lastName: null,
    username: null,
    dateOfBirth: null,
    address: null,
    specialization: null
  };

  constructor(private modalBox: ModalBoxComponent, private profileService: MyProfileService, private toastr: ToastrService, private router: Router) {
    this.profileService.getMe()
      .subscribe(response => {
        this.user.firstName = response.firstName;
        this.user.lastName = response.lastName;
        this.user.username = response.username;
        this.user.dateOfBirth = response.dateOfBirth;
        this.user.address = response.address;
        this.user.specialization = response.specialization;
      });
    this.profileService.getUserRole()
      .subscribe(response => {
        this.role = response.authority;
      });
  }

  openDeleteAccountBox() {
    this.modalBox.openDialog(DialogDeleteAccount, '0ms', '100ms');
  }

  openUpdatePhotoBox() {
    this.modalBox.openDialog(DialogUpdatePhoto, '0ms', '100ms');
  }

  openChangePassword() {
    this.router.navigate(["/change-password"]);
  }

  updateProfile() {
    if (!this.isUserFieldsValid()) {
      return;
    }
    this.toggleEditingMode();
    this.profileService.getUserRole()
      .subscribe(response => {
        this.profileService.updateUser(this.user, response.authority)
          .subscribe(response => {
            this.toastr.success("Profile info was updated", "Success");
          }, error => {
            this.toastr.error("", "Error");
          });
      });
  }

  isUserFieldsValid(): boolean {
    if (this.user.firstName && this.user.firstName.length < 3) {
      this.toastr.error("first name should be minimum length 3", "Error");
      return false;
    }
    if (this.user.lastName && this.user.lastName.length < 3) {
      this.toastr.error("last name should be minimum length 3", "Error");
      return false;
    }
    if (this.user.address && this.user.address.length < 3) {
      this.toastr.error("address should be minimum length 3", "Error");
      return false;
    }
    if (this.user.specialization && this.user.specialization.length < 3) {
      this.toastr.error("specialization should be minimum length 3", "Error");
      return false;
    }
    return true;
  }

  toggleEditingMode() {
    this.isEditing = !this.isEditing;
  }
}
