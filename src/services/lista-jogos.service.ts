import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environments';
import { ListaJogosRequest, ListaJogosResponse } from '../model/api.model';
@Injectable({ providedIn: 'root' })
export class ListaJogosService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/ListaJogos`;
  listar(): Observable<ListaJogosResponse[]> {
    return this.http.get<ListaJogosResponse[]>(this.url);
  }
  buscar(id: number): Observable<ListaJogosResponse> {
    return this.http.get<ListaJogosResponse>(`${this.url}/${id}`);
  }
  porUsuario(id: number): Observable<ListaJogosResponse[]> {
    return this.http.get<ListaJogosResponse[]>(`${this.url}/usuario/${id}`);
  }
  criar(r: ListaJogosRequest): Observable<ListaJogosResponse> {
    return this.http.post<ListaJogosResponse>(this.url, r);
  }
  atualizar(id: number, r: ListaJogosRequest): Observable<ListaJogosResponse> {
    return this.http.put<ListaJogosResponse>(`${this.url}/${id}`, r);
  }
  remover(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
