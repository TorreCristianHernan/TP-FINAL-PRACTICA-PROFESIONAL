import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { map, Observable, startWith } from 'rxjs';
import { PricesService } from '../../../core/services/prices-service';
import { ProductsService } from '../../../core/services/products-service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Toast } from '../../../shared/services/toast';

@Component({
  selector: 'app-price-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './price-form-component.html',
  styleUrl: './price-form-component.css',
})
export class PriceFormComponent {
  private fb = inject(FormBuilder);
  private priceService = inject(PricesService);
  private productsService = inject(ProductsService);
  private toastService = inject(Toast);
  public activeModal = inject(NgbActiveModal);
  private priceEdit: any = null;
  products: any[] = [];
  filteredProducts!: Observable<any[]>;
  productSearch = this.fb.control('');

  priceForm = this.fb.group({
    id: [null],
    salePrice: [0, [Validators.required, Validators.min(0)]],
    effectiveDate: ['', Validators.required],
    active: [true, Validators.required],
    product: [null, Validators.required],
  });

  isEdit = false;

  ngOnInit(): void {
    this.loadProducts();
    this.setupAutocomplete();
  }

  loadProducts(): void {
    this.productsService.getProducts().subscribe({
      next: (data) => {
        this.products = data;
      },
      error: (err) => {
        console.error('Error al cargar productos:', err);
      }
    });
  }

  setPriceData(price: any): void {
    this.isEdit = true;
    this.priceEdit = price;
    
    // Load products first, then set form data
    this.productsService.getProducts().subscribe({
      next: (data) => {
        this.products = data;
        const selectedProduct = this.products.find(p => p.id === price.product?.id);
        const formattedPrice = {
          ...price,
          effectiveDate: price.effectiveDate ? new Date(price.effectiveDate).toISOString().split('T')[0] : '',
          product: selectedProduct
        };
        this.priceForm.patchValue(formattedPrice);
        if (selectedProduct) {
          this.productSearch.setValue(`${selectedProduct.name} (${selectedProduct.sku})`);
        }
      },
      error: (err) => {
        console.error('Error al cargar productos:', err);
      }
    });
  }

  onSubmit(): void {
    if (this.priceForm.valid) {
      const priceData = this.priceForm.value;

      const operation = this.isEdit
        ? this.priceService.updatePrice(
            this.priceEdit.id,
            { ...this.priceEdit, ...priceData }
          )
        : this.priceService.savePrice(priceData);

      operation.subscribe({
        next: () => {
          this.toastService.showSuccess(
            this.isEdit ? 'Precio actualizado' : 'Precio creado'
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

  setupAutocomplete(): void {
    this.filteredProducts = this.productSearch.valueChanges.pipe(
      startWith(''),
      map(value => this.filterProducts(value || ''))
    );
  }

  filterProducts(value: string): any[] {
    const filterValue = value.toLowerCase();
    return this.products.filter(product => 
      product.name.toLowerCase().includes(filterValue) || 
      product.sku.toLowerCase().includes(filterValue)
    );
  }

  selectProduct(product: any): void {
    this.priceForm.patchValue({ product });
    this.productSearch.setValue(`${product.name} (${product.sku})`);
  }

  displayProduct(product: any): string {
    return product ? `${product.name} (${product.sku})` : '';
  }
}