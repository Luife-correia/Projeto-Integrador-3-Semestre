import { Component, Input } from '@angular/core';
import { StatusJogos } from '../../model/api.model';

const STATUS_LABELS: Record<StatusJogos, string> = {
  NAO_JOGADO: 'Não jogado',
  JOGANDO: 'Jogando',
  QUERO_JOGAR: 'Quero jogar',
  ZERADO: 'Zerado',
  ABANDONADO: 'Abandonado',
  PLATINADO: 'Platinado',
};

@Component({
  selector: 'app-status-badge',
  standalone: true,
  templateUrl: './status-badge.html',
})
export class StatusBadgeComponent {
  @Input({ required: true }) status!: StatusJogos;
  protected readonly labels = STATUS_LABELS;
}
