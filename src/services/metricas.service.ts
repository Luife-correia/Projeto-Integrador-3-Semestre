import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environments';
import { MetricaStatusResponse } from '../model/api.model';
@Injectable({ providedIn: 'root' })
export class MetricasService {
  private http = inject(HttpClient);
  listar(): Observable<MetricaStatusResponse[]> {
    return this.http.get<MetricaStatusResponse[]>(`${environment.apiUrl}/metricas`);
  }
}
