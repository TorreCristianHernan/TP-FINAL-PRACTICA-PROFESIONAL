import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-side-drawer',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './side-drawer.html',
  styleUrl: './side-drawer.css',
  host: {
    '[class.show]': 'true',
    '[class.side-drawer]': 'true'
  }
})
export class SideDrawer {
  @Output() closed = new EventEmitter<void>();

  constructor(public activeModal: NgbActiveModal) {}

  close(): void {
    this.closed.emit();
    this.activeModal.dismiss();
  }
}
