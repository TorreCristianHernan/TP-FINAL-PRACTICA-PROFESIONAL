import { Component, inject } from '@angular/core';
import { ClientsService } from '../../../core/services/clients-service';
import { CommonModule } from '@angular/common';
import { NgbModal, NgbToastModule } from '@ng-bootstrap/ng-bootstrap';
import { Toast } from '../../../shared/services/toast';
import { ClientFormComponent } from '../client-form-component/client-form-component';

@Component({
  selector: 'app-clients-list-component',
  standalone: true,
  imports: [CommonModule, NgbToastModule],
  templateUrl: './clients-list-component.html',
  styleUrl: './clients-list-component.css'
})
export class ClientsListComponent {

  clientes: any[] = [];
  isLoading = true;

  private clientService = inject(ClientsService);
  private modalService = inject(NgbModal);
  public toastService = inject(Toast);

  ngOnInit(): void {
    this.loadClientes();
  }

  loadClientes(): void {
    this.isLoading = true;
    this.clientService.getClients().subscribe({
      next: (data) => {
        this.clientes = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar clientes:', err);
        this.isLoading = false;
      }
    });
  }

  openNewClienteDialog(): void {
    const modalRef = this.modalService.open(ClientFormComponent, {
      windowClass: 'side-drawer'
    });

    modalRef.result.then(
      (result) => {
        if (result === true) {
          this.loadClientes();
        }
      },
      () => {}
    );
  }

  openEditClienteDialog(cliente: any): void {
    const modalRef = this.modalService.open(ClientFormComponent, {
      windowClass: 'side-drawer'
    });

    modalRef.componentInstance.setClienteData(cliente);

    modalRef.result.then(
      (result) => {
        if (result === true) {
          this.loadClientes();
        }
      },
      () => {}
    );
  }

  confirmDelete(cliente: any): void {
    if (confirm(`¿Estás seguro de eliminar a ${cliente.firstName + ' ' + cliente.lastName}?`)) {
      this.clientService.deleteClient(cliente.id).subscribe({
        next: () => {
          this.toastService.showSuccess('Cliente eliminado');
          this.loadClientes();
        },
        error: (err) => {
          this.toastService.showError('Error al eliminar cliente');
          console.error(err);
        }
      });
    }
  }

}
