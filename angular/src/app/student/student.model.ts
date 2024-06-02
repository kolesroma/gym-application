import {Trainer} from "../trainer/trainer.model";

export interface Student {
  firstName: string;
  lastName: string;
  email: string;
  dateOfBirth: string;
  address: string;
  trainers: Trainer[];
}

export interface ChangePasswordModel {
  oldPassword: string,
  newPassword: string,
  newPasswordRepeat: string
}
