import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  standalone: true,
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm: FormGroup;
  error: string |null = null;
  loading = false;

  constructor(private fb: FormBuilder, private router: Router, private auth: AuthService ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    this.error = null;
    this.loading = true;

    const { username, password } = this.loginForm.value;

    if (this.loginForm.invalid) {
      this.error = 'Por favor complete todos los datos.';
      return;
    } else if (this.loginForm.value.username.invalid) {
      this.error = 'Por favor ingrese su nombre de usuario.';
    } else if (this.loginForm.value.password.invalid) {
      this.error = 'Por favor ingrese su contraseña.';
    } 

    this.auth.login(username, password).subscribe({
      next: _token => {
        // Hasta acá, token valido
        this.loading = false;
        this.router.navigate(['/dashboard']);
      },
      error: () => {
        this.loading = false;
        this.error = 'Usuario o contraseña incorrectos';
      }
    });
  }
  
}
    