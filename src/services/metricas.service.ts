import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environments';
import { MetricaStatusResponse } from '../model/api.model';
// Consulta as quantidades de jogos agrupadas por status.
@Injectable({ providedIn: 'root' })
export class MetricasService {
  private http = inject(HttpClient);
  // GET /metricas: busca a contagem de jogos por status.
  listar(): Observable<MetricaStatusResponse[]> {
    return this.http.get<MetricaStatusResponse[]>(`${environment.apiUrl}/metricas`);
  }
}
