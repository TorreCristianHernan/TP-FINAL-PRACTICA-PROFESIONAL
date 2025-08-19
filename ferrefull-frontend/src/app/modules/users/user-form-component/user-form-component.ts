import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { UsersService } from '../../../core/services/users-service';
import { Toast } from '../../../shared/services/toast';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-form-component.html',
  styleUrl: './user-form-component.css'
})
export class UserFormComponent {
  private fb = inject(FormBuilder);
  private userService = inject(UsersService);
  private toastService = inject(Toast)
  public activeModal = inject(NgbActiveModal);
  private userEdit: any = null;

  // Verificar definicion formal de userForm
  userForm = this.fb.group({
    id: [null],
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    phone: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    roles: ['', []], //A definir formato
    type: [''], //A definir formato tambien
    password: ['', Validators.required],
  });
  
  isEdit = false;

  setUserData(user: any): void {
    this.isEdit = true;
    this.userForm.patchValue(user);
    this.userEdit = user;
  }

  onSubmit(): void {
    if (this.userForm.valid) {
      const userData = this.userForm.value;

      const operation = this.isEdit
        ? this.userService.updateUser(
            userData.id!,
            this.actualizarObjeto(this.userEdit, userData)
          )
        : this.userService.saveUser(userData);

      operation.subscribe({
        next: () => {
          this.toastService.showSuccess(
            this.isEdit ? 'Usuario actualizado' : 'Usuario creado'
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