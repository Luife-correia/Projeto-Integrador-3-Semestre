import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Game } from '../../data/mock-data';

@Component({
    selector: 'app-game-modal',
    imports: [FormsModule],
    templateUrl: './game-modal.html',
    styleUrl: './game-modal.css'
})

export class GameModal implements OnInit {
    @Input() game: Game | null = null;
    @Output() save = new EventEmitter<Game>();
    @Output() closed = new EventEmitter<void>();

    public nome : string = '';
    public desenvolvedora : string = '';
    public dataLancamento : string = '';
    public generos : string = '';
    public plataformas : string = '';
    public descricao : string = '';
    public capa : string = '';
    public sinopse : string = '';
    public rating : number = 0;

    ngOnInit() : void {
        if(this.game) {
            this.nome = this.game.nome;
            this.desenvolvedora = this.game.desenvolvedora;
            this.dataLancamento = this.game.dataLancamento;
            this.generos = this.game.generos.join(', ');
            this.plataformas = this.game.plataformas.join(', ');
            this.capa = this.game.capa;
            this.sinopse = this.game.sinopse;
        }
    }

      salvar(): void {
    if (this.nome.trim() === '') {
      return;
    }

    const jogo: Game = {
      id: this.game ? this.game.id : 'jogo-' + Date.now(),
      nome: this.nome.trim(),
      desenvolvedora: this.desenvolvedora.trim(),
      dataLancamento: this.dataLancamento,
      generos: this.paraLista(this.generos),
      plataformas: this.paraLista(this.plataformas),
      capa: this.capa.trim(),
      sinopse: this.sinopse.trim(),
    };

    this.save.emit(jogo);
  }
  
  cancelar() : void {this.closed.emit();}

   private paraLista(texto: string): string[] {
    const lista: string[] = [];
    for (const item of texto.split(',')) {
      const limpo = item.trim();
      if (limpo !== '') {
        lista.push(limpo);
      }
    }
    return lista;
  }


}