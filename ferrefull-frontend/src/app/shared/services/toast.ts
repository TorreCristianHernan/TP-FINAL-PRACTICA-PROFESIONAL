import { Injectable, TemplateRef } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class Toast {

  toasts: any[] = [];

  show(textOrTpl: string | TemplateRef<any>, options: any = {}) {
    const toast = { textOrTpl, ...options, hide: false };
    this.toasts.push(toast);

    // Auto-remove después del delay
    setTimeout(() => this.remove(toast), toast.delay || 5000);
  }

  remove(toast: any) {
    toast.hide = true;
    setTimeout(() => {
      this.toasts = this.toasts.filter(t => t !== toast);
    }, 300); // Tiempo igual a la duración de la animación
  }

  showSuccess(message: string) {
    this.show(message, {
      classname: 'bg-success text-light',
      delay: 3000
    });
  }

  showError(message: string) {
    this.show(message, {
      classname: 'bg-danger text-light',
      delay: 5000
    });
  }
}
