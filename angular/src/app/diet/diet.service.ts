import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Store} from "@ngrx/store";
import {AuthState} from "../auth/auth.reducer";
import {Observable} from "rxjs";

@Injectable()
export class DietService {

  private creteDietUrl = 'http://localhost:8080/diet';

  private javaToken: string | null = null;

  constructor(private http: HttpClient, private store: Store<{ auth: AuthState }>) {
    this.store.select("auth")
      .subscribe((auth) => {
        this.javaToken = auth.tokenJava;
      });
  }

  createDiet(createDietDto: any): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.javaToken}`
    });
    const requestOptions = {
      headers: headers
    };
    return this.http.post(this.creteDietUrl, createDietDto, requestOptions);
  }

}
