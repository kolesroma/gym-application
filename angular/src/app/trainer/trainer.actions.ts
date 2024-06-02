import {createAction, props} from "@ngrx/store";
import {Trainer} from "./trainer.model";

export const registerTrainer = createAction('[Trainer] Register', props<Trainer>());
export const registerTrainerSuccess = createAction('[Trainer] Register Success', props<{ username: string, password: string }>());
export const registerTrainerFailure = createAction('[Trainer] Register Failure', props<{ error: any }>());
