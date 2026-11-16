import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environments';
import {
  AvaliacaoAtualizacaoRequest,
  AvaliacaoRequest,
  AvaliacaoResponse,
} from '../model/api.model';
@Injectable({ providedIn: 'root' })
export class AvaliacaoService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/avaliacoes`;
  // POST /avaliacoes: envia uma nova avaliação.
  criar(r: AvaliacaoRequest): Observable<AvaliacaoResponse> {
    return this.http.post<AvaliacaoResponse>(this.url, r);
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
  atualizar(id: number, r: AvaliacaoAtualizacaoRequest): Observable<AvaliacaoResponse> {
    return this.http.put<AvaliacaoResponse>(`${this.url}/${id}`, r);
  }
  // DELETE /avaliacoes/{id}: exclui uma avaliação.
  remover(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
