import {Component, OnInit} from '@angular/core';
import {MatDialog, MatDialogRef, MatDialogModule} from '@angular/material/dialog';
import {MatButtonModule} from '@angular/material/button';
import {AuthService} from "../auth/auth.service";
import {HttpErrorResponse} from "@angular/common/http";
import {ToastrService} from "ngx-toastr";
import {AuthGuardComponent} from "../auth-guard/auth-guard.component";
import {Router} from "@angular/router";
import {MyProfileService} from "../my-profile/my-profile.service";
import {TrainerService} from "../trainer/trainer.service";
import {TrainingService} from "../training/training.service";
import {FormsModule} from "@angular/forms";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-modal-box',
  templateUrl: './modal-box.component.html',
  styleUrls: ['./modal-box.component.css'],
  standalone: true,
  imports: [MatButtonModule, MatDialogModule],
})
export class ModalBoxComponent {
  constructor(public dialog: MatDialog) {
  }

  openDialog(dialogModalBox: any, enterAnimationDuration: string, exitAnimationDuration: string): void {
    this.dialog.open(dialogModalBox, {
      width: '50%',
      enterAnimationDuration,
      exitAnimationDuration,
    });
  }
}

@Component({
  selector: 'dialog-delete-account',
  templateUrl: 'dialog-delete-account.html',
  styleUrls: ['./modal-box.component.css'],
  standalone: true,
  imports: [MatDialogModule, MatButtonModule],
})
export class DialogDeleteAccount {
  constructor(public dialogRef: MatDialogRef<DialogDeleteAccount>, private authService: AuthService, private toastr: ToastrService, private authGuard: AuthGuardComponent, private router: Router) {
  }

  onConfirmDelete(event: any) {
    event.target.disabled = true;
    this.authService.disableAccount()
      .subscribe(response => {
          this.toastr.success(response.message, "Account deleted");
          this.authGuard.logOut();
          this.router.navigate(["/home"]);
        },
        (error: HttpErrorResponse) => {
          this.toastr.error(error.error.message, "Error happened");
        });
    console.log("confirmed delete account");
  }

  onCancel(event: any) {
    event.target.disabled = true;
    console.log("canceled delete account");
  }
}

@Component({
  selector: 'dialog-update-photo',
  templateUrl: 'dialog-update-photo.html',
  styleUrls: ['./modal-box.component.css'],
  standalone: true,
  imports: [MatDialogModule, MatButtonModule],
})
export class DialogUpdatePhoto {
  constructor(public dialogRef: MatDialogRef<DialogUpdatePhoto>, private profileService: MyProfileService, private toastr: ToastrService) {
  }

  selectedFile: File | null = null;

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
    console.log("selected file");
  }

  onConfirmUpdate(event: any) {
    event.target.disabled = true;
    if (!this.selectedFile) {
      this.toastr.error("Pick a file", "Empty body provided")
    } else {
      this.dialogRef.close();
      this.profileService.uploadProfilePhoto(this.selectedFile)
        .subscribe(response => {
          this.toastr.success("Reload the page", "Photo was updated")
        }, (error: HttpErrorResponse) => {
          this.toastr.error(error.error.message, "Error uploading photo")
        });
    }
  }

  onCancel(event: any) {
    event.target.disabled = true;
    console.log("canceled update photo");
  }
}

@Component({
  selector: 'dialog-add-trainer',
  templateUrl: 'dialog-add-trainer.html',
  styleUrls: ['./modal-box.component.css'],
  standalone: true,
  imports: [MatDialogModule, MatButtonModule, FormsModule, NgForOf],
})
export class DialogAddTrainer implements OnInit {
  uniqueTrainers: Map<string, boolean> = new Map<string, boolean>();
  uniqueTrainersKeys: Set<string> = new Set<string>();

  constructor(public dialogRef: MatDialogRef<DialogUpdatePhoto>, private trainerService: TrainerService, private toastr: ToastrService) {
  }

  ngOnInit() {
    this.trainerService.getAllFreeTrainers()
      .subscribe((response: any) => {
        response.forEach((trainer: any) => {
          this.uniqueTrainers.set(trainer.username, false);
          this.uniqueTrainersKeys.add(trainer.username);
        });
      });
  }

  onConfirmAddSelectedTrainers(event: any) {
    event.target.disabled = true;
    this.dialogRef.close();

    const trainerNames = this.filterSelectedTrue(this.uniqueTrainers);

    this.trainerService.addNewTrainers(trainerNames)
      .subscribe(response => {
        this.toastr.success(`Number of new trainers ${trainerNames.length}`, "Successfully added");
      }, (error: HttpErrorResponse) => {
        this.toastr.error(error.error.message, "Error adding trainers")
      });

  }

  onCancel(event: any) {
    event.target.disabled = true;
  }

  toggleTrainerSelection(trainerName: string) {
    const isSelected = this.uniqueTrainers.get(trainerName);
    this.uniqueTrainers.set(trainerName, !isSelected);
  }

  filterSelectedTrue(trainers: Map<string, boolean>): string[] {
    const filteredTrainers: string[] = [];
    trainers.forEach((value, key) => {
      if (value) {
        filteredTrainers.push(key);
      }
    });
    return filteredTrainers;
  }

}
