import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, throwError} from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';

interface LoginResp {
  token: string;
  username?: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private userSubject = new BehaviorSubject<string | null>(null);
  private tokenKey = 'auth_token';
  user$ = this.userSubject.asObservable();
  // Observable para que los componentes se suscriban
  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<string> {
    return this.http
      .post<LoginResp>('http://localhost:8080/api/login', { username, password })
      .pipe(
        map(resp => {
          if (!resp.token) {
            throw new Error('Token no recibido');
          }
          return { token: resp.token, username: resp.username ?? username };
        }),
        tap(({ token, username }) => {
          // Guardar el token en cookie
          document.cookie = `${this.tokenKey}=${token}; path=/;`;
          // Cambiar usersubject para mostrar el login
          this.userSubject.next(username);
        }),
        map(({ token }) => token),
        catchError(err => throwError(() => err))
      );
          
  }

  logout(): void {
    document.cookie = `${this.tokenKey}=; path=/; max-age=0`;
    this.userSubject.next(null);
  }

  getToken(): string | null {
    const match = document.cookie.match(new RegExp('(^| )' + this.tokenKey + '=([^;]+)'));
    return match ? match[2] : null;
  }

  getUsuarioActual(): string | null {
    return this.userSubject.getValue();
  }

  estaLogueado(): boolean {
    return this.getUsuarioActual() !== null;
  }
}
