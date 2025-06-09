import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  standalone: true,
  selector: 'app-dashboard',
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  //para mostrar el nombre de usuario en el dashboard
  //hardcodeado de aca en adelante
  username: string | null;
  orders: { id: number; date: string; amount: number; }[];
  createdAt: Date;
  clientsCount: number;

   constructor(private auth: AuthService) {
      // Obtener usuario actual
    this.username = this.auth.getUsuarioActual();

    // Datos hardcodeados
    this.orders = [
      { id: 101, date: '2025-05-01', amount: 250.00 },
      { id: 102, date: '2025-05-10', amount: 125.50 },
      { id: 103, date: '2025-05-20', amount: 320.75 }
    ];

    // Fecha de creación de usuario hardcodeada
    this.createdAt = new Date('2024-01-15T10:30:00');

    // Cantidad de clientes hardcodeada
    this.clientsCount = 42;
   }
   get daysSinceCreation(): number {
    const diff = Date.now() - this.createdAt.getTime();
    return Math.floor(diff / (1000 * 60 * 60 * 24));
  }
}
