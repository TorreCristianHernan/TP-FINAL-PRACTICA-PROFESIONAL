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
    const username = this.loginForm.value.username;
    const password = this.loginForm.value.password;
    if (this.loginForm.invalid) {
      this.error = 'Por favor complete todos los datos.';
      return;
    } else if (this.loginForm.value.username.invalid) {
      this.error = 'Por favor ingrese su nombre de usuario.';
    } else if (this.loginForm.value.password.invalid) {
      this.error = 'Por favor ingrese su contraseña.';
    } 


    this.loading = true;
    this.auth.login(username, password).subscribe(success => {
      this.loading = false;
      if (success) {
        this.router.navigate(['/']);
      } else {
        this.error = 'Usuario o contraseña incorrectos';
      }
    }, () => {
      this.loading = false;
      this.error = 'Falla de conexión con el servidor';
    });
    }
  }
    