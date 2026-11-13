import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environments';
import { JogoRequest, JogoResponse } from '../model/api.model';

@Injectable({ providedIn: 'root' })
export class JogoService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/jogos`;

  listar(nome?: string): Observable<JogoResponse[]> {
    let params = new HttpParams();
    if (nome?.trim()) {
      params = params.set('nome', nome.trim());
    }
    return this.http.get<JogoResponse[]>(this.url, { params });
  }
  buscar(id:number):Observable<JogoResponse>{return this.http.get<JogoResponse>(`${this.url}/${id}`)}
  criar(request:JogoRequest):Observable<JogoResponse>{return this.http.post<JogoResponse>(this.url,request)}
  atualizar(id:number,request:JogoRequest):Observable<JogoResponse>{return this.http.put<JogoResponse>(`${this.url}/${id}`,request)}
  remover(id:number):Observable<void>{return this.http.delete<void>(`${this.url}/${id}`)}
}
