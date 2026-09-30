import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-confirm-modal',
  templateUrl: './confirm-modal.html',
  styleUrl: './confirm-modal.css'
  })

export class ConfirmModal {
    @Input() title : string = "Confirmar";
    @Input() message : string = "";
    @Output() confirmed = new EventEmitter<void>();
    @Output() canceled = new EventEmitter<void>();

    confirmar() : void {this.confirmed.emit();}

    cancelar() : void {this.canceled.emit();}

}