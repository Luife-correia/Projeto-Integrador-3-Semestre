import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { JogoResponse, StatusJogos } from '../../../model/api.model';
import { RatingComponent } from '../rating/rating';
import { StatusBadgeComponent } from '../status-badge/status-badge';

// Recebe os dados de um jogo e monta seu cartão no catálogo.
@Component({
  selector: 'app-game-card',
  standalone: true,
  imports: [RouterLink, RatingComponent, StatusBadgeComponent],
  templateUrl: './game-card.html',
})
export class GameCardComponent {
  @Input({ required: true }) game!: JogoResponse;
  @Input() score = 0;
  @Input() status?: StatusJogos;
}
