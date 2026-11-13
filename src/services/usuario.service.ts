import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable, shareReplay } from 'rxjs';
import { environment } from '../environments/environments';
import {
  Usuario,
  UsuarioAtual,
  UsuarioAtualizacaoRequest,
  UsuarioCadastroRequest,
  UsuarioLoginRequest,
} from '../model/api.model';

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/usuarios`;
  private readonly cache = new Map<number, Observable<Usuario>>();

  cadastrar(request: UsuarioCadastroRequest): Observable<Usuario> {
    return this.http.post<Usuario>(this.url, request);
  }

  login(request: UsuarioLoginRequest): Observable<UsuarioAtual> {
    return this.http.post<UsuarioAtual>(`${this.url}/login`, request);
  }

  listar(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.url);
  }

  buscar(id: number): Observable<Usuario> {
    let result = this.cache.get(id);
    if (!result) {
      result = this.http.get<Usuario>(`${this.url}/${id}`).pipe(shareReplay(1));
      this.cache.set(id, result);
    }
    return result;
  }

  invalidarCache(id: number): void {
    this.cache.delete(id);
  }

  atualizar(id: number, request: UsuarioAtualizacaoRequest): Observable<Usuario> {
    this.invalidarCache(id);
    return this.http.patch<Usuario>(`${this.url}/${id}/Me`, request);
  }

  remover(id: number): Observable<void> {
    this.invalidarCache(id);
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
