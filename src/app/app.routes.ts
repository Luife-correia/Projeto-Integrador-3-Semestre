import { Routes } from '@angular/router';
import { CatalogPage } from './pages/catalog-page';
import { GameDetailPage } from './pages/game-detail-page';
import { ProfilePage } from './pages/profile-page';
import { AdminPage } from './pages/admin-page/admin-page';
import { LoginCadastro } from './pages/login-cadastro/login-cadastro';


export const routes: Routes = [
	{ path: '', pathMatch: 'full', redirectTo: 'home' },
	{ path: 'home', component: CatalogPage, title: 'Explorar jogos | GameBox' },
	{ path: 'games/:id', component: GameDetailPage, title: 'Detalhes do jogo | GameBox' },
	{ path: 'profile', component: ProfilePage, title: 'Meu perfil | GameBox' },
	{ path: 'admin', component: AdminPage, title: 'Administração | GameBox' },
	{ path: 'login', component: LoginCadastro, title: 'Entrar | GameBox' },
	{ path: '**', redirectTo: 'home' },
];
