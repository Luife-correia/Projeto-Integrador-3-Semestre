// Situações que um jogo pode ter na biblioteca do usuário.
export type StatusJogos =
  'NAO_JOGADO' | 'JOGANDO' | 'QUERO_JOGAR' | 'ZERADO' | 'PLATINADO' | 'ABANDONADO';

// Dados opcionais de erro que a API pode retornar.
export interface ApiError {
  timestamp?: string;
  status?: number;
  error?: string;
  message?: string;
}
// Dados completos usados para exibir um usuário.
export interface Usuario {
  id: number;
  nome: string;
  email: string;
  fotoPerfil?: string;
  bio?: string;
  dataCriacao?: string;
}
// Dados mínimos mantidos durante a sessão local.
export interface UsuarioAtual {
  id: number;
  nome: string;
  email: string;
}
// Campos enviados para criar uma conta.
export interface UsuarioCadastroRequest {
  nome: string;
  email: string;
  senha: string;
  fotoPerfil?: string;
  bio?: string;
}
// Campos enviados ao atualizar os dados do perfil.
export interface UsuarioAtualizacaoRequest extends UsuarioCadastroRequest {}
// Campos usados para entrar na conta.
export interface UsuarioLoginRequest {
  email: string;
  senha: string;
}
// Dados enviados ao cadastrar ou editar um jogo.
export interface JogoRequest {
  nome: string;
  capa: string;
  plataformas: string[];
  generos: string[];
  desenvolvedora: string;
  dataLancamento: string;
  sinopse: string;
}
// Dados de jogo recebidos da API, incluindo ID e nota.
export interface JogoResponse extends JogoRequest {
  id: number;
  notaTotal: number;
}
// Dados enviados para publicar uma avaliação.
export interface AvaliacaoRequest {
  usuarioId: number;
  jogoId: number;
  notaEmMeiasEstrelas: number;
  comentario: string;
}
// Campos alterados ao editar uma avaliação.
export interface AvaliacaoAtualizacaoRequest {
  notaEmMeiasEstrelas: number;
  comentario: string;
}
// Dados de avaliação recebidos da API.
export interface AvaliacaoResponse extends AvaliacaoRequest {
  id: number;
  dataCriacao: string;
}
// Resposta do vínculo entre usuário e jogo na biblioteca.
export interface UsuarioJogoResponse {
  id: number;
  usuarioId: number;
  jogoId: number;
  status: StatusJogos;
}
// Dados enviados ao criar/editar uma lista e seus jogos.
export interface ListaJogosRequest {
  nome: string;
  publica: boolean;
  principal: boolean;
  usuarioId: number;
  jogoIds?: number[];
}
// Lista recebida da API, com seus jogos e dados do dono.
export interface ListaJogosResponse extends ListaJogosRequest {
  id: number;
  usuarioNome: string;
  jogos: JogoResponse[];
}
// Quantidade de jogos agrupados por status.
export interface MetricaStatusResponse {
  status: StatusJogos;
  quantidade: number;
}

// Dados de jogo exibidos na biblioteca do perfil.
export interface BibliotecaJogoResponse {
  jogoId: number;
  desenvolvedora: string;
  status: StatusJogos;
  capa: string;
  nome: string;
  notaTotal: number;
}
