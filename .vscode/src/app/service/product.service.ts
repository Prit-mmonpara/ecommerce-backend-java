import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root' // Automatically provided to the whole app
})
export class ProductService {
  private baseUrl = 'http://localhost:8080/api/products'; // API Endpoint

  constructor(private http: HttpClient) {}

  // Fetch All Products with Pagination
  getProducts(): Observable<any> {
    return this.http.get<any>(this.baseUrl);
  }

  // Get Product by ID
  getProductById(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/${id}`);
  }

  // Create New Product
  createProduct(product: any): Observable<any> {
    return this.http.post<any>(this.baseUrl, product);
  }

  // Update Product
  updateProduct(id: number, product: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${id}`, product);
  }

  // Delete Product by ID
  deleteProduct(id: number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/${id}`);
  }
}
