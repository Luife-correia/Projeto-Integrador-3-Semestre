import { Injectable, signal } from '@angular/core';
import { UsuarioAtual } from '../model/api.model';
const KEY = 'gamebox_user';
@Injectable({ providedIn: 'root' })
export class AuthService {
  private current = signal<UsuarioAtual | null>(this.read());
  readonly usuario = this.current.asReadonly();
  get usuarioAtual(): UsuarioAtual | null {
    return this.current();
  }
  login(u: UsuarioAtual): void {
    this.current.set({ id: u.id, nome: u.nome, email: u.email });
    localStorage.setItem(KEY, JSON.stringify(this.current()));
  }
  logout(): void {
    this.current.set(null);
    localStorage.removeItem(KEY);
  }
  private read(): UsuarioAtual | null {
    try {
      const x = localStorage.getItem(KEY);
      if (!x) return null;
      const u = JSON.parse(x);
      return typeof u.id === 'number' && typeof u.nome === 'string' && typeof u.email === 'string'
        ? { id: u.id, nome: u.nome, email: u.email }
        : null;
    } catch {
      return null;
    }
  }
}
