import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environments';
import { Jogo } from '../model/jogo.model';

@Injectable({ providedIn: 'root' })
export class JogoService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/jogos`;

  listar(nome?: string): Observable<Jogo[]> {
    let params = new HttpParams();
    if (nome?.trim()) {
      params = params.set('nome', nome.trim());
    }
    return this.http.get<Jogo[]>(this.url, { params });
  }
}