import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environments';
import { StatusJogos, UsuarioJogoResponse, BibliotecaJogoResponse } from '../model/api.model';

// Reúne as chamadas usadas para consultar a biblioteca e mudar status.
@Injectable({ providedIn: 'root' })
export class BibliotecaService {
  private readonly http = inject(HttpClient);
  private readonly url = environment.apiUrl;

  // GET /usuarios/{usuarioId}/jogos: lista a biblioteca do usuário.
  listarBiblioteca(usuarioId: number): Observable<BibliotecaJogoResponse[]> {
    return this.http.get<BibliotecaJogoResponse[]>(`${this.url}/usuarios/${usuarioId}/jogos`);
  }

  // GET /usuarios/{usuarioId}/jogos/{jogoId}: consulta um vínculo individual.
  buscar(usuarioId: number, jogoId: number): Observable<UsuarioJogoResponse> {
    return this.http.get<UsuarioJogoResponse>(`${this.url}/usuarios/${usuarioId}/jogos/${jogoId}`);
  }

  // POST /usuarios/{usuarioId}/jogos/{jogoId}: adiciona um jogo.
  adicionar(usuarioId: number, jogoId: number): Observable<UsuarioJogoResponse> {
    return this.http.post<UsuarioJogoResponse>(
      `${this.url}/usuarios/${usuarioId}/jogos/${jogoId}`,
      null,
    );
  }

  // PATCH /usuarios/{usuarioId}/jogos/{jogoId}/status: altera o progresso.
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
