import { Component, OnInit } from '@angular/core';
import { ConfirmModal} from '../../components/confirm-modal/confirm-modal';
import { GameModal } from '../../components/game-modal/game-modal';
import { Game, MockDataService } from '../../data/mock-data';

@Component({
  selector: 'app-admin-page',
  standalone: true,
  templateUrl: './admin-page.html',
  styleUrl: './admin-page.css',
  imports: [GameModal, ConfirmModal],
})

export class AdminPage {
    public games : Game[] = [];

    public modalAberto : boolean = false;
    public gameEditando : Game | null = null;

    public gameExcluindo : Game | null = null;

    private mockService : MockDataService;

    constructor(mockService : MockDataService) {
        this.mockService = mockService;
    }

    ngOnInit() : void {
        this.games = [...this.mockService.games];
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
        // Procura se o jogo ja existe na lista para atualizar
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

    // Guarda o jogo a ser excluido e abre o modal de confirmacao
    excluirGame(game : Game) : void {
        this.gameExcluindo = game;
    }

    // Exclui o jogo apos o usuario confirmar no modal
    excluirConfimarcao() : void {
        if (this.gameExcluindo) {
            const idExcluir = this.gameExcluindo.id;
            // Remove o jogo da tabela da tela
            this.games = this.games.filter(g => g.id !== idExcluir);
            // Procura a posicao do jogo na lista geral e remove 1 item com splice
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