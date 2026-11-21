import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environments';
import {
  AvaliacaoAtualizacaoRequest,
  AvaliacaoRequest,
  AvaliacaoResponse,
} from '../model/api.model';
// Reúne as chamadas da API para avaliações de usuários e jogos.
@Injectable({ providedIn: 'root' })
export class AvaliacaoService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/avaliacoes`;
  // POST /avaliacoes: envia uma nova avaliação.
  criar(dadosAvaliacao: AvaliacaoRequest): Observable<AvaliacaoResponse> {
    return this.http.post<AvaliacaoResponse>(this.url, dadosAvaliacao);
  }
  // GET /avaliacoes/{id}: busca a avaliação antes de editar.
  buscar(id: number): Observable<AvaliacaoResponse> {
    return this.http.get<AvaliacaoResponse>(`${this.url}/${id}`);
  }
  // GET /avaliacoes/usuario/{id}: lista avaliações de um usuário.
  porUsuario(id: number): Observable<AvaliacaoResponse[]> {
    return this.http.get<AvaliacaoResponse[]>(`${this.url}/usuario/${id}`);
  }
  // GET /avaliacoes/jogo/{id}: lista avaliações de um jogo.
  porJogo(id: number): Observable<AvaliacaoResponse[]> {
    return this.http.get<AvaliacaoResponse[]>(`${this.url}/jogo/${id}`);
  }
  // PUT /avaliacoes/{id}: atualiza a nota e o comentário.
  atualizar(id: number, dadosAtualizacao: AvaliacaoAtualizacaoRequest): Observable<AvaliacaoResponse> {
    return this.http.put<AvaliacaoResponse>(`${this.url}/${id}`, dadosAtualizacao);
  }
  // DELETE /avaliacoes/{id}: exclui uma avaliação.
  remover(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
