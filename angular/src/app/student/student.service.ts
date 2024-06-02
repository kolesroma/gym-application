import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {Student} from "./student.model";

@Injectable()
export class StudentService {
  private hostUrl = 'http://localhost:8080'
  private studentsUrl = this.hostUrl + '/trainees';

  private registerUrl = 'http://localhost:3000/register/trainee';

  constructor(private http: HttpClient) { }

  getAll(): Observable<Student[]> {
    return this.http.get<Student[]>(this.studentsUrl);
  }

  registerStudent(student: Student) {
    return this.http.post<{username: string, password: string}>(this.registerUrl, student);
  }
}
