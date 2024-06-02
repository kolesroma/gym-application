import {createAction, props} from '@ngrx/store';

export const loginRequest = createAction('[Auth] Login Request', props<{ username: string, password: string }>());
export const loginSuccess = createAction('[Auth] Login Success', props<{ tokenNode: string, tokenJava: string }>());
export const loginFailure = createAction('[Auth] Login Failure', props<{ error: any }>());
export const logout = createAction('[Auth] Log out');
