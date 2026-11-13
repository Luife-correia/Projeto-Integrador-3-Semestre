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
  criar(r: AvaliacaoRequest): Observable<AvaliacaoResponse> {
    return this.http.post<AvaliacaoResponse>(this.url, r);
  }
  buscar(id: number): Observable<AvaliacaoResponse> {
    return this.http.get<AvaliacaoResponse>(`${this.url}/${id}`);
  }
  porUsuario(id: number): Observable<AvaliacaoResponse[]> {
    return this.http.get<AvaliacaoResponse[]>(`${this.url}/usuario/${id}`);
  }
  porJogo(id: number): Observable<AvaliacaoResponse[]> {
    return this.http.get<AvaliacaoResponse[]>(`${this.url}/jogo/${id}`);
  }
  atualizar(id: number, r: AvaliacaoAtualizacaoRequest): Observable<AvaliacaoResponse> {
    return this.http.put<AvaliacaoResponse>(`${this.url}/${id}`, r);
  }
  remover(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
