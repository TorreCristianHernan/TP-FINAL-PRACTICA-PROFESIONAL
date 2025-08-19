import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PricesService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/price';

  getPrices(): Observable<any> {
    return this.http.get(`${this.apiUrl}`);
  }

  savePrice(price: any): Observable<any> {
    return this.http.post(`${this.apiUrl}`, price);
  }

  updatePrice(id: number, price: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, price);
  }

  deletePrice(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}