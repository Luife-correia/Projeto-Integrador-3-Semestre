import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { JogoResponse } from '../../../model/api.model';
import { apiErrorMessage } from '../../../services/api-error';
import { JogoService } from '../../../services/jogo.service';
import { GameCardComponent } from '../../components/game-card';

@Component({
  selector: 'app-catalog-page',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, GameCardComponent],
  templateUrl: './catalog-page.html',
})
export class CatalogPage {
  private readonly service = inject(JogoService);

  // Converte gêneros em inglês para nomes usados nos filtros da tela.
  private readonly traducaoGeneros: Record<string, string> = {
    action: 'Ação',
    adventure: 'Aventura',
    'action-adventure': 'Ação e aventura',
    'action adventure': 'Ação e aventura',
    'role-playing': 'RPG',
    'role playing': 'RPG',
    rpg: 'RPG',
    strategy: 'Estratégia',
    simulation: 'Simulação',
    sports: 'Esportes',
    sport: 'Esporte',
    racing: 'Corrida',
    driving: 'Corrida',
    fighting: 'Luta',
    shooter: 'Tiro',
    'first-person shooter': 'Tiro em primeira pessoa',
    'third-person shooter': 'Tiro em terceira pessoa',
    platformer: 'Plataforma',
    puzzle: 'Quebra-cabeça',
    horror: 'Terror',
    'survival horror': 'Terror de sobrevivência',
    'hack and slash': 'Ação e combate',
    music: 'Música',
    mystery: 'Mistério',
    stealth: 'Furtividade',
    'visual novel': 'Romance visual',
    'battle royale': 'Battle royale',
    'open world': 'Mundo aberto',
    sandbox: 'Mundo aberto',
    educational: 'Educativo',
    arcade: 'Fliperama',
    casual: 'Casual',
    indie: 'Independente',
    'card game': 'Jogo de cartas',
    'board game': 'Jogo de tabuleiro',
    mmorpg: 'MMORPG',
  };

  // Guarda os jogos, os filtros escolhidos e os estados de carregamento/erro.
  readonly jogos = signal<JogoResponse[]>([]);
  readonly query = signal('');
  readonly genre = signal('');
  readonly platform = signal('');
  readonly loading = signal(true);
  readonly error = signal('');

  private timer?: ReturnType<typeof setTimeout>;

  // Cria as opções e a lista filtrada a partir dos jogos carregados.
  readonly genres = computed(() =>
    [...new Set(this.jogos().flatMap((game) => game.generos))].sort((a, b) =>
      this.nomeGenero(a).localeCompare(this.nomeGenero(b), 'pt-BR'),
    ),
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
    // Carrega o catálogo assim que a tela é aberta.
    this.buscar();
  }

  // Exibe o gênero traduzido sem mudar o valor original usado na busca.
  nomeGenero(genero: string): string {
    return this.traducaoGeneros[genero.trim().toLocaleLowerCase('pt-BR')] ?? genero;
  }

  // Atualiza o texto de busca e inicia a nova consulta com atraso.
  onQueryChange(value: string): void {
    this.query.set(value);
    this.buscar();
  }

  // GET /jogos: busca por nome na API; gênero e plataforma filtram no front.
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
    // Ativa o gênero escolhido ou remove o filtro se ele já estiver selecionado.
    this.genre.set(this.genre() === value ? '' : value);
  }

  limparFiltros(): void {
    // Limpa gênero, plataforma e texto de busca.
    this.genre.set('');
    this.platform.set('');
    this.query.set('');
    this.buscar();
  }
}
