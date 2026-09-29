import { CommonModule, DatePipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { GameCardComponent } from '../components/game-card';
import { StatusBadgeComponent } from '../components/status-badge';
import { Game, LibraryStatus, MockDataService } from '../data/mock-data';

@Component({
  selector: 'app-game-detail-page',
  standalone: true,
  imports: [CommonModule, DatePipe, RouterLink, StatusBadgeComponent, GameCardComponent],
  template: `
    <article class="page-enter">
      <section class="detail-hero">
        <div class="detail-backdrop" [style.background-image]="'url(' + game().capa + ')'" aria-hidden="true"></div>
        <img class="detail-cover" [src]="game().capa" [alt]="'Capa de ' + game().nome" />
        <div class="detail-copy">
          <span class="eyebrow">Diário de jogo / {{ game().plataformas[0] }}</span>
          <h1>{{ game().nome }}</h1>
          <div class="detail-studio">Desenvolvido por {{ game().desenvolvedora }}</div>
          <div class="detail-facts"><span>{{ game().dataLancamento | date: 'yyyy' }}</span><span>{{ game().plataformas.join(' · ') }}</span></div>
          <div class="genre-tags">@for (tag of game().generos; track tag) { <span class="genre-tag">{{ tag }}</span> }</div>
          <p class="detail-synopsis">{{ game().sinopse }}</p>
          <div class="rating-summary">
            <span class="rating-number">{{ average().toFixed(1) }}</span>
            <span class="rating-stars">★★★★★</span>
            <span class="rating-caption">{{ reviews().length }} avaliações da comunidade</span>
          </div>
          @if (currentStatus()) { <app-status-badge [status]="currentStatus()!" /> }
          <div class="library-add">
            <select class="library-select" aria-label="Status na biblioteca" [value]="selectedStatus()" (change)="selectedStatus.set($any($event.target).value)">
              @for (status of statuses; track status.value) { <option [value]="status.value">{{ status.label }}</option> }
            </select>
            <button class="button-primary" (click)="addToLibrary()">{{ currentStatus() ? 'Atualizar biblioteca' : 'Adicionar à biblioteca' }}</button>
            @if (saved()) { <span class="library-feedback" role="status">Salvo no seu diário</span> }
          </div>
        </div>
      </section>

      <section class="detail-content">
        <div class="section-heading">
          <div><span class="eyebrow">Entre jogadores</span><h2 class="section-title">Diários da comunidade</h2></div>
          <span class="catalog-count">{{ reviews().length }} ENTRADAS</span>
        </div>
        <div class="reviews-list">
          @for (review of reviews(); track review.usuarioId + review.dataCriacao) {
            <article class="review-row">
              <div class="review-user">
                <img [src]="data.users.find(user => user.id === review.usuarioId)?.fotoPerfil" alt="" />
                <div><strong>{{ data.users.find(user => user.id === review.usuarioId)?.nome }}</strong><span>{{ review.dataCriacao | date: 'd MMM yyyy' }}</span></div>
              </div>
              <p class="review-text">{{ review.texto }}</p>
              <span class="review-score">★ {{ review.nota.toFixed(1) }}</span>
            </article>
          }
          @if (reviews().length === 0) { <div class="empty-state">Ainda não há avaliações para este jogo. Seu diário pode ser o primeiro.</div> }
        </div>
        <div class="section-heading">
          <div><span class="eyebrow">Mais para descobrir</span><h2 class="section-title">Talvez você curta</h2></div>
          <a class="text-link" routerLink="/home">Ver catálogo <span>↗</span></a>
        </div>
        <div class="game-grid">
          @for (related of relatedGames(); track related.id) { <app-game-card [game]="related" /> }
        </div>
      </section>
    </article>
  `,
})
export class GameDetailPage {
  protected readonly data = inject(MockDataService);
  private readonly route = inject(ActivatedRoute);
  protected readonly game = signal<Game>(this.data.gameById(this.route.snapshot.paramMap.get('id')));
  protected readonly selectedStatus = signal<LibraryStatus>('ZERADO');
  protected readonly saved = signal(false);
  protected readonly statuses: { value: LibraryStatus; label: string }[] = [
    { value: 'JOGANDO', label: 'Jogando agora' },
    { value: 'ZERADO', label: 'Zerado' },
    { value: 'ABANDONADO', label: 'Abandonado' },
    { value: 'DESEJO_JOGAR', label: 'Quero jogar' },
    { value: 'PLATINADO', label: 'Platinado' },
  ];
  protected readonly currentStatus = signal<LibraryStatus | null>(null);
  protected readonly reviews = signal(this.data.reviews.filter((review) => review.jogoId === this.game().id));
  protected readonly average = signal(this.reviews().length ? this.reviews().reduce((sum, review) => sum + review.nota, 0) / this.reviews().length : 0);
  protected readonly relatedGames = signal(this.data.games.filter((item) => item.id !== this.game().id).slice(0, 4));

  constructor() {
    this.route.paramMap.subscribe((params) => {
      const game = this.data.gameById(params.get('id'));
      this.game.set(game);
      this.reviews.set(this.data.reviews.filter((review) => review.jogoId === game.id));
      const gameReviews = this.reviews();
      this.average.set(gameReviews.length ? gameReviews.reduce((sum, review) => sum + review.nota, 0) / gameReviews.length : 0);
      this.relatedGames.set(this.data.games.filter((item) => item.id !== game.id).slice(0, 4));
      const entry = this.data.library().find((item) => item.usuarioId === 1 && item.jogoId === game.id);
      this.currentStatus.set(entry?.status ?? null);
      this.selectedStatus.set(entry?.status ?? 'ZERADO');
      this.saved.set(false);
    });
  }

  protected addToLibrary(): void {
    this.data.setLibraryStatus(this.game().id, this.selectedStatus());
    this.currentStatus.set(this.selectedStatus());
    this.saved.set(true);
  }
}