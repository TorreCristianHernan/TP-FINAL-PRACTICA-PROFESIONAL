import { inject, Injectable, TemplateRef } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SideDrawer } from '../components/side-drawer/side-drawer';

@Injectable({
  providedIn: 'root'
})
export class Drawer {

  private modalService = inject(NgbModal);

  open(title: string, content: TemplateRef<any>, options: any = {}): any {
    const modalRef = this.modalService.open(SideDrawer, {
      backdrop: 'static',
      keyboard: false,
      modalDialogClass: 'side-drawer-modal',
      windowClass: 'side-drawer-container'
    });

    modalRef.componentInstance.title = title;
    modalRef.componentInstance.content = content;
    modalRef.componentInstance.options = options;

    return modalRef;
  }
}
