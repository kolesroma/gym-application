import { createReducer, on } from '@ngrx/store';
import * as AuthActions from './auth.actions';

export interface AuthState {
  tokenNode: string | null;
  tokenJava: string | null;
  error: string | null;
}

export const initialState: AuthState = {
  tokenNode: null,
  tokenJava: null,
  error: null,
};

export const authReducer = createReducer(
  initialState,
  on(AuthActions.loginSuccess, (state, { tokenNode, tokenJava }) => ({
    ...state,
    tokenNode,
    tokenJava,
    error: null,
  })),
  on(AuthActions.loginFailure, (state, { error }) => ({
    ...state,
    error: error.message
  })),
  on(AuthActions.logout, (state) => ({
    ...state,
    tokenNode: null,
    tokenJava: null,
    error: null,
  }))
);
