import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ClientsService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/customer';

  getClients(): Observable<any> {
    return this.http.get(`${this.apiUrl}`);
  }

  saveClient(client: any): Observable<any> {
    return this.http.post(`${this.apiUrl}`, client);
  }

  updateClient(id: number, client: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, client);
  }

  deleteClient(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
