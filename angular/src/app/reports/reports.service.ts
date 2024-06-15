import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Store} from "@ngrx/store";
import {AuthState} from "../auth/auth.reducer";
import {Observable} from "rxjs";

@Injectable()
export class ReportService {

  private generateVisitedReportUrl = 'http://localhost:8080/report/visited';
  private generateVelocityReportUrl = 'http://localhost:8080/report/velocity';

  private javaToken: string | null = null;

  constructor(private http: HttpClient, private store: Store<{ auth: AuthState }>) {
    this.store.select("auth")
      .subscribe((auth) => {
        this.javaToken = auth.tokenJava;
      });
  }

  generateVisitedReport(): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.javaToken}`
    });
    const requestOptions = {
      headers: headers
    };
    return this.http.get(this.generateVisitedReportUrl, requestOptions);
  }

  generateVelocityReport(): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.javaToken}`
    });
    const requestOptions = {
      headers: headers
    };
    return this.http.get(this.generateVelocityReportUrl, requestOptions);
  }

}
