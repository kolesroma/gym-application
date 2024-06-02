import {createReducer, on} from "@ngrx/store";
import {registerTrainer, registerTrainerFailure, registerTrainerSuccess} from "./trainer.actions";

export interface TrainerRegistrationState {
  username: string | null;
  password: string | null;
  error: string | null;
}

export const initialStateRegistration: TrainerRegistrationState = {
  username: null,
  password: null,
  error: null,
};

export const trainerRegistrationReducer = createReducer(
  initialStateRegistration,
  on(registerTrainer, state => state),
  on(registerTrainerSuccess, (state, { username, password }) => ({...state, username, password, error: null})),
  on(registerTrainerFailure, (state, { error }) => ({...state, error: error.message}))
);
