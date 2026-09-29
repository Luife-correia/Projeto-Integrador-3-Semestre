import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Game, LibraryStatus } from '../data/mock-data';
import { RatingComponent } from './rating';
import { StatusBadgeComponent } from './status-badge';

@Component({
  selector: 'app-game-card',
  standalone: true,
  imports: [RouterLink, RatingComponent, StatusBadgeComponent],
  template: `
    <article class="game-card">
      <a class="cover-link" [routerLink]="['/games', game.id]" [attr.aria-label]="'Ver ' + game.nome">
        <img [src]="game.capa" [alt]="'Capa de ' + game.nome" loading="lazy" />
        <span class="cover-score"><app-rating [value]="score" /></span>
      </a>
      <h3 class="game-card-title"><a [routerLink]="['/games', game.id]">{{ game.nome }}</a></h3>
      <div class="game-card-meta">{{ game.desenvolvedora }}@if (status) { <span> · </span><app-status-badge [status]="status" /> }</div>
    </article>
  `,
})
export class GameCardComponent {
  @Input({ required: true }) game!: Game;
  @Input() score = 4.3;
  @Input() status?: LibraryStatus;
}