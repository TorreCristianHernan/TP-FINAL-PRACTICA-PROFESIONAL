import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ClientsService } from '../../../core/services/clients-service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Toast } from '../../../shared/services/toast';

@Component({
  selector: 'app-client-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './client-form-component.html',
  styleUrl: './client-form-component.css',
})
export class ClientFormComponent {
  private fb = inject(FormBuilder);
  private clientService = inject(ClientsService);
  private toastService = inject(Toast);
  public activeModal = inject(NgbActiveModal);
  private clientEdit: any = null;

  clienteForm = this.fb.group({
    id: [null],
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    phone: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
  });

  isEdit = false;

  setClienteData(cliente: any): void {
    this.isEdit = true;
    this.clienteForm.patchValue(cliente);
    this.clientEdit = cliente;
  }

  onSubmit(): void {
    if (this.clienteForm.valid) {
      const clienteData = this.clienteForm.value;

      const operation = this.isEdit
        ? this.clientService.updateClient(
            clienteData.id!,
            this.actualizarObjeto(this.clientEdit, clienteData)
          )
        : this.clientService.saveClient(clienteData);

      operation.subscribe({
        next: () => {
          this.toastService.showSuccess(
            this.isEdit ? 'Cliente actualizado' : 'Cliente creado'
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

  actualizarObjeto(objetoPrincipal: any, objetoActualizacion: any): any {
    for (const key in objetoActualizacion) {
      if (objetoActualizacion.hasOwnProperty(key)) {
        objetoPrincipal[key] = objetoActualizacion[key];
      }
    }
    return objetoPrincipal;
  }
}
