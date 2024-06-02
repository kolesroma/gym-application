import {Trainer} from "./trainer.model";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {AuthState} from "../auth/auth.reducer";

@Injectable()
export class TrainerService {

  private registerUrl = 'http://localhost:3000/register/trainer';

  private addNewTrainersUrl = 'http://localhost:8080/trainees/trainers';
  private getAllFreeTrainersUrl = 'http://localhost:8080/trainers/free';

  constructor(private http: HttpClient, private store: Store<{ auth: AuthState }>) {
    this.store.select("auth")
      .subscribe((auth) => {
        this.javaToken = auth.tokenJava;
      });
  }

  private javaToken: string | null = null;

  registerTrainer(trainer: Trainer) {
    return this.http.post<{ username: string, password: string }>(this.registerUrl, trainer);
  }

  getAllFreeTrainers() {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.javaToken}`
    });
    const requestOptions = {
      headers: headers
    };
    return this.http.get(this.getAllFreeTrainersUrl, requestOptions);
  }

  addNewTrainers(trainerUsernames: string[]) {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.javaToken}`
    });
    const requestOptions = {
      headers: headers
    };
    return this.http.post(this.addNewTrainersUrl, trainerUsernames, requestOptions);
  }
}
