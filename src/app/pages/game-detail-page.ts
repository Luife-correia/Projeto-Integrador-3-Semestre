import { CommonModule, DatePipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { GameCardComponent } from '../components/game-card';
import { StatusBadgeComponent } from '../components/status-badge';
import { Game, LibraryStatus, MockDataService, Review } from '../data/mock-data';

@Component({
  selector: 'app-game-detail-page',
  standalone: true,
  imports: [CommonModule, FormsModule, DatePipe, RouterLink, StatusBadgeComponent, GameCardComponent],
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
            <select class="library-select" aria-label="Status na biblioteca" [ngModel]="selectedStatus()" (ngModelChange)="selectedStatus.set($event)">
              @for (status of statuses; track status.value) { <option [value]="status.value" [selected]="status.value === selectedStatus()">{{ status.label }}</option> }
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
        <div style="background: var(--panel); border: 1px solid var(--line); padding: 18px; margin-bottom: 24px;">
          <h3 style="margin-top: 0; font-size: 14px; margin-bottom: 12px;">Deixe sua avaliação</h3>
          <div style="display: flex; gap: 14px; margin-bottom: 12px; flex-wrap: wrap;">
            <div>
              <label style="display: block; font-size: 11px; color: var(--muted); margin-bottom: 4px;">Sua nota:</label>
              <select class="filter-select" [(ngModel)]="novaNota" style="min-width: 140px;">
                <option [value]="5">★★★★★ (5.0)</option>
                <option [value]="4.5">★★★★☆ (4.5)</option>
                <option [value]="4">★★★★☆ (4.0)</option>
                <option [value]="3.5">★★★☆☆ (3.5)</option>
                <option [value]="3">★★★☆☆ (3.0)</option>
                <option [value]="2">★★☆☆☆ (2.0)</option>
                <option [value]="1">★☆☆☆☆ (1.0)</option>
              </select>
            </div>
            <div style="flex: 1; min-width: 240px;">
              <label style="display: block; font-size: 11px; color: var(--muted); margin-bottom: 4px;">Seu comentário:</label>
              <textarea class="filter-select" [(ngModel)]="novoComentario" rows="2" placeholder="Escreva sua opinião sobre o jogo..." style="width: 100%; min-height: 50px; padding: 8px; resize: vertical;"></textarea>
            </div>
          </div>
          <div style="display: flex; justify-content: flex-end; align-items: center; gap: 12px;">
            @if (mensagemAvaliacao) { <span style="color: #4ade80; font-size: 12px;">{{ mensagemAvaliacao }}</span> }
            <button type="button" class="button-primary" (click)="publicarAvaliacao()">Publicar avaliação</button>
          </div>
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
  public novaNota: number = 5;
  public novoComentario: string = '';
  public mensagemAvaliacao: string = '';
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
      this.selectedStatus.set(entry?.status ?? 'JOGANDO');
      this.saved.set(false);
    });
  }

  // Salva ou atualiza o status do jogo (ex: Zerado, Jogando) na biblioteca
  protected addToLibrary(): void {
    this.data.setLibraryStatus(this.game().id, this.selectedStatus());
    this.currentStatus.set(this.selectedStatus());
    this.saved.set(true);
  }

  // Envia a avaliacao digitada e atualiza a nota na biblioteca
  public publicarAvaliacao(): void {
    // Nao deixa enviar com comentario vazio
    if (!this.novoComentario.trim()) {
      return;
    }
    // Monta o objeto com os dados da avaliacao
    const novaReview: Review = {
      usuarioId: 1,
      jogoId: this.game().id,
      texto: this.novoComentario.trim(),
      nota: Number(this.novaNota),
      dataCriacao: new Date().toISOString().split('T')[0],
    };
    // Coloca a nova avaliacao no comeco da lista
    this.data.reviews.unshift(novaReview);
    this.reviews.update((lista) => [novaReview, ...lista]);
    // Recalcula a media: soma as notas com reduce e divide pelo total
    const listaAtual = this.reviews();
    this.average.set(listaAtual.reduce((sum, r) => sum + r.nota, 0) / listaAtual.length);
    // Atualiza a nota do jogo tambem na biblioteca do usuario
    this.data.library.update((entries) => entries.map((e) => (e.usuarioId === 1 && e.jogoId === this.game().id ? { ...e, nota: Number(this.novaNota) } : e)));
    // Salva no navegador e limpa o formulario
    this.data.salvarReviews();
    this.data.salvarLibrary();
    this.novoComentario = '';
    this.novaNota = 5;
    this.mensagemAvaliacao = 'Avaliação publicada com sucesso!';
    setTimeout(() => { this.mensagemAvaliacao = ''; }, 4000);
  }
};