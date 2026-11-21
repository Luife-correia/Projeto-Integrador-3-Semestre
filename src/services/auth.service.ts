import { Injectable, signal } from '@angular/core';
import { UsuarioAtual } from '../model/api.model';
const KEY = 'gamebox_user';
// Mantém os dados mínimos da sessão no estado Angular e no navegador.
@Injectable({ providedIn: 'root' })
export class AuthService {
  private current = signal<UsuarioAtual | null>(this.read());
  readonly usuario = this.current.asReadonly();
  get usuarioAtual(): UsuarioAtual | null {
    return this.current();
  }
  // Guarda somente ID, nome e e-mail; a senha não fica salva no navegador.
  login(usuario: UsuarioAtual): void {
    this.current.set({ id: usuario.id, nome: usuario.nome, email: usuario.email });
    localStorage.setItem(KEY, JSON.stringify(this.current()));
  }
  // Encerra a sessão local e remove os dados persistidos.
  logout(): void {
    this.current.set(null);
    localStorage.removeItem(KEY);
  }
  // Recupera do localStorage a sessão mínima, se ela estiver válida.
  private read(): UsuarioAtual | null {
    try {
      const dadosSalvos = localStorage.getItem(KEY);
      if (!dadosSalvos) return null;
      const usuario = JSON.parse(dadosSalvos);
      return typeof usuario.id === 'number' && typeof usuario.nome === 'string' && typeof usuario.email === 'string'
        ? { id: usuario.id, nome: usuario.nome, email: usuario.email }
        : null;
    } catch {
      return null;
    }
  }
}
