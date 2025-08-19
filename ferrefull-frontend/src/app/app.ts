import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { NgbCollapse } from '@ng-bootstrap/ng-bootstrap';
import { AuthService } from './core/services/auth-service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected title = 'ferrefull-frontend';
  isMenuCollapsed = true;
  authService = inject(AuthService);

  logout(): void {
    this.authService.logout();
  }
}
