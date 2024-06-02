import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {catchError, map, mergeMap} from "rxjs/operators";
import {of} from "rxjs";
import {TrainerService} from "./trainer.service";
import {Trainer} from "./trainer.model";
import {registerTrainer, registerTrainerFailure, registerTrainerSuccess} from "./trainer.actions";

@Injectable()
export class TrainerEffects {

  registerTrainer$ = createEffect(() => this.actions.pipe(
      ofType(registerTrainer.type),
      mergeMap((action: Trainer) => this.trainerService.registerTrainer(action)
        .pipe(
          map(response => registerTrainerSuccess({username: response.username, password: response.password})),
          catchError(error => of(registerTrainerFailure(error)))
        ))
    )
  );

  constructor(private actions: Actions, private trainerService: TrainerService) {
  }

}
