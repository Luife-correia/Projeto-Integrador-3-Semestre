import { Component, Input } from '@angular/core';
import { LibraryStatus } from '../data/mock-data';

const STATUS_LABELS: Record<LibraryStatus, string> = {
  JOGANDO: 'Jogando',
  ZERADO: 'Zerado',
  ABANDONADO: 'Abandonado',
  DESEJO_JOGAR: 'Quero jogar',
  PLATINADO: 'Platinado',
};

@Component({
  selector: 'app-status-badge',
  standalone: true,
  template: `<span class="status-badge" [class.playing]="status === 'JOGANDO'" [class.abandoned]="status === 'ABANDONADO'" [class.wishlist]="status === 'DESEJO_JOGAR'">{{ labels[status] }}</span>`,
})
export class StatusBadgeComponent {
  @Input({ required: true }) status!: LibraryStatus;
  protected readonly labels = STATUS_LABELS;
}