import { Component, inject } from '@angular/core';
import { ProductsService } from '../../../core/services/products-service';
import { CommonModule } from '@angular/common';
import { NgbModal, NgbToastModule } from '@ng-bootstrap/ng-bootstrap';
import { Toast } from '../../../shared/services/toast';
import { ProductFormComponent } from '../product-form-component/product-form-component';

@Component({
  selector: 'app-products-list',
  standalone: true,
  imports: [CommonModule, NgbToastModule],
  templateUrl: './products-list-component.html',
  styleUrl: './products-list-component.css'
})
export class ProductsListComponent {

  products: any[] = [];
  isLoading = true;

  private productService = inject(ProductsService);
  private modalService = inject(NgbModal);
  public toastService = inject(Toast);

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.isLoading = true;
    this.productService.getProducts().subscribe({
      next: (data) => {
        this.products = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar productos:', err);
        this.isLoading = false;
      }
    });
  }

  openNewProductDialog(): void {
    const modalRef = this.modalService.open(ProductFormComponent, {
      windowClass: 'side-drawer'
    });

    modalRef.result.then(
      (result) => {
        if (result === true) {
          this.loadProducts();
        }
      },
      () => {}
    );
  }

  openEditProductDialog(product: any): void {
    const modalRef = this.modalService.open(ProductFormComponent, {
      windowClass: 'side-drawer'
    });

    modalRef.componentInstance.setProductData(product);

    modalRef.result.then(
      (result) => {
        if (result === true) {
          this.loadProducts();
        }
      },
      () => {}
    );
  }

  confirmDelete(product: any): void {
    if (confirm(`¿Estás seguro de eliminar el producto ${product.name}?`)) {
      this.productService.deleteProduct(product.id).subscribe({
        next: () => {
          this.toastService.showSuccess('Producto eliminado');
          this.loadProducts();
        },
        error: (err) => {
          this.toastService.showError('Error al eliminar producto');
          console.error(err);
        }
      });
    }
  }
}