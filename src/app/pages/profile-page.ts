import { CommonModule, DatePipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import {
  AvaliacaoResponse,
  JogoResponse,
  ListaJogosRequest,
  ListaJogosResponse,
  Usuario,
} from '../../model/api.model';
import { apiErrorMessage } from '../../services/api-error';
import { AuthService } from '../../services/auth.service';
import { AvaliacaoService } from '../../services/avaliacao.service';
import { JogoService } from '../../services/jogo.service';
import { ListaJogosService } from '../../services/lista-jogos.service';
import { UsuarioService } from '../../services/usuario.service';

@Component({
  selector: 'app-profile-page',
  standalone: true,
  imports: [CommonModule, DatePipe, FormsModule],
  templateUrl: './profile-page.html',
})
export class ProfilePage {
  readonly auth = inject(AuthService);
  private readonly avaliacoes = inject(AvaliacaoService);
  private readonly listaApi = inject(ListaJogosService);
  private readonly jogoApi = inject(JogoService);
  private readonly usuarioApi = inject(UsuarioService);

  readonly profile = signal<Usuario | null>(null);
  readonly reviews = signal<AvaliacaoResponse[]>([]);
  readonly lists = signal<ListaJogosResponse[]>([]);
  readonly loading = signal(true);
  readonly listLoading = signal(true);
  readonly error = signal('');
  readonly profileError = signal('');
  readonly profileSaving = signal(false);
  readonly profileFormOpen = signal(false);
  readonly formOpen = signal(false);
  readonly editing = signal<ListaJogosResponse | null>(null);
  private readonly games = signal<JogoResponse[]>([]);

  profileFormData = { nome: '', email: '', senha: '', bio: '', fotoPerfil: '' };

  constructor() {
    const user = this.auth.usuarioAtual;
    if (!user) return;

    this.usuarioApi.buscar(user.id).subscribe({
      next: (profile) => {
        this.profile.set(profile);
        this.profileFormData = {
          nome: profile.nome,
          email: profile.email,
          senha: '',
          bio: profile.bio ?? '',
          fotoPerfil: profile.fotoPerfil ?? '',
        };
      },
      error: (error: unknown) => this.profileError.set(apiErrorMessage(error)),
    });
    this.avaliacoes.porUsuario(user.id).subscribe({
      next: (reviews) => {
        this.reviews.set(reviews);
        this.loading.set(false);
      },
      error: (error: unknown) => {
        this.error.set(apiErrorMessage(error));
        this.loading.set(false);
      },
    });
    this.listaApi.porUsuario(user.id).subscribe({
      next: (lists) => {
        this.lists.set(lists);
        this.listLoading.set(false);
      },
      error: (error: unknown) => {
        this.error.set(apiErrorMessage(error));
        this.listLoading.set(false);
      },
    });
    this.jogoApi.listar().subscribe({
      next: (games) => this.games.set(games),
      error: (error: unknown) => this.error.set(apiErrorMessage(error)),
    });
  }

  jogoNome(id: number): string {
    return this.games().find((game) => game.id === id)?.nome ?? `Jogo #${id}`;
  }

  salvarPerfil(form: NgForm): void {
    form.control.markAllAsTouched();
    const user = this.auth.usuarioAtual;
    const bio = this.profileFormData.bio.trim();
    if (!user || form.invalid || (bio.length > 0 && (bio.length < 6 || bio.length > 100))) {
      this.profileError.set(
        'Confira os campos: nome mínimo de 5 caracteres, e-mail válido, senha mínima de 6 e bio vazia ou de 6 a 100 caracteres.',
      );
      return;
    }

    this.profileSaving.set(true);
    this.profileError.set('');
    const request = {
      nome: this.profileFormData.nome.trim(),
      email: this.profileFormData.email.trim(),
      senha: this.profileFormData.senha,
      ...(bio ? { bio } : {}),
      ...(this.profileFormData.fotoPerfil.trim()
        ? { fotoPerfil: this.profileFormData.fotoPerfil.trim() }
        : {}),
    };
    this.usuarioApi.atualizar(user.id, request).subscribe({
      next: (profile) => {
        this.profile.set(profile);
        this.auth.login({ id: user.id, nome: profile.nome, email: profile.email });
        this.profileFormData.senha = '';
        this.profileSaving.set(false);
        this.profileFormOpen.set(false);
      },
      error: (error: unknown) => {
        this.profileError.set(apiErrorMessage(error));
        this.profileSaving.set(false);
      },
    });
  }

  editarLista(list: ListaJogosResponse | null = null): void {
    this.editing.set(list);
    this.formOpen.set(true);
  }

  salvarLista(nome: string, publica: boolean, principal: boolean): void {
    const user = this.auth.usuarioAtual;
    if (!user || !nome.trim()) return;

    const request: ListaJogosRequest = {
      nome: nome.trim(),
      publica,
      principal,
      usuarioId: user.id,
    };
    const operation = this.editing()
      ? this.listaApi.atualizar(this.editing()!.id, request)
      : this.listaApi.criar(request);

    operation.subscribe({
      next: () => {
        this.formOpen.set(false);
        this.loadLists(user.id);
      },
      error: (error: unknown) => this.error.set(apiErrorMessage(error)),
    });
  }

  excluirLista(list: ListaJogosResponse): void {
    this.listaApi.remover(list.id).subscribe({
      next: () => this.lists.update((items) => items.filter((item) => item.id !== list.id)),
      error: (error: unknown) => this.error.set(apiErrorMessage(error)),
    });
  }

  sair(): void {
    this.auth.logout();
    location.assign('/home');
  }

  private loadLists(userId: number): void {
    this.listaApi.porUsuario(userId).subscribe({
      next: (lists) => {
        this.lists.set(lists);
        this.listLoading.set(false);
      },
      error: (error: unknown) => {
        this.error.set(apiErrorMessage(error));
        this.listLoading.set(false);
      },
    });
  }
}
