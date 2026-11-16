import { HttpErrorResponse } from '@angular/common/http';
import { CommonModule, DatePipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AvaliacaoResponse, JogoResponse, StatusJogos } from '../../model/api.model';
import { apiErrorMessage } from '../../services/api-error';
import { AuthService } from '../../services/auth.service';
import { AvaliacaoService } from '../../services/avaliacao.service';
import { BibliotecaService } from '../../services/biblioteca.service';
import { JogoService } from '../../services/jogo.service';
import { UsuarioService } from '../../services/usuario.service';
import { StatusBadgeComponent } from '../components/status-badge';

@Component({
  selector: 'app-game-detail-page',
  standalone: true,
  imports: [CommonModule, FormsModule, DatePipe, RouterLink, StatusBadgeComponent],
  templateUrl: './game-detail-page.html',
})
export class GameDetailPage {
  private readonly route = inject(ActivatedRoute);
  private readonly jogos = inject(JogoService);
  private readonly avaliacoes = inject(AvaliacaoService);
  private readonly biblioteca = inject(BibliotecaService);
  private readonly usuarios = inject(UsuarioService);
  readonly auth = inject(AuthService);

  readonly game = signal<JogoResponse | null>(null);
  readonly reviews = signal<AvaliacaoResponse[]>([]);
  readonly loading = signal(true);
  readonly reviewsLoading = signal(true);
  readonly error = signal('');
  readonly libraryError = signal('');
  readonly message = signal('');
  readonly saving = signal(false);
  readonly reviewLoading = signal(false);
  readonly status = signal<StatusJogos | null>(null);
  readonly editingReview = signal<AvaliacaoResponse | null>(null);
  private readonly names = signal<Record<number, string>>({});

  selectedStatus: StatusJogos = 'JOGANDO';
  nota = 8;
  comentario = '';
  readonly noteValues = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  readonly statuses: StatusJogos[] = [
    'NAO_JOGADO',
    'JOGANDO',
    'QUERO_JOGAR',
    'ZERADO',
    'PLATINADO',
    'ABANDONADO',
  ];
  readonly labels: Record<StatusJogos, string> = {
    NAO_JOGADO: 'Não jogado',
    JOGANDO: 'Jogando',
    QUERO_JOGAR: 'Quero jogar',
    ZERADO: 'Zerado',
    PLATINADO: 'Platinado',
    ABANDONADO: 'Abandonado',
  };
  private readonly id = Number(this.route.snapshot.paramMap.get('id'));

  constructor() {
    // GET /jogos/{id} carrega os dados principais do jogo.
    this.jogos.buscar(this.id).subscribe({
      next: (game) => {
        this.game.set(game);
        this.loading.set(false);
      },
      error: (error: unknown) => {
        this.error.set(apiErrorMessage(error));
        this.loading.set(false);
      },
    });
    // GET /avaliacoes/jogo/{id} carrega as avaliações exibidas na tela.
    this.avaliacoes.porJogo(this.id).subscribe({
      next: (reviews) => {
        this.reviews.set(reviews);
        this.reviewsLoading.set(false);
        this.loadReviewerNames(reviews);
      },
      error: (error: unknown) => {
        this.message.set(apiErrorMessage(error));
        this.reviewsLoading.set(false);
      },
    });
    this.loadStatus();
  }

  usuarioNome(id: number): string {
    return this.names()[id] ?? 'Carregando usuário…';
  }

  // POST cria o vínculo e PATCH /status salva o progresso escolhido.
  salvarStatus(): void {
    const user = this.auth.usuarioAtual;
    if (!user) return;

    this.saving.set(true);
    this.libraryError.set('');
    const salvarOuAtualizar = () => {
      this.biblioteca.atualizarStatus(user.id, this.id, this.selectedStatus).subscribe({
        next: (entry) => {
          this.status.set(entry.status);
          this.selectedStatus = entry.status;
          this.saving.set(false);
          this.message.set('Biblioteca atualizada.');
        },
        error: (error: unknown) => {
          this.saving.set(false);
          this.message.set(apiErrorMessage(error));
        },
      });
    };

    if (!this.status()) {
      this.biblioteca.adicionar(user.id, this.id).subscribe({
        next: () => salvarOuAtualizar(),
        error: (error: unknown) => {
          this.saving.set(false);
          this.message.set(apiErrorMessage(error));
        },
      });
    } else {
      salvarOuAtualizar();
    }
  }

