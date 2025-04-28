import { Component } from '@angular/core';
import { RouterModule, RouterOutlet, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './services/auth.service';




@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, RouterModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Tienda Ferrefull';
  usuarioActual: string | null = null;
  constructor(private authService: AuthService, private router: Router) {
    this.authService.user$.subscribe(usuario => {
      this.usuarioActual = usuario;
    });
  }

  irAlLogin() {
    this.router.navigate(['/login']);
  }
  
  logout() {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
