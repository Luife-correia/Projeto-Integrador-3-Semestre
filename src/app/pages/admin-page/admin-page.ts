import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { JogoService } from '../../../services/jogo.service';
import { UsuarioService } from '../../../services/usuario.service';
import { MetricasService } from '../../../services/metricas.service';
import {
  JogoRequest,
  JogoResponse,
  Usuario,
  MetricaStatusResponse,
} from '../../../model/api.model';
import { apiErrorMessage } from '../../../services/api-error';
@Component({
  selector: 'app-admin-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  styleUrl: './admin-page.css',
  templateUrl: './admin-page.html',
})
export class AdminPage {
  private jogoApi = inject(JogoService);
  private userApi = inject(UsuarioService);
  private metricApi = inject(MetricasService);
  tab = signal<'games' | 'users'>('games');
  jogos = signal<JogoResponse[]>([]);
  usuarios = signal<Usuario[]>([]);
  metricas = signal<MetricaStatusResponse[]>([]);
  loading = signal(true);
  error = signal('');
  formOpen = signal(false);
  editId = signal<number | null>(null);
  form: JogoRequest = this.blank();
  plataformas = '';
  generos = '';
  constructor() {
    this.load();
  }
  private load() {
    this.loading.set(true);
    this.jogoApi.listar().subscribe({
      next: (x) => this.jogos.set(x),
      error: (e) => this.error.set(apiErrorMessage(e)),
    });
    this.userApi.listar().subscribe({
      next: (x) => this.usuarios.set(x),
      error: (e) => this.error.set(apiErrorMessage(e)),
    });
    this.metricApi.listar().subscribe({
      next: (x) => {
        this.metricas.set(x);
        this.loading.set(false);
      },
      error: (e) => {
        this.error.set(apiErrorMessage(e));
        this.loading.set(false);
      },
    });
  }
  private blank(): JogoRequest {
    return {
      nome: '',
      capa: '',
      plataformas: [],
      generos: [],
      desenvolvedora: '',
      dataLancamento: '',
      sinopse: '',
    };
  }
  novo() {
    this.form = this.blank();
    this.plataformas = '';
    this.generos = '';
    this.editId.set(null);
    this.formOpen.set(true);
  }
  editar(g: JogoResponse) {
    this.form = {
      nome: g.nome,
      capa: g.capa,
      plataformas: g.plataformas,
      generos: g.generos,
      desenvolvedora: g.desenvolvedora,
      dataLancamento: g.dataLancamento,
      sinopse: g.sinopse,
    };
    this.plataformas = g.plataformas.join(', ');
    this.generos = g.generos.join(', ');
    this.editId.set(g.id);
    this.formOpen.set(true);
  }
  salvarJogo() {
    const r = {
      ...this.form,
      plataformas: this.plataformas
        .split(',')
        .map((x) => x.trim())
        .filter(Boolean),
      generos: this.generos
        .split(',')
        .map((x) => x.trim())
        .filter(Boolean),
    };
    if (!r.plataformas.length || !r.generos.length) {
      this.error.set('Informe ao menos uma plataforma e um gênero.');
      return;
    }
    const req = this.editId() ? this.jogoApi.atualizar(this.editId()!, r) : this.jogoApi.criar(r);
    req.subscribe({
      next: (g) => {
        this.jogos.update((a) =>
          this.editId() ? a.map((x) => (x.id === g.id ? g : x)) : [g, ...a],
        );
        this.formOpen.set(false);
        this.error.set('');
      },
      error: (e) => this.error.set(apiErrorMessage(e)),
    });
  }
  excluirJogo(g: JogoResponse) {
    this.jogoApi.remover(g.id).subscribe({
      next: () => this.jogos.update((a) => a.filter((x) => x.id !== g.id)),
      error: (e) => this.error.set(apiErrorMessage(e)),
    });
  }
  excluirUsuario(u: Usuario) {
    this.userApi.remover(u.id).subscribe({
      next: () => this.usuarios.update((a) => a.filter((x) => x.id !== u.id)),
      error: (e) => this.error.set(apiErrorMessage(e)),
    });
  }
}
