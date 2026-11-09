import { Component, OnInit } from '@angular/core';
import { ConfirmModal} from '../../components/confirm-modal/confirm-modal';
import { GameModal } from '../../components/game-modal/game-modal';
import { Game, MockDataService, User } from '../../data/mock-data';
import { FormsModule } from '@angular/forms';


export type AdminTab = 'games' | 'users';


@Component({
  selector: 'app-admin-page',
  standalone: true,
  templateUrl: './admin-page.html',
  styleUrl: './admin-page.css',
  imports: [GameModal, ConfirmModal, FormsModule],
})



export class AdminPage {

    public games : Game[] = [];
    public users : User[] = [];

    abaAtiva: AdminTab = 'games';
    gameBusca = '';
    userBusca = '';

    
    //retorna uma outra lista filtrada para games e users
    get gamesFiltrados() : Game[]{
        const termo = this.gameBusca.trim().toLocaleLowerCase();

        return this.games.filter((game) => 
            `${game.nome} ${game.desenvolvedora}`.toLocaleLowerCase().includes(termo));
    }

    get usersFiltrados() : User[] {
        const termo = this.userBusca.trim().toLocaleLowerCase();

        return this.users.filter((user) =>
        `${user.nome} ${user.email}`.toLocaleLowerCase().includes(termo));
    }

    public modalAberto : boolean = false;
    public gameEditando : Game | null = null;

    public gameExcluindo : Game | null = null;

    private mockService : MockDataService;

    constructor(mockService : MockDataService) {
        this.mockService = mockService;
    }

    ngOnInit() : void {
        this.games = [...this.mockService.games];
        this.users = [...this.mockService.users];
    }

    abrirNovo() : void {
        this.gameEditando = null;
        this.modalAberto = true;
    }

    abrirEdicao(game : Game) : void {
        this.gameEditando = game;
        this.modalAberto = true;
    }

    fecharModal() : void {
        this.modalAberto = false;
        this.gameEditando = null;
    }

    // Salva o jogo cadastrado ou atualiza se for uma edicao
    salvarGame(jogo : Game) : void {
        const index = this.games.findIndex(g => g.id === jogo.id);
        if (index >= 0) {
            this.games[index] = jogo;
            const mockIndex = this.mockService.games.findIndex(g => g.id === jogo.id);
            if (mockIndex >= 0) {
                this.mockService.games[mockIndex] = jogo;
            }
        } else {
            // Se for jogo novo, insere no inicio da lista
            this.games.unshift(jogo);
            this.mockService.games.unshift(jogo);
        }
        this.mockService.salvarJogos();
        this.modalAberto = false;
        this.gameEditando = null;
    }

    excluirGame(game : Game) : void {
        this.gameExcluindo = game;
    }

    excluirConfimarcao() : void {
        if (this.gameExcluindo) {
            const idExcluir = this.gameExcluindo.id;
            this.games = this.games.filter(g => g.id !== idExcluir);
            const mockIndex = this.mockService.games.findIndex(g => g.id === idExcluir);
            if (mockIndex >= 0) {
                this.mockService.games.splice(mockIndex, 1);
                this.mockService.salvarJogos();
            }
        }
        this.gameExcluindo = null;
    }

    excluirCancelar() : void {
        this.gameExcluindo = null;
    }
}