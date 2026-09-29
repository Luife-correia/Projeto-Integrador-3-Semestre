import { CommonModule, DatePipe } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { GameCardComponent } from '../components/game-card';
import { MockDataService, LibraryStatus } from '../data/mock-data';

@Component({
  selector: 'app-profile-page',
  standalone: true,
  imports: [CommonModule, DatePipe, GameCardComponent],
  template: `
    <div class="page-wrap page-enter">
      <section class="profile-banner">
        <img class="profile-avatar" [src]="profile.fotoPerfil" [alt]="'Foto de ' + profile.nome" />
        <div class="profile-info">
          <span class="eyebrow">Perfil / membro desde 2024</span>
          <h1>{{ profile.nome }}</h1>
          <p>{{ profile.bio }}</p>
        </div>
        <div class="profile-stats">
          <div class="profile-stat"><strong>{{ library().length }}</strong><span>jogos no diário</span></div>
          <div class="profile-stat"><strong>{{ reviewCount() }}</strong><span>avaliações</span></div>
          <div class="profile-stat"><strong>{{ totalHours() }}h</strong><span>tempo de jogo</span></div>
        </div>
      </section>

      <nav class="profile-tabs" aria-label="Seções do perfil">
        <button class="profile-tab" [class.active]="tab() === 'library'" (click)="tab.set('library')">Biblioteca</button>
        <button class="profile-tab" [class.active]="tab() === 'reviews'" (click)="tab.set('reviews')">Avaliações</button>
        <button class="profile-tab" [class.active]="tab() === 'activity'" (click)="tab.set('activity')">Atividade</button>
      </nav>

      @if (tab() === 'library') {
        @for (group of statusGroups; track group.status) {
          @if (entriesByStatus(group.status).length > 0) {
            <section class="library-section">
              <div class="library-section-heading"><h3>{{ group.label }}</h3><span>{{ entriesByStatus(group.status).length }} JOGOS</span></div>
              <div class="library-row">
                @for (entry of entriesByStatus(group.status); track entry.jogoId) {
                  @if (gameFor(entry.jogoId); as game) { <app-game-card [game]="game" [score]="entry.nota ?? 0" [status]="entry.status" /> }
                }
              </div>
            </section>
          }
        }
        @if (library().length === 0) { <div class="empty-state">Sua biblioteca está vazia. Encontre algo para jogar no catálogo.</div> }
      }

      @if (tab() === 'reviews') {
        <section class="library-section">
          <div class="library-section-heading"><h3>Seus diários publicados</h3><span>{{ myReviews().length }} AVALIAÇÕES</span></div>
          @for (review of myReviews(); track review.jogoId) {
            @if (gameFor(review.jogoId); as game) {
              <article class="activity-row">
                <img [src]="game.capa" [alt]="'Capa de ' + game.nome" />
                <div><strong>{{ game.nome }} <span class="rating-stars">★ {{ review.nota.toFixed(1) }}</span></strong><p>{{ review.texto }}</p></div>
                <time>{{ review.dataCriacao | date: 'd MMM yyyy' }}</time>
              </article>
            }
          }
        </section>
      }

      @if (tab() === 'activity') {
        <section class="library-section">
          <div class="library-section-heading"><h3>Atividade recente</h3><span>ÚLTIMOS DIAS</span></div>
          @for (entry of recentActivity(); track entry.jogoId) {
            @if (gameFor(entry.jogoId); as game) {
              <article class="activity-row">
                <img [src]="game.capa" [alt]="'Capa de ' + game.nome" />
                <div><strong>{{ game.nome }}</strong><p>{{ statusLabel(entry.status) }} · {{ entry.horasJogadas }} horas registradas</p></div>
                <time>RECENTE</time>
              </article>
            }
          }
        </section>
      }
    </div>
  `,
})
export class ProfilePage {
  private readonly data = inject(MockDataService);
  protected readonly profile = this.data.users[0];
  protected readonly tab = signal<'library' | 'reviews' | 'activity'>('library');
  protected readonly library = this.data.library;
  protected readonly myReviews = computed(() => this.data.reviews.filter((review) => review.usuarioId === this.profile.id));
  protected readonly reviewCount = computed(() => this.myReviews().length);
  protected readonly totalHours = computed(() => this.library().reduce((sum, entry) => sum + entry.horasJogadas, 0));
  protected readonly recentActivity = computed(() => [...this.library()].reverse());
  protected readonly statusGroups: { status: LibraryStatus; label: string }[] = [
    { status: 'JOGANDO', label: 'Em andamento' },
    { status: 'ZERADO', label: 'Zerados' },
    { status: 'PLATINADO', label: '100% concluído' },
    { status: 'DESEJO_JOGAR', label: 'Quero jogar' },
    { status: 'ABANDONADO', label: 'Deixados para depois' },
  ];

  protected entriesByStatus(status: LibraryStatus) {
    return this.library().filter((entry) => entry.status === status);
  }

  protected gameFor(gameId: string) {
    return this.data.games.find((game) => game.id === gameId);
  }

  protected statusLabel(status: LibraryStatus): string {
    return this.statusGroups.find((group) => group.status === status)?.label ?? 'Na biblioteca';
  }
}