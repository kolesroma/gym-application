import {Component} from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {Router} from "@angular/router";
import {Store} from "@ngrx/store";
import {AuthState} from "../auth/auth.reducer";
import {loginRequest} from "../auth/auth.actions";
import {Observable} from "rxjs";
import {AuthGuardComponent} from "../auth-guard/auth-guard.component";
import {MyProfileService} from "../my-profile/my-profile.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  auth$: Observable<AuthState>;
  errorText: string | null = '';
  isErrorWhileLogin: boolean = false;

  constructor(private authService: AuthService, private router: Router, private store: Store<{ auth: AuthState }>, private authGuard: AuthGuardComponent, private profileService: MyProfileService) {
    this.auth$ = store.select("auth");
    this.auth$.subscribe((auth) => {
      this.errorText = auth.error;
    });
  }

  formData: { username: string, password: string } = {
    username: '',
    password: ''
  };

  isLoginInvalid: boolean = false;
  isPasswordInvalid: boolean = false;
  isLoading: boolean = false;

  onSubmitLogin() {
    if (this.formData.username.length === 0 || this.formData.password.length === 0) {
      this.isLoginInvalid = this.formData.username.length === 0;
      this.isPasswordInvalid = this.formData.password.length === 0;
      return;
    } else {
      this.isLoginInvalid = false;
      this.isPasswordInvalid = false;
    }

    this.isLoading = true;
    setTimeout(() => {
      this.store.dispatch(loginRequest({
        username: this.formData.username,
        password: this.formData.password
      }));

      this.isLoading = false;

      setTimeout(()=> {
        if(this.authGuard.isAuthenticated) {
          this.router.navigate(["/home"])
            .then(() => {
              this.profileService.saveProfilePhotoLocally();
            });
        } else {
          this.isErrorWhileLogin = true;
        }
      }, 500);

    }, 2000);
  }
}
