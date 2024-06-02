import {Injectable} from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
  UrlTree
} from "@angular/router";
import {Observable} from "rxjs";
import {Store} from "@ngrx/store";
import {AuthState} from "../auth/auth.reducer";
import {loginRequest, logout} from "../auth/auth.actions";

@Injectable()
export class AuthGuardComponent implements CanActivate {

  isAuthenticated: boolean = false;

  constructor(private router: Router, private store: Store<{ auth: AuthState }>) {
    this.store.select("auth")
      .subscribe((auth) => {
        this.isAuthenticated = auth.tokenJava != null
          && auth.tokenNode != null;
      });
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (this.isAuthenticated) {
      return true;
    } else {
      console.error("access denied");
      this.router.navigate(["/login"]);
      return false;
    }
  }

  logOut() {
    this.store.dispatch(logout());
    localStorage.clear();
  }
}
