import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { DashboardService, Order, DashboardSummary } from '../../services/dashboard.service';

@Component({
  standalone: true,
  selector: 'app-dashboard',
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  //para mostrar el nombre de usuario en el dashboard
  username: string | null = "";
  orders: Order[] = [];
  createdAt: Date = new Date();
  clientsCount = 0;

   constructor (private auth: AuthService, private dashboardSvc: DashboardService) {}

   ngOnInit() {
    const userId = this.auth.getUsuarioActualId();   // asumimos que guardás ID en AuthService
    this.username = this.auth.getUsuarioActual();

    if (userId) {
      this.dashboardSvc.getSummary(userId).subscribe(
        (data: DashboardSummary) => {
          this.orders = data.orders;
          this.createdAt = new Date(data.createdAt);
          this.clientsCount = data.clientsCount;
        },
        err => {
          console.error('Error cargando resumen de dashboard', err);
        }
      );
    }
  }

  get daysSinceCreation(): number {
    const diff = Date.now() - this.createdAt.getTime();
    return Math.floor(diff / (1000 * 60 * 60 * 24));
  }
}
