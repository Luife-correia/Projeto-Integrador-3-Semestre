import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { JogoResponse } from '../../model/api.model';
import { apiErrorMessage } from '../../services/api-error';
import { JogoService } from '../../services/jogo.service';
import { GameCardComponent } from '../components/game-card';

@Component({
  selector: 'app-catalog-page',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, GameCardComponent],
  templateUrl: './catalog-page.html',
})
export class CatalogPage {
  private readonly service = inject(JogoService);

  readonly jogos = signal<JogoResponse[]>([]);
  readonly query = signal('');
  readonly genre = signal('');
  readonly platform = signal('');
  readonly loading = signal(true);
  readonly error = signal('');

  private timer?: ReturnType<typeof setTimeout>;

  readonly genres = computed(() =>
    [...new Set(this.jogos().flatMap((game) => game.generos))].sort((a, b) => a.localeCompare(b)),
  );
  readonly platforms = computed(() =>
    [...new Set(this.jogos().flatMap((game) => game.plataformas))].sort((a, b) =>
      a.localeCompare(b),
    ),
  );
  readonly filteredGames = computed(() =>
    this.jogos().filter(
      (game) =>
        (!this.genre() || game.generos.includes(this.genre())) &&
        (!this.platform() || game.plataformas.includes(this.platform())),
    ),
  );

  constructor() {
    this.buscar();
  }

  onQueryChange(value: string): void {
    this.query.set(value);
    this.buscar();
  }

  buscar(): void {
    clearTimeout(this.timer);
    this.loading.set(true);
    this.error.set('');
    this.timer = setTimeout(() => {
      this.service.listar(this.query()).subscribe({
        next: (games) => {
          this.jogos.set(games);
          this.loading.set(false);
        },
        error: (error: unknown) => {
          this.error.set(apiErrorMessage(error));
          this.loading.set(false);
        },
      });
    }, 250);
  }

  toggleGenre(value: string): void {
    this.genre.set(this.genre() === value ? '' : value);
  }

  limparFiltros(): void {
    this.genre.set('');
    this.platform.set('');
    this.query.set('');
    this.buscar();
  }
}
