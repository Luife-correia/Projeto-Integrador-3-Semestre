import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from '../services/auth.service';

// Componente principal: monta a navegação e encerra a sessão pela barra superior.
@Component({
  imports: [RouterLink, RouterLinkActive, RouterOutlet],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {
  protected readonly auth = inject(AuthService);
  // Faz o botão Sair da barra superior encerrar a sessão local.
  protected logout(): void { this.auth.logout(); }
}
