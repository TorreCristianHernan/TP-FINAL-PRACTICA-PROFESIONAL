import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private userSubject = new BehaviorSubject<string | null>(null);
  
  user$ = this.userSubject.asObservable();
  // Observable para que los componentes se suscriban
  constructor(private http: HttpClient) {}

  login(usuario: string, password: string): Observable<boolean> {
    return this.http
      .post<{ success: boolean; usuario?: string }>(
        'http://localhost:8080/api/login',
        { usuario, password }
      )
      .pipe(
        map(resp => {
          if (resp.success && resp.usuario) {
            this.userSubject.next(resp.usuario);
            return true;
          }
          return false;
        }),
        catchError(() => of(false))
      );
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
