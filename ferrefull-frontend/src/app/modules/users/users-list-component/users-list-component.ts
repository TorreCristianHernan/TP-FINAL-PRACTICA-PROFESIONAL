import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { NgbModal, NgbToastModule } from '@ng-bootstrap/ng-bootstrap';
import { UsersService } from '../../../core/services/users-service';
import { Toast } from '../../../shared/services/toast';
import { UserFormComponent } from '../user-form-component/user-form-component';

@Component({
  selector: 'app-users-list-component',
  standalone: true,
  imports: [CommonModule, NgbToastModule],
  templateUrl: './users-list-component.html',
  styleUrl: './users-list-component.css'
})
export class UsersListComponent {

  users: any[] = [];
  isLoading = true;

  private usersService = inject(UsersService);
  private modalService = inject(NgbModal);
  public toastService = inject(Toast);

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.isLoading = true;
    this.usersService.getUsers().subscribe({
      next: (data) => {
        this.users = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar usuarios:', err);
        this.isLoading = false;
      }
    });
  }
  openNewUserDialog(): void {
    const modalRef = this.modalService.open(UserFormComponent, {
      windowClass: 'side-drawer'
    });

    modalRef.result.then(
      (result) => {
        if (result === true) {
          this.loadUsers();
        }
      },
      () => {}
    );
  }

  openEditUserDialog(user: any): void {
    const modalRef = this.modalService.open(UserFormComponent, {
      windowClass: 'side-drawer'
    });

    modalRef.componentInstance.setUserData(user);

    modalRef.result.then(
      (result) => {
        if (result === true) {
          this.loadUsers();
        }
      },
      () => {}
    );
  }

  confirmDelete(user: any): void {
    if (confirm(`¿Estás seguro de que deseas eliminar al usuario ${user.firstName + ' ' + user.lastName}?`)) {
      this.usersService.deleteUser(user.id).subscribe({
        next: () => {
          this.toastService.showSuccess('Usuario eliminado correctamente');
          this.loadUsers();
        },
        error: (err) => {
          this.toastService.showError('Ocurrió un error al eliminar el usuario');
          console.error('Error al eliminar usuario:', err);
        }
      });
    }
  }

}
