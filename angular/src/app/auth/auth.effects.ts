import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import {catchError, map, mergeMap} from 'rxjs/operators';
import * as AuthActions from './auth.actions';
import { AuthService } from './auth.service';

@Injectable()
export class AuthEffects {

  login$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.loginRequest.type),
      mergeMap((action: {username: string, password: string} ) =>
        this.authService.login(action.username, action.password).pipe(
          map((response) => AuthActions.loginSuccess({tokenNode: response.tokenNode, tokenJava: response.tokenJava})),
          catchError(error => of(AuthActions.loginFailure(error)))
        )
      )
    )
  );

  constructor(private actions$: Actions, private authService: AuthService) {}
}
