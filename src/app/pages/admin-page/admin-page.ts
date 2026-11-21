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
  // Guarda dados das tabelas, estado do painel e valores do formulário de jogo.
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
    // Busca jogos, usuários e métricas ao abrir o painel.
    this.load();
  }
  // GET /jogos, /usuarios e /metricas carrega os dados do painel.
  private load() {
    this.loading.set(true);
    // GET /jogos preenche a tabela de jogos.
    this.jogoApi.listar().subscribe({
      next: (jogosRecebidos) => this.jogos.set(jogosRecebidos),
      error: (erro) => this.error.set(apiErrorMessage(erro)),
    });
    // GET /usuarios preenche a tabela de usuários.
    this.userApi.listar().subscribe({
      next: (usuariosRecebidos) => this.usuarios.set(usuariosRecebidos),
      error: (erro) => this.error.set(apiErrorMessage(erro)),
    });
    // GET /metricas carrega os totais agrupados por status.
    this.metricApi.listar().subscribe({
      next: (metricasRecebidas) => {
        this.metricas.set(metricasRecebidas);
        this.loading.set(false);
      },
      error: (erro) => {
        this.error.set(apiErrorMessage(erro));
        this.loading.set(false);
      },
    });
  }
  private blank(): JogoRequest {
    // Cria um formulário de jogo vazio para cadastro ou limpeza após edição.
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
    // Prepara o formulário vazio para cadastrar um jogo.
    this.form = this.blank();
    this.plataformas = '';
    this.generos = '';
    this.editId.set(null);
    this.formOpen.set(true);
  }
  editar(jogoParaEditar: JogoResponse) {
    // Copia os dados do jogo escolhido para o formulário.
    this.form = {
      nome: jogoParaEditar.nome,
      capa: jogoParaEditar.capa,
      plataformas: jogoParaEditar.plataformas,
      generos: jogoParaEditar.generos,
      desenvolvedora: jogoParaEditar.desenvolvedora,
      dataLancamento: jogoParaEditar.dataLancamento,
      sinopse: jogoParaEditar.sinopse,
    };
    this.plataformas = jogoParaEditar.plataformas.join(', ');
    this.generos = jogoParaEditar.generos.join(', ');
    this.editId.set(jogoParaEditar.id);
    this.formOpen.set(true);
  }
  // POST cria o jogo ou PUT /jogos/{id} atualiza; depois sincroniza a tabela.
  salvarJogo() {
    const dadosJogo = {
      ...this.form,
      plataformas: this.plataformas
        .split(',')
        .map((plataforma) => plataforma.trim())
        .filter(Boolean),
      generos: this.generos
        .split(',')
        .map((genero) => genero.trim())
        .filter(Boolean),
    };
    if (!dadosJogo.plataformas.length || !dadosJogo.generos.length) {
      this.error.set('Informe ao menos uma plataforma e um gênero.');
      return;
    }
    const req = this.editId()
      ? this.jogoApi.atualizar(this.editId()!, dadosJogo)
      : this.jogoApi.criar(dadosJogo);
    req.subscribe({
      next: (jogoSalvo) => {
        this.jogos.update((listaDeJogos) =>
          this.editId()
            ? listaDeJogos.map((jogoExistente) =>
                jogoExistente.id === jogoSalvo.id ? jogoSalvo : jogoExistente,
              )
            : [jogoSalvo, ...listaDeJogos],
        );
        this.formOpen.set(false);
        this.error.set('');
      },
      error: (erro) => this.error.set(apiErrorMessage(erro)),
    });
  }
  // DELETE /jogos/{id} exclui o jogo e remove sua linha da tabela.
  excluirJogo(jogoParaExcluir: JogoResponse) {
    this.jogoApi.remover(jogoParaExcluir.id).subscribe({
      next: () =>
        this.jogos.update((listaDeJogos) =>
          listaDeJogos.filter((jogoExistente) => jogoExistente.id !== jogoParaExcluir.id),
        ),
      error: (erro) => this.error.set(apiErrorMessage(erro)),
    });
  }
  // DELETE /usuarios/{id} exclui o usuário selecionado.
  excluirUsuario(usuarioParaExcluir: Usuario) {
    this.userApi.remover(usuarioParaExcluir.id).subscribe({
      next: () =>
        this.usuarios.update((listaDeUsuarios) =>
          listaDeUsuarios.filter((usuarioExistente) => usuarioExistente.id !== usuarioParaExcluir.id),
        ),
      error: (erro) => this.error.set(apiErrorMessage(erro)),
    });
  }
}
