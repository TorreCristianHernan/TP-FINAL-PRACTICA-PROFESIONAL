import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private userSubject = new BehaviorSubject<string | null>(null);

  // Observable para que los componentes se suscriban
  user$ = this.userSubject.asObservable();

  login(usuario: string) {
    this.userSubject.next(usuario);
  }

  logout() {
    this.userSubject.next(null);
  }

  getUsuarioActual(): string | null {
    return this.userSubject.getValue();
  }

  estaLogueado(): boolean {
    return this.getUsuarioActual() !== null;
  }
}