  // POST cria a avaliação ou PUT /avaliacoes/{id} atualiza a existente.
  salvarAvaliacao(): void {
    const user = this.auth.usuarioAtual;
    if (!user) return;
    if (!this.validReview()) return;

    this.saving.set(true);
    const current = this.editingReview();
    const request = current
      ? this.avaliacoes.atualizar(current.id, {
          notaEmMeiasEstrelas: this.nota,
          comentario: this.comentario.trim(),
        })
      : this.avaliacoes.criar({
          usuarioId: user.id,
          jogoId: this.id,
          notaEmMeiasEstrelas: this.nota,
          comentario: this.comentario.trim(),
        });

    request.subscribe({
      next: (review) => {
        this.reviews.update((reviews) =>
          current
            ? reviews.map((item) => (item.id === review.id ? review : item))
            : [review, ...reviews],
        );
        this.editingReview.set(null);
        this.comentario = '';
        this.saving.set(false);
        this.message.set(current ? 'Avaliação atualizada.' : 'Avaliação publicada.');
      },
      error: (error: unknown) => {
        this.saving.set(false);
        this.message.set(apiErrorMessage(error));
      },
    });
  }

  // GET /avaliacoes/{id} carrega a avaliação antes de abrir a edição.
  iniciarEdicao(review: AvaliacaoResponse): void {
    this.reviewLoading.set(true);
    this.message.set('');
    this.avaliacoes.buscar(review.id).subscribe({
      next: (fullReview) => {
        if (fullReview.usuarioId !== this.auth.usuarioAtual?.id) {
          this.message.set('Você só pode editar suas próprias avaliações.');
          this.reviewLoading.set(false);
          return;
        }
        this.editingReview.set(fullReview);
        this.nota = fullReview.notaEmMeiasEstrelas;
        this.comentario = fullReview.comentario;
        this.reviewLoading.set(false);
      },
      error: (error: unknown) => {
        this.message.set(apiErrorMessage(error));
        this.reviewLoading.set(false);
      },
    });
  }

  cancelarEdicao(): void {
    this.editingReview.set(null);
    this.comentario = '';
    this.nota = 8;
  }

  // DELETE /avaliacoes/{id} remove a avaliação e atualiza a lista.
  excluir(review: AvaliacaoResponse): void {
    this.avaliacoes.remover(review.id).subscribe({
      next: () => this.reviews.update((items) => items.filter((item) => item.id !== review.id)),
      error: (error: unknown) => this.message.set(apiErrorMessage(error)),
    });
  }

  // Consulta o vínculo do jogo; 404 significa que ainda não está na biblioteca.
  private loadStatus(): void {
    const user = this.auth.usuarioAtual;
    if (!user) return;

    this.biblioteca.buscar(user.id, this.id).subscribe({
      next: (entry) => {
        this.status.set(entry.status);
        this.selectedStatus = entry.status;
      },
      error: (error: unknown) => {
        if (error instanceof HttpErrorResponse && error.status === 404) {
          this.status.set(null);
          this.libraryError.set('');
          return;
        }
        this.libraryError.set(apiErrorMessage(error));
      },
    });
  }

  // Busca nomes dos autores para não exibir apenas o ID nas avaliações.
  private loadReviewerNames(reviews: AvaliacaoResponse[]): void {
    // GET /usuarios/{id} resolve o nome de cada autor; UsuarioService usa cache.
    for (const review of reviews) {
      if (this.names()[review.usuarioId]) continue;
      this.usuarios.buscar(review.usuarioId).subscribe({
        next: (user) => this.names.update((names) => ({ ...names, [user.id]: user.nome })),
        error: () =>
          this.names.update((names) => ({
            ...names,
            [review.usuarioId]: `Usuário #${review.usuarioId}`,
          })),
      });
    }
  }

  private validReview(): boolean {
    if (
      !Number.isInteger(this.nota) ||
      this.nota < 0 ||
      this.nota > 10 ||
      this.comentario.trim().length < 10 ||
      this.comentario.trim().length > 500
    ) {
      this.message.set('Informe nota entre 0 e 10 e comentário entre 10 e 500 caracteres.');
      return false;
    }
    return true;
  }
}
