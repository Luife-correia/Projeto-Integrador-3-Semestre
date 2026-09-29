import { Routes } from '@angular/router';
import { CatalogPage } from './pages/catalog-page';
import { GameDetailPage } from './pages/game-detail-page';
import { ProfilePage } from './pages/profile-page';

export const routes: Routes = [
	{ path: '', pathMatch: 'full', redirectTo: 'home' },
	{ path: 'home', component: CatalogPage, title: 'Explorar jogos | OrganoStation' },
	{ path: 'games/:id', component: GameDetailPage, title: 'Detalhe do jogo | OrganoStation' },
	{ path: 'profile', component: ProfilePage, title: 'Meu perfil | OrganoStation' },
	{ path: '**', redirectTo: 'home' },
];
