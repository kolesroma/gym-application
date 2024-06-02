import {Component} from '@angular/core';
import {MyProfileService} from "../my-profile/my-profile.service";
import {NotificationService} from "./notification.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-notification-settings',
  templateUrl: './notification-settings.component.html',
  styleUrls: ['./notification-settings.component.css']
})
export class NotificationSettingsComponent {

  email: string = '';
  status: string = '';

  constructor(private notificationService: NotificationService, private profileService: MyProfileService, private toastr: ToastrService) {
    this.profileService.getMe()
      .subscribe(response => {
        this.email = response.email;
      });
    this.notificationService.getNotificationStatus()
      .subscribe(response => {
        this.status = response.enabled;
      });
  }

  toggleStatusTo(newStatus: boolean) {
    this.notificationService.toggleNotificationStatus(newStatus)
      .subscribe(response => {
        this.toastr.success("Status was updated to " + newStatus, "Success");
        this.status = newStatus.toString();
      });
  }

}
