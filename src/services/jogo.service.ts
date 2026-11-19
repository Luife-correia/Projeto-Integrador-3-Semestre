import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environments';
import { JogoRequest, JogoResponse } from '../model/api.model';

// Reúne as chamadas para listar, consultar e administrar jogos.
@Injectable({ providedIn: 'root' })
export class JogoService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/jogos`;

  // GET /jogos: lista jogos e pode filtrar por nome.
  listar(nome?: string): Observable<JogoResponse[]> {
    let params = new HttpParams();
    if (nome?.trim()) {
      params = params.set('nome', nome.trim());
    }
    return this.http.get<JogoResponse[]>(this.url, { params });
  }
  // GET /jogos/{id}: busca os dados completos de um jogo.
  buscar(id: number): Observable<JogoResponse> {
    return this.http.get<JogoResponse>(`${this.url}/${id}`);
  }
  // POST /jogos: cadastra um jogo novo no catálogo.
  criar(request: JogoRequest): Observable<JogoResponse> {
    return this.http.post<JogoResponse>(this.url, request);
  }
  // PUT /jogos/{id}: atualiza os dados de um jogo.
  atualizar(id: number, request: JogoRequest): Observable<JogoResponse> {
    return this.http.put<JogoResponse>(`${this.url}/${id}`, request);
  }
  // DELETE /jogos/{id}: exclui um jogo do catálogo.
  remover(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
