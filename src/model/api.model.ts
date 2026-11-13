export type StatusJogos =
  'NAO_JOGADO' | 'JOGANDO' | 'QUERO_JOGAR' | 'ZERADO' | 'PLATINADO' | 'ABANDONADO';
export interface ApiError {
  timestamp?: string;
  status?: number;
  error?: string;
  message?: string;
}
export interface Usuario {
  id: number;
  nome: string;
  email: string;
  fotoPerfil?: string;
  bio?: string;
  dataCriacao?: string;
}
export interface UsuarioAtual {
  id: number;
  nome: string;
  email: string;
}
export interface UsuarioCadastroRequest {
  nome: string;
  email: string;
  senha: string;
  fotoPerfil?: string;
  bio?: string;
}
export interface UsuarioAtualizacaoRequest extends UsuarioCadastroRequest {}
export interface UsuarioLoginRequest {
  email: string;
  senha: string;
}
export interface JogoRequest {
  nome: string;
  capa: string;
  plataformas: string[];
  generos: string[];
  desenvolvedora: string;
  dataLancamento: string;
  sinopse: string;
}
export interface JogoResponse extends JogoRequest {
  id: number;
  notaTotal: number;
}
export interface AvaliacaoRequest {
  usuarioId: number;
  jogoId: number;
  notaEmMeiasEstrelas: number;
  comentario: string;
}
export interface AvaliacaoAtualizacaoRequest {
  notaEmMeiasEstrelas: number;
  comentario: string;
}
export interface AvaliacaoResponse extends AvaliacaoRequest {
  id: number;
  dataCriacao: string;
}
export interface UsuarioJogoResponse {
  id: number;
  usuarioId: number;
  jogoId: number;
  status: StatusJogos;
}
export interface ListaJogosRequest {
  nome: string;
  publica: boolean;
  principal?: boolean;
  usuarioId: number;
}
export interface ListaJogosResponse extends ListaJogosRequest {
  id: number;
  usuarioNome: string;
  jogos: JogoResponse[];
}
export interface MetricaStatusResponse {
  status: StatusJogos;
  quantidade: number;
}
