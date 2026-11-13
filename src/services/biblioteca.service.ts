import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environments';
import { StatusJogos, UsuarioJogoResponse } from '../model/api.model';

@Injectable({ providedIn: 'root' })
export class BibliotecaService {
  private readonly http = inject(HttpClient);
  private readonly url = environment.apiUrl;

  buscar(usuarioId: number, jogoId: number): Observable<UsuarioJogoResponse> {
    return this.http.get<UsuarioJogoResponse>(`${this.url}/usuarios/${usuarioId}/jogos/${jogoId}`);
  }

  adicionar(usuarioId: number, jogoId: number): Observable<UsuarioJogoResponse> {
    return this.http.post<UsuarioJogoResponse>(
      `${this.url}/usuarios/${usuarioId}/jogos/${jogoId}`,
      null,
    );
  }

  atualizarStatus(
    usuarioId: number,
    jogoId: number,
    status: StatusJogos,
  ): Observable<UsuarioJogoResponse> {
    return this.http.patch<UsuarioJogoResponse>(
      `${this.url}/usuarios/${usuarioId}/jogos/${jogoId}/status`,
      { status },
    );
  }
}
