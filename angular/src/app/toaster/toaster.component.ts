import {Component, Input, OnInit} from '@angular/core';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-toaster',
  templateUrl: './toaster.component.html',
  styleUrls: ['./toaster.component.css']
})
export class ToasterComponent implements OnInit {
  @Input() type: 'success' | 'error' = 'success';
  @Input() title = 'Title';
  @Input() message = 'Important message';

  constructor(private toastr: ToastrService) {
  }

  showToaster() {
    if (this.type === "error") {
      this.showError();
    } else {
      this.showSuccess();
    }
  }

  showSuccess() {
    this.toastr.success(this.message, this.title);
  }

  showError() {
    this.toastr.error(this.message, this.title);
  }

  ngOnInit() {
    this.showToaster();
  }
}
