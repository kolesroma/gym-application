import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Store} from "@ngrx/store";
import {AuthState} from "../auth/auth.reducer";
import {BehaviorSubject, Observable} from "rxjs";

@Injectable()
export class TrainingService {
  private getTrainingsUrl = 'http://localhost:8080/trainings';
  private saveTrainingUrl = 'http://localhost:8080/trainings';

  constructor(private http: HttpClient, private store: Store<{ auth: AuthState }>) {
    this.store.select("auth")
      .subscribe((auth) => {
        this.javaToken = auth.tokenJava;
      });
  }

  private rowDataSubject: BehaviorSubject<any[]> = new BehaviorSubject<any[]>([]);
  rowData$: Observable<any[]> = this.rowDataSubject.asObservable();

  setRowData(rowData: any[]): void {
    this.rowDataSubject.next(rowData);
  }

  getRowData(): Observable<any[]> {
    return this.rowData$;
  }

  private javaToken: string | null = null;

  getTrainings(): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.javaToken}`
    });
    const requestOptions = {
      headers: headers
    };
    return this.http.get(this.getTrainingsUrl, requestOptions);
  }

  saveTraining(training: any): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.javaToken}`
    });
    const requestOptions = {
      headers: headers
    };
    return this.http.post(this.saveTrainingUrl, training, requestOptions);
  }
}
