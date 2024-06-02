import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {ChangePasswordModel} from "../student/student.model";
import {Store} from "@ngrx/store";
import {AuthState} from "./auth.reducer";

@Injectable()
export class AuthService {
  private hostUrl = 'http://localhost:3000';
  private loginUrl = this.hostUrl + '/login';
  private changePasswordUrl = this.hostUrl + '/change-password';

  private disableAccountUrl = 'http://localhost:8080/auth/disable-account';

  constructor(private http: HttpClient, private store: Store<{ auth: AuthState }>) {
    this.store.select("auth")
      .subscribe((auth) => {
        this.javaToken = auth.tokenJava;
      });
  }

  private javaToken: string | null = null;

  login(username: string, password: string): Observable<{message: string, tokenNode: string, tokenJava:string}> {
    return this.http.post<{message: string, tokenNode: string, tokenJava:string}>(this.loginUrl, { login: username, password: password });
  }

  changePassword(changePasswordModel: ChangePasswordModel) {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.javaToken}`
    });
    const requestOptions = {
      headers: headers
    };
    return this.http.post<{message: string}>(this.changePasswordUrl, changePasswordModel, requestOptions);
  }

  disableAccount() {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.javaToken}`
    });
    const requestOptions = {
      headers: headers
    };
    return this.http.post<{message: string}>(this.disableAccountUrl, {}, requestOptions);
  }
}
