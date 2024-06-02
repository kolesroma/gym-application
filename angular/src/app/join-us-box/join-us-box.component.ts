import {Component} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-join-us-box',
  templateUrl: './join-us-box.component.html',
  styleUrls: ['./join-us-box.component.css']
})
export class JoinUsBoxComponent {

  constructor(private router: Router) {
  }

  navigateRegistrationStudent() {
    this.router.navigate(["/registration/student"])
  }

  navigateRegistrationTrainer() {
    this.router.navigate(["/registration/trainer"])
  }
}
