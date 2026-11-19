import { Component, Input } from '@angular/core';
import { StatusJogos } from '../../model/api.model';

// Traduz os códigos de status recebidos da API para rótulos da tela.
const STATUS_LABELS: Record<StatusJogos, string> = {
  NAO_JOGADO: 'Não jogado',
  JOGANDO: 'Jogando',
  QUERO_JOGAR: 'Quero jogar',
  ZERADO: 'Zerado',
  ABANDONADO: 'Abandonado',
  PLATINADO: 'Platinado',
};

// Exibe o status do jogo usando o rótulo correspondente.
@Component({
  selector: 'app-status-badge',
  standalone: true,
  templateUrl: './status-badge.html',
})
export class StatusBadgeComponent {
  @Input({ required: true }) status!: StatusJogos;
  protected readonly labels = STATUS_LABELS;
}
