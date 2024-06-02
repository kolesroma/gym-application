import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {fromEvent, Observable} from "rxjs";
import {Store} from "@ngrx/store";
import {AuthState} from "../auth/auth.reducer";
import {map} from "rxjs/operators";
import {FullUserModel} from "./full-user.model";

@Injectable()
export class MyProfileService {
  private profilePhotoUrl = 'http://localhost:3000/user-photo';
  private uploadPhotoUrl = 'http://localhost:3000/upload-photo';

  private getMeUrl = 'http://localhost:8080/users/me';
  private getUserRoleUrl = 'http://localhost:8080/users/me/role';
  private updateUserUrl = 'http://localhost:8080/users';

  private profilePhotoLocationDefault = 'assets/img/profile-icon.png';

  constructor(private http: HttpClient, private store: Store<{ auth: AuthState }>) {
    this.store.select("auth")
      .subscribe((auth) => {
        this.nodeToken = auth.tokenNode;
        this.javaToken = auth.tokenJava;
      });
  }

  private nodeToken: string | null = null;
  private javaToken: string | null = null;

  getProfilePhoto(): Observable<Blob> {
    const headers = new HttpHeaders({
      'tokenNode': `${this.nodeToken}`
    });
    return this.http.get(this.profilePhotoUrl, {headers: headers, responseType: 'blob'});
  }

  getMe(): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.javaToken}`
    });
    const requestOptions = {
      headers: headers
    };
    return this.http.get(this.getMeUrl, requestOptions);
  }

  getUserRole(): Observable<{ authority: string }> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.javaToken}`
    });
    const requestOptions = {
      headers: headers
    };
    return this.http.get<{ authority: string }>(this.getUserRoleUrl, requestOptions);
  }

  updateUser(fullUser: FullUserModel, role: string): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.javaToken}`
    });
    const requestOptions = {
      headers: headers
    };
    if (role === 'ROLE_TRAINEE') {
      return this.http.put(this.updateUserUrl, {trainee: fullUser}, requestOptions);
    }
    if (role === 'ROLE_TRAINER') {
      return this.http.put(this.updateUserUrl, {trainer: fullUser}, requestOptions);
    }
    throw new Error(`provided role ${role} is not supported`)
  }

  uploadProfilePhoto(file: File): Observable<any> {
    const headers = new HttpHeaders({
      'tokenNode': `${this.nodeToken}`
    });
    const formData = new FormData();
    formData.append("photo", file);
    const protoResponse = this.http.post(this.uploadPhotoUrl, formData, {headers: headers});
    protoResponse
      .subscribe(() => {
        this.saveProfilePhotoLocally();
      });
    return protoResponse;
  }

  saveProfilePhotoLocally() {
    this.getProfilePhoto()
      .subscribe(img => {
        this.toBase64(img)
          .subscribe(base64image => {
            localStorage.setItem("user-photo", base64image + "");
          });
      });
  }

  getProfilePhotoOrDefault(): string {
    const userPhotoBase64 = localStorage.getItem("user-photo");
    if (userPhotoBase64) {
      return userPhotoBase64;
    } else {
      return this.profilePhotoLocationDefault;
    }
  }

  toBase64(blob: Blob): Observable<string> {
    const reader = new FileReader();
    reader.readAsDataURL(blob);
    return fromEvent(reader, 'load')
      .pipe(map(() => (reader.result as string)))
  }
}
