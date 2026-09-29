import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-rating',
  standalone: true,
  template: `<span class="rating-widget" [attr.aria-label]="'Nota ' + value.toFixed(1) + ' de 5'">★ {{ value.toFixed(1) }}</span>`,
})
export class RatingComponent {
  @Input() value = 0;
}