import { Component, inject } from '@angular/core';
import { PricesService } from '../../../core/services/prices-service';
import { CommonModule } from '@angular/common';
import { NgbModal, NgbToastModule } from '@ng-bootstrap/ng-bootstrap';
import { Toast } from '../../../shared/services/toast';
import { PriceFormComponent } from '../price-form-component/price-form-component';

@Component({
  selector: 'app-prices-list',
  standalone: true,
  imports: [CommonModule, NgbToastModule],
  templateUrl: './prices-list-component.html',
  styleUrl: './prices-list-component.css'
})
export class PricesListComponent {

  prices: any[] = [];
  isLoading = true;

  private priceService = inject(PricesService);
  private modalService = inject(NgbModal);
  public toastService = inject(Toast);

  ngOnInit(): void {
    this.loadPrices();
  }

  loadPrices(): void {
    this.isLoading = true;
    this.priceService.getPrices().subscribe({
      next: (data) => {
        this.prices = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar precios:', err);
        this.isLoading = false;
      }
    });
  }

  openNewPriceDialog(): void {
    const modalRef = this.modalService.open(PriceFormComponent, {
      windowClass: 'side-drawer'
    });

    modalRef.result.then(
      (result) => {
        if (result === true) {
          this.loadPrices();
        }
      },
      () => {}
    );
  }

  openEditPriceDialog(price: any): void {
    const modalRef = this.modalService.open(PriceFormComponent, {
      windowClass: 'side-drawer'
    });

    modalRef.componentInstance.setPriceData(price);

    modalRef.result.then(
      (result) => {
        if (result === true) {
          this.loadPrices();
        }
      },
      () => {}
    );
  }

  confirmDelete(price: any): void {
    if (confirm(`¿Estás seguro de eliminar el precio de ${price.salePrice}?`)) {
      this.priceService.deletePrice(price.id).subscribe({
        next: () => {
          this.toastService.showSuccess('Precio eliminado');
          this.loadPrices();
        },
        error: (err) => {
          this.toastService.showError('Error al eliminar precio');
          console.error(err);
        }
      });
    }
  }
}