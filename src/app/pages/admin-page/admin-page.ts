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
    }

    salvarGame() : void {
        this.modalAberto = false;
    }

    excluirGame(game : Game) : void {
        this.gameExcluindo = game;
    }

    excluirConfimarcao() : void {
        this.gameExcluindo = null;
    }

    excluirCancelar() : void {
        this.gameExcluindo = null;
    }
}