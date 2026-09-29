import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { GameCardComponent } from '../components/game-card';
import { MockDataService } from '../data/mock-data';

@Component({
  selector: 'app-catalog-page',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, GameCardComponent],
  template: `
    <div class="page-wrap page-enter">
      <section class="catalog-hero" aria-labelledby="catalog-title">
        <div class="catalog-hero-copy">
          <span class="eyebrow">Arquivo 001 / PlayStation 2</span>
          <h1 id="catalog-title">O que fica depois do jogo?</h1>
          <p>Um espaço para guardar suas jornadas, revisitar clássicos e descobrir o próximo mundo que vai morar com você.</p>
          <div class="hero-meta"><span>2000 — 2013</span><span>12 jogos em destaque</span></div>
          <div class="hero-actions">
            <a class="button-primary" routerLink="/games/shadow-of-the-colossus">Abrir diário em destaque</a>
            <span class="text-link">Feito para jogar devagar <span>↗</span></span>
          </div>
        </div>
        <div class="catalog-hero-art" aria-hidden="true">
          <img src="https://greenhillszone.com/wp-content/uploads/2012/04/shadow-of-the-colossus-box-art-pal.jpg" alt="" />
          <span class="hero-index">SOTC / 2005 / TEAM ICO</span>
        </div>
      </section>

      <section aria-label="Explorar catálogo">
        <div class="section-heading">
          <div><span class="eyebrow">Sua próxima memória</span><h2 class="section-title">Explore o catálogo</h2></div>
          <span class="catalog-count">{{ filteredGames().length }} RESULTADOS</span>
        </div>
        <div class="catalog-controls">
          <label class="search-box"><span class="visually-hidden">Buscar jogos</span><input class="search-input" type="search" placeholder="Buscar jogo ou desenvolvedora" [(ngModel)]="query" /></label>
          <select class="filter-select" aria-label="Filtrar por gênero" [(ngModel)]="genre">
            <option value="">Todos os gêneros</option>
            @for (item of genres; track item) { <option [value]="item">{{ item }}</option> }
          </select>
          <select class="filter-select" aria-label="Filtrar por plataforma" [(ngModel)]="platform">
            <option value="">Plataformas</option>
            @for (item of platforms; track item) { <option [value]="item">{{ item }}</option> }
          </select>
        </div>
        <div class="genre-strip" aria-label="Atalhos de gênero">
          @for (item of genres; track item) {
            <button class="genre-pill" [class.selected]="genre === item" (click)="genre = genre === item ? '' : item">{{ item }}</button>
          }
        </div>

        <div class="feature-layout">
          <div class="game-grid">
            @for (game of filteredGames(); track game.id) { <app-game-card [game]="game" [score]="scoreFor(game.id)" /> }
            @if (filteredGames().length === 0) { <div class="empty-state">Nenhum jogo encontrado. Tente outro termo ou limpe os filtros.</div> }
          </div>
          <aside class="side-note">
            <span class="side-note-label">NOTA DE CAMPO / 07</span>
            <blockquote>“Alguns jogos não acabam quando os créditos sobem. Eles só encontram um lugar novo para existir.”</blockquote>
            <cite>DO CADERNO DE MARINA</cite>
          </aside>
        </div>
      </section>
    </div>
  `,
})
export class CatalogPage {
  private readonly data = inject(MockDataService);
  protected query = '';
  protected genre = '';
  protected platform = '';
  protected readonly genres = [...new Set(this.data.games.flatMap((game) => game.generos))].sort();
  protected readonly platforms = [...new Set(this.data.games.flatMap((game) => game.plataformas))].sort();
  protected readonly filteredGames = computed(() => {
    const query = this.query.trim().toLocaleLowerCase();
    return this.data.games.filter((game) =>
      (!query || `${game.nome} ${game.desenvolvedora}`.toLocaleLowerCase().includes(query)) &&
      (!this.genre || game.generos.includes(this.genre)) &&
      (!this.platform || game.plataformas.includes(this.platform)),
    );
  });

  protected scoreFor(gameId: string): number {
    const reviews = this.data.reviews.filter((review) => review.jogoId === gameId);
    return reviews.length ? reviews.reduce((total, review) => total + review.nota, 0) / reviews.length : 4.2;
  }
}