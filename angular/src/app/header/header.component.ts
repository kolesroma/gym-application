import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {Store} from "@ngrx/store";
import {AuthGuardComponent} from "../auth-guard/auth-guard.component";
import {AuthState} from "../auth/auth.reducer";
import {MyProfileService} from "../my-profile/my-profile.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  isAuthenticated: boolean = false;
  isHorizontalNavOpened: boolean = false;
  isMiniProfileOpened: boolean = false;

  profilePhotoUrl: string = this.profileService.getProfilePhotoOrDefault()

  constructor(private router: Router, private store: Store<{ auth: AuthState }>, private profileService: MyProfileService) {
    this.store.select("auth")
      .subscribe((auth) => {
        this.isAuthenticated = auth.tokenJava != null
          && auth.tokenNode != null;
      });
  }

  navigateToHomePage() {
    this.router.navigate(['/']);
  }

  signIn() {
    this.router.navigate(['/login']);
  }

  joinUs() {
    this.router.navigate(['/join-us']);
  }

  toggleMiniProfile() {
    this.isMiniProfileOpened = !this.isMiniProfileOpened;
  }
}
