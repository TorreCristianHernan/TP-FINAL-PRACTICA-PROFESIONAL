import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NgbAlert } from '@ng-bootstrap/ng-bootstrap';
import { AuthService } from '../../../core/services/auth-service';

@Component({
  selector: 'app-login-component',
  standalone: true,
  // imports: [ReactiveFormsModule, CommonModule, NgbAlert, RouterLink],
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './login-component.html',
  styleUrl: './login-component.css'
})
export class LoginComponent {

  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required]
  });

  errorMessage: string = '';

  onSubmit(): void {
    if (this.loginForm.valid) {
      const { email, password } = this.loginForm.value;
      this.authService.login(email!, password!).subscribe({
        next: () => {
          this.router.navigate(['/clients']);
        },
        error: (err) => {
          this.errorMessage = 'Credenciales incorrectas';
          console.error(err);
        }
      });
    }
  }

}
