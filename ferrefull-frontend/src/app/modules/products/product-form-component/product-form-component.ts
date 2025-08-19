import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProductsService } from '../../../core/services/products-service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Toast } from '../../../shared/services/toast';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './product-form-component.html',
  styleUrl: './product-form-component.css',
})
export class ProductFormComponent {
  private fb = inject(FormBuilder);
  private productService = inject(ProductsService);
  private toastService = inject(Toast);
  public activeModal = inject(NgbActiveModal);
  private productEdit: any = null;

  productForm = this.fb.group({
    id: [null],
    sku: ['', Validators.required],
    name: ['', Validators.required],
    description: [''],
    stock: [0, [Validators.required, Validators.min(0)]],
  });

  isEdit = false;

  setProductData(product: any): void {
    this.isEdit = true;
    this.productForm.patchValue(product);
    this.productEdit = product;
  }

  onSubmit(): void {
    if (this.productForm.valid) {
      const productData = this.productForm.value;

      const operation = this.isEdit
        ? this.productService.updateProduct(
            productData.id!,
            Object.assign(this.productEdit, productData)
          )
        : this.productService.saveProduct(productData);

      operation.subscribe({
        next: () => {
          this.toastService.showSuccess(
            this.isEdit ? 'Producto actualizado' : 'Producto creado'
          );
          this.activeModal.close(true);
        },
        error: (err) => {
          this.toastService.showError('Ocurrió un error al guardar');
          console.error(err);
        },
      });
    }
  }
}