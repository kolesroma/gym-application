import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Store} from "@ngrx/store";
import {AuthState} from "../auth/auth.reducer";
import {Observable} from "rxjs";
import {MyProfileService} from "../my-profile/my-profile.service";

@Injectable()
export class NotificationService {

  private getNotificationStatusUrl = 'http://localhost:8080/notification/status/me';
  private toggleNotificationStatusUrl = 'http://localhost:8080/notification/status';

  private javaToken: string | null = null;

  constructor(private http: HttpClient, private store: Store<{ auth: AuthState }>, private profileService: MyProfileService) {
    this.store.select("auth")
      .subscribe((auth) => {
        this.javaToken = auth.tokenJava;
      });
  }

  getNotificationStatus(): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.javaToken}`
    });
    const requestOptions = {
      headers: headers
    };
    return this.http.get(this.getNotificationStatusUrl, requestOptions);
  }

  toggleNotificationStatus(newStatus: boolean): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.javaToken}`
    });
    let params = new HttpParams();
    params = params.append('status', newStatus.toString());
    const requestOptions = {
      headers: headers,
      params: params
    };
    return this.http.post(this.toggleNotificationStatusUrl, {}, requestOptions);
  }

}
