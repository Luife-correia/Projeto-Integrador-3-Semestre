import { Component, Input } from '@angular/core';

// Recebe uma nota e a transforma na exibição visual de estrelas.
@Component({
  selector: 'app-rating',
  standalone: true,
  templateUrl: './rating.html',
})
export class RatingComponent {
  @Input() value = 0;
}
