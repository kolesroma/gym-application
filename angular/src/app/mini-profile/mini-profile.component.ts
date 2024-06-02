import {Component, ElementRef, HostListener} from '@angular/core';
import {Router} from "@angular/router";
import {AuthGuardComponent} from "../auth-guard/auth-guard.component";
import {HeaderComponent} from "../header/header.component";
import {MyProfileService} from "../my-profile/my-profile.service";

@Component({
  selector: 'app-mini-profile',
  templateUrl: './mini-profile.component.html',
  styleUrls: ['./mini-profile.component.css']
})
export class MiniProfileComponent {
  user: { firstName: string, lastName: string, username: string, dateOfBirth: string, address: string, email: string, specialization: string } = {
    firstName: 'Roma',
    lastName: 'Kolesnyk',
    username: 'kolesroma',
    dateOfBirth: '27-11-2002',
    email: 'koles@roma.com',
    address: 'Shevchenka, 20a',
    specialization: ''
  };

  constructor(private router: Router, private authGuard: AuthGuardComponent, private eRef: ElementRef, private header: HeaderComponent, private profileService: MyProfileService) {
  }

  isNightMode = false;

  profilePhotoUrl: string = this.profileService.getProfilePhotoOrDefault()

  toggleNightMode() {
    this.isNightMode = !this.isNightMode;
  }

  navigateToMyAccount() {
    this.closeMiniProfile();
    this.router.navigate(['/my-account']);
  }

  logOut() {
    this.closeMiniProfile();
    this.authGuard.logOut();
    this.router.navigate(['/login']);
  }

  private firstTimeOpenMiniProfile = true;

  @HostListener('document:click', ['$event'])
  clickOutMiniProfile(event: MouseEvent) {
    if (this.firstTimeOpenMiniProfile) {
      this.firstTimeOpenMiniProfile = false;
    } else if (!this.eRef.nativeElement.contains(event.target)) {
      this.closeMiniProfile();
    }
  }

  private closeMiniProfile() {
    this.header.isMiniProfileOpened = false;
    this.firstTimeOpenMiniProfile = true;
  }

}
