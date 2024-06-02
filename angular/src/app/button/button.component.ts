import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.css']
})
export class ButtonComponent {
  @Input() state: 'standard' | 'hover' | 'pressed' | 'disabled' = 'standard';
  @Input() type: 'prime' | 'secondary' | 'important' = 'prime';
  @Input() icon: string = '';
  @Input() text: string = 'Button';
}
