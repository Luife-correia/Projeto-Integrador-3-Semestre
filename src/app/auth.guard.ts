import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

// A API não retorna role; este guard só redireciona a navegação e não é controle de segurança.
export const authGuard: CanActivateFn = () =>
  inject(AuthService).usuarioAtual ? true : inject(Router).createUrlTree(['/login']);
