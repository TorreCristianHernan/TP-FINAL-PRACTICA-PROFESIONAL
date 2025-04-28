import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  standalone: true,
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm: FormGroup;
  error: string |null = null;

  constructor(private fb: FormBuilder, private router: Router, private auth: AuthService ) {
    this.loginForm = this.fb.group({
      username: [''],
      password: ['']
    });
  }

  onSubmit(): void {
    const username = this.loginForm.value.username;
    const password = this.loginForm.value.password;
    
    

    if (!username && !password) {
      this.error = 'Por favor complete todos los datos.';
    } else if (!username) {
      this.error = 'Por favor ingrese su nombre de usuario.';
    } else if (!password) {
      this.error = 'Por favor ingrese su contraseña.';
    } else {

    // Simulacion de autenticacion
      if (username === 'admin' && password === '1234') {
       
        this.auth.login(username); 
        this.error = '';
        this.router.navigate(['/']); // manda a Inicio
      } else {
        this.error = 'Usuario o contraseña incorrectos';
      }
    }
  }
}
