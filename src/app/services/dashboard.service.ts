import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Order {
  id: number;
  date: string;
  amount: number;
}

export interface DashboardSummary {
  orders: Order[];
  createdAt: string;
  clientsCount: number;
}

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private baseUrl = 'http://localhost:8080/api/dashboard';

  constructor(private http: HttpClient) {}

  getSummary(userId: string): Observable<DashboardSummary> {
    return this.http.get<DashboardSummary>(
      `${this.baseUrl}/summary?userId=${encodeURIComponent(userId)}`
    );
  }
}
