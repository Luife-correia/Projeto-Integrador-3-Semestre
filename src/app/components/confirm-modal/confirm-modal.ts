import { Component, EventEmitter, Input, Output } from '@angular/core';

// Modal simples que avisa o componente pai quando confirmar ou cancelar.
@Component({
  selector: 'app-confirm-modal',
  templateUrl: './confirm-modal.html',
  styleUrl: './confirm-modal.css'
  })

export class ConfirmModal {
    // Texto do título e da mensagem mostrados no modal.
    @Input() title : string = "Confirmar";
    @Input() message : string = "";
    @Output() confirmed = new EventEmitter<void>();
    @Output() canceled = new EventEmitter<void>();

    // Avisa o componente pai que a pessoa confirmou a ação.
    confirmar() : void {this.confirmed.emit();}

    // Avisa o componente pai que a pessoa cancelou a ação.
    cancelar() : void {this.canceled.emit();}

}
