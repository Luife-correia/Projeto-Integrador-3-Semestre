import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-rating',
  standalone: true,
  templateUrl: './rating.html',
})
export class RatingComponent {
  @Input() value = 0;
}
