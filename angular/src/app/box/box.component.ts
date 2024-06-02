import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-box',
  templateUrl: './box.component.html',
  styleUrls: ['./box.component.css']
})
export class BoxComponent {
  @Input() imageUrl: string = '';
  @Input() title: string = '';
  @Input() text: string = '';
  @Input() date: string = '';
  @Input() readTime: string = '';
}
