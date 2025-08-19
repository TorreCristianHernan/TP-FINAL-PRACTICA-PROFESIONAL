import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductsService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/product';

  getProducts(): Observable<any> {
    return this.http.get(`${this.apiUrl}`);
  }

  saveProduct(product: any): Observable<any> {
    return this.http.post(`${this.apiUrl}`, product);
  }

  updateProduct(id: number, product: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, product);
  }

  deleteProduct(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}