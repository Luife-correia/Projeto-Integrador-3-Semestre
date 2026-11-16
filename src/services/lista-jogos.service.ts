import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environments';
import { ListaJogosRequest, ListaJogosResponse } from '../model/api.model';
@Injectable({ providedIn: 'root' })
export class ListaJogosService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/ListaJogos`;
  // GET /ListaJogos: lista as listas para a aba Comunidade.
  listar(): Observable<ListaJogosResponse[]> {
    return this.http.get<ListaJogosResponse[]>(this.url);
  }
  // GET /ListaJogos/{id}: busca uma lista e seus jogos.
  buscar(id: number): Observable<ListaJogosResponse> {
    return this.http.get<ListaJogosResponse>(`${this.url}/${id}`);
  }
  // GET /ListaJogos/usuario/{id}: lista as listas de um usuário.
  porUsuario(id: number): Observable<ListaJogosResponse[]> {
    return this.http.get<ListaJogosResponse[]>(`${this.url}/usuario/${id}`);
  }
  // POST /ListaJogos: cria uma lista com seus metadados.
  criar(r: ListaJogosRequest): Observable<ListaJogosResponse> {
    return this.http.post<ListaJogosResponse>(this.url, r);
  }
  // PUT /ListaJogos/{id}: atualiza os metadados de uma lista.
  atualizar(id: number, r: ListaJogosRequest): Observable<ListaJogosResponse> {
    return this.http.put<ListaJogosResponse>(`${this.url}/${id}`, r);
  }
  // DELETE /ListaJogos/{id}: exclui uma lista.
  remover(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
