import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { JogoService } from '../../../services/jogo.service';
import { UsuarioService } from '../../../services/usuario.service';
import { MetricasService } from '../../../services/metricas.service';
import { AuthService } from '../../../services/auth.service';
import { JogoRequest, JogoResponse, Usuario,MetricaStatusResponse,} from '../../../model/api.model';
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
  readonly auth = inject(AuthService);

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

  // Busca jogos, usuários e métricas ao abrir o painel.
  // GET /jogos, /usuarios e /metricas carrega os dados do painel.
  ngOnInit(): void{
    
    this.loading.set(true);
    
    // GET /jogos preenche a tabela de jogos.
    this.jogoApi.listar().subscribe({
      next: j => this.jogos.set(j), 
      error: e => this.error.set(apiErrorMessage(e))
    });

    // GET /usuarios preenche a tabela de usuários.
    this.userApi.listar().subscribe({
      next: u => this.usuarios.set(u), 
      error: e => this.error.set(apiErrorMessage(e))
    });

    // GET /metricas carrega os totais agrupados por status.
    this.metricApi.listar().subscribe({next: m => {this.metricas.set(m)
      this.loading.set(false);}, 
      error: e => {
        this.error.set(apiErrorMessage(e));
        this.loading.set(false);
      }
    });

  }

// Lista de plataformas já usadas nos jogos do catálogo sem repetição
  get opcaoPlataforma(): string[]{
    return [...new Set(this.jogos().flatMap((jogo) => jogo.plataformas))].sort();
  }

  get opcaoGenero(): string[]{
    return [...new Set(this.jogos().flatMap((jogo) => jogo.generos))].sort();
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
    this.editId.set(null);
    this.formOpen.set(true);
  }

  editar(jogoParaEditar: JogoResponse) {
    // Copia os dados do jogo escolhido para o formulário.
    this.form = {
      nome: jogoParaEditar.nome,
      capa: jogoParaEditar.capa,
      plataformas: [...jogoParaEditar.plataformas],
      generos: [...jogoParaEditar.generos],
      desenvolvedora: jogoParaEditar.desenvolvedora,
      dataLancamento: jogoParaEditar.dataLancamento,
      sinopse: jogoParaEditar.sinopse,
    };

    this.editId.set(jogoParaEditar.id);
    this.formOpen.set(true);
  }


  // Chamado ao marcar/desmarcar uma plataforma no formulário: adiciona ou remove ela da lista do jogo.
  selecionarPlataforma(plataforma: string, selecionado: boolean){
    if(selecionado){
      this.form.plataformas.push(plataforma);
    }else{
      this.form.plataformas = this.form.plataformas.filter((item) => item !== plataforma);
    }
  }

  selecionarGenero(genero: string, selecionado: boolean){
    if(selecionado){
      this.form.generos.push(genero);
    }else{
      this.form.generos = this.form.generos.filter((item) => item !== genero);
    }
  }


  // POST cria o jogo ou PUT /jogos/{id} atualiza; depois sincroniza a tabela.
  salvarJogo() {
    const dadosJogo = {...this.form};

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
