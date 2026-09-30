import { Injectable } from '@angular/core';
/* Biblioteca para gerenciar as requisições HTTP */
import {HttpClient} from '@angular/common/http';
/* Nossa classe para criação dos objetos */
//import { ToDo } from './app/ToDo';
/* Biblioteca para respostas assíncronas */
import { Observable } from 'rxjs';

// ARQUIVO CRIADO PREVIAMENTE PARA FURUTAS IMPLEMENTAÇÔES COM BACKEND

@Injectable({
    providedIn: 'root'
})
export class AppService {
    private backURL = 'http://localhost:8080';

    constructor(private http: HttpClient) { }

    getLista(): Observable<any> {
        return this.http.get<any[]>(this.backURL);
    }
}
