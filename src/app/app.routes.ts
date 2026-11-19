import { Routes } from '@angular/router';
import { CatalogPage } from './pages/catalog-page/catalog-page';
import { GameDetailPage } from './pages/game-detail-page/game-detail-page';
import { ProfilePage } from './pages/profile-page/profile-page';
import { AdminPage } from './pages/admin-page/admin-page';
import { LoginCadastro } from './pages/login-cadastro/login-cadastro';
import { authGuard } from './auth.guard';


// Relaciona cada endereço do navegador com sua tela.
export const routes: Routes = [
	{ path: '', pathMatch: 'full', redirectTo: 'home' },
	{ path: 'home', component: CatalogPage, title: 'Explorar jogos | GameBox' },
	{ path: 'games/:id', component: GameDetailPage, title: 'Detalhes do jogo | GameBox' },
	{ path: 'profile', component: ProfilePage, canActivate: [authGuard], title: 'Meu perfil | GameBox' },
	{ path: 'admin', component: AdminPage, canActivate: [authGuard], title: 'Administração | GameBox' },
	{ path: 'login', component: LoginCadastro, title: 'Entrar | GameBox' },
	{ path: '**', redirectTo: 'home' },
];
